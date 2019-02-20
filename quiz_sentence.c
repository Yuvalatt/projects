#include <stdio.h>
#include <stdlib.h> /* EXIT_SUCCESS */
#include <assert.h>  /* assert */
#include <string.h>  /* strlen */

static void SwapChars(char *ch1, char *ch2);
static void ReverseString(char *str, char *end);
static void ReverseSentence(char *str);


int main()
{
	char sentence[] = "I am a student"; 

	printf("String before reverse is: %s\n", sentence);	

	ReverseSentence(sentence);

	printf("String after reverse is: %s\n", sentence);
	
	return EXIT_SUCCESS;
}


static void ReverseString(char *str, char *end)
{
	char *runner = str;	
	assert(str);

	while (runner < end)
	{
		SwapChars(runner, end);
		++runner;
		--end;
	}
}

static void ReverseSentence(char *str)
{
	char *start = str;
	char *runner = NULL;
	char *end = NULL;

	assert(str);
	
	runner = str;
	end = str + strlen(str) - 1; 
	
	ReverseString(start, end);
	
	runner = str;
	end = str + strlen(str) - 1;	

	while (runner < end)
	{
		if (' ' == *runner)
		{
			ReverseString(str, runner - 1);
			str = runner + 1;
		}
		++runner;
	}
}

static void SwapChars(char *runner, char *end)
{
		char temp = *runner;
		*runner = *end;
		*end = temp;
}

