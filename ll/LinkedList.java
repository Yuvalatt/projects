package il.co.ilrd.ll;

public class LinkedList {

	private Node head;

	private class Node {
		private Object data;
		private Node next;

		private Node(Object data, Node next) {
			this.data = data;
			this.next = next;
		}
	}
	
	public LinkedList() {
		head = new Node(null, null);
	}

	public void pushFront(Object data) {
		head = new Node(data, head);
	}

	public Object popFront() {

		if (isEmpty()) {
			return null;
		}

		Node temp = head;
		head = head.next;
		return (temp.data);
	}

	public boolean isEmpty() {
		return (head.data == null);
	}

	public int getlistSize() {
		int size = 0;
		for (ListIterator it = begin(); it.hasNext(); it.next()) {
			++size;
		}
		return size;
	}

	public ListIterator find(Object data) {

		ListIterator it = begin();
		ListIterator curr = begin();
		
		while (it.hasNext()) {
			if (it.next().equals(data)) {
				return curr;
			}
			curr.next();
		}
		return null;
	}

	public ListIterator begin() {
		return (new ListIterator(head));
	}

	private class ListIterator implements InterfaceIterator {

		private Node node;

		public ListIterator(Node node) {
			this.node = node;
		}

		@Override
		public boolean hasNext() {
			return (node.next != null);
		}

		@Override
		public Object next() {
			Object data = node.data;
			node = node.next;
			return (data);
		}
	}
}