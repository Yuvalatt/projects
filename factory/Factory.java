package il.co.ilrd.factory;
import java.util.function.Function;
import il.co.ilrd.map.HashMap;

public class Factory<K, D, T> {
	HashMap<K, Function<D, ? extends T>> map =  new HashMap<>(); 
	
	public void add(K key, Function<D, ? extends T> func) {
		map.put(key,func);
	}

	public T create(K key, D data) {		
		return map.get(key).apply(data);
	}
}
