#include <stdio.h>			    /*   fputs()          */
#include <pthread.h> 			    /*   thread funcs     */
#include <stdlib.h>  			    /*   EXIT_SUCCESS     */
#include <signal.h>			    /*   sig_atomic_t     */
#include <assert.h>			    /*   assert           */
#include <semaphore.h>
#include  "../ds/include/dlist.h"

#define PRODUCERS_NUM (5)
#define CONSUMERS_NUM (5)
#define UNUSED(x)(void)(x)

typedef enum {FALSE, TRUE} bool_t;

static int prod_counter = 0;
 sig_atomic_t consume = TRUE;
 sig_atomic_t produce = 0;

static dlist_t *dlist = NULL;
static pthread_mutex_t dlist_mutex;
static sem_t job_count;

void *Producer(void *args)  
{ 	
	UNUSED(args);
	
	while ('q' != produce)
	{	
		pthread_mutex_lock(&dlist_mutex);
		
		if (0 != sem_post(&job_count))
		{
			fputs("Error: sem_post failure.", stderr);
		}

		++prod_counter;	
		if (DListEnd(dlist) == DListPushBack(dlist, (void *)prod_counter))
		{
			fputs("Error: malloc failure.", stderr);
		}
 		 	
		pthread_mutex_unlock(&dlist_mutex);
	}

	return NULL;
}

void *Consumer(void *args)  
{
	int temp = 0;
	bool_t c_exit = FALSE;

	UNUSED(args);	

	while (1)
	{	
		if (0 != sem_wait(&job_count))
		{
			fputs("Error: sem_wait failure.", stderr);
		}

		if (!consume)
		{
			break;
		}

		pthread_mutex_lock(&dlist_mutex);

		temp = (int)DListGetData(DListBegin(dlist));		
		DListPopFront(dlist);
		
		pthread_mutex_unlock(&dlist_mutex);

		printf("Consumed value: %d\n", temp);							
	}

	return NULL;
}

int main(void)
{	
	int i = 0;
	int j = 0;
	
	pthread_t producers_arr[PRODUCERS_NUM] = {0};
	pthread_t consumers_arr[CONSUMERS_NUM] = {0};

	dlist = DListCreate();
	if (!dlist)
	{
		fputs("Error: malloc failure.", stderr);
	}

	if (-1 == pthread_mutex_init(&dlist_mutex, NULL))
	{
		fputs("Error: pthread_mutex_init.", stderr);
	}

	if (-1 == sem_init(&job_count, 0, 0))
	{
		fputs("Error: sem_init.", stderr);
	}	

	for (i = 0; i < CONSUMERS_NUM; ++i)
	{
		if (0 != pthread_create(&consumers_arr[i], NULL, Consumer, NULL))
		{
			fputs("Error: pthread_create", stderr); 
		}
	}

	for (i = 0; i < PRODUCERS_NUM; ++i)
	{
		if (0 != pthread_create(&producers_arr[i], NULL, Producer, NULL))
		{
			fputs("Error: pthread_create", stderr);
		}
	}

	produce = fgetc(stdin);	
	for (i = 0; i < PRODUCERS_NUM; ++i)
	{	
		if (0 != pthread_join(producers_arr[i], NULL))
		{
			fputs("Error: pthread_create", stderr);
		}
	}
	
	while (1)
	{
		if (0 == pthread_mutex_lock(&dlist_mutex))
		{
			if (DListIsEmpty(dlist))
			{
				consume = FALSE;
				break;
			}
		}
		
		pthread_mutex_unlock(&dlist_mutex);		
	}

	for (j = 0; j < CONSUMERS_NUM; ++j)
	{
			sem_post(&job_count);
	}

	for (i = 0; i < CONSUMERS_NUM; ++i)
	{
		if (0 != pthread_join(consumers_arr[i], NULL))
		{
			fputs("Error: pthread_create", stderr);
		}
	}
		
	assert(DListIsEmpty(dlist));
	pthread_mutex_destroy(&dlist_mutex);	
	DListDestroy(dlist);

	return EXIT_SUCCESS;
}
