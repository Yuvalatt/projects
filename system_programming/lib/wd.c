#define _POSIX_SOURCE                   /*   struct sigaction    */
#include <stdlib.h>                     /*   EXIT_SUCCESS        */
#include <stdio.h>                      /*   puts, printf        */
#include <sys/types.h>	                /*   pid_t               */
#include <pthread.h>                    /*   pthread_create()    */
#include <signal.h>                     /*   SIGUSR1, SIGUSR2    */
#include <semaphore.h>                  /*   sem_post, sem_wait  */
#include <fcntl.h> 						/*   O_CREAT, O_EXEC     */
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
    sem_t *wd_ready; 						/*      synch semaphore         */
    sem_t *wd_down;
} args_t;

static args_t args = {0}; 

static int KeepAlive(void);
static void Handler(int signum);
static int SignalAlive(void *param);
static int InitResources(void);
static void CleanResources(void);

int main(int argc, char *argv[])
{	
    args.argv = argv;
    args.argc = argc;
    
	if (FAILED == KeepAlive())
	{
	    perror("KeepAlive failed.");
	    	    
	    return EXIT_FAILURE;
	}
   
	return EXIT_SUCCESS;
}

static int KeepAlive(void)
{  	
	if (FAILED == InitResources())
	{
		return FAILED;
	}
    
	args.dest_pid = getppid();	
		
	sem_post(args.wd_ready);
 
 	args.last_signal_time = time(0);
 	
    SCHRun(args.sch);
       	
	return SUCCESS;
}

static void Handler(int signum)
{
	if (SIGUSR1 == signum)			        /* got a SIGUSR1 from App, "App is alive." */
	{
		DBG(puts("App Alive"));
		
	    args.last_signal_time = time(0);
	}
	
	else if (SIGUSR2 == signum)             /* got a SIGUSR2 from App, WD exits. */
	{	
	    DBG(puts("WD - EXIT"));
	    	
        SCHStop(args.sch);
		
		CleanResources();
		
		sem_post(args.wd_down);
		
		sem_close(args.wd_down);            
  		
	    exit(0);
	}		
}
/*     TASK FUNC         */
/*if APP fail--> exec - WD runs APP, APP forks a new WD */
static int SignalAlive(void *param)
{	
    UNUSED(param);
    
    if (difftime(time(0), args.last_signal_time) >= FAULT_CHECK_INTERVAL)      
	{			
		DBG(puts("WD: detected App failed"));	
		kill(args.dest_pid, SIGTERM);	    
		
		if (-1 == execv(args.argv[0], args.argv))
		{
			perror("Error: execv failure.");
			return FAILED;
		}				
		exit(0);
	}
    else
    {
        kill(args.dest_pid, SIGUSR1);           /* Send app_handler a signal - "WD alive". */
    }
	
	return SUCCESS;
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
	
	if (-1 == sigaction(SIGUSR1, &act, NULL) ||
	   (-1 == sigaction(SIGUSR2, &act, NULL)))
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
        return FAILED;
	}
	  
	return SUCCESS;
}

static void CleanResources(void)
{       	
		SCHDestroy(args.sch);	    
        sem_close(args.wd_ready);
        sem_unlink("wdre");
}
