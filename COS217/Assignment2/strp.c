/*--------------------------------------------------------------------*/
/* strp.c                                                             */
/* Author: Tyler Campbell                                             */
/*--------------------------------------------------------------------*/

#include "str.h"
#include <assert.h>

size_t Str_getLength(const char *src)
{
	const char *end;
	assert(src != NULL);
	end = src;
	while (*end != '\0')
		end++;
	return (size_t)(end - src);
}

char* Str_copy(char *dest, const char *src)
{
	char *tmp = dest;
	assert(dest != NULL && src != NULL);
	while((*dest++ = *src++) != '\0');
	return tmp;
}

char* Str_concat(char *dest, const char *src)
{
	char *tmp = dest;
	assert(dest != NULL && src != NULL);
	while (*dest)
		dest++;
	while ((*dest++ = *src++));
	return tmp;
}

int Str_compare(const char *s1, const char *s2)
{
	const char *tmp1 = s1;
	const char *tmp2 = s2;
	assert(s1 != NULL && s2 != NULL);
	for (; *tmp1 != '\0'; tmp1++, tmp2++) 
	{
		if (*tmp2 == '\0') 
			return 1;
		if (*tmp2 > *tmp1)   
			return -1;
		if (*tmp1 > *tmp2)   
			return 1;
	}
	if (*tmp2 != '\0') 
		return -1;
	return 0;
}

char *Str_search(const char *str, const char *substr)
{
	const char *start = (char*)str;
	const char *sub = substr;
	const char *tmpstr, *tmpsub;
	assert(str != NULL && substr != NULL);
	tmpsub = substr;
	if (*tmpsub == '\0')
		return (char*)start;
	for (; *start != '\0'; start++)
	{
		if (*start != *sub)
			continue;
		tmpstr = start;
		while (*tmpstr && *sub && *tmpstr == *sub) 
		{
			tmpstr++;
			sub++;
    }
		if (*sub == '\0')
			return (char*)start;
		sub = tmpsub;	
	}
	return NULL;
}