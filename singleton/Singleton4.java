package il.co.ilrd.singleton;

class Singleton4 {

	// Thread safe & lazy - most efficient 
	
	//SingletonHolder class is not loaded into memory
	//and only when someone calls the getInstance method,
	//this class gets loaded and creates the Singleton class instance.
	
	private Singleton4() {
		
	}
	
	private static class SingletonHolder{
		public static Singleton4 instance = new Singleton4();
	}
	
	public static Singleton4 getInstance() {
		return SingletonHolder.instance;
	}
}