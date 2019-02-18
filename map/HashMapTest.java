package il.co.ilrd.map;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map.Entry;
import org.junit.Assert;

import org.junit.jupiter.api.Test;

class HashMapTest {
	HashMap<Integer, String> map = new HashMap<>();

		
	@Test
	void TestHashMap() {
		assertTrue(map.isEmpty());
		map.put(1, "A");
		map.put(2, "B");
		map.put(3, "C");
		map.put(4, "D");
		map.put(5, "E");
		map.put(1, "A");
		map.put(2, "B");
		map.put(3, "C");
		map.put(4, "D");
		map.put(5, "E");
		assertEquals(5, map.size());
	}

	@Test
	void Put() {
		map.put(1, "A");
		map.put(2, "B");
		map.put(3, "C");
		map.put(4, "D");
		map.put(17, "AA");
		map.put(18, "BB");
		map.put(19, "CC");
		map.put(20, "DD");
		map.put(17, "AA");
		map.put(18, "BB");
		map.put(19, "CC");
		map.put(20, "DD");
		assertEquals(8, map.size());
	}
	
	@Test
	void Put0() {
		map.put(0, "A");
		assertEquals("A", map.get(0));

	}
	@Test
	void PutNull() {
		map.put(null, "A");
		map.put(null, "A");
		assertEquals(1, map.size());
		System.out.println("$$$");		
		assertEquals("A", map.get(null));


	}

	@Test
	void Get() {
		map.put(0, "$");
		map.put(1, "A");
		map.put(2, "B");
		map.put(3, "C");
		map.put(4, "D");
		map.put(17, "AA");
		map.put(18, "BB");
		map.put(19, "CC");
		map.put(20, "DD");

		assertEquals("$", map.get(0));
		assertEquals("A", map.get(1));
		assertEquals("B", map.get(2));
		assertEquals("C", map.get(3));
		assertEquals("D", map.get(4));
		assertEquals("AA", map.get(17));
		assertEquals("BB", map.get(18));
		assertEquals("CC", map.get(19));
		assertEquals("DD", map.get(20));

		assertEquals("$", map.get(0));
		assertEquals(null, map.get(33));
		assertEquals(null, map.get(22));
		assertEquals(null, map.get(null));

	}

	@Test
	void Remove() {
		map.put(1, "A");
		map.put(2, "B");
		map.put(3, "C");
		map.put(4, "D");
		map.put(17, "AA");
		map.put(18, "BB");
		map.put(19, "CC");
		map.put(20, "DD");

		assertEquals("A", map.remove(1));
		assertEquals(null, map.remove(1));
		map.remove(2);
		map.remove(3);
		map.remove(4);
		map.remove(18);

		assertEquals(3, map.size());
	}

	@Test
	void Remove2() {
		for (int i = 0; i < 2000; ++i) {
			map.put(i, "$");
		}
		assertEquals(2000, map.size());
		for (int i = 0; i < 2000; ++i) {
			map.remove(i);
		}
		assertEquals(0, map.size());
	}

	@Test
	void Remove3() {
		map.put(null, "A");
		assertEquals(1, map.size());
		map.remove(null);
		map.remove(null);
		assertEquals(0, map.size());


	}

	@Test
	void ContainsKey() {
		map.put(1, "A");
		map.put(2, "B");
		map.put(3, "C");
		map.put(4, "D");
		map.put(17, "AA");
		map.put(18, "BB");
		map.put(19, "CC");
		map.put(20, "DD");
		assertEquals(true, map.containsKey(1));
		assertEquals(true, map.containsKey(2));
		assertEquals(true, map.containsKey(3));
		assertEquals(true, map.containsKey(4));
		assertEquals(true, map.containsKey(17));
		assertEquals(true, map.containsKey(18));
		assertEquals(true, map.containsKey(19));
		assertEquals(false, map.containsKey(5));
		assertEquals(false, map.containsKey(6));
		assertEquals(false, map.containsKey(7));
	}

	@Test
	void ContainsValue() {
		map.put(1, "A");
		map.put(2, "B");
		map.put(3, "C");
		map.put(4, "D");
		map.put(17, "AA");
		map.put(18, "BB");
		map.put(19, "CC");
		map.put(20, "DD");
		assertEquals(true, map.containsValue("A"));
		assertEquals(true, map.containsValue("B"));
		assertEquals(true, map.containsValue("C"));
		assertEquals(true, map.containsValue("AA"));
		assertEquals(true, map.containsValue("BB"));
		assertEquals(true, map.containsValue("CC"));
		assertEquals(false, map.containsValue("CV"));
		assertEquals(false, map.containsValue("aa"));
		assertEquals(false, map.containsValue("adsa"));
	}

	@Test
	void Clear() {
		map.put(1, "A");
		map.put(2, "B");
		map.put(3, "C");
		map.put(4, "D");
		map.put(17, "AA");
		map.put(18, "BB");
		map.put(19, "CC");
		map.put(20, "DD");
		map.clear();
		assertEquals(false, map.containsValue("A"));
		assertEquals(false, map.containsKey(3));
		assertEquals(0, map.size());
	}

	@Test
	void KeySet() {
		map.put(1, "A");
		map.put(2, "B");
		map.put(3, "C");
		map.put(4, "D");
		map.put(5, "AA");
		map.put(6, "BB");
		map.put(7, "CC");
		map.put(8, "DD");

		Integer i = 0;
		Iterator<Integer> iter = map.keySet().iterator();
		while (iter.hasNext()) {
			++i;
			assertEquals(i, iter.next());
		}
	}

	@Test
	void ValueSet() {
		map.put(1, "A");
		map.put(2, "B");
		map.put(3, "C");
		map.put(1233, "D");
		map.put(423, "AA");
		map.put(12, "BB");
		map.put(321, "CC");
		map.put(81, "DD");

		Iterator<String> iter = map.values().iterator();
		assertEquals("A", iter.next());
		assertEquals(true, map.containsValue(iter.next()));
	}

	@Test
	void EntrySetKey() {
		map.put(1, "A");
		map.put(2, "B");
		map.put(3, "C");
		map.put(4, "D");
		map.put(5, "AA");
		map.put(6, "BB");
		map.put(7, "CC");
		map.put(8, "DD");

		Iterator<Entry<Integer, String>> iter = map.entrySet().iterator();
		Integer i = 0;
		while (iter.hasNext()) {
			++i;
			assertEquals(i, iter.next().getKey());
		}
	}

	@Test
	void EntrySeValuest() {
		map.put(1, "A");
		map.put(2, "B");
		map.put(3, "C");
		map.put(4, "D");
		map.put(5, "AA");
		map.put(6, "BB");
		map.put(7, "CC");
		map.put(8, "DD");

		Iterator<Entry<Integer, String>> iter = map.entrySet().iterator();
		assertEquals(true, map.containsValue(iter.next().getValue()));
	}
	
	@Test
	void putAll() {
		map.put(1, "A");
		map.put(2, "B");
		map.put(3, "C");
		map.put(4, "D");
		map.put(5, "AA");
		map.put(6, "BB");
		map.put(7, "CC");
		map.put(8, "DD");
		
		HashMap<Integer, String> map2 = new HashMap<>();
		map2.put(1, "A");
		map2.put(2, "B");
		map2.put(3, "C");
		map2.put(4, "D");
	
		map2.put(55, "$");
		map2.put(66, "!!");
		map2.put(77, "#@!");
		map2.put(88, "&^*&");

		map.putAll(map2);
		assertEquals(12, map.size());
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
}