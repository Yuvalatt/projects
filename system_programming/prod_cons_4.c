#include <stdio.h>			    /*   fputs()          */
#include <pthread.h> 			    /*   thread funcs     */
#include <stdlib.h>  			    /*   EXIT_SUCCESS     */
#include <signal.h>			    /*   sig_atomic_t     */
#include <semaphore.h>			    /*   semaphore        */

#define PRODUCERS_NUM (1)
#define CONSUMERS_NUM (5)
#define UNUSED(x)(void)(x)

typedef enum {FALSE, TRUE} bool_t;

static int p_count = 0;

static sig_atomic_t produce = 0;
static sig_atomic_t c_exit = FALSE;
static sig_atomic_t p_exit = FALSE;

static sem_t c_available;  
static pthread_mutex_t mutex;
static pthread_cond_t message_ready = PTHREAD_COND_INITIALIZER;

static void *Producer(void *args)  
{
	int i = 0; 	
	UNUSED(args);
	
	while ('q' != produce)
	{	
		for (i = 0; i < CONSUMERS_NUM; ++i)
		{
			if (0 != sem_wait(&c_available))
			{
				fputs("Error: sem_wait failure.", stderr);
			}			
		}
		
		if (p_exit)
		{
			break;
		}
		
		pthread_mutex_lock(&mutex);
		
		++p_count;
		if (0 != pthread_cond_broadcast(&message_ready))
		{
			fputs("Error: broadcast failure.", stderr);
		}
	
		pthread_mutex_unlock(&mutex);
	}

	return NULL;
}

static void *Consumer(void *args)  
{	
	UNUSED(args);	

	while(1)	
	{	
		pthread_mutex_lock(&mutex);
		
		if (0 != sem_post(&c_available))
		{
			fputs("Error: sem_post failure.", stderr);
		}
		
		if (0 != pthread_cond_wait(&message_ready,&mutex))
		{
			fputs("Error: pthread_cond_wait failure.", stderr);
		}
		
		pthread_mutex_unlock(&mutex);
		
		if (c_exit)
		{
			break;
		}
		
		printf("Consumed value: %d\n", p_count);
	}

	return NULL;
}

int main(void)
{	
	int i = 0;
	
	pthread_t producers[PRODUCERS_NUM] = {0};
	pthread_t consumers[CONSUMERS_NUM] = {0};

	if (-1 == pthread_mutex_init(&mutex, NULL))
	{
		fputs("Error: pthread_mutex_init.", stderr);
	}

	if (-1 == sem_init(&c_available, 0, 0))
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

	for (i = 0; i < PRODUCERS_NUM; ++i)
	{	
		if (0 != pthread_join(producers[i], NULL))
		{
			fputs("Error: pthread_create", stderr);
		}
	}

	c_exit = TRUE;

	if (0 != pthread_cond_broadcast(&message_ready))
	{
		fputs("Error: broadcast failure.", stderr);
	}

	for (i = 0; i < CONSUMERS_NUM; ++i)
	{
		if (0 != pthread_join(consumers[i], NULL))
		{
			fputs("Error: pthread_create", stderr);
		}
	}
	
	pthread_mutex_destroy(&mutex);
	pthread_cond_destroy(&message_ready);
	sem_destroy(&c_available);

	return EXIT_SUCCESS;
}
