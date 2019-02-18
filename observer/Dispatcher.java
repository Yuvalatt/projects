package il.co.ilrd.observer;

import java.util.ArrayList;
import java.util.Collection;

public class Dispatcher<T> {
	private Collection<CallBack<T>> list = new ArrayList<CallBack<T>>();

	public void register(CallBack<T> cb) {
		if (!list.contains(cb)) {
			cb.setDispatcher(this);
			list.add(cb);
		}
	}

	public void unregister(CallBack<T> cb) {
		cb.setDispatcher(null);
		list.remove(cb);
	}

	public void notifyAllCallBacks() {
		if (null != list) {
			for (CallBack<T> cb : list) {
				cb.update();
			}
		}
	}

	public void stopService(T data) {
		for (CallBack<T> cb : list) {
			cb.setDispatcher(null);
			cb.stopService(data);
		}
		list.clear();
	}
}