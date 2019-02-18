package il.co.ilrd.map;
import java.util.Comparator;
import java.util.Map;
import java.util.function.BiFunction;

public class Pair<K,V> implements Map.Entry<K,V>{	
	private K key;
	private V value;
	
	private Pair(K key, V value) {
		this.key = key;
		this.value = value;
	}
	
	public void setKey(K key) {
		this.key = key;
	}

	@Override
	public V setValue(V value) {
		V oldValue = this.value;
		this.value = value;
		return oldValue;
	}
	
	@Override
	public K getKey() {
		return key;
	}

	@Override
	public V getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		  return key + "=" + value; 
	}
	
	@Override
	public boolean equals(Object obj) {
		//if (!(Pair.class.isInstance(obj))) {
			//return false;
		//}	
		return(key == null ? ((Pair<?,?>)obj).getKey() == null : key.equals(((Pair<?,?>)obj).getKey()))  &&
			  (value == null ? ((Pair<?,?>) obj).getValue() == null : value.equals(((Pair<?,?>)obj).getValue()));
	} 	
	
	@Override
	public int hashCode() {
		return (key == null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode());
	}
	
	public static <K,V> Pair<K,V> of(K key, V val){
		return new Pair<K,V>(key,val);
	}
	
	public static <K,V> Pair<V,K> swap(Pair<K,V> pair){
		return of(pair.getValue(), pair.getKey());
	 } 
		
	private static <T> Pair<T,T> minMax(T[] arr, BiFunction<T, T, Integer> biFunc){
		T currMax = arr[arr.length-1];
		T currMin = arr[arr.length-1];
		
		for (int i = 0; i < arr.length - 1; i += 2) {
			 if (biFunc.apply(arr[i], arr[i+1]) > 0){
				if (biFunc.apply(arr[i], currMax) > 0) {
					currMax = arr[i];
				}
				if (biFunc.apply(arr[i+1], currMin) < 0) {
					currMin = arr[i+1];
				}		
			}
			else {
				if (biFunc.apply(arr[i], currMin) < 0) {
					currMin = arr[i];
				}
				if (biFunc.apply(arr[i+1], currMax) > 0) {
					currMax = arr[i+1];
				}
			}
		}
		return of(currMin, currMax);
	}
	
	public static <T extends Comparable<T>> Pair<T,T> minMax(T[] arr){
		BiFunction<T, T, Integer> biFunc = (x, y)-> x.compareTo(y); 
		return minMax(arr, biFunc);
	}
	
	public static <T> Pair<T,T> minMax(T[] arr, Comparator<T> comperator){
		BiFunction<T, T, Integer> biFunc = (x, y)-> comperator.compare(x, y); 
		return minMax(arr, biFunc);
	}	
}