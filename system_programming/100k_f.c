#include <stdio.h>
#include <pthread.h> 	 		/* thread operations  */
#include <stdlib.h>  			/*  EXIT_SUCCESS      */

/*  using PRAGMA: Compile with -fopenmp     */

#define ARR_SIZE 100000000

typedef enum {SUCCESS = 0, FAILURE = 1} status_t;
static int arr[ARR_SIZE] = {0};

static status_t CheckArray(void)
{
	int i = 0;
	for (i = 0; i < ARR_SIZE; ++i)
	{		
		if (arr[i] != i)
		{
			return FAILURE;
		}
	}
	return SUCCESS;
}

int main(void)
{	
	int i = 0;
	
	#pragma omp parallel for
	for (i = 0; i < ARR_SIZE; ++i)
	{		
		arr[i] = i;	
	}

	puts("Test: 100k - pragma");
	printf("%s\n", SUCCESS ==  CheckArray() ? "SUCCESS!" : "FAILURE!");
	
	return EXIT_SUCCESS;
}
