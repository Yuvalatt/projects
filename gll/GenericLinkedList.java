package il.co.ilrd.gll;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

public class GenericLinkedList<T> implements Iterable<T>{
	
	private Node<T> head = new Node<T>(null, null);
	private int modifiedCount; 

	public GenericLinkedList() {
		
	}
	
	public void pushFront(T data) { 	
		head = new Node<T>(data, head);
		++modifiedCount;
	}

	public boolean isEmpty() {
		return (iterator().hasNext() == false);
	}

	public T popFront() {
		if (isEmpty()) {
			return null;
		}

		T data = head.data;
		head = head.next;
		++modifiedCount;
		
		return data;
	}

	public int getListSize() {
		int size = 0;		
		for (Iterator<T> it = iterator(); it.hasNext(); it.next()) {
			++size;
		}		
		return size;
	}

	public Iterator<T> find(T data) {
		Iterator<T> it = iterator();
		Iterator<T> curr = iterator();

		while (it.hasNext()) {
			if (it.next().equals(data)) {
				return curr;
			}
			curr.next();
		}
		return null;
	}

	public static <T> GenericLinkedList<T> newReverse(GenericLinkedList<T> list){		
		GenericLinkedList<T> reversed = new GenericLinkedList<>();
		
		for (Iterator<T> it = list.iterator(); it.hasNext();) {
			reversed.pushFront(it.next());
		}			
		return reversed;
	}
	
	//Nested Class - prevents access from Class Node to the outer Class
	private static class Node<U> {
		private U data;
		private Node<U> next;

		private Node(U data, Node<U> next) {
			this.data = data;
			this.next = next;
		}
	}
	
	private class GenericLinkedListIterator implements Iterator<T>{
		
		private int iterModifiedCount;
		private Node<T> node;

		private GenericLinkedListIterator(Node<T> head){
			this.node = head;
			iterModifiedCount = modifiedCount;
		}

		@Override
		public boolean hasNext() {
			return (node.next != null);
		}

		@Override
		public T next(){		
			if(iterModifiedCount != modifiedCount) {
				throw new ConcurrentModificationException();
			}
			T data = node.data;
			node = node.next;
			return data;
		}	
	}
	
	@Override
	public Iterator<T> iterator() {
		return new GenericLinkedListIterator(head);
	}
}
