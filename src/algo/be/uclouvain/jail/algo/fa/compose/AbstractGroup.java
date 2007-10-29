package be.uclouvain.jail.algo.fa.compose;

import java.util.Iterator;
import java.util.Set;

import net.chefbe.javautils.collections.set.ListOrderedSet;
import net.chefbe.javautils.comparisons.HashCodeUtils;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.utils.ITotalOrder;
import be.uclouvain.jail.uinfo.IUserInfo;

/** State definition. */
public abstract class AbstractGroup implements Iterable<Object> {
	
	/** Indexes. */
	protected int[] components;
	
	/** Hash code. */
	private int hash = -1;
	
	/** Creates an empty group. */
	public AbstractGroup() {
		this(new int[0]);
	}
	
	/** Creates a state definition. */
	public AbstractGroup(int[] components) {
		this.components = components;
	}
	
	/** Sets the components. */
	public final void setComponents(int[] components) {
		this.components = components;
		this.hash = -1;
	}
	
	/** Sets the components. */
	public final void setComponents(Object[] components) {
		int size = components.length;
		this.components = new int[size];
		for (int i=0; i<size; i++) {
			this.components[i] = components[i] == null ? -1 : 
				getTotalOrder(i).indexOf(components[i]);
		}
		this.hash = -1;
	}
	
	/** Returns size of the group. */
	public final int size() {
		return components.length;
	}
	
	/** Returns the i-th component. */
	public final Object getComponent(int i) {
		return components[i] == -1 ? null : getTotalOrder(i).getElementAt(components[i]);
	}
	
	/** Returns the i-th value. */
	public final int getComponentIndex(int i) {
		return components[i];
	}
	
	/** Returns the i-th user info. */
	public final IUserInfo getUserInfo(int i) {
		Object component = getComponent(i);
		return component == null ? null : getGraph(i).getUserInfoOf(component);
	}
	
	/** Returns the equivalent user infos. */
	public final Set<IUserInfo> getUserInfos() {
		Set<IUserInfo> infos = new ListOrderedSet<IUserInfo>();
		int size = size();
		for (int i=0; i<size; i++) {
			IUserInfo info = getUserInfo(i);
			if (info != null) {
				infos.add(info);
			}
		}
		return infos;
	}

	/** Returns an iterator on components. */
	public final Iterator<Object> iterator() {
		return new Iterator<Object>() {

			/** Next element to return. */
			private int next = 0;
			
			/** Checks if there is another component to return. */
			public boolean hasNext() {
				return next < size();
			}

			/** Returns the next component to return. */
			public Object next() {
				return getComponent(next++);
			}

			/** Throws UnsupportedOperationException. */
			public void remove() {
				throw new UnsupportedOperationException("Cannot remove from group iterator.");
			}
			
		};
	}
	
	/** Returns the i-th vertices total order. */
	public final ITotalOrder<Object> getVerticesTotalOrder(int i) {
		return getGraph(i).getVerticesTotalOrder();
	}
	
	/** Returns the i-th edges total order. */
	public final ITotalOrder<Object> getEdgesTotalOrder(int i) {
		return getGraph(i).getEdgesTotalOrder();
	}
	
	/** Returns the i-th total order. */
	public abstract ITotalOrder<Object> getTotalOrder(int i);
	
	/** Returns the i-th graph. */
	public abstract IDirectedGraph getGraph(int i);
	
	/** Compares with another definition. */
	public final boolean equals(Object o) {
		if (o instanceof AbstractGroup == false) {
			throw new AssertionError("o is an AbstractGroup.");
		}
		AbstractGroup other = (AbstractGroup) o;
		if (other.size() != size()) {
			throw new AssertionError("AbstractGroup sizes are the same.");
		}
		int size = size();
		for (int i=0; i<size; i++) {
			if (components[i] != other.components[i]) {
				return false;
			}
		}
		return true;
	}
	
	/** Computes an hash code. */
	public final int hashCode() {
		// look in cache
		if (hash != -1) { return hash; }
		
		// compute it
		hash = HashCodeUtils.SEED;
		int size = size();
		for (int i=0; i<size; i++) {
			hash = HashCodeUtils.hash(hash, components[i]);
		}
		return hash;
	}
	
	/** Returns a string representation. */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append('[');
		int size = size();
		for (int i=0; i<size; i++) {
			if (i!=0) { sb.append(','); }
			sb.append(components[i]);
		}
		sb.append(']');
		return sb.toString();
	}
	
}