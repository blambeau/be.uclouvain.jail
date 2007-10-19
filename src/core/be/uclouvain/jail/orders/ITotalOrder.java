package be.uclouvain.jail.orders;

import java.util.Comparator;

/**
 * <p> Provides a mechanism to get a total order on a collection. Such a total order informer provides methods to get
 * the index of an element or the element at a specific index and extends the Comparator contract for (the real)
 * comparison point of view of a total order. </p>
 * 
 * <p> The comparator must be consistent with equals. The informer must be consistent between the indexing
 * functionalities and the comparison utilities. In other words : <ul> <li>getElementIndex(a) > getElementIndex(b) =>
 * compare(a,b)>0</li> <li>getElementIndex(a) < getElementIndex(b) => compare(a,b)<0</li> <li>getElementIndex(a) =
 * getElementIndex(b) => compare(a,b)=0</li> </ul> </p>
 * 
 * @author blambeau
 * 
 */
public interface ITotalOrder<T> extends Comparator<T>, Iterable<T> {

	/** Returns the size of the collection. */ 
	public int size();
	
	/**
	 * <p> Returns the total ordering as an array. Implementations must explicitely specify the effect of a write on the
	 * returned array on the source collection. </p>
	 */
	public Object[] getTotalOrder();

	/**
	 * <p> Returns the index of a specific element. </p>
	 * 
	 * <p> Special care must be taken for the specific points : <ul> <li>If the underlying collection contains
	 * duplicate, this method should return the index of the first element found. Each implementation must explicitely
	 * inform about the strategy it use in case of duplicates. </li> <li>A -1 value should be returned when the element
	 * is not found ! </p> </ul>
	 * 
	 * @param element an element in the collection.
	 * @return the index of the element in the underlying collection.
	 */
	public int getElementIndex(T element);

	/**
	 * <p> Returns the element at a specific index in the underlying collection. </p>
	 * 
	 * @param index a int position.
	 * @return the element at index in the total order.
	 * @throws IndexOutOfBoundsException if 0>index or index>=collection size.
	 */
	public T getElementAt(int index);

}
