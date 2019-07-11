/*-------------------------------------------------------------------*/
/* symtablehash.c                                                    */
/* Author: Tyler Campbell                                            */
/*-------------------------------------------------------------------*/

#include "symtable.h"

/* Bucket sizes for expanding hash table */
static const size_t bucketSizes[] = {509, 1021, 2039, 4093, 8191,
                                   16381, 32749, 65521};

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

/* Array of linked list to represent Symbol Table */
struct SymTable
{
	/* Number of bindings */
	size_t length;
	/* Number of buckets */
	size_t bucketCount;
	/* Index for bucketSize array */
	size_t iBucketSize;
	/* pointer to first bucket*/
	struct Node **bucket;
};

/* Return a hash code for pcKey that is between 0 and uBucketCount-1,
	inclusive. */
static size_t SymTable_hash(const char *pcKey, size_t uBucketCount)
{
	enum {HASH_MULTIPLIER = 65599};
	size_t u;
	size_t uHash = 0;

	assert(pcKey != NULL);

	for (u = 0; pcKey[u] != '\0'; u++)
           uHash = uHash * (size_t)HASH_MULTIPLIER + (size_t)pcKey[u];
		
	return uHash % uBucketCount;
}

/* Expands oSymTable to the next bucket count if sufficient memory, 
otherwise no change to oSymTable is made */
static void SymTable_expand(SymTable_T oSymTable)
{
	struct Node **newBucket, *current, *tmp;
	size_t i, j, newI;

	assert(oSymTable != NULL);
	
	j = oSymTable->iBucketSize;
	if (j < 7)
		j++;
	else 
		return;
	
	newBucket = (struct Node **)calloc(bucketSizes[j],
                                           sizeof(struct Node *));
	if (newBucket == NULL)
		return;
		
	for (i = 0; i < oSymTable->bucketCount; i++) 
	{
        	for (current = oSymTable->bucket[i]; current != NULL;
                                                        current = tmp) 
		{
			tmp = current->next;
                        newI = SymTable_hash(current->key,
                                             bucketSizes[j]);
	        	current->next = newBucket[newI];
                        newBucket[newI] = current;
		}
	}
	free(oSymTable->bucket);
	oSymTable->bucket = newBucket;
	oSymTable->bucketCount = bucketSizes[j];
	oSymTable->iBucketSize = j;
}

SymTable_T SymTable_new(void)
{
	SymTable_T oSymTable;
	struct Node **tmp;
	
	oSymTable = (SymTable_T) malloc(sizeof(struct SymTable));
	if (oSymTable == NULL)
		return NULL;
			
	tmp = (struct Node **)calloc(sizeof(struct Node *),
                                     bucketSizes[0]);
	if (tmp == NULL) 
	{
		free(oSymTable);
		return NULL;
	}
	oSymTable->iBucketSize = 0;
	oSymTable->length = 0;
	oSymTable->bucketCount = bucketSizes[0];
	oSymTable->bucket = tmp;
	return oSymTable;		
}


void SymTable_free(SymTable_T oSymTable)
{
	struct Node *current, *next;
	size_t i;

	assert(oSymTable != NULL);

	for (i = 0; i < oSymTable->bucketCount; i++)
		for (current = oSymTable->bucket[i]; current != NULL;
                                                       current = next) 
		{
			next = current->next;
			free(current->key);
			free(current);
		}
	free(oSymTable->bucket);
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
	size_t i;
	
	assert(oSymTable != NULL);
	assert(pcKey != NULL);
	
	if (SymTable_contains(oSymTable, pcKey))
		return 0;
		
	newNode = (struct Node *)malloc(sizeof(struct Node));
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
	
	if (oSymTable->length > oSymTable->bucketCount)
		SymTable_expand(oSymTable);
	
	i = SymTable_hash(pcKey, oSymTable->bucketCount);
	newNode->next = oSymTable->bucket[i];
	oSymTable->bucket[i] = newNode;
	oSymTable->length++;
	return 1;	
}

void *SymTable_replace(SymTable_T oSymTable, const char *pcKey,
                                                 const void *pvValue)
{
	struct Node *current;
	void *tmp;
	size_t i;

	assert(oSymTable != NULL);
	assert(pcKey != NULL);

	if (!SymTable_contains(oSymTable, pcKey))
		return NULL;
		
	i = SymTable_hash(pcKey, oSymTable->bucketCount);
	for (current = oSymTable->bucket[i]; current != NULL;
                                              current = current->next) 
	{
		if (strcmp(current->key, pcKey) == 0) 
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
	size_t i;
	
	assert(oSymTable != NULL);
	assert(pcKey != NULL);
	
	i = SymTable_hash(pcKey, oSymTable->bucketCount);
	for (current = oSymTable->bucket[i]; current != NULL;
                                              current = current->next) 
	{
		if (strcmp(current->key, pcKey) == 0) 
			return 1;
	}
	return 0;
}


void *SymTable_get(SymTable_T oSymTable, const char *pcKey)
{
	struct Node *current;
	size_t i;
		
	assert(oSymTable != NULL);
	assert(pcKey != NULL);
	
	if (!SymTable_contains(oSymTable, pcKey))
		return NULL;
	
	i = SymTable_hash(pcKey, oSymTable->bucketCount);
	for (current = oSymTable->bucket[i]; current != NULL;
                                               current= current->next) 
	{
		if (strcmp(current->key, pcKey) == 0) 
			return current->value;
	}
	return NULL;
}

void *SymTable_remove(SymTable_T oSymTable, const char *pcKey)
{
	struct Node *current, *prev;
	void *tmp;
	size_t i;
			
	assert(oSymTable != NULL);
	assert(pcKey != NULL);
	
	if (!SymTable_contains(oSymTable, pcKey))
		return NULL;
		
	prev = NULL;
	i = SymTable_hash(pcKey, oSymTable->bucketCount);
	for (current = oSymTable->bucket[i]; current != NULL;
                              prev = current, current = current->next) 
	{
		if (strcmp(current->key, pcKey) == 0) 
		{
			tmp = current->value;

			if (prev == NULL)
				oSymTable->bucket[i] = current->next;
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
 (const char *pcKey,void *pvValue, void *pvExtra), const void *pvExtra)
{
	struct Node *current;
	size_t i;

	assert(oSymTable != NULL);
	assert(pfApply != NULL);
	
	for (i = 0; i < oSymTable->bucketCount; i++)
	{
		for (current = oSymTable->bucket[i]; current != NULL;
                                               current = current->next)
                   (*pfApply)(current->key, current->value,
                              (void *)pvExtra);
	}
}
