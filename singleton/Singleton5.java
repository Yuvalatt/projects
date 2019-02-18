package il.co.ilrd.singleton;

//Enum 
enum Color{
	
	RED(1), GREEN(2), YELLOW(3);
	
	private int nCode;
	
	private Color(int code) {
		nCode = code;
	}
	
	public String toString() {
		return String.valueOf(nCode);
	}
}