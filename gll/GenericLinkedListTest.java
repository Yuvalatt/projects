package il.co.ilrd.gll;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import org.junit.jupiter.api.Test;

class GenericLinkedListTest {

	@Test
	void testPushFront() {
		GenericLinkedList<Integer> list = new GenericLinkedList<>();
		list.pushFront(1);
		list.pushFront(2);
		list.pushFront(3);
		list.pushFront(4);
		list.pushFront(5);
		assertTrue(5 == list.getListSize());
	}

	@Test
	void testIsEmpty() {
		GenericLinkedList<Integer> list = new GenericLinkedList<>();
		assertTrue(list.isEmpty());
		assertTrue(0 == list.getListSize());
	}

	@Test
	void testPopFront() {
		GenericLinkedList<Integer> list = new GenericLinkedList<>();

		list.pushFront(1);
		list.pushFront(2);
		list.pushFront(3);
		list.pushFront(4);
		list.pushFront(5);
		
		assertTrue(5 == (int)list.popFront());
		
		list.popFront();
		list.popFront();
		list.popFront();
		
		assertTrue(1 == (int)list.popFront());

		list.popFront();
		assertTrue(0 == list.getListSize());
		assertTrue(null == list.popFront());
	}

	@Test
	void testgetListSize() {
		GenericLinkedList<Integer> list = new GenericLinkedList<>();
		list.pushFront(1);
		list.pushFront(2);
		list.pushFront(3);
		assertTrue(!list.isEmpty());
		assertTrue(3 == list.getListSize());
	}

	@Test
	void testFind() {
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
		
		assertTrue((2 == (int) li1.next()));
		assertTrue(1 == (int) li2.next());
		assertTrue(null == li3);
	}

	@Test
	void testBegin() {
		GenericLinkedList<String> list = new GenericLinkedList<>();
		Iterator<String> iter = list.iterator();
		assertTrue(null != iter);	
	}

	@Test
	void testNewReverse() {
		GenericLinkedList<Integer> list = new GenericLinkedList<>();
		Iterator<Integer> iter;
		list.pushFront(1);
		list.pushFront(2);
		list.pushFront(3);
		list.pushFront(4);
		list.pushFront(5);

		GenericLinkedList<Integer> reversed = GenericLinkedList.newReverse(list);	
		iter = reversed.iterator();		
		assertTrue(1 == iter.next());
		assertTrue(2 == iter.next());
		assertTrue(3 == iter.next());
		assertTrue(4 == iter.next());
		assertTrue(5 == iter.next());
	}

	@Test
	void testIterator() {
		GenericLinkedList<Float> flist = new GenericLinkedList<>(); 
		Iterator<?> iter = flist.iterator();
		assertTrue(null != iter);
	}

	@Test
	void testGenericLinkedList() {
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
				assertNotNull(e);
				break;
			}
			++test_count;
		}
	}
}
