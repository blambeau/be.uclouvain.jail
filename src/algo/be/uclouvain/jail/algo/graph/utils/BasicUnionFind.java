package be.uclouvain.jail.algo.graph.utils;

import java.util.HashSet;
import java.util.Set;

import net.chefbe.javautils.collections.arrays.ArrayUtils;

/** 
 * Provides a basic union find.
 * 
 * @author blambeau
 */
public class BasicUnionFind {

	/** Members. */
	private int[] members;
	
	/** Transaction array. */
	private int[] transaction;
	
	/** Creates a basic union find instance. */
	public BasicUnionFind(int size) {
		this.members = new int[size];
		for (int i=0; i<size; i++) {
			members[i] = i;
		}
	}
	
	/** Returns size of the ufds. */
	public int size() {
		return members.length;
	}
	
	/** Finds i-th representor. */
	public int find(int i) {
		if (members[i] == i) { return i; }
		return find(members[i]);
	}
	
	/** Makes a union. */
	public void union(int i, int j) {
		if (i==j) { return; }
		i = find(i);
		j = find(j);
		if (i>j) {
			int k=i;
			i = j;
			j = k;
		}
		members[j] = i;
	}
	
	/** Creates the set of integer in same block as i. */
	public Set<Integer> set(int i) {
		Set<Integer> set = new HashSet<Integer>();
		int size = size();
		for (int j=0; j<size; j++) {
			if (find(j)==i) {
				set.add(j);
			}
		}
		return set;
	}
	
	/** Starts a transaction. */
	public void startTransaction() {
		assert (transaction == null) : "No nested transaction";
		transaction = new int[members.length];
		System.arraycopy(members, 0, transaction, 0, size());
	}

	/** Commits the transaction. */
	public void commit() {
		assert (transaction != null) : "Transaction previously started.";
		transaction = null;
	}

	/** Rollbacks the transaction. */
	public void rollback() {
		assert (transaction != null) : "Transaction previously started.";
		System.arraycopy(transaction, 0, members, 0, size());
		transaction = null;
	}
	
	/** Returns a string rep. */
	public String toString() {
		return ArrayUtils.toString(members,",");
	}
	
}
