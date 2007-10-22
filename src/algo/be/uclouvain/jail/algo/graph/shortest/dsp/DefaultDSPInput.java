package be.uclouvain.jail.algo.graph.shortest.dsp;

import be.uclouvain.jail.graph.IDirectedGraph;

/**
 * Provides a default implementation of IDSPInput.
 * 
 * @author blambeau
 */
public class DefaultDSPInput implements IDSPInput {

	/** Input graph. */
	private IDirectedGraph graph;
	
	/** Root vertex. */
	private Object root;
	
	/** Weight informer to use. */
	private IWeightInformer<?> weightInformer;
	
	/** 
	 * Creates input instance for some graph and root vertex. 
	 * 
	 * <p>Weight informer will be AttributeWeightInformer and will use edge attribute 
	 * mapped to weightAttr key.</p>
	 */
	public DefaultDSPInput(IDirectedGraph graph, Object root, String weightAttr) {
		this(graph,root,new AttributeWeightInformer(weightAttr));
	}
	
	/** 
	 * Creates input instance for some graph and root vertex. 
	 * 
	 * <p>Weight informer will be AllOneWeightInformer.</p>
	 */
	public DefaultDSPInput(IDirectedGraph graph, Object root) {
		this(graph,root,new AllOneWeightInformer());
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

	/** Sets the weight informer to use. */
	public void setWeightInformer(IWeightInformer<?> informer) {
		this.weightInformer = informer;
	}

	/** Sets the root vertex. */
	public void setRootVertex(Object root) {
		this.root = root;
	}

}
