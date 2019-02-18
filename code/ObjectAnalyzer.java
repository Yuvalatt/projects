package il.co.ilrd.code;
import java.lang.reflect.*;

interface A {

}

interface B {

}

class F {
	
	private int x;
	private int y;

	F(){
		x = 0;
		y = 0;
	}
		
	F(int x, int y) {
		this.x = x;
		this.y = y;
	}
}

abstract class Fo extends F {	

}

class Foo extends F implements A, B {

	// fields
	private int x = 1;
	private int y = 2;
	static final int COUNT = 10;

	// constructors
	public Foo() {
		super(7, 9);
	}

	Foo(int a, int b) {
		
	}

	// methods
	void f1() {
		System.out.println("f1: " + (x + y) * COUNT);
	}

	protected void Fun2() {
		System.out.println("Fun2");
	}

	public void Fun3(String name, int age) {
		System.out.println(name +"'s age is: " + age);
	}
	
	private void Fun4(String name, int age) {
		System.out.println(name +"'s age is: " + age);
	}
}

public class ObjectAnalyzer {

	public static void main(String[] args) throws Exception {

		Class c = Foo.class;
		Class d = Fo.class;

		System.out.println("Class ancestors:");
		while (null != c) {
			System.out.println(c);
			c = c.getSuperclass();
		}
		
		int mo = d.getModifiers();
		String retval = Modifier.toString(mo);
		System.out.println("\nClass Modifiers: " + retval);

		c = Foo.class;
		Class inter_arr[] = c.getInterfaces();
		System.out.println("\nClass implemented intefaces:");
		for (Class i : inter_arr) {
			System.out.println(i);
		}

		Field fields[] = c.getDeclaredFields();
		System.out.println("\nClass Members:");
		System.out.println("Fields:");
		for (Field f : fields) {
			System.out.println(f);
		}

		Method methods[] = c.getDeclaredMethods();
		System.out.println("\nClass methods:");
		for (Method m : methods) {
			System.out.println(m);
		}

		Method methods1[] = c.getMethods();
		System.out.println("\nMethods:");
		for (Method m : methods1) {
			System.out.println(m);
		}
		
		Constructor ctor[] = c.getConstructors();	
		System.out.println("\nConstrctos:");
		for (Constructor ct : ctor) {
			System.out.println(ct);
		}

		Foo f = Foo.class.newInstance();		
		Method m1 = c.getMethod("Fun3", String.class, int.class);
		m1.invoke(c.newInstance(), "Yuval", 28);
	
		Method m2 = c.getDeclaredMethod("Fun4", String.class, int.class);
		m2.setAccessible(true); 
		m2.invoke(f, "Nir", 32);
		
	}
}
