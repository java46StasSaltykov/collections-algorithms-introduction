package telran.util;

import java.util.Currency;
import java.util.Iterator;
import java.util.function.Predicate;

public class LinkedList<T> implements List<T> {

	private static class Node<T> {
		T obj;
		Node<T> next;
		Node<T> prev;

		Node(T obj) {
			this.obj = obj;
		}
	}

	private Node<T> head;
	private Node<T> tail;
	private int size;

	private class LinkedListIterator implements Iterator<T> {

		Node<T> current = head;

		@Override
		public boolean hasNext() {

			return current != null;
		}

		@Override
		public T next() {
			if (hasNext()) {
				T obj = current.obj;
				current = current.next;
				return obj;
			}
			return null;
		}

	}

	@Override
	public boolean add(T obj) {
		Node newNode = new Node<>(obj);
		if (head == null) {
			head = tail = newNode;
		} else {
			newNode.prev = tail;
			tail.next = newNode;
			tail = newNode;
		}
		size++;
		return true;
	}

	@Override
	public boolean remove(Object pattern) {
		if (head.obj == pattern) {
			head = head.next;
			size--;
			return true;
		} else {
			Node<T> current = head;
			while (current.next != null) {
				if (current.next.obj.equals(pattern)) {
					current.next = current.next.next;
					size--;
					return true;
				} else {
					current = current.next;
				}
			}
		}
		return false;
	}

	@Override
	public boolean removeIf(Predicate<T> predicate) {
		int sizeOld = size;
		Node<T> current = head;
		while (current != null) {
			if (predicate.test(current.obj)) {
				current = current.next;
				size--;
			} else {
				current = current.next;
			}
		}
		return sizeOld > size;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public Iterator<T> iterator() {

		return new LinkedListIterator();
	}

	@Override
	public boolean add(int index, T obj) {
		boolean res = false;
		if (index >= 0 && index <= size) {
			res = true;
			if (index == size) {
				add(obj);
			} else if (index == 0) {
				addHead(obj);
			} else {
				addIndex(index, obj);
			}
		}
		return res;
	}

	private void addIndex(int index, T obj) {
		size++;
		Node<T> newNode = new Node<>(obj);
		Node<T> afterNode = getNodeIndex(index);
		Node<T> beforeNode = afterNode.prev;
		newNode.next = afterNode;
		afterNode.prev = newNode;
		beforeNode.next = newNode;
		newNode.prev = beforeNode;
	}

	private Node<T> getNodeIndex(int index) {

		return index > size / 2 ? getNodeRightToLeft(index) : getNodeLeftToRight(index);
	}

	private Node<T> getNodeLeftToRight(int index) {
		Node<T> current = head;
		for (int i = 0; i < index; i++) {
			current = current.next;
		}
		return current;
	}

	private Node<T> getNodeRightToLeft(int index) {
		Node<T> current = tail;
		for (int i = size - 1; i > index; i--) {
			current = current.prev;
		}
		return current;
	}

	private void addHead(T obj) {
		size++;
		Node<T> newNode = new Node<>(obj);
		newNode.next = head;
		head.prev = newNode;
		head = newNode;
	}

	private boolean checkExistingIndex(int index) {
		return index >= 0 && index < size;
	}

	@Override
	public T remove(int index) {
		if (index < 0 || index >= size) {
			return null;
		}
		Node<T> current = head;
		for (int i = 0; i < index; i++) {
			current = current.next;
		}
		if (index == 0) {
			head = current.next;
		} else {
			current.prev.next = current.next;
		}
		size--;
		return current.obj;
	}

	@Override
	public int indexOf(Object pattern) {
		int index = 0;
		Node<T> current = head;
		while (current != null) {
			if (current.obj.equals(pattern)) {
				return index;
			}
			index++;
			current = current.next;
		}
		return -1;
	}

	@Override
	public int lastIndexOf(Object pattern) {
		Node<T> current = head;
		int foundIndex = -1;
		int index = 0;
		while (current != null) {
			if (current.obj.equals(pattern)) {
				foundIndex = index;
			}
			index++;
			current = current.next;
		}
		return foundIndex;
	}

	@Override
	public T get(int index) {
		T res = null;
		if (checkExistingIndex(index)) {
			Node<T> node = getNodeIndex(index);
			res = node.obj;
		}
		return res;
	}

}
