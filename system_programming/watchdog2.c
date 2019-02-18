#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <unistd.h>

#define UNUSED(X)(void)(X)

int main(int argc, char *argv[])
{	
		
	while (1)
	{
		int status = 0;
			
		sleep(1);		
		system(argv[1]);						
		
		wait(&status);
	}

	return EXIT_SUCCESS;
}
