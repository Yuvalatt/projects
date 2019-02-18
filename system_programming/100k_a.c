#include <stdio.h>
#include <pthread.h> 	 			/* thread operations  */
#include <stdlib.h>  	 			/*  EXIT_SUCCESS     */
#include <unistd.h>	 			/*    sleep          */

/*       ATTACH mode - 100k threads              */
#define ARR_SIZE 100000        		        /* max possible num of threads- 32753 */

typedef enum {SUCCESS = 0, FAILURE = 1} status_t;

static int arr[ARR_SIZE] = {0};

void *write_index(void *num)  
{ 
   	arr[(int)num] = (int)num;	
	return NULL;
}

static status_t TestArray(void)
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
	int stat = 0;
	
	pthread_t threads_arr[ARR_SIZE] = {0}; 

	for (i = 0; i < ARR_SIZE; ++i)
	{		
		if (0 != (stat = pthread_create(&threads_arr[i], NULL, &write_index, (void *)i)))	/* check return value of pthread_create,  returns 0 on SUCCESS */	
		{
			printf("Error value: %d \n");
			break;
		}
	}
	
	sleep(10);
	
	puts("Test: 100k - ATTACH mode");
	printf("%s\n", ((SUCCESS ==  TestArray()) ? "SUCCESS!" : "FAILED!"));

	return EXIT_SUCCESS;
}
