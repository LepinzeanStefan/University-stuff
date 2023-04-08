/* LEPINZEAN STEFAN - 315CD */
#ifndef _COMMANDS_
#define _COMMANDS_
#include <stdlib.h>

typedef char T;

// --------------------------------------------------------------------
// Queue type that will be used to retain commands
typedef struct node {
    T command; // character defining the type of command
    T value; // if there is also a value present, it will be written here
    struct node *next; // pointer to the next element in the queue
} TNode;

typedef struct queue {
    TNode *head, *tail;
} TQueue;

// Command type that will be used to return only the command and value
typedef struct command {
    T command;
    T value;
} TCommand;
// --------------------------------------------------------------------

/**
 * Initiates an empty queue
 */
TQueue* initQueue() {
    TQueue* queue = (TQueue *) calloc(1, sizeof(TQueue));
    return queue;
}
// --------------------------------------------------------------------

/**
 * Checks if the queue is empty
 */
int isEmptyQueue(TQueue* queue) {
    return queue == NULL || queue->head == NULL;
}
// --------------------------------------------------------------------

/**
 * Initiates a new node with a command type and value
 */
TNode* initNodeQueue(T command, T value) {
    TNode *node = (TNode *)calloc(1, sizeof(TNode));
    node->command = command;
    node->value = value;
    return node;
}
// --------------------------------------------------------------------

/**
 * Frees a single node
 */
TNode* freeNodeQueue(TNode *node) {
    if (node)
        free(node);
    return NULL;
}
// --------------------------------------------------------------------

/**
 * Adds a new node to the queue
 */
TQueue* enqueue(TQueue* queue, T command, T value) {
    TNode *node = initNodeQueue(command, value);
    if (isEmptyQueue(queue)) {
        if (queue == NULL) {
            queue = initQueue();
        }
        queue->head = queue->tail = node;
        return queue;
    }
    queue->tail->next = node;
    queue->tail = node;
    return queue;
}
// --------------------------------------------------------------------

/**
 * Removes one element from the queue, respecting the LIFO principle
 */
TQueue* dequeue(TQueue* queue) {
    TNode* tmp;
    if (!isEmptyQueue(queue)) {
        tmp = queue->head;
        queue->head = queue->head->next;
        tmp = freeNodeQueue(tmp);
    }
    return queue;
}
// --------------------------------------------------------------------

/**
 * Returns the command and value stocked in the head of the queue
 */
TCommand frontQueue(TQueue* queue) {
    if (!isEmptyQueue(queue)) {
        TCommand comm;
        comm.command = queue->head->command;
        comm.value = queue->head->value;
        return comm;
    }
    else {
        TCommand comm;
        comm.command = 'E';
        comm.value = 'E';
        return comm;
    }
        
}
// --------------------------------------------------------------------

/**
 * Frees the queue 
 */
TQueue* freeQueue(TQueue* queue) {
    while (!isEmptyQueue(queue)) {
        queue = dequeue(queue);
    }
    if (queue)
        free(queue);
    return NULL;
}
// --------------------------------------------------------------------
#endif