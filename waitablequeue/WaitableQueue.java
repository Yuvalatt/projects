package il.co.ilrd.waitablequeue;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import javax.naming.TimeLimitExceededException;

public class WaitableQueue<E> {
	private Queue<E> queue;
	private Object queueLock = new Object();
	private final Semaphore available = new Semaphore(0);
	private final static int DEFAULT_SIZE = 11;

	public WaitableQueue() {
		this(DEFAULT_SIZE, null);
	}

	public WaitableQueue(int initialCapacity, Comparator<? super E> comparator) {
		queue = new PriorityQueue<>(initialCapacity, comparator);
	}

	public boolean enQueue(E element) {
		boolean success = false;

		synchronized (queueLock) {
			success = queue.add(element);
			available.release();
		}
		return success;
	}

	//Check option of deadlock
	public boolean remove(E element) {	
		try {
			available.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		synchronized (queueLock) {			
			if (false == queue.remove(element)) {
				available.release();
				return false;
			}
		}
		return true;
	}

	public E deQueue() {
		E data = null;

		try {
			available.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		synchronized (queueLock) {
			data = queue.poll();
		}
		return data;
	}

	public E timeDeQueue(long timeout, TimeUnit units) throws InterruptedException, TimeLimitExceededException {
		E data = null;
		if (available.tryAcquire(timeout, units)) {
			synchronized (queueLock) {
				data = queue.poll();
			}
		} else {
			throw new TimeLimitExceededException();
		}
		return data;
	}
}