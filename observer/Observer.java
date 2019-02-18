package il.co.ilrd.observer;

import java.util.function.Consumer;

public class Observer<T> {
	private CallBack<T> cb;

	public Observer(Runnable update, Consumer<T> stopService) {
		cb = new CallBack<T>(update, stopService);
	}

	public CallBack<T> getCallBack() {
		return cb;
	}

	public void register(NewsPaper<T> paper) {
		paper.getDispatcher().register(cb);
	}

	public void unregister(NewsPaper<T> paper) {
		paper.getDispatcher().unregister(cb);
	}
}