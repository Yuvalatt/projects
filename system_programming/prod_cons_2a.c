#include <stdio.h>			    /*   fputs()          */
#include <pthread.h> 			    /*   thread funcs     */
#include <stdlib.h>  			    /*   EXIT_SUCCESS     */
#include <signal.h>			    /*   sig_atomic_t     */
#include <assert.h>			    /*   assert           */
#include  "../ds/include/dlist.h"

#define PRODUCERS_NUM (5)
#define CONSUMERS_NUM (5)

typedef enum {FALSE, TRUE} status_t;

static int prod_counter = 0;
static pthread_mutex_t dlist_mutex;
static dlist_t *dlist = NULL;

sig_atomic_t consume = TRUE;
sig_atomic_t produce = TRUE;
sig_atomic_t p_exit = FALSE;

void *Producer(void *args)  
{ 	
	while (!p_exit)
	{		
		pthread_mutex_lock(&dlist_mutex);
		
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
	void *temp = NULL;

	while (1)
	{	
		pthread_mutex_lock(&dlist_mutex);
	
		if (!DListIsEmpty(dlist))
		{
			temp = DListGetData(DListBegin(dlist));		
			DListPopFront(dlist);										
		}
		else if (!consume)
		{
			pthread_mutex_unlock(&dlist_mutex);
			break;
		}
		
		pthread_mutex_unlock(&dlist_mutex);
		printf("Consumed value: %d\n", (int)temp);	
	}

	return NULL;
}

int main(void)
{	
	int i = 0;	
	pthread_t producers_arr[PRODUCERS_NUM] = {0};
	pthread_t consumers_arr[CONSUMERS_NUM] = {0};

	dlist = DListCreate();
	if (!dlist)
	{
		fputs("Error: malloc failure.", stderr);
	}

	if (-1 == pthread_mutex_init(&dlist_mutex, NULL))
	{
		fputs("Error: pthread_mutex_init", stderr);
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
	p_exit = TRUE;

	for (i = 0; i < PRODUCERS_NUM; ++i)
	{	
		if (0 != pthread_join(producers_arr[i], NULL))
		{
			fputs("Error: pthread_create", stderr);
		}
	}
	
	consume = FALSE;
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
