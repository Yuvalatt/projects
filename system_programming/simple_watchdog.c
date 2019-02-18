#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <unistd.h>

#define UNUSED(X)(void)(X)

int main(int argc, char *argv[])
{	
	int i = 0;

	pid_t pid = getpid();				/* get pid of current process  */
	pid_t parent_pid = getppid();			/* get pid of parent process  */					
	
	printf("Childs PID: %d \n", (int)pid); 
	printf("Parent PID: %d \n", (int)parent_pid); 
	
	while (1)
	{		
		int child_status = 0;
		pid_t child_pid = fork();		 /* creates a child process - duplicates current process. return value in parent: pid of child, in childs process: 0 */
							 /* when child_pid > 0 -  parent process is running, child_pid == 0 - childs process is running */
		if (child_pid == 0) 			 /* childs process is running */ 
		{			
			sleep(1);
			execv(argv[1], &argv[1]);
			
			exit(1);			/*    in case execv failed     */
		}
		else					/*  parent process is running */ 
		{
			/*wait(&child_status);*/
			waitpid(child_pid, &child_status, NULL);
		}
	}

	return EXIT_SUCCESS;
}
