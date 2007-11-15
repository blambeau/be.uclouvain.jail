package be.uclouvain.jail.algo.graph.utils;

import java.util.ArrayList;
import java.util.List;

import net.chefbe.javautils.comparisons.HashCodeUtils;

/**
 * Partitions some domain according to sub-partitionners.
 * 
 * @author blambeau
 */
public final class MultiPartitionner<T> implements IGraphPartitionner<T> {

	/** Key of a partition. */
	static final class PartitionKey {
		
		/** Partitionner keys. */
		private Object[] keys;

		/** Hash code. */
		private int hash = -1;
		
		/** Creates a partition key. */
		public PartitionKey(Object[] keys) {
			this.keys = keys;
		}

		/** Computes an hash code. */
		@Override
		public int hashCode() {
			if (hash == -1) {
				hash = HashCodeUtils.SEED;
				for (Object key: keys) {
					hash = HashCodeUtils.hash(hash,key);
				}
			}
			return hash;
		}
		
		/** Checks null safe equality. */
		private boolean nullOrEqual(Object o, Object p) {
			if (o==null && p==null) { return true; }
			if (o==null || p==null) { return false; }
			assert (o != null && p != null);
			return (o.equals(p));
		}
		
		/** Checks equality. */
		@Override
		public boolean equals(Object o) {
			if (o instanceof PartitionKey == false) {
				return false;
			}
			PartitionKey other = (PartitionKey) o;
			if (keys.length != other.keys.length) {
				return false;
			}
			for (int i=0; i<keys.length; i++) {
				if (!nullOrEqual(keys[i],other.keys[i])) {
					return false;
				}
			}
			return true;
		}
		
	}
	
	/** Partitionners to use. */
	private List<IGraphPartitionner<T>> partitionners;

	/** Creates a partitionner instance. */
	public MultiPartitionner(IGraphPartitionner<T>...ps) {
		partitionners = new ArrayList<IGraphPartitionner<T>>();
		if (ps != null) {
			for (IGraphPartitionner<T> p: ps) {
				if (p != null) {
					partitionners.add(p);
				}
			}
		}
	}

	/** Returns number of partitionners. */
	private int size() {
		return partitionners.size();
	}
	
	/** Adds a partitionner. */
	public void addPartitionner(IGraphPartitionner<T> p) {
		partitionners.add(p);
	}
	
	/** Computes the partition of a value. */
	public Object getPartitionOf(T value) {
		Object[] key = new Object[size()];
		int i=0;
		for (IGraphPartitionner<T> p: partitionners) {
			key[i++] = p.getPartitionOf(value);
		}
		return new PartitionKey(key);
	}

}
