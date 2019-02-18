package il.co.ilrd.concurrency;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumerC {
	private static final int CONSUMERS = 10;
	private Semaphore available = new Semaphore(CONSUMERS);
	
	int producerMessage = 0;
	final Lock lock = new ReentrantLock();
	final Condition ready  = lock.newCondition(); 
	
	Runnable produce = new Runnable() {
		@Override
		public void run() {
			while (!Thread.interrupted()) {
					 try {
						 available.acquire(CONSUMERS);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				lock.lock();
						producerMessage++;
						System.out.println("Produced: " + producerMessage);
						ready.signalAll();
				lock.unlock();
			}
		}
	};

	Runnable consume = new Runnable() {
		@Override
		public void run() {
			while (!Thread.interrupted()) {		
				lock.lock();
					available.release();
					try {
						ready.await();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("Consumed: " + producerMessage);
				lock.unlock();
			}
		}
	};
	
	public void PingPong() throws InterruptedException {
		Thread producer = new Thread(produce);
		Thread consumers[] = new Thread[CONSUMERS];
				
		for (int i = 0; i < CONSUMERS; i++) {
			consumers[i] = new Thread(consume);
		}
		
		for (int i = 0; i < CONSUMERS; i++) {
			consumers[i].start();
		}
		producer.start();
		
		Thread.sleep(4000);

		for (int i = 0; i < CONSUMERS; i++) {
			consumers[i].interrupt();
		}
		producer.interrupt();
	}
	
	public static void main(String[] args) throws InterruptedException {
		new ProducerConsumerC().PingPong();
	}
}
