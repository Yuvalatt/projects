#include <stdlib.h>                     /*   EXIT_SUCCESS         */
#include <stdio.h>                      /*   puts, printf         */
#include <sys/types.h>	                /*   pid_t                */
#include <signal.h>                     /*   SIGKILL		      */
#include <unistd.h>                     /*   sleep   		      */

#include "../include/wd.h"

static void FailApp()
{
	kill(getpid(), SIGTERM);
}

int main(int argc, char *argv[])
{
	int count = 0;
	KeepMeAlive(argc, argv);
	
	while (count < 50)
	{
	    /*if (10 == count)
	    {
	       FailApp(); 
	    }*/
	    
		puts("RUNNING...\n");
			
		sleep(1);
		
		++count;
	}
	
	return EXIT_SUCCESS;
}
