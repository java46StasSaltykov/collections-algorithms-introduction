package telran.util;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.naming.OperationNotSupportedException;

public class TreeSet<T> implements SortedSet<T> {
	
	private static class Node<T> {
		T obj;
		Node<T> parent;
		Node<T> left;
		Node<T> right;

		Node(T obj) {
			this.obj = obj;
		}
	}

	private Node<T> root;
	int size;
	Comparator<T> comp;
	
	private Node<T> getLeastNodeFrom(Node<T> node) {
		while(node.left != null) {
			node = node.left;
		}
		return node;
	}
	private class TreeSetIterator implements Iterator<T> {
		
		Node<T> current = root == null ? null : getLeastNodeFrom(root);
		
		@Override
		public boolean hasNext() {
			
			return current != null;
		}

		@Override
		public T next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			T res = current.obj;
			updateCurrent();
			return res;
		}
		private void updateCurrent() {
			current = current.right != null ? getLeastNodeFrom(current.right) : getGreaterParent(current);
		}

		private Node<T> getGreaterParent(Node<T> node) {
			
			while(node.parent != null && node.parent.left != node) {
				node = node.parent;
			}
			return node.parent;
		}

		@Override
		public void remove() {
			if (!hasNext()) {
				throw new IllegalStateException();
			}
			Node<T> prevNode = getLeastNodeFrom(current) != null ? getLeastNodeFrom(current) : current.parent;
			if (!isJunction(prevNode)) {
				TreeSet.this.remove(prevNode);
			} else {
				TreeSet.this.remove(current);
			}
			
			size--;
		}
		
	}
	public TreeSet(Comparator<T> comp) {
		this.comp = comp;
	}
	@SuppressWarnings("unchecked")
	public TreeSet() {
		this((Comparator<T>)Comparator.naturalOrder());
	}
	@Override
	public boolean add(T obj) {
		Node<T> parent = getNodeOrParent(obj);
		boolean res = false;
		int compRes = 0;
		if (parent == null || (compRes = comp.compare(obj, parent.obj)) != 0) {
			//obj doesn't exist
			Node<T> newNode = new Node<>(obj);
			if (parent == null) {
				//added first element that is the root
				root = newNode;
			} else if(compRes > 0) {
				parent.right = newNode;
			} else {
				parent.left = newNode;
			}
			res = true;
			newNode.parent = parent;
			size++;
		}
		return res;
	}

	private Node<T> getNodeOrParent(T obj) {
		Node<T> current = root;
		Node<T> parent = null;
		int compRes = 0;
		while (current != null) {
			parent = current;
			compRes = comp.compare(obj, current.obj);
			if (compRes == 0) {
				break;
			}
			current = compRes > 0 ? current.right : current.left;
		}
		return parent;
	}
	
	@Override
	public boolean remove(Object pattern) {
		Node<T> node = getNodeOrParent((T) pattern);
		boolean res = false;
		boolean junction = isJunction(node);
		if (junction == true) {
			removeJunctionNode(node);
			res = true;
		} else {
			removeNonJunctionNode(node);
			res = true;
		}
		return res;
	}

	private void removeNode(Node<T> node) {
		if (node.parent.left == node) {
			node.parent.left = null;
		} else if (node.parent.right == node) {
			node.parent.right = null;
		}
	}
	private void removeNonJunctionNode(Node<T> node) {
		if (node.left == null && node.right != null) {
			node.parent.right = node.right;
		} else if (node.right == null && node.left != null) {
			node.parent.right = node.left;
		} else if (node.right == null && node.left == null) {
			removeNode(node);
		}
		size--;
	}
	private void removeJunctionNode(Node<T> node) {
		if (node == root && node.left != null) {
			node = node.left;
		} else if (node == root && node.right != null) {
			node = node.right;
		} else {
			node = null;
		}
		
		if (node.right != null && node.left != null) {
			node.parent.right = node.right;
			node.parent.left = node.left;
		} else if (node.right != null && node.left == null) {
			node.parent.right = node.right;
		} else if (node.right == null && node.left != null) {
			node.parent.left = node.left;
		}
		
		size--;
	}
	private boolean isJunction(Node<T> node) {
		boolean res = false;
		if (node.left != null && node.right != null) {
			res = true;
		}
		return res;
	}

	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean contains(Object pattern) {
		boolean res = false;
		Iterator it = new TreeSetIterator();
		while (it.hasNext()) {
			if (it.next() == pattern) {
				res = true;
			}
		}
		return res;
	}

	@Override
	public int size() {
		
		return size;
	}

	@Override
	public Iterator<T> iterator() {
		
		return new TreeSetIterator();
	}

	@Override
	public T first() {
		if (root == null) {
			return null;
		}
		return getLeastNodeFrom(root).obj;
	}

	@Override
	public T last() {
		if (root == null) {
			return null;
		}
		return getMostNodeFrom(root).obj;
	}
	
	private Node<T> getMostNodeFrom(Node<T> node) {
		while(node.right != null) {
			node = node.right;
		}
		return node;
	}

}
