#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "heap.h"

typedef int TCost;
#define INFINITY 999999
#define MAXSIZE 20 // We assume that a name does not go longer

typedef struct node {
	int v;
	TCost c;
	struct node *next;
} TNode, *ATNode;

typedef struct graph {
	int nn;
	ATNode *adl;
}	TGraphL;

typedef struct name {
    char nam[MAXSIZE];
}NodeName;

int cmpfunc (const void * a, const void * b) {
   return ( *(int*)a - *(int*)b );
}

int getCost(TGraphL g, int u, int v)
{
	ATNode tmp = g.adl[u];
	while (tmp != NULL) {
		if (tmp->v == v)
			return tmp->c;
		tmp = tmp->next;
	}
	return INFINITY;
}


void alloc_list(TGraphL * G, int n)
{
    int i;
    G->nn = n;
	G->adl = (ATNode*) malloc((n+1)*sizeof(ATNode));
	for(i = 0; i < n; i++)
		G->adl[i] = NULL;

}

void free_list(TGraphL *G)
{
    int i;
    ATNode it;
    for(i = 0; i < G->nn; i++){
		it = G->adl[i];
		while(it != NULL){
			ATNode aux = it;
			it = it->next;
			free(aux);
		}
		G->adl[i] = NULL;
	}
	G->nn = 0;
	free(G->adl);
}

void insert_edge_list(TGraphL *G, int v1, int v2, int c)
{
    TNode *t;
    t=(ATNode)malloc(sizeof(TNode));
    t->v = v2; t->c=c; t->next = G->adl[v1]; G->adl[v1]=t;
    t=(ATNode)malloc(sizeof(TNode));
    t->v = v1;  t->c=c; t->next = G->adl[v2]; G->adl[v2]=t;
}

// Calculates the sum of distances in a tree
int calctree(int* parent,int root,int* dist,int size, int sum) {
    for (int i = 0; i < size; i++) {
        if (parent[i] == root) {
            sum += dist[i];
            sum = calctree(parent, i, dist, size, sum);
        }
    }
    return sum;
}

/**
 * Creates a path (tree) that covers the whole graph using Prim's algorithm.
 * It also works on separated components, the root of each component will
 * have the parent -1.
 */
void Prim(TGraphL G, FILE* fout)
{
    int i, v;
    int V = G.nn;
    int parent[V]; // path of the tree
    int d[V]; // costs of said path

    MinHeap* h = newQueue(V);
	h->size = V; // Initially, the heap will have all the nodes
	d[0] = 0; // the "0" node of the graph will be considered the first root
    h->elem[0] = newNode(0, d[0]);
    h->pos[0] = 0;
	parent[0] = -1;

    for (v = 1; v < V; v++)
    {
		// All costs are "infinite" at the start
        parent[v] = -1;
        d[v] = INFINITY;
        h->elem[v] = newNode(v, d[v]);
        h->pos[v] = v;
    }

    while (!isEmpty(h))
    {
        MinHeapNode* first = removeMin(h);
        int u = first->v;
        TNode* tmp = G.adl[u];

        while (tmp != NULL)
		// search all the paths of a node
        {
            int v = tmp->v;
            if (isInMinHeap(h, v) && tmp->c < d[v])
            {
				// If a path has a lower cost, then update the tree
                d[v] = tmp->c;
                parent[v] = u;
                SiftUp(h, v, d[v]);
            }
            tmp = tmp->next;
        }
		free(first);
    }
    
    int sum[V]; // Calculate the sum of the paths here
    int j = 0; // Iterator for the sum
    sum[0] = -1;
    for (i = 0; i < G.nn; ++i) {
        if (parent[i] == -1) {
            sum[++j] = calctree(parent, i, d, V, 0); // Found a root to a tree
        }
	}
    fprintf(fout, "%d\n", j);
    ++j;
    qsort(sum, j, sizeof(int), cmpfunc);
    for (i = 1; i < j; ++i) {
        fprintf(fout, "%d\n", sum[i]);
    }
	free(h->elem);
	free(h->pos);
	free(h);
}

int main()
{
    int i, j, v1, v2, c;
    char s1[MAXSIZE], s2[MAXSIZE];
	int V, E;
	TGraphL G;
	FILE* fin = fopen ("tema3.in", "r");
    FILE* fout = fopen("tema3.out", "w");
	fscanf (fin, "%d %d", &V, &E);
	alloc_list(&G, V);
    NodeName* nodes = calloc(V, sizeof(NodeName));
    int size = 0; // the current size of the name vector

	for (i=1; i<=E; i++)
	{
        int ok = 0; // Boolean that indicates if we found the name
		fscanf(fin,"%s %s %d", s1, s2, &c);
        for (j = 0; j < size; ++j) {
            if (strcmp(nodes[j].nam, s1) == 0) {
                // We already have this node
                ok = 1;
                v1 = j;
                break;
            }
        }
        if (ok == 0) {
            v1 = size;
            strcpy(nodes[size].nam, s1);
            size++;
        }

        ok = 0;
        for (j = 0; j < size; ++j) {
            if (strcmp(nodes[j].nam, s2) == 0) {
                // We already have this node
                ok = 1;
                v2 = j;
                break;
            }
        }
        if (ok == 0) {
            v2 = size;
            strcpy(nodes[size].nam, s2);
            size++;
        }

	    insert_edge_list(&G, v1, v2, c);
	}

	Prim(G, fout);

    fclose(fout);
    fclose(fin);
	free_list(&G);
    free(nodes);
	return 0;
}
