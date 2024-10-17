#include <arpa/inet.h>
#include <ctype.h>
#include <errno.h>
#include <netdb.h>
#include <netinet/in.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/poll.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <unistd.h>
#include <netinet/tcp.h>
#include <math.h>

#include "common.hpp"

using namespace std;

void run_client(int tcpfd, char *ID) {
  char buf[MSG_MAXSIZE + 1];
  memset(buf, 0, MSG_MAXSIZE + 1);

  struct chat_packet sent_packet;
  struct chat_packet recv_packet;

  sent_packet.type = TYPE_LOG;
  strcpy(sent_packet.topic, ID);
  send_all(tcpfd, &sent_packet, sizeof(sent_packet));

  char exit[5] = "exit";
  char sub[10] = "subscribe";
  char unsub[12] = "unsubscribe";

  struct pollfd poll_fds[2];
  int num_sockets = 2;
  int rc;
  poll_fds[0].fd = tcpfd;
  poll_fds[0].events = POLLIN;
  poll_fds[1].fd = STDIN_FILENO;
  poll_fds[1].events = POLLIN;

  while (1) {
    // Asteptam sa primim ceva pe unul dintre cei num_sockets socketi
    rc = poll(poll_fds, num_sockets, -1);

    if (poll_fds[0].revents & POLLIN) {
      int rc = recv_all(tcpfd, &recv_packet, sizeof(struct chat_packet));
      char address_ip[INET_ADDRSTRLEN];
      const char *ip = inet_ntop(AF_INET, &recv_packet.saddr,
      address_ip, INET_ADDRSTRLEN);
      char type[11];
      switch (recv_packet.type) {
      case TYPE_INT:
        strcpy(type, "INT");
        uint32_t int_value;
        memcpy(&int_value, recv_packet.message + 1, sizeof(uint32_t));
        if (recv_packet.message[0] == 0) {
          printf("%s:%d - %s - %s - %d\n", ip, ntohs(recv_packet.sport),
            recv_packet.topic, type, ntohl(int_value));
        } else {
          printf("%s:%d - %s - %s - -%d\n", ip, ntohs(recv_packet.sport),
            recv_packet.topic, type, ntohl(int_value));
        }
        break;
      case TYPE_SHORT:
        strcpy(type, "SHORT_REAL");
        float float_value;
        float_value = ntohs(*(uint16_t *)recv_packet.message);
        memcpy(&float_value, recv_packet.message, sizeof(uint16_t));
        printf("%s:%d - %s - %s - %f\n", ip, ntohs(recv_packet.sport),
          recv_packet.topic, type, float_value / 100);
        break;
      case TYPE_FLOAT:
        strcpy(type, "FLOAT");
        float_value = ntohl(*(uint32_t *)(recv_packet.message + 1));
        int_value = *(uint8_t *)(recv_packet.message + 5);
        int_value = pow(10, int_value);
        if (recv_packet.message[0] == 0) {
          printf("%s:%d - %s - %s - %f\n", ip, ntohs(recv_packet.sport),
            recv_packet.topic, type, float_value / int_value);
        } else {
          printf("%s:%d - %s - %s - -%f\n", ip, ntohs(recv_packet.sport),
            recv_packet.topic, type, float_value / int_value);
        }
        break;
      case TYPE_STRING:
        strcpy(type, "STRING");
        printf("%s:%d - %s - %s - %s\n", ip, ntohs(recv_packet.sport),
          recv_packet.topic, type, recv_packet.message) ;
        break;
      case TYPE_EXIT:
        return;
      default:
        strcpy(type, "ERROR");
        printf("%s:%d - %s - %s - Type couldn't be identified\n", ip, ntohs(recv_packet.sport),
          recv_packet.topic, type);
        break;
      }
    }
    if (poll_fds[1].revents & POLLIN) {
      if (fgets(buf, sizeof(buf), stdin)) {
        if (strncmp(buf, exit, strlen(exit)) == 0) {
          strcpy(sent_packet.message, buf);
          sent_packet.type = TYPE_EXIT;
          strcpy(sent_packet.topic, ID);
          send_all(tcpfd, &sent_packet, sizeof(struct chat_packet));
          return;
        } else if (strncmp(buf, sub, strlen(sub)) == 0) {
          // Pentru a scapa de newlineul de la input
          buf[strlen(buf) - 1] = '\0';
          // Folosim aceeasi structura de transmisie a mesajelor udp pentru subscribe
          sent_packet.type = TYPE_SUB;
          // In topic se va afla ID-ul clientului
          strcpy(sent_packet.topic, ID);
          strcpy(sent_packet.message, buf + strlen(sub) + 1);
          send_all(tcpfd, &sent_packet, sizeof(struct chat_packet));
          printf("Subscribed to topic %s\n", sent_packet.message);
        } else if (strncmp(buf, unsub, strlen(unsub)) == 0) {
          buf[strlen(buf) - 1] = '\0';
          sent_packet.type = TYPE_UNSUB;
          strcpy(sent_packet.topic, ID);
          strcpy(sent_packet.message, buf + strlen(unsub) + 1);
          send_all(tcpfd, &sent_packet, sizeof(struct chat_packet));
          printf("Unsubscribed from topic %s\n", sent_packet.message);
        } else {
          printf("Invalid command, commands accepted are:\n"
            "- exit\n  - subscribe <TOPIC>\n  - unsubscribe <TOPIC>\n");
        }
      }
    }
  }
}

int main(int argc, char *argv[]) {
  setvbuf(stdout, NULL, _IONBF, BUFSIZ);
  if (argc != 4) {
    printf("\n Usage: %s <id> <ip> <port>\n", argv[0]);
    return 1;
  }

  // Parsam port-ul ca un numar
  uint16_t port;
  int rc = sscanf(argv[3], "%hu", &port);

  // Obtinem un socket TCP pentru conectarea la server
  const int tcpfd = socket(AF_INET, SOCK_STREAM, 0);

  const int enable = 1;
  if (setsockopt(tcpfd, SOL_TCP, TCP_NODELAY, &enable, sizeof(int)) < 0)
  perror("setsockopt(TCP_NODELAY) failed");

  // Completăm in serv_addr adresa serverului, familia de adrese si portul
  // pentru conectare
  struct sockaddr_in serv_addr;
  socklen_t socket_len = sizeof(struct sockaddr_in);

  memset(&serv_addr, 0, socket_len);
  serv_addr.sin_family = AF_INET;
  serv_addr.sin_port = htons(port);
  rc = inet_pton(AF_INET, argv[2], &serv_addr.sin_addr.s_addr);

  char ID[11];
  strcpy(ID, argv[1]);

  // Ne conectăm la server
  rc = connect(tcpfd, (struct sockaddr *)&serv_addr, sizeof(serv_addr));

  run_client(tcpfd, ID);

  // Inchidem conexiunea si socketul creat
  close(tcpfd);

  return 0;
}
