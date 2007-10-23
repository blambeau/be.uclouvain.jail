/*
 * YAJUtils - Yet Another Java Utilities.  
 * Copyright (C) 2006 - LAMBEAU Bernard
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  
 * 02110-1301, USA.
 */
package be.uclouvain.jail.graph.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Implementation of the CollectionTotalOrderInformer contract by collection
 * copy.
 * </p>
 * 
 * <p>
 * This class copies all elements of the underlying collection into an internal
 * collection structures in order to respect the contract. Two collections are
 * used : an array is get on the source collection using the toArray[] method to
 * implement (index -> element) function, while a second map is used to
 * implement (element -> index).
 * </p>
 * 
 * <p>
 * This class has a hard spacial complexity due to the double copy of the
 * source. The total ordering array should never been writed ! No mechanism is
 * provided to reflect late source collection changes.
 * </p>
 * 
 * @author LAMBEAU Bernard
 */
public class CopyTotalOrder<T> implements ITotalOrder<T> {

	/**
	 * Array used to get fast mapping from index to element;
	 */
	public List<T> collectionArray;

	/**
	 * Map used to get fast mapping from element to index.
	 */
	public Map<T, Integer> collectionMap;

	/**
	 * Constructor.
	 * 
	 * @param c the source collection to decorate with total ordering.
	 */
	@SuppressWarnings("unchecked")
	public CopyTotalOrder(Collection<T> c) {
		collectionArray = new ArrayList<T>(c.size());
		collectionMap = new HashMap<T, Integer>();

		int i=0;
		for (T o : c) {
			collectionArray.add(o);
			collectionMap.put(o, i++);
		}
	}
	
	/** Returns collection size. */
	public int size() {
		return collectionArray.size();
	}

	/**
	 * Returns the total ordering as an array. This array should never been
	 * accessed for writing.
	 */
	public Object[] getTotalOrder() {
		return collectionArray.toArray();
	}

	/**
	 * <p>
	 * Returns the index of an element in the total order, or -1 if the element
	 * is not in the collection. User of this method is ensured that the index
	 * is the smallest possible index for that element (duplicates case).
	 */
	public int getElementIndex(Object element) {
		Integer index = collectionMap.get(element);
		if (index == null) {
			return -1;
		}
		return index.intValue();
	}

	/**
	 * Returns the element at a specific index in the collection.
	 */
	public T getElementAt(int index) {
		return collectionArray.get(index);
	}

	/**
	 * Comparison on the total order. This method reuse getElementIndex(...) to
	 * make it work.
	 */
	public int compare(Object arg0, Object arg1) {
		int iIndex = getElementIndex(arg0);
		int jIndex = getElementIndex(arg1);
		return iIndex == jIndex ? 0 : iIndex < jIndex ? -1 : 1;
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<T> iterator() {
		return collectionArray.iterator();
	}

}
