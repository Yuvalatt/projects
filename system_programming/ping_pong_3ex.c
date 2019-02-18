#define _POSIX_SOURCE		  		/*  for sigaction          */
#include <stdio.h>		  	    	/*  for puts, printf       */
#include <stdlib.h>		  		    /*  for EXIT_SUCCESS       */
#include <sys/types.h>	  	   		/*  for pid_t              */
#include <unistd.h>			 	    /*  for getpid()           */
#include <signal.h>   			   	/*  for SIGUSR1, SIGUSR2   */

typedef enum {FALSE, TRUE} status_t;	

static volatile sig_atomic_t stop = FALSE;

static void Handler(int signum)
{	
	if (SIGUSR1 == signum)
	{		
		puts("Ping");		
		kill(getppid(), SIGUSR2);	        /* Sends a signal from child to parent */
	}

	if (SIGINT == signum)			        /* Defines Ctrl-c action               */
	{		
		puts("		 EXIT ");
		stop = TRUE;
		exit(EXIT_SUCCESS);
	}
}

int main(void)
{	
    struct sigaction act = {0};
	act.sa_handler = Handler;

	if (-1 == sigaction(SIGUSR1, &act, NULL))
	{
		fputs("Error: sigaction failure.", stderr);
		return EXIT_FAILURE;
	}
	
	if (-1 == sigaction(SIGINT, &act, NULL))
	{
		fputs("Error: sigaction failure.", stderr);
		return EXIT_FAILURE;
	}
	
	kill(getppid(), SIGUSR2);

	while (!stop);
	
	return EXIT_SUCCESS;
}
