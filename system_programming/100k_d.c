#include <stdio.h>
#include <pthread.h> 			 /* thread operations  */
#include <stdlib.h>  			 /*  EXIT_SUCCESS     */
#include <unistd.h>			 /*    sleep          */

typedef enum {SUCCESS = 0, FAILURE = 1} status_t;

/* ATTACH state, Using join. 32k works, 100k - not */

#define ARR_SIZE 32000     		 /* max possible num of threads- 32753 */

static int arr[ARR_SIZE] = {0};

void *write_index(void *num)  
{ 
   	arr[(int)num] = (int)num;	
	return NULL;
}

static status_t CheckArray(void)
{
	int i = 0;
	for (i = 0; i < ARR_SIZE; ++i)
	{		
		if (arr[i] != i)
		{
			printf("arr[%d] = %d \n", i, arr[i]);
			return FAILURE;
		}
	}
	return SUCCESS;
}

int main(void)
{	
	int i = 0;	
	pthread_t threads_arr[ARR_SIZE] = {0}; 

	for (i = 0; i < ARR_SIZE; ++i)
	{		
		if (0 != pthread_create(&threads_arr[i], NULL, &write_index, (void *)i))     /* ATTACH state - works both with 32k & 100k threads*/						
		{
			puts("Error: pthread_create");
			break;
		}
	}

	for (i = 0; i < ARR_SIZE; ++i)
	{
		if (0 != pthread_join(threads_arr[i], NULL))
		{
			break;
		}
	}     							

	puts("Test: 32k - ATTACH mode & Join");
	printf("%s\n", (SUCCESS ==  CheckArray() ? "SUCCESS!" : "FAILURE!"));

	return EXIT_SUCCESS;
}
