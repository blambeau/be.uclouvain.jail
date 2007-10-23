package be.uclouvain.jail.graph.utils;

import java.util.Iterator;
import java.util.List;

/** Total informer for lists. */
public class ListTotalOrder<T> implements ITotalOrder<T> {

	/** Decorated list. */
	private List<T> list;
	
	/** Creates an informer. */
	public ListTotalOrder(List<T> list) {
		this.list = list;
	}

	/** Returns collection size. */
	public int size() {
		return list.size();
	}

	/* (non-Javadoc)
	 * @see be.uclouvain.jail.utils.ITotalOrderInformer#getElementAt(int)
	 */
	public T getElementAt(int index) {
		return list.get(index);
	}

	/* (non-Javadoc)
	 * @see be.uclouvain.jail.utils.ITotalOrderInformer#getElementIndex(java.lang.Object)
	 */
	public int getElementIndex(T element) {
		return list.indexOf(element);
	}

	/* (non-Javadoc)
	 * @see be.uclouvain.jail.utils.ITotalOrderInformer#getTotalOrder()
	 */
	public Object[] getTotalOrder() {
		return list.toArray();
	}

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(T o1, T o2) {
		int iIndex = getElementIndex(o1);
		int jIndex = getElementIndex(o2);
		return iIndex == jIndex ? 0 : iIndex < jIndex ? -1 : 1;
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<T> iterator() {
		return list.iterator();
	}

}
