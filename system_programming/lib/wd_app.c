#include <stdlib.h>                     /*   EXIT_SUCCESS         */
#include <stdio.h>                      /*   puts, printf         */
#include <sys/types.h>	                /*   pid_t                */
#include <pthread.h>                    /*   pthread_create()     */
#include <signal.h>                     /*   SIGUSR1, SIGUSR2     */
#include <semaphore.h>                  /*   sem_post, sem_wait   */
#include <fcntl.h> 						/*   O_CREAT, O_EXEC      */
#include <time.h>						/*   struct timespec      */
#include <unistd.h>						/*   sleep 			      */
#include "../include/wd.h"
#include "../include/sch.h"

#ifndef NDEBUG
#define DBG(x) x
#else
#define DBG(x)
#endif 

#define SIGNAL_INTERVAL 1
#define FAULT_CHECK_INTERVAL 10
#define NAMED_SEM_PERM 0644
#define SIG2_TRIES 3
#define SIG2_WAIT_TIME 1
#define WTCH_DOG "wd.out"
#define UNUSED(x)(void)(x)

typedef enum {SUCCESS, FAILED} status_t;

typedef struct args
{
    sch_t *sch;
    pid_t dest_pid;
    time_t last_signal_time;
    struct sigaction action;
    int argc;
    char **argv;
    sem_t *wd_ready; 				/*      synch semaphore         */
    sem_t *wd_down; 				/*      synch semaphore         */
} args_t;

static pthread_t app_handler = {0};
static sem_t system_ready;
static args_t args = {0};
static int is_wd_down = 0;


static int InitResources(void);
void *ThreadFunc(void *args);
static int KeepAlive(void);
static int CreateWD(void);
static void Handler(int signum);
static int SignalAlive(void *param);
static void CleanResources(void);

int KeepMeAlive(int argc, char *argv[])
{				
	args.argc = argc;
	args.argv = argv;	
	
	if (0 != pthread_create(&app_handler, NULL, ThreadFunc, NULL))
	{
		perror("Error: pthread_create");
		return FAILED;	
	}
	
	atexit(LetMeDie);
	
	sem_wait(&system_ready);
	sem_destroy(&system_ready);
		
	return SUCCESS;	
}

void *ThreadFunc(void *args)
{	
    UNUSED(args);	
	
	if (FAILED == KeepAlive())
	{
	    perror("Error: KeepAlive failed.");
	}
		
	return NULL;
}

static int KeepAlive(void)
{
    if (FAILED == InitResources())
	{
		return FAILED;
	}
	
	if (FAILED == CreateWD())
	{
		return FAILED;
	}
	
	sem_post(&system_ready);
	
	args.last_signal_time = time(0);
	
	SCHRun(args.sch);
	
	return SUCCESS;
}

static void Handler(int signum)
{				
	if (SIGUSR1 == signum)
	{		
		DBG(puts("                      WD Alive"));	    
	    args.last_signal_time = time(0);
	}
}
/*   TASK FUNC        */
/* 	If WD failed -> App forks WD.out again.	*/  
static int SignalAlive(void *param)
{  
    UNUSED(param);
    
    if (difftime(time(0), args.last_signal_time) >= FAULT_CHECK_INTERVAL)       
	{	
		DBG(puts("App: detected WD failed"));		
		kill(args.dest_pid, SIGTERM);                /* Make sure WD is killed */
		
		if (FAILED == CreateWD())
		{
			perror("Error: CreateWD failure.");
            return FAILED;
		}
	}
	else
	{
		kill(args.dest_pid, SIGUSR1);     			/* Send WD a signal - "app alive" */
	}
	
	return SUCCESS;
}

static int CreateWD(void)   /* return value - status*/
{
    args.dest_pid = fork();			    	
	if (!args.dest_pid)
	{
		if (-1 == execv(WTCH_DOG, args.argv))  /*check return value */
		{
			 perror("Error: execv failed.");
        	 return FAILED;
		}
	}
	sem_wait(args.wd_ready);
}

void LetMeDie(void)
{   
	int i = 0;
	struct timespec timeout = {0};
	
	SCHStop(args.sch);
	
	if (0 != pthread_join(app_handler, NULL))
	{
		perror("Error: pthread_join");
	}
       
	if (clock_gettime(CLOCK_REALTIME, &timeout) == -1)
    {
        perror("clock_gettime failed");
    }
    
	for (i = 0; i < SIG2_TRIES; ++i)
	{	    
	    kill(args.dest_pid, SIGUSR2);
	    
        if (0 == sem_timedwait(args.wd_down, &timeout))  /* if got a sem_post during defined time */
        {
        	is_wd_down = 1;
            break;
        }
        timeout.tv_sec += SIG2_WAIT_TIME;
	}
	
	if (!is_wd_down)									/* if didnt get a sem_post - force termination */
	{
		kill(args.dest_pid, SIGTERM);
	}
	
	CleanResources();
}

static int InitResources(void)
{
	sch_t *sch = NULL;
	uniqid_t uid = {0};
	struct sigaction act = {0};
	
	sch = SCHCreate();
	if (!sch)
	{
		perror("Error: malloc failure.");
		return FAILED;
	}
	
	uid = SCHAddTask(sch, SignalAlive, NULL, SIGNAL_INTERVAL);		
	if (UIDIsBad(&uid))
	{
		perror("Error: malloc failure.");
		return FAILED;
	}
	
	args.sch = sch;	
	act.sa_handler = Handler;
	
	if (-1 == sigaction(SIGUSR1, &act, NULL) || (-1 == sigaction(SIGUSR2, &act, NULL)))
	{
		perror("Error: sigaction failure.");
		return FAILED;
	}
	
	args.action = act;
	
	args.wd_ready = sem_open("wdre", O_CREAT, NAMED_SEM_PERM, 0);
	if (SEM_FAILED == args.wd_ready)
    {
        perror("Error: sem_open");
        return FAILED;
    }
    
	args.wd_down = sem_open("wddo", O_CREAT, NAMED_SEM_PERM, 0);	
	if (SEM_FAILED == args.wd_down)
    {
            perror("Error: sem_open wd_down");
    }
    
    if (-1 == sem_init(&system_ready, 0, 0))
	{
		perror("Error: sem_init.");
		return FAILED;
	}
		
	return SUCCESS;
}

static void CleanResources(void)
{
	SCHDestroy(args.sch);
	sem_close(args.wd_ready);
	sem_unlink("wdre");
	sem_close(args.wd_down);
	sem_unlink("wddo");
}
