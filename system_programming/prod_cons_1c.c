#include <stdio.h>			    /*   fputs()          */
#include <pthread.h> 			    /*   thread funcs     */
#include <stdlib.h>  			    /*   EXIT_SUCCESS     */
#include <signal.h>			    /*   sig_atomic_t     */

#define ARR_SIZE (50000)
#define MAX_COUNT 500

typedef enum {TO_PRODUCE, TO_CONSUME} action_t;

static int arr[ARR_SIZE] = {0};
static int prod_counter = 0;
static sig_atomic_t action = TO_PRODUCE;

static void FillArray(void)
{
	int i = 0;
	
	++prod_counter;
	printf("arr[i] = %2d  ", prod_counter);

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

void *Producer(void *args)  
{ 	
	while (prod_counter < MAX_COUNT)
	{
		while(TO_PRODUCE != action);
		FillArray();
		action = TO_CONSUME;
	}
	return NULL;
}

void *Consumer(void *args)  
{
	while (prod_counter < MAX_COUNT)
	{	
		while (TO_CONSUME != action);
		CheckArray();
		action = TO_PRODUCE;
	}
	return NULL;
}

int main(void)
{		
	pthread_t threads_arr[2] = {0};
	
	if (0 != pthread_create(&threads_arr[0], NULL, Consumer, NULL))
	{
		fputs("Error: pthread_create", stderr); 
	}
		
	if (0 != pthread_create(&threads_arr[1], NULL, Producer, NULL))
	{
		fputs("Error: pthread_create", stderr);
	}

	if (0 != pthread_join(threads_arr[0], NULL))
	{
		fputs("Error: pthread_create", stderr);
	}
	
	if (0 != pthread_join(threads_arr[1], NULL))
	{
		fputs("Error: pthread_create", stderr);
	}

	return EXIT_SUCCESS;
}
