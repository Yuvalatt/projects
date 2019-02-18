package il.co.ilrd.observer;

import java.util.function.Consumer;

public class NewsPaper<T> {
	private Dispatcher<T> dispatcher = new Dispatcher<>();

	public Dispatcher<T> getDispatcher() {
		return dispatcher;
	}

	public void setDispatcher(Dispatcher<T> d) {
		dispatcher = d;
	}

	public static void main(String[] args) {
		NewsPaper<String> maariv = new NewsPaper<>();

		Runnable update1 = new Runnable() {
			@Override
			public void run() {
				System.out.println("I'd like to get maariv by mail.");
			}
		};

		Runnable update2 = new Runnable() {
			@Override
			public void run() {
				System.out.println("I'd like to get maariv to my back yard.");
			}
		};

		Runnable update3 = new Runnable() {
			@Override
			public void run() {
				System.out.println("I'd like to get maariv in english!");
			}
		};

		Consumer<String> stopService = (message) -> {
			System.out.println(message);
		};

		Observer<String> a = new Observer<>(update1, stopService);
		Observer<String> b = new Observer<>(update2, stopService);
		Observer<String> c = new Observer<>(update3, stopService);
		System.out.println("======= Register 3 observers =======");
		a.register(maariv);
		b.register(maariv);
		c.register(maariv);
		System.out.println("========= Notify observers =========");
		maariv.getDispatcher().notifyAllCallBacks();
		System.out.println("========== Stop service ==========");
		maariv.getDispatcher().stopService("Service Stopped");
		maariv.getDispatcher().notifyAllCallBacks();
		System.out.println("======= Register 2 observers =======");
		b.register(maariv);
		c.register(maariv);
		System.out.println("========= Notify observers =========");
		maariv.getDispatcher().notifyAllCallBacks();
		System.out.println("======== Unregister 1 observer =======");
		b.unregister(maariv);
		System.out.println("========= Notify observers =========");
		maariv.getDispatcher().notifyAllCallBacks();
	}
}
