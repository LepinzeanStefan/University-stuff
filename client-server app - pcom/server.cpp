#include <arpa/inet.h>
#include <errno.h>
#include <netinet/in.h>
#include <poll.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <unistd.h>
#include <netinet/tcp.h>
#include <map>
#include <string>
#include <set>
#include "common.hpp"

#define MAX_SIZE 1500
#define MAX_CONNECTIONS 50
#define LIS_POLL 2
#define UDP_POLL 1
#define INP_POLL 0
#define SRV_IP "127.0.0.1"

void run_chat_multi_server(int listenfd, int udpfd) {
  struct pollfd poll_fds[MAX_CONNECTIONS];
  int num_sockets = 3;
  int rc;
  char exit[5] = "exit";
  struct chat_packet received_packet;
  struct sockaddr_in client_addr;
  struct chat_packet udp_packet;
  std::map<std::string, struct client_data> clients;
  std::map<std::string, std::set<std::string>> sub_topics;

  // Setam socket-ul listenfd pentru ascultare
  rc = listen(listenfd, MAX_CONNECTIONS);

  // Adaugam noul file descriptor (socketul pe care se asculta conexiuni) in
  // multimea poll_fds
  poll_fds[INP_POLL].fd = STDIN_FILENO;
  poll_fds[INP_POLL].events = POLLIN;
  poll_fds[UDP_POLL].fd = udpfd;
  poll_fds[UDP_POLL].events = POLLIN;
  poll_fds[LIS_POLL].fd = listenfd;
  poll_fds[LIS_POLL].events = POLLIN;

  while (1) {
    // Asteptam sa primim ceva pe unul dintre cei num_sockets socketi
    rc = poll(poll_fds, num_sockets, -1);

    for (int i = 0; i < num_sockets; i++) {
      if (poll_fds[i].revents & POLLIN) {
        if (poll_fds[i].fd == listenfd) {
          // Am primit o cerere de conexiune pe socketul de listen, pe care
          // o acceptam
          struct sockaddr_in cli_addr;
          socklen_t cli_len = sizeof(cli_addr);
          const int newsockfd =
              accept(listenfd, (struct sockaddr *)&cli_addr, &cli_len);

          recv_all(newsockfd, &received_packet, sizeof(received_packet));
          if (clients.find(received_packet.topic) != clients.end()
              && clients[received_packet.topic].status == Status::Online) {
            printf("Client %s already connected.\n", received_packet.topic);
            received_packet.type = TYPE_EXIT;
            send_all(newsockfd, &received_packet, sizeof(received_packet));
            close(newsockfd);
          } else {
            clients[received_packet.topic].fd = newsockfd;
            clients[received_packet.topic].status = Status::Online;
            char address_ip[INET_ADDRSTRLEN];

            const char *ip = inet_ntop(AF_INET, &cli_addr.sin_addr.s_addr,
              address_ip, INET_ADDRSTRLEN);
            printf("New client %s connected from %s:%u.\n",
              received_packet.topic, ip, ntohs(cli_addr.sin_port));

            // Adaugam noul socket intors de accept() la multimea descriptorilor
            // de citire
            poll_fds[num_sockets].fd = newsockfd;
            poll_fds[num_sockets].events = POLLIN;
            num_sockets++;
          }
          break;
        } else if (poll_fds[i].fd == udpfd) {
          memset(&udp_packet, 0, sizeof(struct chat_packet));
          socklen_t clen = sizeof(client_addr);
          int rc = recvfrom(udpfd, &udp_packet, sizeof(struct chat_packet) - ADDR_SIZE, 0,
                    (struct sockaddr *)&client_addr, &clen);
          // Pe langa mesajul primit de clientul udp adaugam si adresa internet si portul
          // pe langa asta, serverul nu se ocupa de orice alta transformare a informatiei
          udp_packet.saddr = client_addr.sin_addr.s_addr;
          udp_packet.sport = client_addr.sin_port;
          for (const auto& ID : sub_topics[udp_packet.topic]) {
            if (clients[ID].status == Status::Online) {
              send_all(clients[ID].fd, &udp_packet, sizeof(struct chat_packet));
            }
          }
        } else if (poll_fds[i].fd == STDIN_FILENO) {
          char buf[5];
          fgets(buf, sizeof(buf), stdin);
          if (strncmp(buf, exit, strlen(exit)) == 0) {
            memset(&received_packet, 0, sizeof(received_packet));
            received_packet.type = TYPE_EXIT;
            for (int j = 3; j < num_sockets; j++) {
              send_all(poll_fds[j].fd, &received_packet, sizeof(received_packet));
              close(poll_fds[j].fd);
            }
            return;
          } else {
            printf("Invalid command, only command accepted is: exit\n");
          }
        } else {
          // Am primit date pe unul din socketii de client, asa ca le receptionam
          // singurele date care pot fi primite de la clienti sunt comenzi de tip subscribe
          // sau deconectari
          int rc = recv_all(poll_fds[i].fd, &received_packet,
                            sizeof(received_packet));

          if (received_packet.type == TYPE_EXIT) {
            close(poll_fds[i].fd);
            clients[received_packet.topic].status = Status::Offline;
            clients[received_packet.topic].fd = -1; 

            printf("Client %s disconnected.\n", received_packet.topic);
            // Scoatem din multimea de citire socketul inchis
            for (int j = i; j < num_sockets - 1; j++) {
              poll_fds[j] = poll_fds[j + 1];
            }

            num_sockets--;
          } else if (received_packet.type == TYPE_SUB) {
            sub_topics[received_packet.message].insert(received_packet.topic);
          } else if (received_packet.type == TYPE_UNSUB) {
            sub_topics[received_packet.message].erase(received_packet.topic);
          }
        }
      }
    }
  }
}

int main(int argc, char *argv[]) {
  setvbuf(stdout, NULL, _IONBF, BUFSIZ);

  if (argc != 2) {
      printf("\n Usage: %s <port>\n", argv[0]);
      return 1;
  }


  // Parsam port-ul ca un numar
  uint16_t port;
  int rc = sscanf(argv[1], "%hu", &port);

  // Obtinem un socket TCP pentru receptionarea conexiunilor
  const int listenfd = socket(AF_INET, SOCK_STREAM, 0);

  // Completăm in serv_addr adresa serverului, familia de adrese si portul
  // pentru conectare
  struct sockaddr_in serv_addr;
  socklen_t socket_len = sizeof(struct sockaddr_in);

  const int enable = 1;
  if (setsockopt(listenfd, SOL_SOCKET, SO_REUSEADDR, &enable, sizeof(int)) < 0)
    perror("setsockopt(SO_REUSEADDR) failed");

  if (setsockopt(listenfd, IPPROTO_TCP, TCP_NODELAY, &enable, sizeof(int)) < 0)
    perror("setsockopt(TCP_NODELAY) failed");

  char ip[20];
  strcpy(ip, SRV_IP);
  memset(&serv_addr, 0, socket_len);
  serv_addr.sin_family = AF_INET;
  serv_addr.sin_port = htons(port);
  rc = inet_pton(AF_INET, ip, &serv_addr.sin_addr.s_addr);

  // Asociem adresa serverului cu socketul creat folosind bind
  rc = bind(listenfd, (const struct sockaddr *)&serv_addr, sizeof(serv_addr));
  // printf("%d\n", rc);

  int udpfd;

  // Socketul pentru clientii udp
  if ((udpfd = socket(AF_INET, SOCK_DGRAM, 0)) < 0) {
    perror("socket creation failed");
    exit(EXIT_FAILURE);
  }

  if (setsockopt(udpfd, SOL_SOCKET, SO_REUSEADDR, &enable, sizeof(int)) < 0)
    perror("setsockopt(SO_REUSEADDR) failed");

  // Bind pentru socketul udp sa accepte orice datagrama care are portul serverului ca
  // destinatie
  rc = bind(udpfd, (const struct sockaddr *)&serv_addr, sizeof(serv_addr));

  // run_chat_server(listenfd);
  run_chat_multi_server(listenfd, udpfd);

  // Inchidem listenfd
  close(listenfd);
  close(udpfd);

  return 0;
}
