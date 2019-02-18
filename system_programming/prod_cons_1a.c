#include <stdio.h>			/*   fputs()          */
#include <pthread.h> 			/*   thread funcs     */
#include <stdlib.h>  			/*   EXIT_SUCCESS     */
#include <signal.h>			/*   sig_atomic_t     */

#define ARR_SIZE (50000)
#define MAX_COUNT 500
#define TRUE (1)
#define FALSE (0)

typedef enum {SUCCESS = 0, FAILURE = 1} status_t;

static int arr[ARR_SIZE] = {0};
static int prod_counter = 0;
static sig_atomic_t is_busy = FALSE;

static void FillArray(void)
{
	int i = 0;
	
	++prod_counter;
	printf("Producer - value written: %d\n", prod_counter);

	for (i = 0; i < ARR_SIZE; ++i)
	{		
		arr[i] = prod_counter;
	}	
}

static void CheckArray(void)
{
	int i = 0;
	int val = arr[0];

	for (i = 1; i < ARR_SIZE; ++i)
	{		
		if (arr[i] != val)
		{
			puts("Consumer - invalid value found.");
			break;		
		}		
	}
}

static void PrintArray(void)
{
	int i = 0;
	for (i = 1; i < ARR_SIZE; ++i)
	{		
		printf("%d ", arr[i]);			
	}
	puts("");
}

void *Producer(void *args)  
{ 	
	while (prod_counter < MAX_COUNT)
	{
		while (TRUE == is_busy);

		is_busy = TRUE;
		FillArray();
		is_busy = FALSE;
	}
	return NULL;
}

void *Consumer(void *args)  
{
	while (prod_counter < MAX_COUNT)
	{	
		while (TRUE == is_busy);

		is_busy = TRUE;
		CheckArray();
		is_busy = FALSE;
	}
	return NULL;
}

int main(int argc, char *argv[])
{		
	status_t stat = SUCCESS;
	pthread_t threads_arr[2] = {0};
	
	if (0 != pthread_create(&threads_arr[0], NULL, Consumer, NULL))
	{
		puts("Error: pthread_create");
		stat = FAILURE; 
	}
		
	if (0 != pthread_create(&threads_arr[1], NULL, Producer, NULL))
	{
		puts("Error: pthread_create");
		stat = FAILURE; 
	}

	if (0 != pthread_join(threads_arr[0], NULL))
	{
		stat = FAILURE; 
	}
	
	if (0 != pthread_join(threads_arr[1], NULL))
	{
		stat = FAILURE; 
	}

	printf("\n\n%s\n", (SUCCESS == stat) ? ("ProducerConsumer - SUCCESS") : ("ProducerConsumer - FALIRE"));

	return EXIT_SUCCESS;
}
