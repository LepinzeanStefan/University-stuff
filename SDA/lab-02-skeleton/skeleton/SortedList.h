#ifndef SORTED_LIST_H
#define SORTED_LIST_H

#include <stdlib.h>
#include <assert.h>

/*
    IMPORTANT!

    As we stick to pure C, we cannot use templates. We will just asume
    some type T was previously defined.
*/

// -----------------------------------------------------------------------------

typedef struct node {
    T value;
    struct node* next;
} TNode, *TSortedList;

// TODO : Cerința 1 & Cerința 2

TSortedList create(T value) {
    TSortedList head = (TSortedList) calloc(1, sizeof(TNode));
    head->next = NULL;
    head->value = value;
    return head;
}

int isEmpty(TSortedList head) {
    if(head == NULL)
        return 1;
    return 0;
}

int contains(TSortedList head, T element) {
    TSortedList iter = head;
    while(iter != NULL) {
        if(iter->value == element)
            return 1;
        iter = iter->next;
    }
    return 0;
}

TSortedList insert(TSortedList head, T element) {
    // daca lista este vida o initializam
    if(head == NULL) {
        head = create(element);
        return head;
    }

    TSortedList nod = create(element), iter = head;

    if(nod->value < iter->value) {
        nod->next = iter;
        return nod;
    }
    TSortedList prev = iter;
    iter = iter->next;

    while(iter != NULL) {
        if(iter->value >= nod->value) {
            nod->next = iter;
            prev->next = nod;
            return head;
        }
        prev = iter;
        iter = iter->next;
    }

    prev->next = nod;
    return head;
}

TSortedList deleteOnce(TSortedList head, T element){
    TSortedList prev = head;
    if(head->value == element) {
        head = head->next;
        free(prev);
        return head;
    }

    TSortedList iter = prev->next;
    while(iter->next) {
        if(iter->value == element) {
            prev->next = iter->next;
            free(iter);
            return head;
        }
        prev = iter;
        iter = iter->next;
    }

    free(iter);
    prev->next = NULL;
    return head;
}

long length(TSortedList head){
    long len = 0;
    TSortedList iter = head;
    while(iter) {
        len++;
        iter = iter->next;
    }
    return len;
}

T getNth(TSortedList head, unsigned n){
    T value;
    TSortedList iter = head;
    for(unsigned i = 1; i < n; i++) {
        iter = iter->next;
    }
    value = iter->value;
    return value;
}

TSortedList freeTSortedList(TSortedList head){
    TSortedList prev = head, iter = head->next;
    while(iter) {
        free(prev);
        prev = iter;
        iter = iter->next;
    }
    free(prev);
    return iter;
}

#endif
