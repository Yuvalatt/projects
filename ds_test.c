#include <time.h>
#include <stdlib.h>
#include <stdio.h>
#include <assert.h>
#include <string.h>
#include "../include/slist.h"
#include "../include/stack.h"


/***************** DS Test Solution ******************/

/* 1-a */
#define ARR_SIZE 20
typedef struct node
{
    int data;
    time_t last_update;
} node_t;

static node_t arr[ARR_SIZE] = {0};
static time_t setall_last_update = 0;
static int setall_num = 0;

void setval(int indx, int val){   
    arr[indx].last_update = time(0);
    arr[indx].data = val;
}

int getval(int indx){
    if(arr[indx].last_update >= setall_last_update)
    {
        return arr[indx].data;
    }
    return setall_num;
}

void setall(int val){
    setall_num = val;
    setall_last_update = time(0);
}
int Compare(const void *data1, const void *data2)
{
    return(*(int *)data1 - *(int *)data2);
}

/* 1-b */
void PrintMatchingTwo(unsigned int arr[], int size,  unsigned sum_of_two){
    int i = 0;
    unsigned curr_sum = 0;
    int j = size - 1;
    qsort(arr, size, sizeof(unsigned int), &Compare);

    while (i <= j)
    {
        curr_sum = arr[i] + arr[j];

        if (curr_sum == sum_of_two)
        {
            printf("2 matching numbers found: [%u, %u]\n", arr[i], arr[j]);
            break;
        }
        if (curr_sum < sum_of_two)
        {
            ++i;
        }
		--j;        
    }
    
    if (i >= j)
    {
        puts("\nNo 2 matching numbers were found\n");
    }
}

/* 1-b */
void PrintMatchingTwoB(int arr[], int size, int sum_of_two){
    int i = 0;
    int *checked = NULL;   
    checked = (int *)calloc(sum_of_two, sizeof(int));
    
    for (i = 0; i < size; ++i)
    {
    	if (arr[i] < sum_of_two)
    	{
			if (checked[i] != 0)
			{
					printf("found 2 matching numbers: [%d, %d]\n", i, checked[i]);
					break;
			}
			checked[sum_of_two - arr[i]] = arr[i];
    	}
    }
    
    if (i == size)
    {
        puts("No 2 matching numbers were found]\n");
    }
}

/* 1-c */
int IsFound(char arr[], size_t size, char number)
{
    char result = 1;
    size_t i = 0;

    for (i = 0; i < size; ++i)
    {
        result *= (number ^ arr[i]);
    }

    return (0 == result);
}

/* 2-a 
N bytes circular shift */

/* 3-a */
#define STACK_SIZE 50
typedef struct min_stack
{
    stack_t *values;
    stack_t *minimum;
} min_stack_t;

min_stack_t *CreateMinStack(size_t elements, size_t element_size)
{
    min_stack_t *min_stack = malloc(elements * element_size);
    
    if(!min_stack)
    {
        return NULL;
    }
    
    min_stack->values = StackCreate(elements, element_size);
    min_stack->minimum = StackCreate(elements, element_size);
 
    return min_stack;
}

void MinStackPush(min_stack_t *min_stack, void *data){
   	int num = 0;
    if (0 == StackSize(min_stack->values))
    {  
        StackPush(min_stack->minimum, data);
    }
    else
    {
    	num = *(int *)StackPeek(min_stack->minimum);
		if (*(int *)data < num)
		{
	  
		    StackPush(min_stack->minimum, data);
		}
		else
		{
		    StackPush(min_stack->minimum, &num);
		}
    }
    StackPush(min_stack->values, data);
}

void *MinStackPop(min_stack_t *min_stack)
{
    void *data = StackPeek(min_stack->values);
    StackPop(min_stack->values);
    StackPop(min_stack->minimum);
    return data;
}

void *getMinimum(min_stack_t *min_stack)
{
    void *data = StackPeek(min_stack->minimum);
    return data;
}

#define VALID 1
#define INVALID 0
#define OPEN_BRACETS(x)(((x) == '[') || ((x) == '{') || ((x) == '('))
#define CLOSE_BRACETS(x)(((x) == ']') || ((x) == '}') || ((x) == ')'))

char Match(char ch)
{
	switch(ch) {
		case '[':
		return ']';
		
		case '{':
		return '}';
	
		case '(':
		return ')';
		
		default:
		return ' ';
	}
}

int IsValidExpression(char *str)
{
    stack_t *stack = StackCreate(STACK_SIZE, sizeof(char));
    
    while ('\0' != *str)
    {
        if (1 == OPEN_BRACETS(*str))
        {
            StackPush(stack, str);
        }
        else if (1 == CLOSE_BRACETS(*str))
        {
            if (Match(*(char *)StackPeek(stack)) != *str)
            {
            	 return INVALID;
            	
            }
            StackPop(stack);       
        }
        ++str;
    }
    return ((0 == StackSize(stack)) ? VALID : INVALID);
}

/* 4 -a */
slist_node_t *FlipList(slist_node_t *head)
{
    slist_node_t *next = NULL;
    slist_node_t *prev = NULL;

    while (NULL != head)
    {
        next = head->next;
        head->next = prev;
        prev = head;
        head = next;
    }

    return prev;
}

slist_node_t *RecFlipList(slist_node_t *head)
{
    slist_node_t *new_head;
	
	if (NULL == head || NULL == head->next)
	{
		return head;
	}
	
    new_head = RecFlipList(head->next);
    head->next->next = head;
    head->next = NULL;

    return new_head;
}

int HasSharedNode(slist_node_t *head_1, slist_node_t *head_2)
{
	while (NULL != head_1->next)
	{
		head_1 = head_1->next;
	}
	
	while (NULL != head_2->next)
	{
		head_2 = head_2->next;
	}
	
	return (head_1 == head_2);
}

/* 4 -b */
void Swap(char *ch1, char *ch2)
{
	char temp = *ch1;
	*ch1 = *ch2;
	*ch2 = temp;
}

void Permute(char *str, int l, int r)
{
	int i;	
	if (l == r)
	{
		printf("%s\n", str);
	}
	
	for(i = l; i < r; ++i)
	{
		Swap(str, str + i);
		Permute(str, l + 1, r);
		Swap(str, str + i);
	}	
}

void PrintPermutation(char *str)
{
	Permute(str, 0, strlen(str));
}

int RecFibonacci(int a1, int a2, int n)
{
	if (0 == n)
	{
		return 0;
	}
	
	if (1 == n)
	{
		return 1;
	}
	return a1 + RecFibonacci(a2, a1 + a2, n-1);
}

int Fibonnaci(int n)
{
	return RecFibonacci(0, 1, n);
}

int RecMultiply(int x, int y)
{
	if (0 == y)
	{
		return 0;
	}
	return x + RecMultiply(x, y -1);
}

int Multiply(int x, int y)
{	
	if (x < 0 && y > 0)
	{
		return -RecMultiply(-x, y);
	}
	
	if (x > 0 && y < 0)
	{
		return -RecMultiply(x, -y);
	}
	
	if (x < 0 && y < 0 )
	{
		return RecMultiply(-x, -y);
	}
	
	return RecMultiply(x, y);
}

int RecDevision(int x, int y)
{
	if (0 == x)
	{
		return 0;
	}
	return 1 + RecDevision(x - y, y);
}

int Devision(int x, int y)
{	
	if (x < 0 && y > 0)
	{
		return -RecDevision(-x, y);
	}
	
	if (x > 0 && y < 0)
	{
		return -RecDevision(x, -y);
	}
	
	if (x < 0 && y < 0 )
	{
		return RecDevision(-x, -y);
	}
	
	return RecDevision(x, y);
}

int AddOne(int num) 
{ 
    int m = 1;  
    while (num & m) 
    { 
        num = num ^ m; 
        m <<= 1; 
    } 
    num = num ^ m; 
 
    return num; 
}

int RecAddOne(int a, int b)
{
	if (b == 0)
	{
		return a;
	}
	
	return RecAddOne(a ^ b, (a & b) << 1);
}

/***********************************************************************/
static void PrintList(slist_node_t *head)
{
	slist_node_t *current = head;
	slist_node_t *next = NULL;
	int i = 1;
	
	while(NULL != current)
	{
		next = current->next;
		printf("%d -> ", *(int *)current->val);
		current = next;
		++i;	
	}
	printf("NULL\n");
}

void FlipTest1()
{
	int val1 = 1;
	int val2 = 2;
	int val3 = 3;	

	slist_node_t *node1 = NULL;
	slist_node_t *node2 = NULL;
	slist_node_t *node3 = NULL;

	node1 = SListCreateNode(&val1, NULL);     /* Create nodes */
	node2 = SListCreateNode(&val2, node1);    
	node3 = SListCreateNode(&val3, node2);

	puts("\n---------------Before flip----------------------");
	PrintList(node3);
	node3 = (slist_node_t *)FlipList(node3);
	
	puts("---------------After flip----------------------");
	PrintList(node3);

	puts("\nSUCCESS - Fliplist Test\n");
}

void FlipTest2()
{
	int val1 = 1;
	int val2 = 2;
	int val3 = 3;	

	slist_node_t *node1 = NULL;
	slist_node_t *node2 = NULL;
	slist_node_t *node3 = NULL;

	node1 = SListCreateNode(&val1, NULL);     /* Create nodes */
	node2 = SListCreateNode(&val2, node1);    
	node3 = SListCreateNode(&val3, node2);

	puts("\n---------------Before flip----------------------");
	PrintList(node3);
	node3 = (slist_node_t *)RecFlipList(node3);
	
	puts("---------------After flip----------------------");
	PrintList(node3);

	puts("\nSUCCESS - Fliplist Test\n");
}


void TestMatching2()
{
	int arr2[] = {8, 4 ,3, 2, 6};
	unsigned int arr4[] = {8, 4 ,3, 2, 6};
	PrintMatchingTwo(arr4, 5, 6);
	PrintMatchingTwoB(arr2, 5, 6);
	PrintMatchingTwo(arr4, 5, 25);
	PrintMatchingTwoB(arr2, 5, 25);
}

void TestMinStack()
{
		
	int arr3[] = {10, 8 ,3, 2, 6};
	min_stack_t *min_stack = NULL;

	min_stack = CreateMinStack(STACK_SIZE, sizeof(int));
	MinStackPush(min_stack, &arr3[0]);
	MinStackPush(min_stack, &arr3[1]);
	printf("Minimun stack value: %d\n", *(int *)getMinimum(min_stack));
	MinStackPush(min_stack, &arr3[2]);
	printf("Minimun stack value: %d\n", *(int *)getMinimum(min_stack));
	MinStackPush(min_stack, &arr3[3]);
	printf("Minimun stack value: %d\n", *(int *)getMinimum(min_stack));
	MinStackPush(min_stack, &arr3[4]);
	printf("Minimun stack value: %d\n", *(int *)getMinimum(min_stack));
	MinStackPop(min_stack);
	printf("Minimun stack value: %d\n", *(int *)getMinimum(min_stack));
	MinStackPop(min_stack);
	printf("Minimun stack value: %d\n", *(int *)getMinimum(min_stack));
	MinStackPop(min_stack);
	printf("Minimun stack value: %d\n", *(int *)getMinimum(min_stack));
	MinStackPop(min_stack);
	printf("Minimun stack value: %d\n\n", *(int *)getMinimum(min_stack));
}

void TestValidExpression()
{
	char *str = "(x+3*[4+6])1";
	char *str2 = "((x+3*[4+6])1()";
	printf("Is valid expression? %s\n", (IsValidExpression(str) ? "Yes" : "No"));
	printf("Is valid expression? %s\n", (IsValidExpression(str2) ? "Yes" : "No"));
}

void TestIsNumFound()
{
	char arr[] = {8, 4 ,3, 2, 6};
	size_t size = 5;
	printf("Is num found %d\n\n", IsFound(arr, size, 6));
}

void TestPermute()
{
	char str3[] = "live";
	Permute(str3, 0, 4);
}

void TestRecursionFuncs()
{
	printf("\nFibonacci - 10: %d \n", Fibonnaci(10));
	printf("Fibonacci - 12: %d \n", Fibonnaci(12));
	
	printf("Multiply of 5 * 3: %d \n", Multiply(5, 3));
	printf("Multiply of -2 * -3: %d \n", Multiply(-2, -3));
	printf("Multiply of 2 * -3: %d \n", Multiply(2, -3));
	printf("Multiply of -10 * 3: %d \n\n", Multiply(-10, 3));
	
	printf("Division of 15 / 3: %d \n", Devision(15, 3));
	printf("Division of -12 / -3: %d \n", Devision(-12, -3));
	printf("Division of 12 / -3: %d \n", Devision(12, -3));
	printf("Division of -10 / 1: %d \n", Devision(-10, 1));
}

void TestSetAll()
{
	
	setall(5);
	printf("\nValue of index 0: %d\n", getval(0));
	setval(2, 40);
	printf("Value of index 2: %d\n", getval(2));
	printf("Value of index 3: %d\n", getval(3));
}

void TestSharedNode()
{
	int val1 = 1;
	int val2 = 2;
	int val3 = 3;	

	slist_node_t *node1 = NULL;
	slist_node_t *node2 = NULL;
	slist_node_t *head1 = NULL;
	slist_node_t *node3 = NULL;
	slist_node_t *node4 = NULL;
	slist_node_t *node5 = NULL;
	slist_node_t *head2 = NULL;
	
	node1 = SListCreateNode(&val1, NULL);
	node2 = SListCreateNode(&val2, node1);    
	head1 = SListCreateNode(&val3, node2);
	
	node3 = SListCreateNode(&val1, NULL);
	node4 = SListCreateNode(&val1, node3);
	node5 = SListCreateNode(&val1, node4);
	head2 = SListCreateNode(&val1, node5);
	
	printf("\nDo lists have a shared node? %s\n", (HasSharedNode(head1, head2) ? "YES" : "NO"));
	
	node1 = SListCreateNode(&val1, NULL);    	
	node2 = SListCreateNode(&val2, node1);    
	head1 = SListCreateNode(&val3, node2);
	
	node3 = SListCreateNode(&val1, NULL);
	node4 = SListCreateNode(&val1, node1);
	node5 = SListCreateNode(&val1, node4);
	head2 = SListCreateNode(&val1, node5);
	
	printf("Do lists have a shared node? %s\n", (HasSharedNode(head1, head2) ? "YES" : "NO"));
}

void AddOneTest()
{
	printf("Add one - 12 ---> %d\n", AddOne(12));
	printf("Recursive Add one - 5 --> %d\n", RecAddOne(5, 1));
}

int main()
{	
	TestIsNumFound();
	FlipTest1();
	FlipTest2();
	TestMinStack();
	TestMatching2();
	TestValidExpression();
	TestPermute();
	TestRecursionFuncs();
	TestSetAll();
	TestSharedNode();
	AddOneTest();
	
    return 0;
}
