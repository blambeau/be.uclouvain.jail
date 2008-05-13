package be.uclouvain.jail.algo.graph.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import net.chefbe.javautils.collections.iterable.IterableIterator;
import be.uclouvain.jail.uinfo.functions.IAggregateFunction;

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
		
		/** Sets the member attached to the node. */
		public void setMember(T member) {
			this.member = member;
		}
		
		/** Sets the list head. */
		public void setHead(UnionFindNode head) {
			assert (head != this) : "Never put me as head.";
			this.head = head;
		}
		
		/** Sets next node in the list. */
		public void setNext(UnionFindNode next) {
			assert (next != this) : "Never put me as next.";
			this.next = next;
		}
		
		/** Sets next node in the list. */
		public void setLast(UnionFindNode last) {
			assert (last != this) : "Never put me as last.";
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
			head = headB; headB = null;
			next = nextB; nextB = null;
			last = lastB; lastB = null;
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
	
	/** Creates an empty union size with null members. */
	public UnionFind(int size) {
		// create structure
		blocks = new ArrayList<UnionFindNode>(size);
		
		// install members
		for (int i=0; i<size; i++) {
			blocks.add(new UnionFindNode(null,i));
		}
		this.size = size;
	}

	/** Returns number of elements in the UnionFind. */
	public int size() {
		return size;
	}
	
	/** Checks if two members are in the same block. */
	public boolean inSameBlock(int i, int j) {
		return findHead(i) == findHead(j);
	}
	
	/** Checks if an element is master in its block. */
	public boolean isMaster(int i) {
		assert (i<size) : "Correct index.";
		return findHead(i).id() == i;
	}
	
	/** Finds head node. */
	protected UnionFindNode findHead(int i) {
		assert (i<size) : "Correct index.";
		return blocks.get(i).head();
	}
	
	/** Returns the index of the representor of i. */
	public int findi(int i) {
		return findHead(i).id;
	}
	
	/** Finds representors of a set of i. */
	public Set<Integer> findi(Set<Integer> s) {
		Set<Integer> t = new HashSet<Integer>();
		for (Integer i: s) {
			t.add(findi(i));
		}
		return t;
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
		
		assert (iHead != jHead) : "Not same heads.";
		iHeadLast.setNext(jHead);
		iHead.setLast(jHeadLast);
		jHead.setHead(iHead);
	}
	
	/** Sets member of i. */
	public void setMember(int i, T member) {
		UnionFindNode node = findHead(i);
		node.setMember(member);
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
	
	/** Merge composants of i-th block with an aggregate function. */
	public T merge(int i, IAggregateFunction<T> f) {
		T res = f.compute(new IterableIterator<T>(iterator(i)));
		return res;
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
		assert (transaction != null) : "Transaction previously started.";
		for (UnionFindNode node: transaction) {
			node.commit();
		}
		transaction = null;
	}
	
	/** Rollbacks the transaction. */
	public void rollback() {
		assert (transaction != null) : "Transaction previously started.";
		for (UnionFindNode node: transaction) {
			node.rollback();
		}
		transaction = null;
	}
	
	/** Provides full debug information. */
	public String debug() {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for (int i=0; i<size; i++) {
			if (i != 0) { sb.append(","); }
			UnionFindNode n = blocks.get(i);
			sb.append("(")
			  .append(n.head == null ? i : n.head.id())
			  .append(n.next == null ? "," : "," + n.next.id())
			  .append(n.last == null ? "," : "," + n.last.id())
			  .append(")");
		}
		sb.append("]");
		return sb.toString();
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
