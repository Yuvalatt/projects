package il.co.ilrd.map;
import java.util.Comparator;

class ComparatorC<T extends Comparable<T>> implements Comparator<T>{
	@Override
	public int compare(T o1, T o2) {
		return  o1.compareTo(o2);
	}	
}