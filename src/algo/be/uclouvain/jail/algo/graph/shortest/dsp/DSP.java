package be.uclouvain.jail.algo.graph.shortest.dsp;

import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.IDirectedGraphPath;
import be.uclouvain.jail.graph.IDirectedGraphWriter;
import be.uclouvain.jail.graph.utils.GraphQueryUtils;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.functions.IAggregateFunction;

/**
 * Provides a user-friendly API for common usage of the 
 * DijkstraShortestPath algorithm.
 * 
 * @author blambeau
 */
@SuppressWarnings("unchecked")
public class DSP {

	/** Algorithm output. */
	private DefaultDSPInput input;
	
	/** Algorithm output. */
	private DefaultDSPOutput<?> output;
	
	/** DSP executes? */
	private boolean computed = false;
	
	/** Factor a DSP instance. */
	public DSP(IDirectedGraph graph, Object root) {
		input = new DefaultDSPInput(graph,root);
		output = new DefaultDSPOutput();
	}

	/** Returns used root. */
	public Object getRootVertex() {
		return input.getRootVertex();
	}
	
	/** Sets the root vertex to use. */
	public void setRootVertex(Object root) {
		this.input.setRootVertex(root);
		computed = false;
	}
	
	/** Forces AllOneWeightInformer to be used. */
	public void allOneWeights() {
		input.setWeightInformer(new AllOneWeightInformer());
		computed = false;
	}
	
	/** Sets weight to be found in an edge attribute. */
	public void attributeWeight(String attr) {
		input.setWeightInformer(new AttributeWeightInformer(attr));
		computed = false;
	}
	
	/** Computes DSP on the graph. */
	public void computeDSP() {
		new DSPAlgo().execute(input, output);
		computed = true;
	}

	/** Returns the distance to a vertex. */
	public <T> T getDistance(Object vertex) {
		if (!computed) { computeDSP(); }
		return (T) output.getDistance(vertex);
	}

	/** Returns the incoming edge to use to reach a vertex. */ 
	public Object getIncomingEdge(Object vertex) {
		return output.getIncommingEdge(vertex);
	}
	
	/** Returns the shortest path to a vertex. */
	public IDirectedGraphPath shortestPathTo(Object vertex) {
		if (!computed) { computeDSP(); }
		return new DSPResult(input,output).getShortestPathTo(vertex);
	}

	/** Converts DSP result to a spanning tree. */
	public void getSpanningTree(IDirectedGraphWriter writer) {
		if (!computed) { computeDSP(); }
		new DSPResult(input,output).asSpanningTree(writer);
	}
	
	/** 
	 * Flushes DSP result as attributes of the input graph.
	 * 
	 * @param distAttr attribute to use to keep distance, null to ignore it.
	 * @param edgeAttr attribute to use to keep incoming edge, null to ignore it.
	 */ 
	public void markInVertices(String distAttr, String edgeAttr) {
		if (!computed) { computeDSP(); }
		IDirectedGraph graph = input.getGraph();
		for (Object vertex: graph.getVertices()) {
			IUserInfo info = graph.getVertexInfo(vertex);
			if (distAttr != null) {
				info.setAttribute(distAttr, getDistance(vertex));
			}
			if (edgeAttr != null) {
				info.setAttribute(edgeAttr, getIncomingEdge(vertex));
			}
		}
	}
	
	/** 
	 * Computes some function on edges of the shortest path reaching some
	 * target vertex. 
	 * 
	 * @param target vertex to reach.
	 * @param attr attribute to apply function on.
	 * @param f function to compute on the path.
	 * @return result of the computation.
	 */
	public <T> T edgePathCompute(Object target, String attr, IAggregateFunction<T> f) {
		if (!computed) { computeDSP(); }
		IDirectedGraphPath path = shortestPathTo(target);
		return GraphQueryUtils.compute(input.getGraph(),path.edges(),attr,f);
	}
	
	/** 
	 * Computes some function on vertices of the shortest path reaching some
	 * target vertex. 
	 * 
	 * @param target vertex to reach.
	 * @param attr attribute to apply function on.
	 * @param f function to compute on the path.
	 * @return result of the computation.
	 */
	public <T> T vertexPathCompute(Object target, String attr, IAggregateFunction<T> f) {
		if (!computed) { computeDSP(); }
		IDirectedGraphPath path = shortestPathTo(target);
		return GraphQueryUtils.compute(input.getGraph(),path.vertices(),attr,f);
	}
	
}
