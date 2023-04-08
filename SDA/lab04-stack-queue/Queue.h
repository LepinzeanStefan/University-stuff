#ifndef QUEUE_H_
#define QUEUE_H_

#include <stdio.h>
#include <stdlib.h>

typedef struct QueueNode{
	Item elem;
	struct QueueNode *next;
}QueueNode;

typedef struct Queue{
	QueueNode *front;
	QueueNode *rear;
	long size;
}Queue;

Queue* createQueue(void){
	// TODO: Cerinta 2
	Queue* coada = (Queue *) calloc(1, sizeof(Queue));
	return coada;
} 

int isQueueEmpty(Queue *q){
	// TODO: Cerinta 2
	return q == NULL || q->front == NULL;
}

void enqueue(Queue *q, Item elem){
	// TODO: Cerinta 2
	QueueNode* node = (QueueNode *) calloc(1, sizeof(QueueNode));
	node->elem = elem;
	if(isQueueEmpty(q)) {
		if(q == NULL)
			q = createQueue();
		q->front = q->rear = node;
		q->size++;
		return;
	}
	q->rear->next = node;
	q->rear = node;
	q->size++;
}

Item front(Queue* q){
	// TODO: Cerinta 2
	if(isQueueEmpty(q))
		exit(1);
	return q->front->elem;
}

void dequeue(Queue* q){
	// TODO: Cerinta 2
	QueueNode* node;
	if(!isQueueEmpty(q)) {
		if(q->front == q->rear)
			q->rear = NULL;
		node = q->front;
		q->front = q->front->next;
		free(node);
		q->size--;
	}
}

void destroyQueue(Queue *q){
	// TODO: Cerinta 2
	while(!isQueueEmpty(q)) {
		dequeue(q);
	}
	if(q)
		free(q);
}

#endif
