#ifndef __HEAP_H__
#define __HEAP_H__

#include <stdlib.h>
#include <math.h>

typedef struct MinHeapNode
{
    int  v;
    int d;
} MinHeapNode;


typedef struct MinHeap
{
    int size;
    int capacity;
    int *pos;
    MinHeapNode **elem;
} MinHeap;


MinHeapNode *newNode(int v, int d)
{
    MinHeapNode *n = (MinHeapNode *) malloc(sizeof(MinHeapNode));
    
    n->v = v;
    n->d = d;

    return n;
}

MinHeap *newQueue(int capacity)
{
  MinHeap *h = (MinHeap *) malloc(sizeof(MinHeap));

  h->pos = (int *) malloc(capacity * sizeof(int));
  h->size = 0;
  h->capacity = capacity;
  h->elem = (MinHeapNode **) malloc(capacity * sizeof(MinHeapNode *));
  
  return h;
}

void swap(MinHeapNode **a, MinHeapNode **b)
{
   MinHeapNode *t = *a;

   *a = *b;
   *b = t;
}

void SiftDown(MinHeap *h, int idx)
{
    int smallest, left, right;
    smallest = idx;
    left = 2 * idx + 1;
    right = 2 * idx + 2;

    if (left < h->size && h->elem[left]->d < h->elem[smallest]->d)
    {
        smallest = left;
    }

    if (right < h->size && h->elem[right]->d < h->elem[smallest]->d)
    {
        smallest = right;
    }

    if (smallest != idx)
    {
        MinHeapNode *a = h->elem[smallest];
    }
}

int isEmpty(MinHeap *h)
{
    return h->size == 0;
}

MinHeapNode *removeMin(MinHeap *h)
{
    if (isEmpty(h))
    {
        return NULL;
    }

    MinHeapNode *min = h->elem[0];
    MinHeapNode *last = h->elem
}

void SiftUp(MinHeap *h, int v, int d)
{
    
}

int isInMinHeap(MinHeap *h, int v)
{
    if (h->pos[v] < h->size)
    {
        return 1;
    }
    
    return 0;
}

#endif

