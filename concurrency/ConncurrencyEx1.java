package il.co.ilrd.concurrency;

public class ConncurrencyEx1 {	
	
	class myThread extends Thread{
		@Override
		public void run(){
			System.out.println("Thread run!");
		}
	}
	
	Thread t3 = new myThread();
	
	public static void main(String[] args) throws InterruptedException {
		
		Runnable run = new Runnable() {
			@Override
			public void run() {
				System.out.println("Thread1 runnable");
			}
		};		
		Thread t1 = new Thread(run);

		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("Thread2 runnable");				
			}
		});
		
		Thread t3 = new ConncurrencyEx1().new myThread();		
		t1.start();	
		t2.start();			
		t3.start();	
		Thread.sleep(1000);
		t1.interrupt();
		t2.interrupt();
		t3.interrupt();		
	}
}
