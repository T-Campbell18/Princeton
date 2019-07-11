/*-------------------------------------------------------------------*/
/* symtablelist.c                                                    */
/* Author: Tyler Campbell                                            */
/*-------------------------------------------------------------------*/

#include "symtable.h"

/* Node struct of bindings for linked list*/
struct Node
{
	/* Key */
	char *key;
	/* Value */
	void *value;
	/* pointer to next Node */
	struct Node *next;
};

/* Linked list to represent Symbol Table */
struct SymTable
{
	/* Number of bindings */
	size_t length;
	/* pointer to first Node of SymTable */
	struct Node *first;
};


SymTable_T SymTable_new(void)
{
	SymTable_T oSymTable;
	
	oSymTable = (SymTable_T)malloc(sizeof(struct SymTable));
	if (oSymTable == NULL)
		return NULL;
	
	oSymTable->length = 0;
	oSymTable->first = NULL;
	return oSymTable;
}

void SymTable_free(SymTable_T oSymTable)
{
	struct Node *current, *next;
	
	assert(oSymTable != NULL);
	
	for (current = oSymTable->first; current != NULL;
                                                current = next)
	{
		next = current->next;
		free(current->key);
		free(current);
	}
	free(oSymTable);
}

size_t SymTable_getLength(SymTable_T oSymTable)
{
	assert(oSymTable != NULL);
	
	return oSymTable->length;
}

int SymTable_put(SymTable_T oSymTable, const char *pcKey,
                                                  const void *pvValue)
{
	struct Node *newNode;
	
	assert(oSymTable != NULL);
	assert(pcKey != NULL);
	
	if (SymTable_contains(oSymTable, pcKey))
		return 0;
		
 	newNode = (struct Node*)malloc(sizeof(struct Node));
	if (newNode == NULL)
		return 0;
		
	newNode->key = malloc(strlen(pcKey) + 1);
	if (newNode->key == NULL) 
	{
		free(newNode);
		return 0;
	}
	strcpy(newNode->key, pcKey);
	newNode->value = (void *)pvValue;
	newNode->next = oSymTable->first;
	oSymTable->first = newNode;
	oSymTable->length++;
	return 1;
}

void *SymTable_replace(SymTable_T oSymTable, const char *pcKey, 
			      			const void *pvValue)
{
	struct Node *current;
	void *tmp;
	
	assert(oSymTable != NULL);
	assert(pcKey != NULL);
	
	if (!SymTable_contains(oSymTable, pcKey))
		return NULL;
	
	for (current = oSymTable->first; current != NULL; 
		       			current = current->next)
	{
		if (strcmp(pcKey, current->key) == 0) 
		{
			tmp = current->value;
			current->value = (void *)pvValue;
			return tmp;
		}
	}	
	return NULL;		
}

int SymTable_contains(SymTable_T oSymTable, const char *pcKey)
{
	struct Node *current;
	
	assert(oSymTable != NULL);
	assert(pcKey != NULL);	
	
	for (current = oSymTable->first; current != NULL; 
		       			  current = current->next)
	{
		if (strcmp(pcKey, current->key) == 0)
			return 1;
	}
	return 0;	
}

void *SymTable_get(SymTable_T oSymTable, const char *pcKey)
{
	struct Node *current;
		
	assert(oSymTable != NULL);
	assert(pcKey != NULL);	
	
	if (!SymTable_contains(oSymTable, pcKey))
		return NULL;
	
	for (current = oSymTable->first; current != NULL; 
	                                       current = current->next)
	{
		if (strcmp(pcKey, current->key) == 0)
			return current->value;
	}
	return NULL;
}

void *SymTable_remove(SymTable_T oSymTable, const char *pcKey)
{
	struct Node *current, *prev;
	void *tmp;
	
	assert(oSymTable != NULL);
	assert(pcKey != NULL);
	
	if (!SymTable_contains(oSymTable, pcKey))
		return NULL;
		
	prev = NULL;
	for (current = oSymTable->first; current != NULL;
                               prev = current, current = current->next)
	{
		if (strcmp(pcKey, current->key) == 0) 
		{
			tmp = current->value;
			
			if (prev == NULL)
			     oSymTable->first = oSymTable->first->next;
			else
                             prev->next = current->next;

			free(current->key);
			free(current);
			oSymTable->length--;
			return tmp;
		}
	}	
	return NULL;		 
}


void SymTable_map(SymTable_T oSymTable, void (*pfApply)
(const char *pcKey, void *pvValue, void *pvExtra), const void *pvExtra)
{
	struct Node *current;

	assert(oSymTable != NULL);
	assert(pfApply != NULL);

	for (current = oSymTable->first; current != NULL; 
	current = current->next)
	(*pfApply)(current->key, current->value, (void *)pvExtra);
}
