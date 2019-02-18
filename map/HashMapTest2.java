package il.co.ilrd.map;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map.Entry;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HashMapTest2 {
	int testPassed = 0;
	HashMap<Integer, String> map = new HashMap<>();

	@BeforeEach
	public void init() {
		testPassed = 0;
	}

	@Test
	void testHashMap() {
		HashMap<String, Integer> hm = new HashMap<>();
		HashMap<Double, Double> hm1 = new HashMap<>();
		HashMap<Pair<String, Integer>, Double> hm2 = new HashMap<>();		
		assertNotNull(hm);
		assertNotNull(hm1);
		assertNotNull(hm2);
		assertTrue(hm.isEmpty());
		assertTrue(hm1.isEmpty());
		assertTrue(hm2.isEmpty());
		++testPassed;
	}

	@Test
	void testHashMapInt() {
		HashMap<String, Integer> hm = new HashMap<>(0);
		HashMap<Double, Double> hm1 = new HashMap<>(12);
		HashMap<Pair<String, Integer>, Double> hm2 = new HashMap<>(10);		
		assertNotNull(hm);
		assertNotNull(hm1);
		assertNotNull(hm2);
		assertTrue(hm.isEmpty());
		assertTrue(hm1.isEmpty());
		assertTrue(hm2.isEmpty());
		++testPassed;
	}

	@Test
	void testSize() {
		HashMap<Integer, Double> hm = new HashMap<>();
		HashMap<Pair<String, Integer>, Double> hm2 = new HashMap<>();
		assertTrue(0 == hm.size());
		hm.put(1, 1.2);
		hm.put(1, 1.25);
		hm.put(null, 2.3);
		hm.put(9, 2.3);
		hm.put(9, 10.2);
		assertTrue(3 == hm.size());
		hm.remove(1);
		hm.remove(9);
		hm.remove(null);
		assertTrue(0 == hm.size());
		hm2.put(Pair.of("a",4), 3.5);
		assertTrue(1 == hm2.size());
		hm2.clear();
		assertTrue(0 == hm2.size());
		++testPassed;
	}

	@Test
	void testIsEmpty() {
		HashMap<Integer, Double> hm = new HashMap<>();
		assertTrue(true == hm.isEmpty());
		hm.put(1, 1.2);
		assertTrue(false == hm.isEmpty());
		hm.put(1, 1.25);
		hm.put(9, 2.3);
		hm.put(9, 2.3);
		hm.put(9, 10.2);
		hm.remove(1);
		hm.remove(9);
		hm.clear();
		assertTrue(true == hm.isEmpty());
		++testPassed;
	}


	@Test
	void testPut() {
		HashMap<String, Integer> hm = new HashMap<>();
		assertNotNull(hm);
		assertTrue(0 == hm.size());
		assertTrue(true == hm.isEmpty());
		assertTrue(null == hm.put("a", 1));
		assertTrue(null == hm.put("b", 2));
		assertTrue(null == hm.put("c", 3));
		assertTrue(3 == hm.put("c", 10));
		assertTrue(3 == hm.size());	
		++testPassed;
	}

	@Test
	void testGet() {
		HashMap<String, Integer> hm = new HashMap<>();
		hm.put("a", 1);
		hm.put("b", 2);
		hm.put("c", 3);
		hm.put("2", null);
		assertTrue(null == hm.get("2"));
		hm.put(null, 5);
		assertTrue(5 == hm.get(null));
		assertTrue(1 == hm.get("a"));
		++testPassed;
	}

	@Test
	void testContainsKey() {
		HashMap<String, Integer> hm = new HashMap<>();
		hm.put("a", 1);
		hm.put("b", 2);
		hm.put("c", 3);
		assertTrue(3 == hm.size());
		assertTrue(true == hm.containsKey("c"));
		++testPassed;
	}

	@Test
	void testContainsValue() {
		HashMap<String, Integer> hm4 = new HashMap<>();
		assertTrue(null == hm4.put("aaa", null));
		assertTrue(null == hm4.get("aaa"));		

		assertTrue(true == hm4.containsKey("aaa"));

		assertTrue(null == hm4.put("aaa", 1));
		assertTrue(1 == hm4.get("aaa"));		
		assertTrue(null == hm4.put("tas", null));
		hm4.put("a", 1);
		hm4.put("b", 2);
		hm4.put("c", 3);
		//assertTrue(5 == hm.size());
		assertTrue(true == hm4.containsValue(2));
		assertTrue(true == hm4.containsValue(1));
		//assertTrue(false == hm4.containsValue(5500));
		assertTrue(true == hm4.containsValue(null));
		++testPassed;
	}

	@Test
	void testRemove() {
		HashMap<String, Integer> hm = new HashMap<>();
		hm.put("a", 1);
		hm.put("b", 2);
		hm.put("c", 3);
		assertTrue(3 == hm.size());
		assertTrue(2 == hm.remove("b"));
		assertTrue(2 == hm.size());
		++testPassed;
	}

	@Test
	void testPutAll() {
		HashMap<String, Integer> hm = new HashMap<>();
		HashMap<String, Integer> hm3 = new HashMap<>();
		hm.put("a", 1);
		hm.put("b", 2);		
		hm3.put("d", 11);
		hm3.put("e", 22);
		hm3.put("f", 33);
		hm.putAll(hm3);
		assertTrue(5 == hm.size());

		++testPassed;
	}

	@Test
	void testClear() {
		HashMap<String, Integer> hm = new HashMap<>();
		hm.put("d", 11);
		hm.put("e", 22);
		assertTrue(2 == hm.size());
		hm.clear();
		assertTrue(0 == hm.size());
		++testPassed;
	}

	@Test
	void testKeySet() {
		HashMap<String, Integer> h = new HashMap<>();
		int count = 0;
		h.put("hi", 1);
		h.put("gg", 2);
		h.put("aaa", 3);
		h.put("v", 6);
		h.put("y", 200);
		h.put("dfsfs", 5);
		h.put(null, 5);

		for (Iterator<String> it = h.keySet().iterator(); it.hasNext();) {
			it.next();
			++count;
		}
		assertTrue(7 == count);
		++testPassed;
	}

	@Test
	void testValues() {
		HashMap<String, Integer> h = new HashMap<>();
		int count = 0;
		h.put("hi", 1);
		h.put("gg", 2);
		h.put("aaa", 3);
		h.put("v", 4);
		h.put("y", 5);
		h.put("dfsfs", 6);
		h.put("dfsfs", 5);
		for (Iterator<Integer> it = h.values().iterator(); it.hasNext();) {
			it.next();
			++count;
		}
		assertTrue(6 == count);
		++testPassed;
	}

	@Test
	void testEntrySet() {
		HashMap<String, Integer> h = new HashMap<>();
		int count = 0;
		h.put("hi", 1);
		h.put("gg", 2);
		h.put("aaa", 3);
		h.put("v", 6);
		h.put("y", 200);
		h.put("dfsfs", 5);
		h.put("dfsfs", 5);
		for (Iterator<Entry<String, Integer>> it = h.entrySet().iterator(); it.hasNext();) {
			it.next();
			++count;
		}
		assertTrue(6 == count);
		++testPassed;
	}

	@Test
	void FailFastPutAll() {
		map.put(1, "A");
		map.put(2, "B");
		map.put(3, "C");

		HashMap<Integer, String> map2 = new HashMap<>();
		map2.put(4, "D");
		map2.put(55, "$");
		map2.put(66, "!!");
		map2.put(77, "#@!");
		map2.put(88, "&^*&");

		Iterator<Entry<Integer, String>> iter = map.entrySet().iterator();
		map.putAll(map2);
		try {
			iter.next().getValue();
			Assert.fail("No exception was thrown $$");
		} catch (ConcurrentModificationException e) {	
		}

	}
	@Test
	void FailFastRemove() {
		map.put(1, "A");
		map.put(2, "B");
		map.put(3, "C");

		Iterator<Entry<Integer, String>> iter = map.entrySet().iterator();
		map.remove(1);
		try {
			iter.next().getValue();
			Assert.fail("No exception was thrown $$");
		} catch (ConcurrentModificationException e) {	
		}

	}

	@Test
	void FailFastClear() {
		map.put(1, "A");
		map.put(2, "B");
		map.put(3, "C");

		Iterator<Entry<Integer, String>> iter = map.entrySet().iterator();
		map.clear();
		try {
			iter.next().getValue();
			Assert.fail("No exception was thrown $$");
		} catch (ConcurrentModificationException e) {	
		}

	}

	@AfterEach
	public void IsTestPassed() {
		if (1 == testPassed) {
			System.out.println("Test Passed");
		}
	}
}