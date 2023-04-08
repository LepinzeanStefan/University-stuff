CC = gcc
CFLAGS = -Wall -lm
SRC = tema1.c
HEAD = Commands.h MagicBandList.h
EXE = tema1

build:
	$(CC) -o $(EXE) $(SRC) $(HEAD) $(CFLAGS)

run:
	./$(EXE)

.PHONY : clean
clean: 
	rm -f $(EXE) *~