package be.uclouvain.jail.algo.graph.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Provides a simple UnionFind data-structure.
 * 
 * @author blambeau
 */
public class UnionFind<T> {

	/** Node in the union find. */
	class UnionFindNode {
		
		/** Member attached to the node. */
		private T member;

		/** Identifier. */
		private int id;
		
		/** List head, next and last nodes. */
		private UnionFindNode head, next, last;
		private UnionFindNode headB, nextB, lastB;
		
		/** Creates a node instance. */
		public UnionFindNode(T member, int id) {
			this.id = id;
			this.member = member;
			this.last = this;
		}
		
		/** Returns the member. */
		public T member() {
			return member;
		}
		
		/** Returns the id. */
		public int id() {
			return id;
		}
		
		/** Finds head node. */
		public UnionFindNode head() {
			return head == null ? this : head.head();
		}
		
		/** Returns next node. */
		public UnionFindNode next() {
			return next;
		}
		
		/** Returns last node in the list. */
		public UnionFindNode last() {
			assert (head == null) : "Getting last on head only.";
			return last;
		}
		
		/** Sets the list head. */
		public void setHead(UnionFindNode head) {
			this.head = head;
		}
		
		/** Sets next node in the list. */
		public void setNext(UnionFindNode next) {
			this.next = next;
		}
		
		/** Sets next node in the list. */
		public void setLast(UnionFindNode last) {
			assert (head == null) : "Setting last on head only.";
			this.last = last;
		}
		
		/** Ensures saving of the list elements. */
		public boolean touch(List<UnionFindNode> list) {
			if (lastB == null) {
				headB = head;
				nextB = next;
				lastB = last;
				list.add(this);
				return true;
			}
			return false;
		}
		
		/** Restore list state. */
		public void rollback() {
			assert (lastB != null) : "Previously touched.";
			head = headB;
			next = nextB;
			last = lastB;
		}
		
		/** Commits list state. */
		public void commit() {
			assert (lastB != null) : "Previously touched.";
			headB = null;
			nextB = null;
			lastB = null;
		}
		
	}
	
	/** UnionFind blocks. */
	private List<UnionFindNode> blocks;

	/** Number of blocks. */
	private int size;
	
	/** Creates a UnionFind instance. */
	public UnionFind(Iterable<T> members) {
		// create structure
		blocks = new ArrayList<UnionFindNode>();

		// install members
		int i=0;
		for (T member: members) {
			blocks.add(new UnionFindNode(member,i));
			i++;
		}
		this.size = i;
	}

	/** Returns number of elements in the UnionFind. */
	public int size() {
		return size;
	}
	
	/** Finds head node. */
	protected UnionFindNode findHead(int i) {
		assert (i<size) : "Correct index.";
		return blocks.get(i).head();
	}
	
	/** Finds representor of a block. */
	public T find(int i) {
		return findHead(i).member;
	}
	
	/** Makes a union. */
	public void union(int i, int j) {
		if (i==j) return;

		// find heads
		UnionFindNode iHead = findHead(i);
		UnionFindNode jHead = findHead(j);
		if (iHead == jHead) { return; }
		if (iHead.id() > jHead.id()) {
			UnionFindNode kHead = iHead;
			iHead = jHead;
			jHead = kHead;
		}
		
		
		UnionFindNode iHeadLast = iHead.last();
		UnionFindNode jHeadLast = jHead.last();
		
		// check transaction support
		if (transaction != null) {
			iHeadLast.touch(transaction);
			iHead.touch(transaction);
			jHead.touch(transaction);
		}
		
		iHeadLast.setNext(jHead);
		iHead.setLast(jHeadLast);
		jHead.setHead(iHead);
	}
	
	/** Iterates the block of i-th element. */
	public Iterator<T> iterator(final int i) {
		return new Iterator<T>() {
			
			/** Next node to return. */
			private UnionFindNode next = findHead(i);

			/** Checks if a next node exists. */
			public boolean hasNext() {
				return next != null;
			}

			/** Returns next member. */
			public T next() {
				if (next == null) { throw new NoSuchElementException(); }
				T member = next.member();
				next = next.next();
				return member;
			}

			/** Throws UnsupportedOperationException. */
			public void remove() {
				throw new UnsupportedOperationException();
			}
			
		};
	}
	
	/** Fills a collection with elements of the i-th member list. */
	public void fill(int i, Collection<T> coll) {
		Iterator<T> it = iterator(i);
		while (it.hasNext()) {
			coll.add(it.next());
		}
	}
	
	/** Builds the list of the i-th element list. */
	public List<T> list(int i) {
		List<T> list = new ArrayList<T>();
		fill(i,list);
		return list;
	}

	/** Builds the set of the i-th element list. */
	public Set<T> set(int i) {
		Set<T> list = new HashSet<T>();
		fill(i,list);
		return list;
	}

	// ------------------------------------------------------- Transaction support
	/** Current transaction. */
	private List<UnionFindNode> transaction;
	
	/** Save state. */
	public void startTransaction() {
		assert (transaction == null) : "No nested transaction.";
		transaction = new ArrayList<UnionFindNode>();
	}
	
	/** Commits the transaction. */
	public void commit() {
		for (UnionFindNode node: transaction) {
			node.commit();
		}
		transaction = null;
	}
	
	/** Rollbacks the transaction. */
	public void rollback() {
		for (UnionFindNode node: transaction) {
			node.rollback();
		}
		transaction = null;
	}
	
	/** Returns a string representation. */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for (int i=0; i<size; i++) {
			if (i != 0) { sb.append(","); }
			sb.append(blocks.indexOf(findHead(i)));
		}
		sb.append("]");
		return sb.toString();
	}

}
