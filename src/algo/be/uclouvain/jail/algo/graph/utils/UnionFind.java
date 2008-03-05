package be.uclouvain.jail.algo.graph.utils;

import be.uclouvain.jail.graph.utils.ITotalOrder;

/**
 * Provides a simple UnionFind data-structure.
 * 
 * @author blambeau
 */
public class UnionFind implements IGraphPartitionner<Object> {

	/** Members. */
	private ITotalOrder<Object> members;
	
	/** UnionFind blocks. */
	private int[] blocks;

	/** Creates a UnionFind instance. */
	public UnionFind(ITotalOrder<Object> members) {
		this.members = members;
		int size = members.size();
		this.blocks = new int[size];
		for (int i=0; i<size; i++) {
			blocks[i] = -1;
		}
	}

	/** Finds representor of a block. */
	public int find(int i) {
		if (blocks[i] == -1) { return i; }
		else return find(blocks[i]);
	}
	
	/** Makes a union. */
	public void union(int i, int j) {
		if (i == j) { return; }
		if (i > j) { union(j,i); }
		blocks[j] = i;
	}

	/** Returns the partition of a member. */
	public Object getPartitionOf(Object member) {
		return find(members.indexOf(member));
	}
	
}
