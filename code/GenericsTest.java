package il.co.ilrd.code;
import java.util.ArrayList;
import java.util.List;



public class GenericsTest {

	

	public static void main(String[] args) 
	{
		List<Number> ints = new ArrayList<>();
		ints.add(2);

		List<Number> nums = ints;  // valid if List<Integer> were a subtype of List<Number> according to substitution rule. 
		nums.add(3.14);  

		Number x = ints.get(1); // now 3.14 is assigned to an Integer variable!
		System.out.println(x);
		
		List<? extends Integer> intList = new ArrayList<>();
		List<? extends Number>  numList = intList;

		List<Object> objlist;
		List<String> stringlist;
		
		
	}
}
