/* LEPINZEAN STEFAN - 315CD */
#ifndef _MAGIC_BAND_
#define _MAGIC_BAND_
#include <stdlib.h>
#include <stdio.h>

typedef char Item;

// -------------------------------------------------------------------
// Double linked list that will represent the magic band
typedef struct ListNode {
    Item elem; // the value of the character
    struct ListNode* next; // link to the next node
    struct ListNode* prev; // link to the previous node
} ListNode;

typedef struct List {
    ListNode* sentinel; // the start of the list, it holds no elem
    ListNode* cursor; // pointer to the current location of the cursor
} List;
// --------------------------------------------------------------------

// Create a new node for the magic list
ListNode* createNodeMagic(Item elem) {
    ListNode *node = (ListNode *)malloc(sizeof(ListNode));
	node->elem = elem;
	node->next = node->prev = NULL;
	return node;
}
// --------------------------------------------------------------------

// Create a new list, initialize a sentinel and the first node
List* createList(void){
	List *list = (List *)malloc(sizeof(List));
    list->sentinel = createNodeMagic('\0'); // initializes the sentinel with no value
    list->cursor = createNodeMagic('#'); // initializes the last node with '#'
    list->cursor->prev = list->sentinel;
    list->sentinel->next = list->cursor;
	return list;
}
// --------------------------------------------------------------------

/**
 * Moves the cursor to the left
 * If the cursor is on the first node it will not move further
 */
void moveLeft(List* list) {
    if (list->cursor->prev == list->sentinel)
        return;
    list->cursor = list->cursor->prev;
}

/**
 * Moves cursor to the right
 * If the cursor is on the last node and it will create a new node with value '#' 
 */
void moveRight(List* list) {
    if (list->cursor->next == NULL) {
        ListNode* node = createNodeMagic('#');
        list->cursor->next = node;
        node->prev = list->cursor;
    }
    list->cursor = list->cursor->next;
}
// --------------------------------------------------------------------

/**
 * Moves the cursor to the left until a certain character is reached
 * If said character is not found, the cursor will not moved
 */
void moveLeftChar(List* list, Item ch, FILE* out) {
    ListNode* cursor = list->cursor;
    while (cursor != list->sentinel && cursor->elem != ch) {
        cursor = cursor->prev;
    }
    if (cursor == list->sentinel) {
        fprintf(out, "ERROR\n"); // The search did not find the character
        return;
    }
    list->cursor = cursor;
}
// --------------------------------------------------------------------

/**
 * Moves the cursor to the right until a certain character is reached
 * If said character is not found, the cursor will be moved to a new
 * node created right after the last one in the list
 */
void moveRightChar(List* list, Item ch) {
ListNode* cursor = list->cursor;
    while (cursor->next != NULL && cursor->elem != ch) {
        cursor = cursor->next;
    }
    if (cursor->next == NULL && cursor->elem != ch) {
        ListNode* node = createNodeMagic('#');
        node->prev = cursor;
        cursor->next = node;
        cursor = node;
    }
    list->cursor = cursor;
}
// --------------------------------------------------------------------

/**
 * Overrides the character stored in the cursor with a new value
 */
void write(List* list, Item ch) {
    list->cursor->elem = ch;
}
// --------------------------------------------------------------------

/**
 * Creates a new element to the left of the cursor and moves the cursor left
 * Also updates the links of the list to include the new element
 */
void insertLeft(List* list, Item ch, FILE* out) {
    if (list->cursor->prev == list->sentinel) {
        fprintf(out, "ERROR\n"); //You cannot insert at the start of the list
        return;
    }
    ListNode* node = createNodeMagic(ch);
    node->prev = list->cursor->prev;
    list->cursor->prev->next = node;
    list->cursor->prev = node;
    node->next = list->cursor;
    list->cursor = node;
}
// --------------------------------------------------------------------

/**
 * Creates a new element to the right of the cursor and moves the cursor right
 * Even if the cursor is at the end of the list the insertion will not change
 */
void insertRight(List* list, Item ch) {
    ListNode* node = createNodeMagic(ch);
    node->next = list->cursor->next;
    // Checks if the cursor is at the end of the list to prevent seg fault
    if (list->cursor->next != NULL)
        list->cursor->next->prev = node;
    list->cursor->next = node;
    node->prev = list->cursor;
    list->cursor = node;
}
// --------------------------------------------------------------------

/**
 * Frees the entire list
 */
void destroyList(List* list) {
    ListNode* iter = NULL;
    list->cursor = NULL;
    while (list->sentinel) {
        iter = list->sentinel;
        list->sentinel = list->sentinel->next;
        free(iter);
    }
    free(list);
}
// --------------------------------------------------------------------

// Stack that will be used for the undo/redo commands
typedef struct stack {
    ListNode* pos; // pointer to the previous position of the cursor
    struct stack* next; // pointer to the next element in the stack
} Stack;
// --------------------------------------------------------------------

/**
 * Initiates a stack with the previous position of the cursor
 */
Stack* initStack(ListNode* pos) {
    Stack* s = (Stack*) malloc(sizeof(struct stack));
    s->pos = pos;
    s->next = NULL;
    return s;
}
// --------------------------------------------------------------------

/**
 * Checks if the stack is not initiated
 */
int isEmpyStack(Stack* s) {
    if (s == NULL)
        return 1;
    return 0;
}
// --------------------------------------------------------------------

/**
 * Adds another element to the end of the stack
 */
Stack* pushStack(Stack* s, ListNode* pos) {
    Stack* top;
    if (isEmpyStack(s))
        return initStack(pos);
    top = initStack(pos);
    top->next = s;
    return top;
}
// --------------------------------------------------------------------

/**
 * Removes the first element of the stack, respecting the FIFO principle
 */
Stack* pop(Stack* s) {
    if (isEmpyStack(s))
        return s;
    if (s->next == NULL) {
        s->pos = NULL;
        free(s);
        return NULL;
    }
    Stack* tmp = s;
    s = s->next;
    free(tmp);
    return s;
}
// --------------------------------------------------------------------


/**
 * Shows the position pointed by the first element of the stack
 */
ListNode* topStack(Stack* s) {
    if (isEmpyStack(s))
        return NULL;
    return s->pos;
}
// --------------------------------------------------------------------

/**
 * Frees the stack
 */
Stack* freeStack(Stack* s) {
    Stack* tmp;
    while(s) {
        tmp = s;
        s = s->next;
        free(tmp);
    }
    return NULL;
}
// --------------------------------------------------------------------

#endif
