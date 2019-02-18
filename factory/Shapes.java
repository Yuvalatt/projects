package il.co.ilrd.factory;

interface Shape {
	void draw();
}

class Rectangle implements Shape {
	private int id;

	public Rectangle(Integer num) {
		id = num.intValue();
	}

	@Override
	public void draw() {
		System.out.println("Rectangle." + id);
	}

	public static Rectangle getRectangle(int num) {
		return new Rectangle(num);
	}
}

class Square implements Shape {
	private int id;

	public Square() {
	}

	public Square(Integer num) {
		id = num.intValue();
	}

	@Override
	public void draw() {
		System.out.println("Square." + id);
	}

	public static Square getSquare(int num) {
		return new Square(num);
	}

	Square getInstance(Integer i) {
		return new Square(i);
	}
}

class Circle implements Shape {
	private int id;

	Circle(Integer num) {
		id = num.intValue();
	}

	@Override
	public void draw() {
		System.out.println("Circle." + id);
	}

	public static Circle getCircle(int num) {
		return new Circle(num);
	}
}