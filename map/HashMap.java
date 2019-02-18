package il.co.ilrd.map;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class HashMap<K, V> implements Map<K, V> {
	
	private int modifiedCount;
	private int size;
	private ArrayList<LinkedList<Map.Entry<K, V>>> map;
	private Set<K> keySet;
	private Collection<V> values;
	private Set<Entry<K, V>> entrySet;
	private static final int DEFAULT_CAPACITY = 16;

	public HashMap() {
		this(DEFAULT_CAPACITY);
	}

	public HashMap(int capacity) {
		size = 0;
		map = new ArrayList<>(capacity);
		for (int i = 0; i < capacity; ++i) {
			map.add(new LinkedList<Map.Entry<K, V>>());
		}
	}

	private class Values extends AbstractCollection<V> {
		@Override
		public Iterator<V> iterator() {
			return new ValuesIterator();
		}

		@Override
		public int size() {
			return size;
		}

		private class ValuesIterator implements Iterator<V> {
			Iterator<Map.Entry<K, V>> iter = new EntrySet().iterator();

			@Override
			public boolean hasNext() {
				return iter.hasNext();
			}

			@Override
			public V next() {
				return iter.next().getValue();
			}
		}
	}

	private class KeysSet extends AbstractSet<K> {
		@Override
		public Iterator<K> iterator() {
			return new KeysSetIterator();
		}

		@Override
		public int size() {
			return size;
		}

		private class KeysSetIterator implements Iterator<K> {
			Iterator<Map.Entry<K, V>> iter = new EntrySet().iterator();

			@Override
			public boolean hasNext() {
				return iter.hasNext();
			}

			@Override
			public K next() {
				return iter.next().getKey();
			}
		}
	}

	private class EntrySet extends AbstractSet<Map.Entry<K, V>> {
		@Override
		public Iterator<Entry<K, V>> iterator() {
			return new EntryIterator();
		}

		@Override
		public int size() {
			return size;
		}

		private class EntryIterator implements Iterator<Map.Entry<K, V>> {
			private int iterModifiedCount;
			Iterator<Map.Entry<K, V>> linkedListIt;
			Iterator<LinkedList<Map.Entry<K, V>>> arrIt;

			private EntryIterator() {
				iterModifiedCount = modifiedCount;
				arrIt = map.iterator();

				while (arrIt.hasNext()) {
					linkedListIt = arrIt.next().iterator();
					if (linkedListIt.hasNext()) {
						break;
					}
				}
			}

			@Override
			public boolean hasNext() {
				Iterator<LinkedList<Map.Entry<K, V>>> currIt = arrIt;

				if (linkedListIt.hasNext()) {
					return true;
				}
				while (currIt.hasNext()) {
					linkedListIt = currIt.next().iterator();
					if (linkedListIt.hasNext()) {
						return true;
					}
				}
				return false;
			}

			@Override
			public Entry<K, V> next() {
				if (iterModifiedCount != modifiedCount) {
					throw new ConcurrentModificationException();
				}

				if (linkedListIt.hasNext()) {
					return linkedListIt.next();
				}
				while (arrIt.hasNext()) {
					linkedListIt = arrIt.next().iterator();
					if (linkedListIt.hasNext()) {
						return linkedListIt.next();
					}
				}
				return null;
			}
		}
	}

	private int hashIndex(Object key) {
		return (null == key) ? 0 : Math.abs(key.hashCode()) % map.size();
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return (0 == size);
	}

	@Override
	public boolean containsKey(Object key) {
		Iterator<Map.Entry<K, V>> it = map.get(hashIndex(key)).iterator();

		if (null == key) {
			while (it.hasNext()) {
				if (null == it.next().getKey()) {
					return true;
				}
			}
		} else {
			while (it.hasNext()) {
				if (it.next().getKey().equals(key)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		Iterator<V> it = new Values().iterator();

		if (null == value) {
			while (it.hasNext()) {
				if (null == it.next()) {
					return true;
				}
			}
		} else {
			while (it.hasNext()) {
				if (it.next().equals(value)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public V get(Object key) {
		int index = hashIndex(key);
		Iterator<Map.Entry<K, V>> it = map.get(index).iterator();
		Iterator<Map.Entry<K, V>> prev = map.get(index).iterator();
		V data = null;

		if (null == key) {
			while (it.hasNext()) {
				data = prev.next().getValue();
				if (null == it.next().getKey()) {
					return data;
				}
			}
		} else {
			while (it.hasNext()) {
				data = prev.next().getValue();
				if (it.next().getKey().equals(key)) {
					return data;
				}
			}
		}
		return null;
	}

	@Override
	public V put(K key, V value) {
		V oldVal = null;
		int index = hashIndex(key);

		if (containsKey(key)) {
			oldVal = get(key);
			remove(key);
		}
		++size;
		map.get(index).add(Pair.of(key, value));
		++modifiedCount;

		return oldVal;
	}

	@Override
	public V remove(Object key) {
		V value = get(key);

		if (map.get(hashIndex(key)).remove(Pair.of(key, value))) {
			++modifiedCount;
			--size;
		}

		return value;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
			put(e.getKey(), e.getValue());
		}
	}

	@Override
	public void clear() {
		for (LinkedList<Entry<K, V>> entry : map) {
			entry.clear();
			++modifiedCount;
		}
		size = 0;
	}

	@Override
	public Set<K> keySet() {
		if (null == keySet) {
			keySet = new KeysSet();
		}
		return keySet;
	}

	@Override
	public Collection<V> values() {
		if (null == values) {
			values = new Values();
		}
		return values;
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		if (null == entrySet) {
			entrySet = new EntrySet();
		}
		return entrySet;
	}
}