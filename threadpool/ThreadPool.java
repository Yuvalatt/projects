package il.co.ilrd.threadpool;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import il.co.ilrd.waitablequeue.WaitableQueue;

public class ThreadPool implements Executor {
	private WaitableQueue<Task<?>> taskQueue = new WaitableQueue<>();
	private AtomicBoolean isShutdown = new AtomicBoolean(false);
	private AtomicBoolean isRunning = new AtomicBoolean(false);
	private Semaphore pauseSem = new Semaphore(0);
	private Semaphore shutDownSem = new Semaphore(0);
	private Map<Long, Thread> threadMap;
	
	private static final int PAUSE_PRIORITY = Priority.HIGH.getPriority() + 1;
	private static final int CANCEL_PRIORITY = Priority.HIGH.getPriority() + 2;
	private static final int SHUTDOWN_PRIORITY = Priority.LOW.getPriority() - 1;
	private static final int MIN_PRIORITY = Priority.LOW.getPriority() - 2;
	
	private Callable<Void> cancelCall = () ->{
		threadMap.remove(Thread.currentThread().getId());
		return null;
	}; 	
	
	private Callable<Void> shutdownCall = () ->{
		threadMap.remove(Thread.currentThread().getId());
		shutDownSem.release();
		return null;
	}; 	
	
	private Callable<Void> pauseCall = ()-> { 
		try {
			pauseSem.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	};
	
	public ThreadPool(int nThreads) {
		threadMap = new HashMap<>(nThreads);
		
		for (int i = 0; i < nThreads; i++) {
			Thread t = new Thread(threadRun);
			threadMap.put(t.getId(), t);
		}
	}

	private Runnable threadRun = new Runnable() {
		@Override		
		public void run() {	
			while (threadMap.containsKey(Thread.currentThread().getId())) {
				taskQueue.deQueue().run();	
			}	
		}
	};

	@Override
	public void execute(Runnable r) {
		submit(r, Priority.MED);
	}

	public void start() {
		isRunning.set(true);
		for (Thread t : threadMap.values()) {
			t.start();
		}
	}

	public <T> Future<T> submit(Runnable r, Priority p, T v) {
		return submit(Executors.callable(r, v), p);
	}

	public Future<Void> submit(Runnable r, Priority p) {
		return submit(Executors.callable(r, null), p);
	}

	public <T> Future<T> submit(Callable<T> c) {
		return submit(c, Priority.MED);
	}

	public <T> Future<T> submit(Callable<T> c, Priority p) {
		if (!isShutdown.get()) {
			Task<T> task = new Task<>(c, p.getPriority(), null);
			taskQueue.enQueue(task);
			return task.getFuture();
		}
		return null;
	}

	public void setNumOfThreads(int num) {
		int currSize = threadMap.size();
		
		if (num > currSize) {
			for (int i = 0; i < (num - currSize); i++) {
				Thread t = new Thread(threadRun);
				threadMap.put(t.getId(), t);
				if (isRunning.get()) {
					t.start();
				}	
			}			
		} else {		
			for (int i = 0; i < (currSize - num); i++) {
				Task<Void> task = new Task<Void>(cancelCall, CANCEL_PRIORITY, null);
				taskQueue.enQueue(task);
			}
		}
	}

	public void pause() {
		for (int i = 0; i < threadMap.size(); ++i) {
			taskQueue.enQueue(new Task<Void>(pauseCall, PAUSE_PRIORITY, null));
		}
	}

	public void resume() {
		pauseSem.release(threadMap.size());
	}

	public <T>void shutdown() {
		isShutdown.set(true);	
		for (int i = 0; i < threadMap.size() - 1; ++i) {
			taskQueue.enQueue(new Task<Void>(cancelCall, SHUTDOWN_PRIORITY, null));
		}
		taskQueue.enQueue(new Task<Void>(shutdownCall, MIN_PRIORITY, null));
	}

	public Boolean awaitTermination(long timeOut, TimeUnit unit) throws InterruptedException {
		shutDownSem.tryAcquire(threadMap.size(), timeOut, unit);
		return threadMap.isEmpty();
	}

	private class Task<T> implements Runnable, Comparable<Task<T>> {
		private Callable<T> callable;
		private int priority;
		private T value;
		private Exception exceptionThrown;
		private AtomicBoolean isCancelled = new AtomicBoolean(false);
		private AtomicBoolean isDone = new AtomicBoolean(false);
		private Semaphore taskDone = new Semaphore(0);
		private Future<T> future = new TaskFuture();
		
		public Task(Callable<T> c, int p, T val) {
			callable = c;
			priority = p;
			value = val;
		}

		public Future<T> getFuture() {
			return future;
		}

		public int compareTo(Task<T> o) {
			return (o.priority - priority);
		}

		@Override
		public void run() {
			try {
				if (!isCancelled.get()) {
					value = callable.call();
				}
			} catch (Exception e) {
				exceptionThrown = e;
			} finally {
				isDone.set(true);
				taskDone.release();
			}
		}

		private class TaskFuture implements Future<T> {
			@Override
			public boolean cancel(boolean mayInterruptIfRunning) {
				if (isDone() || isCancelled()) {
					return false;
				}
				isCancelled.set(true);
				isDone.set(true);
				return true;
			}

			@Override
			public T get() throws InterruptedException, ExecutionException {
				taskDone.acquire();
				if (null != exceptionThrown) {
					throw new ExecutionException(exceptionThrown.getCause());
				}
				return value;
			}

			@Override
			public T get(long timeout, TimeUnit unit)
					throws InterruptedException, ExecutionException, TimeoutException {
				if (!taskDone.tryAcquire(timeout, unit)) {
					throw new TimeoutException();
				}
				if (null != exceptionThrown) {
					throw new ExecutionException(exceptionThrown.getCause());
				}
				return value;
			}

			public boolean isCancelled() {
				return isCancelled.get();
			}

			public boolean isDone() {
				return isDone.get();
			}
		}
	}
}