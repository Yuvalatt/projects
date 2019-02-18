package il.co.ilrd.factory;
import java.util.function.Function;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShapeFactoryTest {
	
	private Factory<String, Integer, Shape> factory;
	
	@BeforeEach
	void initFactory() {
		factory = new Factory<>();
	}
	
	@Test
	void testLambda() {		
		Function<Integer, Shape> createCircle = (x) -> {
			return new Circle(x);
		};	
		factory.add("Circle", createCircle);
		Shape s = factory.create("Circle", 1);
		s.draw();
	}

	@Test
	void testAnonymousClass() {		
		Function<Integer, Shape> anonymousShape = new Function<Integer, Shape>() {
			@Override
			public Shape apply(Integer t) {
				return new Circle(t);
			}
		};
		
		factory.add("Circle", anonymousShape::apply);
		Shape s = factory.create("Circle", 2);
		s.draw();
	}
	
	@Test
	void testStaticMethod() {
		factory.add("Circle", Circle::getCircle);
		Shape s = factory.create("Circle", 3);
		s.draw();
	}
	
	@Test
	void testInstanceMethod() {		
		Square sq = new Square();
		factory.add("Square", sq::getInstance);
		Shape s = factory.create("Square", 4);
		s.draw();
	}
	
	@Test
	void testTypeMethod() {	
		factory.add("Square", Square::new);
		Shape s = factory.create("Square", 5);
		s.draw();
	}
}