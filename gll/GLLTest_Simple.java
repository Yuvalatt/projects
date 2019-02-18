package il.co.ilrd.gll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

public class GLLTest_Simple {

	static void Test1() {
		GenericLinkedList<Integer> list = new GenericLinkedList<>();

		if (list.isEmpty() && 0 == list.getListSize()) {
			System.out.println("Test1 - SUCCESS");
		}
		else
		{
			System.out.println("Test1 - FAILURE");
		}
	}

	static void Test2() {
		GenericLinkedList<Integer> list = new GenericLinkedList<>();
		list.pushFront(1);
		list.pushFront(2);
		list.pushFront(3);

		if (!list.isEmpty() && 3 == list.getListSize()) {
			System.out.println("Test2 - SUCCESS");
		}
		else
		{			
			System.out.println("Test2 - FAILURE");

		}
	}

	static void Test3() {
		GenericLinkedList<Integer> list = new GenericLinkedList<>();
		Iterator<Integer> li1;
		Iterator<Integer> li2;
		Iterator<Integer> li3;

		list.pushFront(1);
		list.pushFront(2);
		list.pushFront(3);

		li1 = list.find(2);
		li2 = list.find(1);
		li3 = list.find(10);

		if (2 == (int)li1.next() && 1 == (int) li2.next() && null == li3) {
			System.out.println("Test3 - SUCCESS");
		}
		else
		{
			System.out.println("Test3 - FAILURE");
		}
	}

	static void Test4() {

		GenericLinkedList<Integer> list = new GenericLinkedList<>();

		list.pushFront(1);
		list.pushFront(2);
		list.pushFront(3);
		list.pushFront(4);
		list.pushFront(5);

		if (5 != (int)list.popFront())
		{
			System.out.println("Test4 - FAILURE");
		}
		list.popFront();
		list.popFront();
		list.popFront();
		if (1 != (int)list.popFront())
		{
			System.out.println("Test4 - FAILURE");
		}

		list.popFront();
		if (0 == list.getListSize() && null == list.popFront())
		{
			System.out.println("Test4 - SUCCESS");
		}
		else
		{
			System.out.println("Test4 - FAILURE");
		}
	}

	public static void Test5() {
		//Reverse list test
		GenericLinkedList<Integer> list = new GenericLinkedList<>();
		Iterator<Integer> iter;
		list.pushFront(1);
		list.pushFront(2);
		list.pushFront(3);
		list.pushFront(4);
		list.pushFront(5);

		iter = list.iterator();
		while (iter.hasNext()) {
			System.out.print(iter.next() + "->");
		}

		System.out.println("\nNew Reversed:");
		GenericLinkedList<Integer> reversed = GenericLinkedList.newReverse(list);

		iter = reversed.iterator();
		while (iter.hasNext()) {
			System.out.print(iter.next() + "->");
		}
		System.out.println();
	}

	public static void Test6() {
		//Fail-safe test
		int test_count = 0;
		GenericLinkedList<Integer> list2 = new GenericLinkedList<>();		
		list2.pushFront(1);
		list2.pushFront(2);
		list2.pushFront(3);
		list2.pushFront(4);
		list2.pushFront(5);

		Iterator<Integer> iter2 = list2.iterator();

		while (iter2.hasNext()) {
			if (2 == test_count) {
				list2.pushFront(7);
			}		
			try {
				iter2.next();				
			}
			catch(ConcurrentModificationException e) {
				System.out.println("ConcurrentModificationException caught!!");
				assertNotNull(e);
				break;
			}
			++test_count;
		}
	}

	public static void main(String[] args) 
	{
		Test1();
		Test2();
		Test3();
		Test4();
		Test5();
		Test6();
	}
}