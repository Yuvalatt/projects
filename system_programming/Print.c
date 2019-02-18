#include <stdio.h>
#include <stdlib.h>

int Print2(void);

int main(void)
{
	Print2();
	
	return EXIT_SUCCESS;
}

int Print2(void)
{	
	int i = 0;

	for (i = 0; i < 10; ++i)
	{
		printf("Hello %d :)\n", i);
	}

	return EXIT_SUCCESS;
}
