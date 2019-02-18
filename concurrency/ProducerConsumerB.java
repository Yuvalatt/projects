package il.co.ilrd.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class ProducerConsumerB {
	private List<Integer> list = new ArrayList<>();
	private Object syncObject = new Object();
	private static final int ARR_SIZE = 10;
	private Semaphore available = new Semaphore(0);
	Integer num = new Integer(0);
	
	Runnable producer = new Runnable() {
		@Override
		public void run() {
			while (!Thread.interrupted()) {				
				synchronized(syncObject) {
					list.add(num);
					num++;
					available.release();
				}
			}
		}
	};

	Runnable consumer = new Runnable() {
		@Override
		public void run() {
			while (!Thread.interrupted()) {				
				try {
					available.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				synchronized(syncObject) {
					System.out.println("Consumed: " + list.remove(0));
				}
			}
		}
	};
	
	public void PingPong() throws InterruptedException {		
		Thread producers[] =  new Thread[ARR_SIZE];
		Thread consumers[] = new Thread[ARR_SIZE];
				
		for (int i = 0; i < ARR_SIZE; i++) {
			producers[i] = new Thread(producer);
			consumers[i] = new Thread(consumer);
		}

		for (int i = 0; i < ARR_SIZE; i++) {
			producers[i].start();
			consumers[i].start();
		}
		
		Thread.sleep(4000);

		for (int i = 0; i < ARR_SIZE; i++) {
			producers[i].interrupt();
			consumers[i].interrupt();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		new ProducerConsumerB().PingPong();
	}

}
