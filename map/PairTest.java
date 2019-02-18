package il.co.ilrd.map;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PairTest{	
	@Test
	void testHashCode() {
		Integer key = 1;
		Integer val = 5;
		Pair<Integer,Integer> pair = Pair.of(key, val);
		assertNotNull(key.hashCode() + val.hashCode() == pair.hashCode());
	}

	@Test
	void testSetKey() {
		Integer key = 14564;
		Integer val = 1;
		Pair<Integer,Integer> pair = Pair.of(key, val);
		assertTrue(14564 == pair.getKey());
		pair.setKey(200);
		assertTrue(200 == pair.getKey());
	}

	@Test
	void testSetValue() {
		Integer key = 14564;
		Integer val = 123;
		Pair<Integer,Integer> pair = Pair.of(key, val);
		assertTrue(123 == pair.getValue());
		Integer old_val = pair.setValue(500);
		assertTrue(123 == old_val);
		assertTrue(500 == pair.getValue());
	}

	@Test
	void testGetKey() {
		Integer key = 166788;
		Integer val = 123444;
		Pair<Integer,Integer> pair = Pair.of(key, val);
		assertTrue(166788 == pair.getKey());
	}

	@Test
	void testGetValue() {
		Integer key = 166788;
		Integer val = 123444;
		Pair<Integer,Integer> pair = Pair.of(key, val);
		assertTrue(123444 == pair.getValue());
	}

	@Test
	void testToString() {
		Integer key = 100;
		String val = "hi";
		Pair<Integer,String> pair = Pair.of(key, val);
		System.out.println(pair.toString());
		assertNotNull(pair.toString());
	}

	@Test
	void testEqualsObject() {
		Integer key = 100;
		Integer key1 = 6;
		String val = "hi";		
		Pair<Integer,String> pair = Pair.of(key, val);
		Pair<Integer,String> pair2 = Pair.of(key, val);	
		Pair<Integer,String> pair3 = pair;	
		Pair<Integer,String> pair4 = Pair.of(key1, val);
		assertFalse(pair.equals(pair4));
		assertTrue(pair.equals(pair2));
		assertTrue(pair3.equals(pair));
	}

	@Test
	void testOf() {
		Integer key = 10;
		Integer val = 12;
		Pair<Integer,Integer> pair = Pair.of(key, val);
		assertNotNull(pair);
	}

	@Test
	void testSwap() {
		Integer key = 10;
		Integer val = 12;
		Pair<Integer,Integer> pair = Pair.of(key, val);
		Pair<Integer,Integer> swaped = Pair.swap(pair);
		assertTrue(swaped.getKey() == pair.getValue());
		assertTrue(pair.getKey() == swaped.getValue());
	}

	@Test
	void testMinMaxTArray() {
		Integer[] arr = {100, 4, 9, 6, -8, 5};
		Pair<Integer,Integer> p1 = Pair.minMax(arr);
		assertTrue(-8 == p1.getKey());
		assertTrue(100 ==  p1.getValue());
	}

	@Test
	void testMinMaxTArrayComparatorOfT() {
		Integer[] arr2 = {100, 4, 9, 6, -8, 5, -17};
		Pair<Integer,Integer> p2 = Pair.minMax(arr2, new ComparatorC<Integer>());
		assertTrue(-17 == p2.getKey());
		assertTrue(100 ==  p2.getValue());
	}
}