#include <unistd.h>
#include <fcntl.h>
#include <sys/mman.h>
#include <string.h>

int main(void) 
{
	char *map = NULL;
  	int fd = open("test_file.txt", O_RDWR | O_CREAT, (mode_t)0600);
  	const char *text = "hello";
  	size_t textsize = strlen(text) + 1;
  	lseek(fd, textsize-1, SEEK_SET);
  	/*write(fd, "", 1);*/
  	map = mmap(0, textsize, PROT_READ | PROT_WRITE, MAP_SHARED, fd, 0);
  	memcpy(map, text, strlen(text));
 	/* msync(map, textsize, MS_SYNC);*/
 	 munmap(map, textsize);
 	 close(fd);
 	 return 0;
}

