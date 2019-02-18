package il.co.ilrd.tests;

class A {	
    A() {
        System.out.println("A: constructor");
        f();
    }

    public void f() {
        System.out.println("A: f()");
    }
}

class B extends A {
    static int x = 10;
    B() {
        System.out.println("B: constructor");
        System.out.println("x value: " + x);
    }

    @Override
    public void f() {
        System.out.println("B: f()");
        //this.x++;
        System.out.println("B: x = " + x);
    }
}

public class PolyMethodConst {
    public static void main(String[] args) {
        new B();
    	
    }
}