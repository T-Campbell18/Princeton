/*-------------------------------------------------------------------*/
/* symtable.h                                                        */
/* Author: Tyler Campbell                                            */
/*-------------------------------------------------------------------*/
#ifndef SYMTABLE
#define SYMTABLE

#include <stdio.h>
#include <assert.h>
#include <stdlib.h>
#include <string.h>

/* Defines a SymTable object that is an unordered collection of 
bindings (a binding consists of a key and a value) */
typedef struct SymTable *SymTable_T;

/* Returns a new SymTable object, or NULL if insufficient memory */
SymTable_T SymTable_new(void);

/* Frees all memory occupied by oSymTable */
void SymTable_free(SymTable_T oSymTable);

/* Returns the number of bindings in oSymTable */
size_t SymTable_getLength(SymTable_T oSymTable);

/* Adds a new binding to oSymTable consisting of key pcKey and value
pvValue and returns 1 (TRUE) if oSymTable does not contain a binding 
with pcKey, otherwise oSymTable is unchanged and returns 0 (FALSE).  
If insufficient memory, oSymTable is unchanged and returns 0 (FALSE) */
int SymTable_put(SymTable_T oSymTable, const char *pcKey,
                                                  const void *pvValue);

/* If oSymTable contains a binding with key pcKey, replaces the
binding's value with pvValue and returns the old value. Otherwise 
oSymTable is unchanged and returns NULL. */
void *SymTable_replace(SymTable_T oSymTable, const char *pcKey,
                                                 const void *pvValue);

/* Returns 1 (TRUE) if oSymTable contains a binding at pcKey, 
otherwise returns 0 (FALSE)*/
int SymTable_contains(SymTable_T oSymTable, const char *pcKey);

/* Returns the value of the binding in oSymTable whose key is pcKey,
NULL otherwise */
void *SymTable_get(SymTable_T oSymTable, const char *pcKey);

/* Removes binding from oSymTable and returns the binding's value if 
oSymTable contains a binding with key pcKey, otherwise oSymTable is 
unchanged and returns NULL. */
void *SymTable_remove(SymTable_T oSymTable, const char *pcKey);

/* Applys function *pfApply to each binding in oSymTable, passing 
pvExtra as an extra parameter */
void SymTable_map(SymTable_T oSymTable, void (*pfApply)
          (const char *pcKey, void *pvValue, void *pvExtra),
                                                 const void *pvExtra);
#endif
