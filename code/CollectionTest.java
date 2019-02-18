package il.co.ilrd.code;
import java.util.*;
import java.util.stream.Collectors;

class DataObject{
	 private String code;
	 private Integer value;
	
	public DataObject(String str, int v) {
		this.code = str;
		this.value = v;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}

public class CollectionTest {
	
	public static void main(String[] args) {
		
		Integer [] values = {1,18,6,4,5,1,0};
		System.out.println(Arrays.toString(values));
		
		int[]arr2 = new int[]{1,3,3,3,5,6};
		System.out.println(Arrays.toString(arr2));
		
		List<Integer> list2 = Arrays.stream(arr2).boxed().collect(Collectors.toList());
		System.out.println(list2.toString());
		
		List<Integer> list = Arrays.asList(values);
		Collections.sort(list);
		System.out.println(list.toString());
		System.out.println();
		
		Collection<Integer> c = new LinkedList<>();
		c.add(1223);
		c.add(444);
		System.out.println("Collection:" + c.toString());
		
		Map<String, Integer> weekMap = new LinkedHashMap<>();
		
		weekMap.put("Sunday", 1);
		weekMap.put("Monday", 2);
		weekMap.put("Tuesday", 3);
		weekMap.put("Wednesday", 4);
		weekMap.put("Thursday", 5);
		weekMap.put("Friday", 6);
		weekMap.put("Saturday", 7);
		
		for (Map.Entry<String, Integer> me : weekMap.entrySet()) {
			System.out.println(me.getKey() + ":" + me.getValue());
		}
		
		System.out.println();
		for (String dayname : weekMap.keySet()) { 
			System.out.println(dayname);
		}	
		
		System.out.println();
		for (Integer e : weekMap.values()) { 
			System.out.println(e);
		}
		
		DataObject[] data = {new DataObject("Hey1",1),
							 new DataObject("as",1334),
							 new DataObject("ff ",655),
							 new DataObject("t yu",888),
							 new DataObject("we",-500)};
		
		int sum = Arrays.stream(data).mapToInt(b -> b.getValue()).sum();		
		System.out.println("Total sum:" + sum);
		
	}
}
