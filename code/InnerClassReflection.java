package il.co.ilrd.code;
import java.lang.reflect.*;

class Mother{
	
	class Child{
		
		public Child(){
			System.out.println("I'm a child!");
		}
	}
}

public class InnerClassReflection{

	public static void main(String[] args) throws Exception {
		
		Class<?> enclosingClass = Class.forName("il.co.ilrd.code.Mother");
		Object enclosingInstance = enclosingClass.newInstance();
		
		Class<?> innerClass = Class.forName("il.co.ilrd.code.Mother$Child");
		Constructor<?> ctor = innerClass.getDeclaredConstructor(enclosingClass);
		
		Object innerInstance = ctor.newInstance(enclosingInstance);
	}
		
}
