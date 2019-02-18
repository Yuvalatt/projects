package il.co.ilrd.concurrency;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class ProducerConsumer {	
	private Semaphore producerSem = new Semaphore(0);
	private Semaphore consumerSem = new Semaphore(1);	
	private AtomicBoolean produce = new AtomicBoolean(false);
	
	//Semaphores
	Runnable producer = new Runnable() {		
		@Override
		public void run() {	
			while (!Thread.interrupted()) {
				try {
					consumerSem.acquire();
					System.out.println("Ping!");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}				
				producerSem.release();
			}
		}
	};
	
	Runnable consumer = new Runnable() {		
		@Override
		public void run() {
			while (!Thread.interrupted()) {
				try {
					producerSem.acquire();
					System.out.println("		  Pong!");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}			
				consumerSem.release();
			}					
		}
	};
	
	//Atomic variables
	Runnable producer1 = new Runnable() {		
		@Override
		public void run() {	
			while (!Thread.interrupted()) {
				if (true == produce.get()) {
					System.out.println("Ping!");
					produce.set(false);
				}						
			}
		}
	};
	
	Runnable consumer1 = new Runnable() {		
		@Override
		public void run() {
			while (!Thread.interrupted()) {
				if (false == produce.get()) {
					System.out.println("		Pong!");		
					produce.set(true);
				}
			}					
		}
	};
	
	public void PingPong() throws InterruptedException {	
		Thread t1 = new Thread(producer);
		Thread t2 = new Thread(consumer);		
		Thread t3 = new Thread(producer1);
		Thread t4 = new Thread(consumer1);
		
		t1.start();
		t2.start();	
		Thread.sleep(4000);
		
		t1.interrupt();
		t2.interrupt();	
		System.out.println("************************");	
		t3.start();
		t4.start();	
		Thread.sleep(4000);
		
		t3.interrupt();
		t4.interrupt();	
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		new ProducerConsumer().PingPong();
	}
}
