#ifndef __COMMON_H__
#define __COMMON_H__

#include <stddef.h>
#include <stdint.h>

int send_all(int sockfd, void *buff, size_t len);
int recv_all(int sockfd, void *buff, size_t len);

/* Dimensiunea maxima a mesajului */
#define MSG_MAXSIZE 1500
#define TYPE_INT 0
#define TYPE_SHORT 1
#define TYPE_FLOAT 2
#define TYPE_STRING 3
#define TYPE_SUB 4
#define TYPE_UNSUB 5
#define TYPE_LOG 6
#define TYPE_EXIT 7
//Marimea insumata a adresei ipv4 si a portului, in bytes
#define ADDR_SIZE 6

enum Status {Online, Offline};

struct __attribute__((packed)) chat_packet {
  char topic[50];
  uint8_t type;
  char message[MSG_MAXSIZE + 1];
  uint32_t saddr;
  uint16_t sport;
};

struct client_data {
  int fd;
  Status status = Status::Offline;
};

#endif
