package il.co.ilrd.concurrency;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrencyEx2 {

	static int counter = 0;
	Object lock = new Object();
	AtomicInteger atomicNum = new AtomicInteger(0);
	private final ReentrantLock rLock = new ReentrantLock();
	
	//option 1 - synchronized method
		Runnable r1 = new Runnable() {
			@Override
			public synchronized void run() {
				for (int i = 0; i < 10000000; i++) {
						counter++;
				}
			}
		};
		
	//option 2 - synchronized block
	Runnable r = new Runnable() {
		@Override
		public void run() {
			for (int i = 0; i < 10000000; i++) {
				synchronized (lock) {
					counter++;
				}
			}
		}
	};
	
	//option 3 - Atomics
		Runnable r3 = new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 10000000; i++) {
					atomicNum.incrementAndGet();
				}
			}
		};
		
		//option 3 - Reentrant lock
				Runnable r4 = new Runnable() {				
					@Override
					public void run() {
						for (int i = 0; i < 10000000; i++) {
							rLock.lock();
							counter++;
							rLock.unlock();
						}
					}
				};

	Thread t1 = new Thread(r4);
	Thread t2 = new Thread(r4);

	public void TestThreads() throws InterruptedException {
		long startTime = System.currentTimeMillis();
		t1.start();
		t2.start();
		t1.join();
		t2.join();
		long endTime = System.currentTimeMillis();
		System.out.println("Total time: " + (endTime - startTime));
		System.out.println("Value of counter is:" + counter);
		System.out.println("Value of atomicNum is:" + atomicNum.get());
	}

	public static void main(String[] args) throws InterruptedException {
		new ConcurrencyEx2().TestThreads();
	}
}
