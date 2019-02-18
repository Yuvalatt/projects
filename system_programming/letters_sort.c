#include <stdio.h>		/*   puts, printf      */
#include <stdlib.h>  		/*   EXIT_SUCCESS      */
#include <pthread.h> 		/*   thread funcs      */
#include <sys/mman.h>		/*    mmap             */
#include <sys/stat.h>		/*    struct stat      */
#include <sys/types.h>		/*    fstat        */
#include <fcntl.h>		/*    open             */
#include <string.h>		/*    memset           */
#include <unistd.h>		/*    close, ftruncate  */

#define ASCII_RANGE 256 
#define UNUSED(x)(void)(x)

#define CHECK_RC(x)if(-1 == (x)){ puts("Error: file failure.");\
					exit(1); }
#define CHECK_MAP(x)if(MAP_FAILED == (x)){ puts("Error: mmap failure.");\
				 	   exit(1); }

typedef enum {SUCCESS = 0, fAILURE = 1} status_t;

typedef struct thread_args
{
	unsigned char *start;
	int count;
	int *histogram;
} thread_args_t;

static status_t CounSortThreads(char *file_name, int threads_num);
static void InitArgs(thread_args_t *args, int threads_num, size_t file_size, unsigned char *char_runner);
static void DestroyArgs(thread_args_t *args, int threads_num);
static void ThreadsCount(pthread_t *threads_arr, thread_args_t *args, int threads_num);
static void WriteToFile(unsigned char *runner, thread_args_t *args);

int main(int argc, char *argv[])
{	
	char *file_name = argv[1]; 
	int threads_num = atoi(argv[2]);
		
	UNUSED(argc);
	
	if (SUCCESS == CounSortThreads(file_name, threads_num))
	{
		puts("SUCCESS: created a new sorted file.");
	}

	return EXIT_SUCCESS;
}

/*   Initialization of threads arguments   */
static void InitArgs(thread_args_t *args, int threads_num, size_t file_size, unsigned char *char_runner)
{
	int i = 0;
	size_t block_size = (file_size / threads_num);
		
	for (i = 0; i < threads_num; ++i)
	{	
		args[i].count = (i == threads_num - 1) ? ((file_size % threads_num) + block_size) : block_size;
		args[i].start = char_runner + (i * block_size);
		args[i].histogram = calloc(ASCII_RANGE, sizeof(int)); 
		if (!args[i].histogram)
		{
			puts("Error: calloc failure");
			exit(1);
		}
	}
}

static void DestroyArgs(thread_args_t *args, int threads_num)
{
	int i = 0;
	for (i = 0; i < threads_num; ++i)
	{
		free(args[i].histogram);
		args[i].histogram = NULL;
	}
	free(args);
}


static void *build_histogram(void *dict_args)      /* Thread function */ 
{ 
	int i = 0;
	unsigned char *runner = ((thread_args_t *)dict_args)->start;
	int count = ((thread_args_t *)dict_args)->count;
	
	for (i = 0; i < count; ++i)
	{
		++((thread_args_t *)dict_args)->histogram[(int)*runner];
		++runner;	
	}
	return NULL;
}

static void ThreadsCount(pthread_t *threads_arr, thread_args_t *args, int threads_num)
{
	int i = 0;
	for (i = 0; i < threads_num; ++i)
	{	
		if (0 != pthread_create(&threads_arr[i], NULL, build_histogram, &args[i]))
		{
			puts("Error: pthread_create");
			exit(1);
		}	
	}
	
	for (i = 0; i < threads_num; ++i)
	{
		if (0 != pthread_join(threads_arr[i], NULL))
		{
			puts("Error: pthread_join");
			exit(1);
		}
	}
}

static void MergeResults(thread_args_t *args, int threads_num)
{
	int i = 0;
	int j = 0;

	for (i = 0; i < ASCII_RANGE; ++i)
	{
		for (j = 1; j < threads_num; ++j)
		{
			args[0].histogram[i] += args[j].histogram[i];
		}	
	}
}

static void WriteToFile(unsigned char *char_runner, thread_args_t *args)
{
	int i = 0;
	for (i = 0; i < ASCII_RANGE; ++i)
	{
		if (0 < args[0].histogram[i])
		{
			memset(char_runner, i, args[0].histogram[i]);
			char_runner += args[0].histogram[i];
		}			
	}
}

/* In case of failure: prints error message & exits program. 
   In case of success: returns SUCCESS status             */
static status_t CounSortThreads(char *file_name, int threads_num)
{
	int fd = 0;
	struct stat file_stat = {0};
	size_t file_size = 0; 
	unsigned char *file_start = NULL;
	unsigned char *char_runner = NULL;
	pthread_t *threads_arr = NULL;	
	thread_args_t *args = NULL;
		
	CHECK_RC(fd = open(file_name, O_RDONLY));
	CHECK_RC(fstat(fd, &file_stat));
	file_size = file_stat.st_size;

	CHECK_MAP(char_runner = mmap(NULL, file_size, PROT_READ, MAP_PRIVATE, fd, 0));
	file_start = char_runner;
	
	args = (thread_args_t *)malloc(threads_num * sizeof(thread_args_t));
	if (!args)
	{
		puts("Error: malloc failure");
		exit(1);
	}

	InitArgs(args, threads_num, file_size, char_runner);
	
	threads_arr = (pthread_t *)malloc(threads_num * sizeof(pthread_t));
	if (!threads_arr)
	{
		puts("Error: malloc failure");
		exit(1);
	}

	ThreadsCount(threads_arr, args, threads_num);	
	MergeResults(args, threads_num);
	
	CHECK_RC(close(fd));
	CHECK_RC(munmap(file_start, file_size));
	
	CHECK_RC((fd = open("sorted_dict.txt", O_RDWR | O_CREAT,  S_IRWXU)));
	CHECK_MAP(char_runner = mmap(NULL, file_size, PROT_WRITE, MAP_SHARED, fd, 0));
	file_start = char_runner;	
	CHECK_RC(ftruncate(fd, file_size)); /* Extends the size of an empty file */
	
	
	WriteToFile(char_runner, args);

	DestroyArgs(args, threads_num);
	free(threads_arr);

	CHECK_RC(close(fd));
	CHECK_RC(munmap(file_start, file_size));
		
	return SUCCESS;	
}
