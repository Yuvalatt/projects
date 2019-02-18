#define _POSIX_SOURCE		  		/*  for sigaction         */
#include <stdio.h>		  		    /*  for puts, printf      */
#include <stdlib.h>		  		    /*  for EXIT_SUCCESS      */
#include <sys/types.h>	  	   		/*  for pid_t             */
#include <unistd.h>			 	    /*  for getpid()          */
#include <signal.h>   			   	/*  for SIGUSR1, SIGUSR2  */

typedef enum {FALSE, TRUE} status_t;

static pid_t pid = 0;
static pid_t childs_pid = 0;
static volatile sig_atomic_t stop = FALSE;

static void Handler(int signum)
{	
	if (SIGUSR2 == signum)
	{
		puts("Ping");

		kill(childs_pid, SIGUSR1);	       /* Send a signal from parent to child  */	
	}

	if (SIGUSR1 == signum)
	{
		puts("		Pong");

		kill(pid, SIGUSR2);		           /* Send a signal from child to parent   */
	}

	if (SIGINT == signum)			       /*  Defines Ctrl-c action                */
	{		
		puts("		 EXIT ");
		exit(EXIT_SUCCESS);
	}
}

int main(void)
{	
    struct sigaction act = {0};
    		
	pid = getpid();
	act.sa_handler = Handler;
	
	if (-1 == sigaction(SIGUSR2, &act, NULL))
	{
		fputs("Error: sigaction failure.", stderr);
		return EXIT_FAILURE;
	}

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

	childs_pid = fork();

	if (childs_pid)						   /* parent process is running   */
	{
		kill(childs_pid, SIGUSR1);		   /* Send a signal from parent to child  */
	}
	
	while (!stop);
	
	return EXIT_SUCCESS;
}
