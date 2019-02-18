#include <stdio.h>
#include <pthread.h> 							 /*  thread operations  */
#include <stdlib.h>  							 /*   EXIT_SUCCESS     */
#include <unistd.h>							 /*     sleep          */

/*  ATTACH state, Using join. 
    Testing performance of - 1, 2, 4, 8, 16 threads */

typedef enum {SUCCESS = 0, FAILURE = 1} status_t;
#define ARR_SIZE 100000000     						 /* max possible num of threads- 32753 */

static int arr[ARR_SIZE] = {0};
static int threads_num;

void *write_index(void *num)  
{ 
	int i = 0;
	int from = (ARR_SIZE/ threads_num) * ((int)num);
	int to = from + (ARR_SIZE / threads_num);

	for (i = from; i < to; ++i)
	{		
  		arr[i] = i;
	}
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

int main(int argc, char *argv[])
{	
	int i = 0;
	pthread_t *threads_arr = NULL;
	threads_num = atoi(argv[1]);
	
	threads_arr = (pthread_t *)malloc(threads_num * sizeof(pthread_t));

	for (i = 0; i < threads_num; ++i)
	{		
		if (0 != pthread_create(&threads_arr[i], NULL, &write_index, (void *)i))
		{
			puts("Error: pthread_create");
			break;
		}	
	}

	for (i = 0; i < threads_num; ++i)
	{
		if (0 != pthread_join(threads_arr[i], NULL))
		{
			puts("Error: pthread_join");
			break;
		}
	}

	puts("Test: 100k - ATTACH mode, Join, work by ranges");
	/*printf("%s\n", (SUCCESS ==  CheckArray() ? "SUCCESS!" : "FAILURE!"));
*/
	return EXIT_SUCCESS;
}
