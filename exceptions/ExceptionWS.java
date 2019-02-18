package il.co.ilrd.exceptions;
import java.io.IOException;
import java.util.logging.Handler;

@SuppressWarnings("serial")
class MyException1 extends RuntimeException{

	public MyException1(String string) {
		// TODO Auto-generated constructor stub
		System.out.println(string);
	}
	
}

@SuppressWarnings("serial")
class MyException2 extends IOException{

	 void handle() {
		// TODO Auto-generated method stub
		 System.out.println("HANDLER!");
	}
}

class Foo{
	
	public static void func1() throws IOException{


	}
	
	public static void func2() {
		throw new NullPointerException();
	}
	
	public static void func3(){
		throw new MyException1("BLAH");
	}
	
	public static void func4() throws MyException2{
		throw new MyException2();
	}
	
}

public class ExceptionWS {

	public static void main(String[] args) {
		
		/*try {
			Foo.func1();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		/*try {
			Foo.func2();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		Foo.func3();
		try {
			Foo.func4();
		} catch (MyException2 e) {
			// TODO Auto-generated catch block
			e.handle();
			e.printStackTrace();
		}
		
		/*byte[]arr = new byte[100];
		System.out.println(arr[101]);
		*/
	
	}
}
