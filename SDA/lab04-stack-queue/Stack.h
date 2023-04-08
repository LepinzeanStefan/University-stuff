#ifndef STACK_H_
#define STACK_H_

#include <stdio.h>
#include <stdlib.h>

typedef struct StackNode{
	Item elem;
	struct StackNode *next;
} StackNode;

typedef struct Stack{
	StackNode* head;  // Varful stivei
	long size; // Numarul de elemente din stiva
} Stack;

Stack* createStack(void){
	// TODO: Cerinta 1
	Stack* stiva = (Stack *) calloc(1, sizeof(Stack));
	return stiva;
}

int isStackEmpty(Stack* stack){
	// TODO: Cerinta 1
	if(stack == NULL || stack->head == NULL)
		return 1;
	return 0;
}

void push(Stack *stack, Item elem){
	// TODO: Cerinta 1
	StackNode* node = (StackNode *) calloc(1, sizeof(StackNode));
	node->elem = elem;
	node->next = stack->head;
	stack->head = node;
	stack->size++;
}

Item top(Stack *stack){	
	if(isStackEmpty(stack))
		exit(1);
	return stack->head->elem;
} 

void pop(Stack *stack){
	// TODO: Cerinta 1
	StackNode* node;
	if(isStackEmpty(stack))
		return;
	node = stack->head;
	stack->head = stack->head->next;
	free(node);
	stack->size--;
}

void destroyStack(Stack *stack){
	// TODO: Cerinta 1
	while(stack->head != NULL) {
	StackNode* node = stack->head;
	stack->head = stack->head->next;
	free(node);
	}
	if(stack)
		free(stack);
}

#endif 
