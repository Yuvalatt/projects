package il.co.ilrd.code;

import java.util.ArrayList;
import java.util.List;

class Animal
{
	//private static int count = 0;

	public Animal(){
		//++count;
	}

	public void toSting(){
		//System.out.println(count);
	}
}

public class ReflectionTest {

	public static void noReflectionCheck()
	{
		long start = System.currentTimeMillis();
		for (int i = 0; i < 100000; ++i)
		{
			Animal a = new Animal();
		}
		System.out.println(System.currentTimeMillis() - start);
	}

	public static void reflectionCheck()
	{
		long start = System.currentTimeMillis();

		for (int i = 0; i < 100000; ++i)
		{
			try {
				Animal an = (Animal) Class.forName("il.co.ilrd.code.Animal").newInstance();
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println(System.currentTimeMillis() - start);
	}

	public static void main(String[] args) {		
		noReflectionCheck();
		reflectionCheck();
	}

}
