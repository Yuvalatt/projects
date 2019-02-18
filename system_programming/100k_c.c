#include <stdio.h>
#include <pthread.h> 			  /* thread operations  */
#include <stdlib.h>  			  /*   EXIT_SUCCESS     */
#include <unistd.h>			  /*      sleep         */

/*   DETACH MODE - 100k threads       */

typedef enum {SUCCESS = 0, FAILURE = 1} status_t;
#define ARR_SIZE 100000     					 /* max possible num of threads- 32753 */

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
			return FAILURE;
		}
	}
	return SUCCESS;
}

int main(void)
{	
	int i = 0;
	pthread_t threads_arr[ARR_SIZE] = {0}; 
	
	pthread_attr_t attr;
	pthread_attr_init (&attr);		
	pthread_attr_setdetachstate(&attr, PTHREAD_CREATE_DETACHED);			     /* Init detach attribute. Option 2: using pthread_detach() with thread ID */	

	for (i = 0; i < ARR_SIZE; ++i)
	{		
		if (0 != pthread_create(&threads_arr[i], &attr, &write_index, (void *)i))     /* DETACH state - works both with 32k & 100k threads*/							
		{
			puts("Error: pthread_create");
			break;
		}
	}

	pthread_attr_destroy(&attr); 
	sleep(10);

	puts("Test: 100k - DETACH mode");
	printf("%s\n", (SUCCESS ==  CheckArray() ? "SUCCESS!" : "FAILURE!"));

	return EXIT_SUCCESS;
}
