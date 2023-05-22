#ifndef __HEAP_H__
#define __HEAP_H__

#include <stdlib.h>

typedef struct MinHeapNode
{
    int  v; // refers to the index of the node
    int d; // the distance between two nodes
} MinHeapNode;


typedef struct MinHeap
{
    int size;
    int capacity;
    int *pos; // refers to the position of a node in the heap
    MinHeapNode **elem;
} MinHeap;

// Creates a new node to be added in the heap
MinHeapNode* newNode(int v, int d)
{
    MinHeapNode* tmp =(MinHeapNode*) calloc(1, sizeof(MinHeapNode));
    tmp->v = v;
    tmp->d = d;
    return tmp;
}

// Creates a heap
MinHeap* newQueue(int capacity)
{
    MinHeap* h =(MinHeap*) calloc(1, sizeof(MinHeap));
    h->pos = (int *)calloc(capacity, sizeof(int));
    h->size = 0;    
    h->capacity = capacity;
    h->elem =(MinHeapNode**) calloc(capacity,  sizeof(MinHeapNode*));
    return h;
}

// Swaps the values of two nodes in the heap
void swap(MinHeapNode** a, MinHeapNode** b)
{
    MinHeapNode* tmp = *a;
    *a = *b;
    *b = tmp;
}

// Reorders the heap from the index given 
void SiftDown(MinHeap* h, int idx)
{
    int smallest, left, right;
    smallest = idx;
    left = 2 * idx + 1;
    right = 2 * idx + 2;

    if (left < h->size && h->elem[left]->d < h->elem[smallest]->d )
        smallest = left;

    if (right < h->size && h->elem[right]->d < h->elem[smallest]->d )
        smallest = right;

    if (smallest != idx) {
        MinHeapNode *a = h->elem[smallest];
        MinHeapNode *b = h->elem[idx];
        h->pos[a->v] = idx;
        h->pos[b->v] = smallest;
        swap(&h->elem[smallest], &h->elem[idx]);
        SiftDown(h, smallest);
    }
}

int isEmpty(MinHeap* h)
{
    return h->size == 0;
}

// Removes an element by switching his position with the last element in the heap
MinHeapNode* removeMin(MinHeap* h)
{
    if (isEmpty(h))
        return NULL;
    MinHeapNode* min = h->elem[0];
    MinHeapNode* last = h->elem[h->size - 1];
    h->elem[0] = last;
    h->pos[min->v] = h->size-1;
    h->pos[last->v] = 0;
    h->size--;
    SiftDown(h, 0);
    return min;
}

// Updates the distance of a node in the heap, and reorders the heap accordingly
void SiftUp(MinHeap* h, int v, int d)
{
    int i = h->pos[v];
    h->elem[i]->d = d;
    while (i && h->elem[i]->d < h->elem[(i - 1) / 2]->d) {
        h->pos[h->elem[i]->v] = (i - 1) / 2;
        h->pos[h->elem[(i - 1) / 2]->v] = i;
        swap(&h->elem[i],  &h->elem[(i - 1) / 2]);
        i = (i - 1) / 2;
    }
}

// Checks if a certain node is inside a heap
int isInMinHeap(MinHeap *h, int v)
{   
    if (h->pos[v] < h->size) {
        return 1;
    }
   return 0;
}

#endif

