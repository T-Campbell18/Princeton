#include <stdio.h>

int main(void)
{
	int c = getchar();
	int a = 0;
	while (c != EOF)
	{
		a++;
		c = getchar();
	}
	printf("%d hello, world\n", a);
	return 0;
}