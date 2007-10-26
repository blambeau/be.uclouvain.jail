package be.uclouvain.jail.algo.fa.minimize;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import be.uclouvain.jail.graph.utils.ITotalOrder;

/**
 * Provides a block structure implementation. 
 * 
 * @author blambeau
 */
public class SetBlockStructure<T> implements IBlockStructure<T> {

	/** List of blocks. */
	private List<Set<T>> blocks = new ArrayList<Set<T>>();
	
	/** Creates an empty structure instance. */
	public SetBlockStructure() {
	}
	
	/** Creates a block structure with some existing blocks. */
	public SetBlockStructure(Set<T>...blocks) {
		for (Set<T> block: blocks) {
			if (!block.isEmpty()) {
				this.blocks.add(block);
			}
		}
	}

	/** Returns the i-th block. */
	public Set<T> getBlock(int i) {
		return blocks.get(i);
	}

	/** Updates the structure to create a new block. */
	public int refine(Set<T> block) {
		blocks.add(block);
		return size()-1;
	}
	
	/** Returns the number of blocks. */
	public int size() {
		return blocks.size();
	}

	/** Returns an iterator on blocks. */
	public Iterator<Set<T>> iterator() {
		return blocks.iterator();
	}
	
	/** Returns the block number of an element. */
	public int getBlockOf(Object element) {
		int i=0;
		for (Set<T> block: blocks) {
			if (block.contains(element)) {
				return i;
			} else {
				i++;
			}
		}
		throw new IllegalStateException("Unknown element " + element);
	}

	/** Returns a string representation. */
	public String toString(ITotalOrder<T> order) {
		StringBuffer sb = new StringBuffer();
		int i=0;
		for (Set<T> block: blocks) {
			if (i++ != 0) { 
				sb.append(",");
			}
			sb.append("{");
			int j=0;
			for (T state: block) {
				if (j++ != 0) {
					sb.append(",");
				}
				sb.append(order.indexOf(state));
			}
			sb.append("}");
		}
		return sb.toString();
	}

}
