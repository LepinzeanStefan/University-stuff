CFLAGS = -Wall -g -Werror -Wno-error=unused-variable

# Portul pe care asculta serverul
PORT = 12345

# Adresa IP a serverului
IP_SERVER = 127.0.0.1

# ID-ul unui client
ID = 1234

all: server subscriber

common.o: common.cpp

server: server.cpp common.o

subscriber: subscriber.cpp common.o

.PHONY: clean run_server run_subscriber

run_server:
	./server ${PORT}

run_subscriber:
	./subscriber ${ID} ${IP_SERVER} ${PORT}

clean:
	rm -rf server subscriber *.o *.dSYM
