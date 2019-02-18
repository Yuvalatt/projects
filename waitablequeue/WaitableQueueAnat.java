package il.co.ilrd.waitablequeue;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.naming.TimeLimitExceededException;

public class WaitableQueueAnat <E>{
	Queue<E> priorityQueue; 			//CR: maybe better be private
	private final Lock lock = new ReentrantLock();
	private final Semaphore semaphore = new Semaphore(0);
	
	public WaitableQueueAnat(int initialCpacity, Comparator<? super E> cmp){
		priorityQueue = new PriorityQueue<>(initialCpacity, cmp);
	}
	
	public boolean enqueu(E element) {
		lock.lock(); 
		try {
			if (false == priorityQueue.add(element)) {
				return false;
			}
			semaphore.release();
			return true;
		}
		finally {
			lock.unlock();
		}		
	}
	
	public E dequeue() {
		try {
			semaphore.acquire();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		
		lock.lock();
		try {
			return priorityQueue.poll();
		}
		finally {
			lock.unlock();
		}	
	}
	
	public boolean remove(E element) {	
		lock.lock();
		try {
			if (true == priorityQueue.remove(element)) {
				try {
					semaphore.acquire();
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
				return true;
			}
			return false;
		}
		finally {
			lock.unlock();
		}	
	}
	
	public E timedDequeue(long timeOut, TimeUnit unit) throws TimeLimitExceededException {
		try {
			if (true == semaphore.tryAcquire(timeOut, unit)) {
				lock.lock();
				try {
					return  priorityQueue.poll();
				}
				finally {
					lock.unlock();
				}
			}
		} catch (InterruptedException e){
			e.printStackTrace();
		}
		
		throw new TimeLimitExceededException();
	}
}