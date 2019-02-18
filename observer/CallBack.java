package il.co.ilrd.observer;

import java.util.function.Consumer;

public class CallBack<T> {
	private Dispatcher<T> dispatcher;
	private Runnable update;
	private Consumer<T> stopService;

	public CallBack(Runnable update, Consumer<T> stopService) {
		this.update = update;
		this.stopService = stopService;
	}

	public void update() {
		update.run();
	}

	public void stopService(T data) {
		stopService.accept(data);
	}

	public Dispatcher<T> getDispatcher() {
		return dispatcher;
	}

	public void setDispatcher(Dispatcher<T> dispatcher) {
		this.dispatcher = dispatcher;
	}
}