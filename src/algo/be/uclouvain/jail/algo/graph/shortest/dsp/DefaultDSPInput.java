package be.uclouvain.jail.algo.graph.shortest.dsp;

import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.utils.JavaUtils;

/**
 * Provides a default implementation of IDSPInput.
 * 
 * @author blambeau
 */
public class DefaultDSPInput implements IDSPInput, IWeightInformer<Number> {

	/** Input graph. */
	private IDirectedGraph graph;
	
	/** Root vertex. */
	private Object root;
	
	/** Weight informer to use. */
	private IWeightInformer<?> weightInformer;
	
	/** Weight attribute to use. */
	private String weightAttr;
	
	/** 
	 * Creates input instance for some graph and root vertex. 
	 * 
	 * <p>Weight informer will be this and will use edge attribute mapped 
	 * to weightAttr key.</p>
	 */
	public DefaultDSPInput(IDirectedGraph graph, Object root, String weightAttr) {
		if (graph == null || root == null) {
			throw new IllegalArgumentException("Graph and root are mandatory.");
		}
		this.graph = graph;
		this.root = root;
		this.weightAttr = weightAttr;
		this.weightInformer = this;
	}
	
	/** 
	 * Creates input instance for some graph and root vertex. 
	 * 
	 * <p>Weight informer will be this and will consider all weight being 1.</p>
	 */
	public DefaultDSPInput(IDirectedGraph graph, Object root) {
		this(graph,root,(String)null);
	}

	/** Creates input instance for some graph, root vertex and weight informer. */
	public DefaultDSPInput(IDirectedGraph graph, Object root, IWeightInformer<?> weightInformer) {
		if (graph == null || root == null || weightInformer == null) {
			throw new IllegalArgumentException("All parameters mandatory.");
		}
		this.graph = graph;
		this.root = root;
		this.weightInformer = weightInformer;
	}

	/** Returns the input graph. */
	public IDirectedGraph getGraph() {
		return graph;
	}

	/** Returns the root vertex. */
	public Object getRootVertex() {
		return root;
	}

	/** Returns the weight informer. */
	public IWeightInformer<?> getWeightInformer() {
		return weightInformer;
	}

	/** Returns null. */
	public Number getInfinityDistance() {
		return null;
	}

	/** Returns 0. */
	public Number getNullDistance() {
		return 0;
	}

	/** Returns d+e. */
	public Number sum(Number d, Number e) {
		// infinity case
		if (d == null || e == null) {
			return null;
		}
		
		// other cases
		return JavaUtils.sum(d,e);
	}

	/** Extracts weight information from the edge when weightAttr is not null, 
	 * return 1 otherwise. */
	public Number weight(IDirectedGraph graph, Object edge) {
		return weightAttr == null ? 1 : (Number) graph.getEdgeInfo(edge).getAttribute(weightAttr);
	}

	/** Compares two weights. */
	public int compare(Number o1, Number o2) {
		if (o1 == null && o2 == null) { return 0; }
		else if (o1 == null) { return 1; }
		else if (o2 == null) { return -1; }
		
		double d1 = o1.doubleValue();
		double d2 = o2.doubleValue();
		return ((d1-d2)<0) ? -1 : (d1==d2) ? 0 : 1;
	}

}
