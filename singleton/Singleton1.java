package il.co.ilrd.singleton;

//Lazy & Not thread-safe 

class Singleton1 {

	private static Singleton1 instance;
	
	private Singleton1() {
		
	}
	
	public static Singleton1 getInstance() {
		if (instance == null) {
			instance = new Singleton1();
		}
		return instance;
	}
}