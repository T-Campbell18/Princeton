/*--------------------------------------------------------------------*/
/* stra.c                                                             */
/* Author: Tyler Campbell                                             */
/*--------------------------------------------------------------------*/

#include "str.h"
#include <assert.h>

size_t Str_getLength(const char src[])
{
	size_t length = 0;
	assert(src != NULL);
	while (src[length] != '\0')
		length++;
	return length;
}

char *Str_copy(char dest[], const char src[])
{
	size_t i;
	assert(src != NULL && dest != NULL);
	for (i = 0; src[i] != '\0'; i++)
	{
		char tmp = src[i];
		dest[i] = tmp;
	}
	dest[i] = '\0';
	return dest;
}

char* Str_concat(char dest[], const char src[])
{
	size_t i, j;
	assert(src != NULL && dest != NULL);
	for (i = 0; dest[i] != '\0'; i++);
	for (j = 0; src[j] != '\0'; j++, i++)
		dest[i] = src[j];
	dest[i] = '\0';
	return dest;
	
}

int Str_compare(const char s1[], const char s2[])
{
	size_t i;
	assert(s1 != NULL && s2 != NULL);
	for (i = 0; s1[i] != '\0'; i++)
	{
		if (s2[i] == '\0')
			return 1;
		if (s2[i] > s1[i])
			return -1;	
		if (s2[i] < s1[i])
			return 1;	
	}
	if (s2[i] != '\0') 
		return -1;
	return 0;
}

char *Str_search(const char str[], const char substr[])
{
	size_t i, j;
	assert(str != NULL && substr != NULL);
	/* checks if substr is a empty string */
	if (substr[0] == '\0')
		return (char*)str;
	for (i = 0; str[i] != '\0'; i++)
	{
		/* goto next char in str if char doesn't equal first char of substr*/
		if (str[i] != substr[0])
			continue;
		j = 0;	
		while (str[i] != '\0')	
		{
			if (substr[j] == '\0')
				return (char*)&str[i];
			if (str[j + i] != substr[j])
				break;
			j++;	
		}
	}
	return NULL;
}
