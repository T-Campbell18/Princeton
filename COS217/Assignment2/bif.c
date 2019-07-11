#include <stdio.h>
#include <string.h>
int main(void) {
	int x;
	char tmp[100] = {'\0'};
	for (x  =0; x < 25; x++)
	{
		char buffer[25] = "Tyler";
		strcat(tmp, buffer);
		
	}
	return 0;
}