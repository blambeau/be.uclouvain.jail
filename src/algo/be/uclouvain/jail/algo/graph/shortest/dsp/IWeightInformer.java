package be.uclouvain.jail.algo.graph.shortest.dsp;

import java.util.Comparator;

import be.uclouvain.jail.graph.IDirectedGraph;

/**
 * <p>Provides informations about edge weight of a IDirectedGraph and encapsules
 * weight/distances computations.</p>
 * 
 * <p>For distance computations, this class has to provide :
 * <ul>
 *   <li>Null and infinity distances.</li>
 *   <li>A sum function on distances.</li>
 *   <li>Comparison functions on distances through the Comparator contract.</li>
 * </ul> 
 * </p>
 * 
 * @author LAMBEAU Bernard
 */
public interface IWeightInformer<T> extends Comparator<T> {

	/**
	 * Returns the weight of an edge in a directed graph.
	 * 
	 * @param graph the directed graph.
	 * @param edge an edge of dg.
	 * @return the weight of the edge.
	 */
	public T weight(IDirectedGraph graph, Object edge);

	/** Returns the null distance, the 0 number for example. */
	public T getNullDistance();

	/** Returns the infinity distance, the Integer.MAX_VALUE for example. */
	public T getInfinityDistance();

	/**
	 * Returns the sum of two distances.
	 * 
	 * @param d a distance.
	 * @param e another one.
	 * @return the sum of the two distances.
	 */
	public T sum(T d, T e);

}
