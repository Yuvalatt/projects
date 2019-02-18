package il.co.ilrd.threadpool;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.Test;

class ThreadPoolTest {		
	Future<Integer> f1 = null;
	Future<Integer> f2 = null;
	Future<Void> f3 = null;
	Future<Integer> f4 = null;
	
		@Test
		void Submit() throws InterruptedException, ExecutionException {		
			ThreadPool threadPool = new ThreadPool(10);
			
			Runnable lowRun = new Runnable(){
				@Override 
				public void run(){
					System.out.println("LOW");
					}
				};
			
			Runnable medRun = new Runnable(){ 
				@Override 
				public void run(){
					System.out.println("MED");
					}
				};

			Runnable HighRun = new Runnable(){ 
				@Override 
				public void run(){
					System.out.println("HIGH");
					}
				};
			
				Callable<Integer> c1 = new Callable<Integer>(){ 
					@Override 
					public Integer call(){
						System.out.println("HIGH");
						return new Integer(10);
						}
					};
					
			f1 = threadPool.submit(HighRun, Priority.HIGH, null);
			f2 = threadPool.submit(medRun, Priority.MED, null);
			f3 = threadPool.submit(lowRun, Priority.LOW);
			f4 = threadPool.submit(c1, Priority.HIGH);
			
			threadPool.start();
			
			System.out.println("get " + f4.get());
			System.out.println("isdone " + f4.isDone());
			System.out.println("cancel " + f4.isCancelled());
			
			threadPool.setNumOfThreads(5);
		}

		@Test
		void testQueue1() {
			ThreadPool tp = new ThreadPool(4);
			Runnable low = new Runnable() {
				@Override
				public void run() {
					System.out.println("LOW");
				}

			};

			Runnable med = new Runnable() {
				@Override
				public void run() {
					System.out.println("MED");
				}

			};

			Runnable med2 = new Runnable() {
				@Override
				public void run() {
					System.out.println("MED");
				}

			};

			Runnable high = new Runnable() {
				@Override
				public void run() {
					System.out.println("HIGH");
				}

			};
			
			tp.submit(low, Priority.LOW, null);
			tp.submit(med, Priority.MED, null);
			tp.submit(med2, Priority.MED, null);
			tp.submit(high, Priority.HIGH, null);
			tp.start();
		}
		
		@Test
		void testCheckingPriority1() {
			ThreadPool threadPool = new ThreadPool(5);
			
			threadPool.submit(new Runnable() {	
				@Override
				public void run() {
					System.out.println("task1, priority LOW");
				}
			}, Priority.LOW);
			
			threadPool.submit(new Runnable() {		
						@Override
						public void run() {
							System.out.println("task2, priority HIGH");
						}
					}, Priority.HIGH);
					
			threadPool.submit(new Runnable() {
				@Override
				public void run() {
					System.out.println("task3, priority MED");
				}
			}, Priority.MED);
			
			threadPool.submit(new Runnable() {
				@Override
				public void run() {
					System.out.println("task4, priority MED");
				}
			}, Priority.MED);
			
			threadPool.submit(new Runnable() {		
				@Override
				public void run() {
					System.out.println("task5, priority HIGH");
				}
			}, Priority.HIGH);
			threadPool.start();
		}
		
		@Test
		void testFutureGet1() throws InterruptedException, ExecutionException{
		ThreadPool tp = new ThreadPool(5);
		
		Callable<String> task1 = ()-> {
		System.out.println("Task1");
		return "done";
		};
		
		Callable<Integer> task2 = ()-> {
			System.out.println("task2 done, Priority.HIGH");
			return 5;
		};
		
		Runnable task3 = new Runnable() {
			public void run() {
				System.out.println("task3 done, Priority.LOW");
			}
		};
		
		Future<?> f1 = tp.submit(task1, Priority.LOW);
		Future<?> f2 = tp.submit(task1);//med priority
		Future<?> f3 = tp.submit(task2, Priority.HIGH);
		Future<?> f4 = tp.submit(task2, Priority.MED);
		Future<?> f5 = tp.submit(task3, Priority.LOW);
			
		tp.start();
		
		assertTrue(f1.get().equals("done"));
		assertTrue(f3.get().equals(new Integer(5)));
		assertTrue(f4.get().equals(new Integer(5)));
	
		System.out.println("---------------------------------------");
	}
		
		@Test
		void testPause1() throws InterruptedException, ExecutionException  {
			ThreadPool threadPool = new ThreadPool(10);
			
			Runnable task1 = ()-> {
				int j = 0; 
				while(j < 10000000) {
					++j;
					System.out.println(j);
					}
			};
			
			Callable<Integer> task2 = ()-> {
				int i = 0;
				while(i < 10000000) {
					++i;
					System.out.println("hi");
					}
				return i;
			};
		
			Future<Void> f1 = threadPool.submit(task1, Priority.MED);
			Future<Integer> f2 = threadPool.submit(task2, Priority.LOW);
			Future<Integer> f3 = threadPool.submit(task2, Priority.HIGH);
			Future<Void> f4 = threadPool.submit(task1, Priority.LOW);
			
			threadPool.start();
			Thread.sleep(500);
			threadPool.pause();
		}
		
		
		@Test
		void Submit1() throws InterruptedException, ExecutionException {
			int i = 0;
			Callable<Integer> c1 = new Callable() {

				@Override
				public Object call() throws Exception {
					System.out.println(1);
					return 1;
				}

			};
			Callable<Integer> c2 = new Callable() {

				@Override
				public Object call() throws Exception {
					System.out.println(2);
					return 2;
				}

			};

			Runnable low = new Runnable() {
				@Override
				public void run() {
					System.out.println("I am last!");
				}
			};

			Future<Integer> future1 = null;
			Future<Integer> future2 = null;
			Future<Void> future3 = null;
			Future<Integer> future4 = null;

			ThreadPool threadPool = new ThreadPool(7);
			for (i = 0; i < 30; ++i) {
				future1 = threadPool.submit(c1, Priority.HIGH);
				future2 = threadPool.submit(c2);
				threadPool.submit(c2);
			}
			future3 = threadPool.submit(low, Priority.HIGH);

			threadPool.start();
			System.out.println("get " + future1.get());
			System.out.println("isdone " + future1.isDone());
			System.out.println("cancel " + future1.isCancelled());
			threadPool.setNumOfThreads(1);
		}

		@Test
		void testQueue() {

			ThreadPool threadPool = new ThreadPool(4);
			Runnable low = new Runnable() {

				@Override
				public void run() {
					System.out.println("I am last!");
				}

			};

			Runnable med = new Runnable() {

				@Override
				public void run() {
					System.out.println("I am in the middle!");
				}

			};

			Runnable med2 = new Runnable() {

				@Override
				public void run() {
					System.out.println("I am in the middle TOO!");
				}

			};

			Runnable high = new Runnable() {

				@Override
				public void run() {
					System.out.println("I im First!");
				}

			};
			threadPool.submit(low, Priority.LOW, null);
			threadPool.submit(med, Priority.MED, null);
			threadPool.submit(med2, Priority.MED, null);
			threadPool.submit(high, Priority.HIGH, null);
			threadPool.start();
		}

		@Test
		void testCheckingPriority() {
			ThreadPool threadPool = new ThreadPool(3);
			Future<Void> future = threadPool.submit(new Runnable() {	
				@Override
				public void run() {
					System.out.println("task1, priority LOW");
				}
			}, Priority.LOW);
			threadPool.submit(new Runnable() {		
						@Override
						public void run() {
							System.out.println("task2. priority HIGH");
						}
					}, Priority.HIGH);
			threadPool.submit(new Runnable() {
				@Override
				public void run() {
					System.out.println("task3, priority MED");
				}
			}, Priority.MED);
			
			threadPool.start();
			System.out.println("---------------------------------------");
		};
		
		@Test
		void testFutureGet() {
			ThreadPool threadPool = new ThreadPool(5);
			Callable<String> task1 = ()-> {System.out.println("task1 done, med priority"); return "done";};
			Callable<Integer> task2 = ()-> {System.out.println("task2 done, Priority.HIGH"); return 5;};
			Runnable task3 = new Runnable() {
				public void run() {
					System.out.println("task3 done, Priority.LOW");
				}
			};
			
			Future<?> task1f = threadPool.submit(task3, Priority.LOW);
			Future<?> task2f = threadPool.submit(task1);//med priority
			Future<?> task3f = threadPool.submit(task2, Priority.HIGH);
			Future<?> task4f = threadPool.submit(task2, Priority.HIGH);
			Future<?> task5f = threadPool.submit(task3, Priority.LOW);
		
			
			threadPool.start();
			
			try {
				assertTrue(task2f.get().equals("done"));
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			} catch (ExecutionException e) {
				
				e.printStackTrace();
			}
			try {
				assertTrue(task3f.get().equals(new Integer(5)));
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			} catch (ExecutionException e) {
				
				e.printStackTrace();
			}
			try {
				assertTrue(task4f.get().equals(new Integer(5)));
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			} catch (ExecutionException e) {
				
				e.printStackTrace();
			}
			System.out.println("---------------------------------------");
		}
		
		@Test
		void testCancel() throws InterruptedException, ExecutionException, TimeoutException {
			ThreadPool threadPool = new ThreadPool(2);
			Callable<String> task1 = ()-> {while(true) {System.out.println("task running");}};
			Callable<String> task2 = ()-> {System.out.println("done");return "done";};

			Future<?> task1f = threadPool.submit(task1);//med priority
			Future<?> task2f = threadPool.submit(task2, Priority.LOW);
			task1f.cancel(false);
			threadPool.start();

			assertTrue(task1f.isDone()== true);
			assertTrue(task2f.get(1, TimeUnit.SECONDS).equals("done"));

		}
		
		@Test
		void testPause() throws InterruptedException, ExecutionException  {
			ThreadPool threadPool = new ThreadPool(10);
			Callable<Integer> task1 = ()-> {int i = 0; while(i < 10000000) {++i;};return 1;};
			Callable<Integer> task2 = ()-> {int i = 0; while(i < 10000000) {++i;};return 2;};
			Callable<Integer> task3 = ()-> {System.out.println("new task added1");return 3;};
			Callable<Integer> task4 = ()-> {System.out.println("new task added2");return 4;};
			Future<?> task1f = threadPool.submit(task1);//med priority
			Future<?> task2f = threadPool.submit(task2, Priority.LOW);
			Future<?> task3f = threadPool.submit(task2, Priority.HIGH);
			Future<?> task4f = threadPool.submit(task2);
			
			threadPool.start();
			threadPool.pause();
			
			Future<?> task5f = threadPool.submit(task3, Priority.LOW);
			Future<?> task6f  = threadPool.submit(task4, Priority.HIGH);
			
			threadPool.resume();

			System.out.println(task1f.isDone());
			System.out.println(task2f.isDone());
			System.out.println(task3f.isDone());
			System.out.println(task4f.isDone());
			System.out.println(task5f.isDone());
			System.out.println(task6f.isDone());
			System.out.println("should be 3: "+ task5f.get());
			System.out.println("should be 4: "+ task6f.get());
			Thread.sleep(2000);
			assertTrue(task1f.isDone() && task2f.isDone() && task3f.isDone() && task4f.isDone() &&
					task5f.isDone() && task6f.isDone());
		}	
		
		@Test
		void testQueu2e() {

			ThreadPool threadPool = new ThreadPool(4);
			Runnable low = new Runnable() {

				@Override
				public void run() {
					System.out.println("I am last!");
				}

			};

			Runnable med = new Runnable() {

				@Override
				public void run() {
					System.out.println("I am in the middle!");
				}

			};

			Runnable med2 = new Runnable() {

				@Override
				public void run() {
					System.out.println("I am in the middle TOO!");
				}

			};

			Runnable high = new Runnable() {

				@Override
				public void run() {
					System.out.println("I am First!");
				}

			};
			threadPool.submit(low, Priority.LOW, null);
			threadPool.submit(med, Priority.MED, null);
			threadPool.submit(med2, Priority.MED, null);
			threadPool.submit(high, Priority.HIGH, null);
			threadPool.start();
		//	threadPool.setNumOfThreads(0);

		}


		@Test
		void testget() throws InterruptedException, ExecutionException {
			Integer test = new Integer(100);
			ThreadPool threadPool = new ThreadPool(4);
			Callable sleeps500 = new Callable() {

				@Override
				public Object call() throws Exception {
					Thread.sleep(6000);
					return new Integer(100);
				}

				
				
			};		
		

			Future<?> future = threadPool.submit(sleeps500);

			//FIX ME PLEASE 
			threadPool.start();
			assertFalse(future.isDone());
			try {
				assertTrue(null == future.get(250, TimeUnit.MILLISECONDS));
			} catch (TimeoutException e) {
				e.printStackTrace();
			}
			Thread.sleep(500);

			assertTrue(test.equals(future.get()));
		}
		
		@Test 
		void testSetNumOfThreadsEASY() throws InterruptedException {
			ThreadPool threadPool = new ThreadPool(1);
			
			Callable<Integer> task1 = ()-> {int i = 0; while(i < 100000) {++i;};return 1;};
			Callable<Integer> task2 = ()-> {System.out.println("new task added3");return 2;};
			Callable<Integer> task3 = ()-> {System.out.println("new task added1");return 3;};
			Callable<Integer> task4 = ()-> {System.out.println("new task added2");return 4;};
			
			Future<?> task1f = threadPool.submit(task1);
			threadPool.start();
			threadPool.setNumOfThreads(4);
			Future<?> task2f = threadPool.submit(task2);
			Future<?> task3f = threadPool.submit(task3);
			Future<?> task4f = threadPool.submit(task4);
			Thread.sleep(2000);
			assertTrue(task1f.isDone() && task2f.isDone() && task3f.isDone() &&
					   task4f.isDone());
		}
		
		@Test //print testing in the code - white boxing
		void testSetNumOfThreadsCOMPLEX() throws InterruptedException {
			ThreadPool threadPool = new ThreadPool(3);
			
			Callable<Integer> task1 = ()-> {int i = 0; while(i < 100000) {++i;};return 1;};
			
			threadPool.submit(task1);
			threadPool.submit(task1);
			threadPool.submit(task1);
			threadPool.start();
			Thread.sleep(3000);
			threadPool.setNumOfThreads(1);
			//print size of thread list
		}
}