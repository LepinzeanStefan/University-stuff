/*
*	Created by Nan Mihai on 05.05.2021
*	Copyright (c) 2020 Nan Mihai. All rights reserved.
*	Laborator 6 - Structuri de date
*	Grupa 314CD
*	Facultatea de Automatica si Calculatoare
*	Anul Universitar 2020-2021, Seria CD
*/
#include "tree.h"

/*
*	Funcție care creează un arbore cu un singur nod
*/
Tree createTree(Tree parent, Item value) {
	Tree root = malloc(sizeof(TreeNode));
	root->parent = parent;
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
void init(Tree *root, Tree parent, Item value) {
	*root = createTree(parent, value);
}

/*
*	Funcție care inserează o valoare într-un arbore binar, respectând
* proprietățile unui arbore binar de căutare
*/
Tree insert(Tree root, Item value) {
	if (root == NULL) {
		return createTree(root, value);
	}
	if (root->value == value) {
		return root;
	}
	if (root->value < value && root->right == NULL) {
		root->right = createTree(root, value);
	}
	if (root->value < value && root->right != NULL) {
		root->right = insert(root->right, value);
	}
	if (root->value > value && root->left == NULL) {
		root->left = createTree(root, value);
	}
	if (root->value > value && root->left != NULL) {
		root->left = insert(root->left, value);
	}
	return root;
}


/*
*	Funcție care verifică dacă un arbore este vid sau nu
*		- întoarce 1 dacă arborele este vid
*		- întoarce 0 dacă arborele nu este vid
*/
int isEmpty(Tree root) {
	return root == NULL;
}

/*
*	Funcție care verifică dacă o valoare există sau nu în arbore
*		- 1 dacă value există în arbore
*		- 0 dacă value nu există în arbore
*/
int contains(Tree tree, Item value) {
	while (tree) {
		if (tree->value == value)
			return 1;
		if (tree->value > value) 
			tree = tree->left;
		else
			tree = tree->right;
	}
	return 0;
}

/*
*	Funcție care determină elementul minim dintr-un arbore binar de căutare
*		- NULL pentru arbore vid
*/
Tree minimum(Tree tree) {
	if (tree == NULL)
		return NULL;
	while (tree->left)
		tree = tree->left;
	return tree;
}

/*
*	Funcție care determină elementul maxim dintr-un arbore binar de căutare
*		- NULL pentru arbore vid
*/
Tree maximum(Tree tree) {
	if (tree == NULL)
		return NULL;
	while (tree->right)
		tree = tree->right;
	return tree;
}

/*
*	Funcție care determină succesorul în inordine pentru nodul care conține
* valoarea value.
*/
Tree successor(Tree root, Item value) {
	if (!root)
		return NULL;
	while (root) {
		if (root->value == value)
			break;
		if (root->value > value)
			root = root->left;
		if (root->value < value)
			root = root->right;
	}
	if (!root)
		return NULL;
	if (root->right)
		return minimum(root->right);
	while (root && root->value <= value) {
		root = root->parent;
	}
	return root;
}

/*
*	Funcție care determină predecesorul în inordine pentru nodul care conține
* valoarea value.
*/
Tree predecessor(Tree root, Item value) {
	if (!root)
		return NULL;
	while (root) {
		if (root->value == value)
			break;
		if (root->value > value)
			root = root->left;
		if (root->value < value)
			root = root->right;
	}
	if (!root)
		return NULL;
	if (root->left)
		return maximum(root->left);
	while (root && root->value >= value) {
		root = root->parent;
	}
	return root;
}

/*
*	Funcție care dealocă întreaga memorie alocată pentru un arbore binar
*		- root va pointa către NULL după ce se va apela funcția
*/
void destroyTree(Tree *root) {
	if (*root == NULL)
		return;
	destroyTree(&((*root)->left));
	destroyTree(&((*root)->right));
	Tree tmp = *root;
	free(tmp);
	*root = NULL; 
}

/*
*	Funcție care șterge un nod cu o anumită valoare din arbore
*/
Tree delete(Tree root, Item value) {
	if (root == NULL) {
		return root;
	}
	if (root->value > value)
		root->left = delete(root->left, value);
	else if (root->value < value)
		root->right = delete (root->right, value);
	else if (root->left != NULL && root->right != NULL) {
		Tree tmp = minimum(root->right);
		root->value = tmp->value;
		root->right = delete(root->right, tmp->value);
	}
	else {
		Tree tmp = root;
		if(root->left != NULL)
			root = root->left;
		else
			root = root->right;
		free(tmp);
	}
	return root;
}

/*
*	Funcție care determină cel mai apropiat strămoș comun pentru
* două noduri având cheile value1 și value2
*/
Tree lowestCommonAncestor(Tree root, Item value1, Item value2) {
	while (root) {
		if (root->value < value1 && root->value < value2) {
			root = root->right;
			continue;
		}
		if (root->value > value1 && root->value > value2) {
			root = root->left;
			continue;
		}
		if (root->value == value1 || root->value == value2) {
			root = NULL;
			break;
		}
		break;
	}
	return root;
}
