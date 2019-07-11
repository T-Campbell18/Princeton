/*--------------------------------------------------------------------*/
/* str.h                                                              */
/* Author: Tyler Campbell                                             */
/*--------------------------------------------------------------------*/
#ifndef STR_H
#define STR_H

#include <stddef.h>

/* Returns the length of string pointed to by src */
size_t Str_getLength(const char src[]);

/* Copies the string pointed to by src into the array dest 
				Return a pointer to dest */
char *Str_copy(char dest[], const char src[]);

/* Appends characters from the string pointed to by src to the strng 
pointed to by dest. Returns a dest a pointer to concatented string */
char *Str_concat(char dest[], const char src[]);

/* Returns a negative, zero, or postive; depending on whether the string 
pointed to by s1 is less than, equal to, or greater than the string 
pointed to by s2 */
int Str_compare(const char s1[], const char s2[]);

/* Returns a pointer to the first occurrence in the string pointed to by str
of the sequence of characters in the string pointed to by substr
returns a null pointer if no match is found */
char *Str_search(const char str[], const char substr[]);

#endif