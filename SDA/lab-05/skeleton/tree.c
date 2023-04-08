/*
*	Created by Nan Mihai on 28.03.2021
*	Copyright (c) 2021 Nan Mihai. All rights reserved.
*	Laborator 5 - Structuri de date
*	Facultatea de Automatica si Calculatoare
*	Anul Universitar 2020-2021, Seria CD
*/
#include "tree.h"

/*
*	Funcție care creează un arbore cu un singur nod
*/
Tree createTree(Item value) {
	Tree root = malloc(sizeof(TreeNode));
	root->value = value;
	root->left = NULL;
	root->right = NULL;
	return root;
}

/*
*	Funcție care inițializează un nod de arbore
*		- îi alocă memorie
*		- îi setează câmpul valoare
*		- setează left și right să pointeze către NULL
*/
void init(Tree *root, Item value) {
	*root = (Tree) calloc(1, sizeof(struct node));
	(*root)->value = value;
}

/*
*	Funcție care inserează o valoare într-un arbore binar, respectând
* proprietățile unui arbore binar de căutare
*/
Tree insert(Tree root, Item value) {
	if (root == NULL) {
		init(&root, value);
		return root;
	}
	Tree iter = root;
	while (iter) {
		if (iter->value == value)
			return root;
		if (iter->value > value) {
			if (iter->left == NULL) {
				iter->left = createTree(value);
				return root;
			}
			iter = iter->left;
			continue;
		}
		if (iter->value < value) {
			if (iter->right == NULL) {
				iter->right = createTree(value);
				return root;
			}
			iter = iter->right;
			continue;
		}
	}
	return root;
}

/*
*	Funcție care afișează nodurile folosind parcurgerea în postordine
*/
void printPostorder(Tree root) {
	if (root) {
		printPostorder(root->left);
		printPostorder(root->right);
		printf("%d ", root->value);
	}
}

/*
*	Funcție care afișează nodurile folosind parcurgerea în preordine
*/
void printPreorder(Tree root) {
	if (root) {
		printf("%d ", root->value);
		printPreorder(root->left);
		printPreorder(root->right);
	}
}

/*
*	Funcție care afișează nodurile folosind parcurgerea în inordine
*/
void printInorder(Tree root) {
	if (root) {
		printInorder(root->left);
		printf("%d ", root->value);
		printInorder(root->right);
	}
}

/*
 * Functie recursiva pentru dealocare
*/
Tree destroyTree(Tree root) {
	if (root == NULL)
		return NULL;
	root->left = destroyTree(root->left);
	root->right = destroyTree(root->right);
	free(root);
	return NULL;
}

/*
*	Funcție care dealocă întreaga memorie alocată pentru un arbore binar
*		- root va pointa către NULL după ce se va apela funcția
*/
void freeTree(Tree *root) {
	if (*root == NULL) 
		return;
	(*root) = destroyTree(*root);
}


/*
*	Funcție care determină numărul de noduri dintr-un arbore binar
*/
int size(Tree root) {
	if (root)
		return 1 + size(root->left) + size(root->right);
	return 0;
}

/*
*	Funcție care returnează adâncimea maximă a arborelui
*/
int maxDepth(Tree root) {
	if (root) {
		int left = maxDepth(root->left);
		int right = maxDepth(root->right);
		if (left < right)
			return 1 + right;
		return 1 + left;
	}
	return -1;
}

/*
*	Funcție care construiește oglinditul unui arbore binar
*/
void mirror(Tree root) {
	if (root == NULL)
		return;
	mirror(root->left);
	mirror(root->right);
	Tree tmp = root->left;
	root->left = root->right;
	root->right = tmp;
}

/*
*	Funcție care verifică dacă doi arbori binari sunt identici
*/
int sameTree(Tree root1, Tree root2) {
	if (root1 == NULL && root2 == NULL)
		return 1;
	if (root1 == NULL || root2 == NULL)
	    return 0;
	if (root1->value != root2->value)
		return 0;
	return (sameTree(root1->left, root2->left) && sameTree(root1->right, root2->right));
}
