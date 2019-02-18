package il.co.ilrd.waitablequeue;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;
import javax.naming.TimeLimitExceededException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WaitableQueueTest {
	WaitableQueue<Integer> queue;
	WaitableQueue<String> queue1;
	WaitableQueue<String> queue2;
	
	Comparator<Integer> cmpInt = new Comparator<Integer>() {
		@Override
		public int compare(Integer o1, Integer o2) {
			return o1.compareTo(o2);
		}
	};

	Comparator<String> cmpString = new Comparator<String>() {
		public int compare(String o1, String o2) {
			return o1.compareTo(o2);
		}
	};
	
	@BeforeEach
	void initQueue() {		
		queue = new WaitableQueue<>(50, cmpInt);
		queue1 = new WaitableQueue<>(50, cmpString);
		queue2 = new WaitableQueue<>();
	}

	@Test
	void testWaitableQueue() {		
		assertNotNull(queue);
		assertNotNull(queue1);
		assertNotNull(queue2);
	}

	@Test
	void testEnQueue() throws InterruptedException {
		assertEquals(true, queue.enQueue(1));
		assertEquals(true, queue.enQueue(2));
		assertEquals(true, queue.enQueue(3));
		assertEquals(true, queue.enQueue(4));
		assertEquals(true, queue.enQueue(5));
	}

	@Test
	void testEnQueueThreads() {
		Runnable runA = new Runnable() {
			public void run() {
				String str = new String("Hello");
				if (queue1.enQueue(str)) {
					System.out.println("Enqueued item: " + str);
				}
			}
		};
		
		Runnable runB = new Runnable() {
			public void run() {
				String str = new String("World");
				boolean check = false;
				check = queue1.enQueue(str);
				assertTrue(true == check);
				if (check) {
					System.out.println("Enqueued item: " + str);
				}
			}
		};
		
		Thread threadA = new Thread(runA);
		Thread threadB = new Thread(runB);		
		threadA.start();
		threadB.start();
	}
	
	@Test
	void testDeQueue() throws InterruptedException {
		assertEquals(true, queue.enQueue(1));
		assertTrue(1 == queue.deQueue());
	}
	
	@Test
	void testdeQueueThreads() throws InterruptedException {
		Runnable runA = new Runnable() {
			public void run() {
				for (int i = 0; i < 10; i++) {
				Integer n = new Integer(i);
				if (queue.enQueue(n)) {
					System.out.println("Enqueued item: " + n);
				}
				}
			}
		};
		
		Runnable runB = new Runnable() {
			public void run() {
				while (true) {
				Integer check = null;
				check = queue.deQueue();
				System.out.println("dequeued item: " + check.intValue());
				}
			}
		};
		
		Thread threadA = new Thread(runA);
		Thread threadB = new Thread(runB);
		
		threadA.start();
		threadB.start();
	}

	@Test
	void testRemove() throws InterruptedException {
		assertEquals(true, queue.enQueue(1));
		assertEquals(true, queue.enQueue(2));
		assertEquals(true, queue.enQueue(3));
		assertEquals(true, queue.remove(2));
		assertEquals(true, queue.remove(3));
		assertEquals(true, queue.remove(1));
	}
	
	@Test
	void testRemoveThreads() throws InterruptedException {
		Runnable runA = new Runnable() {
			public void run() {
				Integer n = new Integer(8);
				if (queue.enQueue(n)) {
					System.out.println("Enqueued item: " + n);
				}
			}
		};
		
		Runnable runB = new Runnable() {
			public void run() {
				boolean check = false;
				check = queue.remove(8);
				assertTrue(true == check);
				System.out.println("removed item");
			}
		};
		
		Thread threadA = new Thread(runA);	
		Thread threadB = new Thread(runB);
		
		threadA.start();
		Thread.sleep(200);
		threadB.start();
	}

	@Test
	public void testAddandTimeDeQueue() throws InterruptedException {		
		Integer num = new Integer(5);
		Thread thread1;
		Thread thread2;
			
		thread1 = new Thread(new Runnable() {
			@Override
			public void run() {
				if (queue.enQueue(num)) {
					System.out.println("succed");
				}
				else {
					System.out.println("fail");
				}
			}
		});
		
		thread2 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					try {
						assertTrue(5 == queue.timeDeQueue(8000, TimeUnit.MILLISECONDS));
					} catch (TimeLimitExceededException e) {
						e.printStackTrace();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		
		thread2.start();
		Thread.sleep(5000);
		thread1.start();
	}
}