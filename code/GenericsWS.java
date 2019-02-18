package il.co.ilrd.code;
import java.util.*;

class FooReference<T>{	
   private T t;          

   public void set(T t) {
       this.t = t;
   }

   public T get() {
       return t;
   }
   
   public void printRef() {
	   System.out.println("Runtime class:" + t.getClass().getSimpleName());
   }
}

public class GenericsWS{
	
	public static <T> void PrintArray(T[] arr) {
		for (T var : arr) {
			System.out.println(var);
		}
	}
	
	public static void main(String[] args) {
		/*List<String> v = new ArrayList<>();
		v.add("Hello");
		String s = v.get(0);
		System.out.println(s);
		
		Integer[] arr1 = {1, 2, 3};
		Double[] arr2 = {1.1, 2.2, 3.3};
		String[] arr3 = {"10", "20", "30"};
		
		GenericsWS.PrintArray(arr1);
		GenericsWS.PrintArray(arr2);
		GenericsWS.PrintArray(arr3);
	
		List<Object> l1 = new ArrayList<>();
		List<?> l2 = new ArrayList<Object>();
		
		System.out.println(l1.equals(l2));*/
		
		FooReference<String> f1 = new FooReference<>();
		f1.set("Wow");
		f1.printRef();
		
	}
}
