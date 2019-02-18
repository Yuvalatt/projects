package il.co.ilrd.code;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class myString{
	
}
class Animal1<T extends myString>{
	Animal1(){
		System.out.println("Animal");	
	}
	void sayhello(){
		System.out.println("woof");
	}
}

class Cat<T extends myString> extends Animal1<T> {
	Cat(){
		System.out.println("cat");	
	}
	void sayhello(){
		System.out.println("miewww");
	}
}

class Outer_Demo {
	   int num;
	   
	   // inner class
	   private class Inner_Demo {
	      public void print() {
	    	  
	         System.out.println("This is an inner class");
	      }
	   }
	    
	   // Accessing he inner class from the method within
	   void display_Inner() {
	      Inner_Demo inner = new Inner_Demo();
	      inner.print();
	   }
	}


public class SimpleTest {
	public static void main(String[] args) {
		Integer[]values = {1,2,3,4,20};
		List<Integer> li = Arrays.asList(values);
		List<Integer> li2 = Collections.unmodifiableList(li);
		
		for(Integer e : li2) {
			System.out.println(e);
		}
		
		Cat<myString> c = new Cat<myString>();
		Animal1<myString> a = c;
		a.sayhello();	
	}
}
