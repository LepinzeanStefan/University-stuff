/* LEPINZEAN STEFAN - 315CD */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "MagicBandList.h"
#include "Commands.h"

#define COMM_SIZE 20

// Shows the element pointed by the cursor
void showCurrent(List* list, FILE* out) {
    fprintf(out, "%c\n", list->cursor->elem);
}

// Shows all the band, with two lines between the cursor
void show(List* list, FILE* out) {
    ListNode* iter = list->sentinel->next;
    while (iter) {
        if (iter == list->cursor) {
            fprintf(out, "|%c|", iter->elem);
        }
        else 
            fprintf(out, "%c", iter->elem);
        iter = iter->next;
    }
    fprintf(out, "\n");
}

int main() {
    List* magic_list = createList(); // This will be the magic list that we will use
    TQueue* comm_queue = initQueue(); // This will be the queue for the "Update" commands
    Stack* redo = NULL; // The stack for the redo command
    Stack* undo = NULL; // The stack for the undo command
    FILE* in_file = fopen("tema1.in", "r"); // The input file
    FILE* out_file = fopen("tema1.out", "w"); // The output file

    if (in_file == NULL || out_file == NULL) {
        printf("ERROR! Couldn't open the input/output file");
        exit(1);
    }

    int comm_num; // the number of commands that will be expected in the input file 
    fscanf(in_file, "%d", &comm_num);

    /* Quick note: To make the commands that will be put in the queue easier to store
    and to manipulate, we will be reducing all "Update" commands to a single character.
    All characters and their respective commands will be listed in the README */

    int i = 0;
    TCommand curr_comm; // it will be used to execute command from the queue
    for (;i < comm_num; i++) {
        char value; // string that represents a single command and its respective value
        char comm[COMM_SIZE];
        fscanf(in_file, "%s", comm);

        // First we will search for the "Update" commands to add to the queue
        if (strcmp(comm, "MOVE_LEFT") == 0) {
            comm_queue = enqueue(comm_queue, 'l', '0');
            continue;
        }
        if (strcmp(comm, "MOVE_RIGHT") == 0) {
            comm_queue = enqueue(comm_queue, 'r', '0');
            continue;
        }
        if (strcmp(comm, "MOVE_LEFT_CHAR") == 0) {
            fscanf(in_file, "%c", &value);
            // we need to double scan to get rid of the blank space
            // this will happen to all the commands that use values
            fscanf(in_file, "%c", &value);
            comm_queue = enqueue(comm_queue, 'L', value);
            continue;
        }
        if (strcmp(comm, "MOVE_RIGHT_CHAR") == 0) {
            fscanf(in_file, "%c", &value);
            fscanf(in_file, "%c", &value);
            comm_queue = enqueue(comm_queue, 'R', value);
            continue;
        }
        if (strcmp(comm, "WRITE") == 0) {
            fscanf(in_file, "%c", &value);
            fscanf(in_file, "%c", &value);
            comm_queue = enqueue(comm_queue, 'w', value);
            continue;
        }
        if (strcmp(comm, "INSERT_LEFT") == 0) {
            fscanf(in_file, "%c", &value);
            fscanf(in_file, "%c", &value);
            comm_queue = enqueue(comm_queue, '<', value);
            continue;
        }
        if (strcmp(comm, "INSERT_RIGHT") == 0) {
            fscanf(in_file, "%c", &value);
            fscanf(in_file, "%c", &value);
            comm_queue = enqueue(comm_queue, '>', value);
            continue;
        }
        // If the command is EXECUTE, we need to take the first command from the queue
        // and execute it correspondingly
        if (strcmp(comm, "EXECUTE") == 0) {
            curr_comm = frontQueue(comm_queue);
            comm_queue = dequeue(comm_queue);
            
            // If we have either move_left or move_right we will also add it to the redo stack
            if (curr_comm.command == 'l') {
                // checks if the cursor is at the start of the list
                if (magic_list->cursor->prev == magic_list->sentinel)
                    continue;
                undo = pushStack(undo, magic_list->cursor);
                moveLeft(magic_list);
                continue;
            }
            if (curr_comm.command == 'r') {
                undo = pushStack(undo, magic_list->cursor);
                moveRight(magic_list);
                continue;
            }
            if (curr_comm.command == 'L') {
                moveLeftChar(magic_list, curr_comm.value, out_file);
                continue;
            }
            if (curr_comm.command == 'R') {
                moveRightChar(magic_list, curr_comm.value);
                continue;
            }
            if (curr_comm.command == 'w') {
                write(magic_list, curr_comm.value);
                continue;
            }
            if (curr_comm.command == '<') {
                insertLeft(magic_list, curr_comm.value, out_file);
                continue;
            }
            if (curr_comm.command == '>') {
                insertRight(magic_list, curr_comm.value);
                continue;
            }
            continue;
        }
        if (strcmp(comm, "SHOW_CURRENT") == 0) {
            showCurrent(magic_list, out_file);
            continue;
        }
        if (strcmp(comm, "SHOW") == 0) {
            show(magic_list, out_file);
            continue;
        }
        if (strcmp(comm, "UNDO") == 0) {
            redo = pushStack(redo, magic_list->cursor);
            magic_list->cursor = topStack(undo);
            undo = pop(undo);
            continue;
        }
        if (strcmp(comm, "REDO") == 0) {
            undo = pushStack(undo, magic_list->cursor);
            magic_list->cursor = topStack(redo);
            redo = pop(redo);
            continue;
        }
    }
    
    comm_queue = freeQueue(comm_queue);
    destroyList(magic_list);
    redo = freeStack(redo);
    undo = freeStack(undo);
    fclose(in_file);
    fclose(out_file);
    return 0;
}