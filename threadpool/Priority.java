package il.co.ilrd.threadpool;

public enum Priority {
	LOW(1), MED(5), HIGH(10);

	private int priority;

	Priority(int level) {
		this.priority = level;
	}

	public int getPriority() {
		return priority;
	}
}