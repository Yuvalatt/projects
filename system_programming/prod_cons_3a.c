#include <stdio.h>			    /*   fputs()          */
#include <pthread.h> 			    /*   thread funcs     */
#include <stdlib.h>  			    /*   EXIT_SUCCESS     */
#include <signal.h>			    /*   sig_atomic_t     */
#include <assert.h>			    /*   assert           */
#include <semaphore.h>
#include  "../ds/include/fsq.h"

#define PRODUCERS_NUM (5)
#define CONSUMERS_NUM (5)
#define FSQ_SIZE (10)
#define UNUSED(x)(void)(x)

typedef enum {FALSE, TRUE} bool_t;

static int prod_counter = 0;
static fsq_t *fsq = NULL;

sig_atomic_t produce = 0;
sig_atomic_t c_exit = FALSE;
sig_atomic_t p_exit = FALSE;

static pthread_mutex_t mutex;
static sem_t messages;  
static sem_t empty_slots;

void *Producer(void *args)  
{ 	
	UNUSED(args);
	
	while ('q' != produce)
	{	
		if (0 != sem_wait(&empty_slots))
		{
			fputs("Error: sem_wait failure.", stderr);
		}

		if (p_exit)
		{
				break;
		}

		pthread_mutex_lock(&mutex);
		
		if (0 != sem_post(&messages))
		{
			fputs("Error: sem_post failure.", stderr);
		}

		++prod_counter;	
		FSQEnqueue(fsq, prod_counter);
		
		pthread_mutex_unlock(&mutex);
	}

	return NULL;
}

void *Consumer(void *args)  
{
	int temp = 0;

	UNUSED(args);	

	while (1)
	{	
		if (0 != sem_wait(&messages))
		{
			fputs("Error: sem_wait failure.", stderr);
		}
		
		if (c_exit)
		{		
			break;
		}

		pthread_mutex_lock(&mutex);
		
		temp = FSQDequeue(fsq);
		
		if (0 != sem_post(&empty_slots))
		{
			fputs("Error: sem_post failure.", stderr);
		}
		
		pthread_mutex_unlock(&mutex);
		printf("Consumed value: %d\n", temp);							
	}

	return NULL;
}

int main(void)
{	
	int i = 0;
	int j = 0;

	int messages_left = -1;
	int temp = 0;

	pthread_t producers[PRODUCERS_NUM] = {0};
	pthread_t consumers[CONSUMERS_NUM] = {0};

	fsq = FSQCreate(FSQ_SIZE);
	if (!fsq)
	{
		fputs("Error: malloc failure.", stderr);
	}

	if (-1 == pthread_mutex_init(&mutex, NULL))
	{
		fputs("Error: pthread_mutex_init.", stderr);
	}

	if ((-1 == sem_init(&messages, 0, 0)) || (-1 == sem_init(&empty_slots, 0, 10)))
	{
		fputs("Error: sem_init.", stderr);
	}	

	for (i = 0; i < CONSUMERS_NUM; ++i)
	{
		if (0 != pthread_create(&consumers[i], NULL, Consumer, NULL))
		{
			fputs("Error: pthread_create", stderr); 
		}
	}

	for (i = 0; i < PRODUCERS_NUM; ++i)
	{
		if (0 != pthread_create(&producers[i], NULL, Producer, NULL))
		{
			fputs("Error: pthread_create", stderr);
		}
	}

	produce = fgetc(stdin);	
	p_exit = TRUE;
	
	for (j = 0; j < CONSUMERS_NUM; ++j)
	{
			sem_post(&empty_slots);
	}

	for (i = 0; i < PRODUCERS_NUM; ++i)
	{	
		if (0 != pthread_join(producers[i], NULL))
		{
			fputs("Error: pthread_create", stderr);
		}
	}

	if (0 != sem_getvalue(&messages, &messages_left))
	{
		fputs("Error: sem_getvalue failure.", stderr);
	}
	
	while (messages_left > 0)
	{
		pthread_mutex_lock(&mutex);
		
		temp = FSQDequeue(fsq);
		--messages_left;					
		
		pthread_mutex_unlock(&mutex);

		printf("Consumed value: %d\n", temp);		
	}

	c_exit = TRUE;
	for (j = 0; j < CONSUMERS_NUM; ++j)
	{
			sem_post(&messages);
	}

	for (i = 0; i < CONSUMERS_NUM; ++i)
	{
		if (0 != pthread_join(consumers[i], NULL))
		{
			fputs("Error: pthread_create", stderr);
		}
	}
		
	assert(0 == messages_left);
	
	pthread_mutex_destroy(&mutex);
	FSQDestroy(fsq);

	return EXIT_SUCCESS;
}
