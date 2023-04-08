#ifndef ORDERED_SET_H
#define ORDERED_SET_H

#include <stdio.h>                                          // needed for printf
#include <stdlib.h>                           // needed for alloc, realloc, free

/*
  IMPORTANT!

  As we stick to pure C, we cannot use templates. We will just asume
  some type T was previously defined.
*/

// -----------------------------------------------------------------------------

/*
  OrderedSet represents a set with elements of type T stored in memory
  in an ordered fashion.
*/

typedef struct OrderedSet {
  T* elements;                    // pointer to where the elements are in memory
  long size;                                // the number of elements in the set
  long capacity;                      // the current capacity (allocated memory)
} OrderedSet;

// -----------------------------------------------------------------------------

/*
  Function that creates an OrderedSet with elements of type T with a given
  initial capacity.
*/

OrderedSet* createOrderedSet(long initialCapacity) {

  OrderedSet* newSet = (OrderedSet*) malloc(sizeof(OrderedSet));

  newSet->size = 0;                        // initially, the set is empty
  newSet->capacity = initialCapacity;
  newSet->elements = (T*)malloc(initialCapacity * sizeof(T));

  return newSet;
}

// -----------------------------------------------------------------------------

/*
  Function that checks if an element exists in a given set. As the
  elements of the set are ordered in memory, this function uses binary
  search.

  Function returns 1 if the given element exists in the set, and 0
  otherwise.
*/

int contains(OrderedSet* set, const T element) {
  for (long i = 0; i < set->size; i++) {
    if (element == set->elements[i]) {
      return 1;
    }
  }
  return 0;
}

// -----------------------------------------------------------------------------

/*
  Function that adds a new element to a set. If the element was
  already in the set, nothing changes. Otherwise, the element is added
  to the set. In the end, the elements must be in ascending order.
*/

void add(OrderedSet* set, const T newElement) {
  if(contains(set, newElement)) {
    return;
  }
    T* vect;
    if(set->size == set->capacity)
      {
        set->capacity *= 2;
        vect = realloc(set->elements, set->capacity * sizeof(T));
        if (vect == NULL)
            return;
        set->elements = vect;
      }
    long i = set->size - 1;
    for (i; i >= 0; i--) {
      if (set->elements[i] < newElement) {
        break;
      }
      else {
        set->elements[i + 1] = set->elements[i];
      }
    }
    set->elements[i + 1] = newElement; 
    set->size++;
  }

// -----------------------------------------------------------------------------

/*
  This function takes two ordered sets with elements of type T and it
  returns the addres of a new ordered set representing the union of
  the two
*/

OrderedSet* unionOrderedSets(OrderedSet* s1, OrderedSet* s2) {
  OrderedSet* uni = createOrderedSet(s1->capacity + s2->capacity);
  long i = 0, j= 0;
  while (i < s1->size && j < s2->size) {
    if(s1->elements[i] < s2->elements[j]) {
      uni->elements[uni->size++] = s1->elements[i];
      i++;
    }
    else if(s1->elements[i] > s2->elements[j]) {
      uni->elements[uni->size++] = s2->elements[j];
      j++;
    }
    else if(s1->elements[i] == s2->elements[j]) {
      uni->elements[uni->size++] = s1->elements[i];
      i++;
      j++;
    }
  }
  while (i < s1->size) {
    uni->elements[uni->size++] = s1->elements[i++];
  }
  while (j < s2->size) {
    uni->elements[uni->size++] = s1->elements[j++];
  }
  return uni;
}

// -----------------------------------------------------------------------------

/*
  This function takes two ordered sets with elements of type T and it
  returns the addres of a new ordered set representing the
  intersection of the two
*/

OrderedSet* intersectOrderedSets(OrderedSet* s1, OrderedSet* s2) {
  OrderedSet* uni = createOrderedSet(s1->capacity + s2->capacity);
  long i = 0, j= 0;
  while (i < s1->size && j < s2->size) {
    if(s1->elements[i] < s2->elements[j]) {
      i++;
    }
    else if(s1->elements[i] > s2->elements[j]) {
      j++;
    }
    else if(s1->elements[i] == s2->elements[j]) {
      uni->elements[uni->size++] = s1->elements[i];
      i++;
      j++;
    }
  }
  return uni;
}


// -----------------------------------------------------------------------------

#endif
