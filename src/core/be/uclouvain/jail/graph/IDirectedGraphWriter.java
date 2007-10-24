package be.uclouvain.jail.graph;

import be.uclouvain.jail.uinfo.IUserInfo;

/** 
 * Provides an open but simple interface for graph writing.
 * 
 * <p>This interface abstracts the creation of a graph to its simplest form. Many results 
 * of graph algorithms as well as other abstractions (like graph paths) can be represented 
 * as graphs (or graph creations). This interface is then introduced to allow user 
 * manipulations when flushing such results to a graph.</p>
 * 
 * <p>Let us take a simple example: consider a situation where you want to copy a graph
 * but, doing so, you also want to keep only some vertex attributes. The way JAIL works
 * for such example is quite simple: use the {@link DirectedGraphCopier} with a specific
 * writer which will keep the attributes you want:</p>
 * <pre>
 *     // I've got a graph, and I want to copy it, but keeping only some attributes
 *     // a, b and c on vertices
 *     IDirectedGraph graph = [...]
 *     
 *     // create a default graph writer 
 *     IDirectedGraphWriter writer = new DirectedGraphWriter();
 *     
 *     // mark only attributes a, b and c to keep on vertices 
 *     writer.getVertexCopier().keep("a","b","c");
 *     
 *     // keep all attributes on edges
 *     writer.getEdgeCopier().keepAll();
 *     
 *     // copy graph
 *     IDirectedGraph copy = DirectedGraphCopier.copy(graph,writer);
 * </pre>
 * 
 * <p>This graph creation scheme is used by many JAIL algorithms and tools, some examples
 * being:</p>
 * <ul>
 *     <li>Graph copy through {@link DirectedGraphCopier}.</li>
 *     <li>Adaptation of {@link IDirectedGraphPath} to {@link IDirectedGraph}.</li>
 *     <li>Spanning tree of a directed graph using {@link DSPAlgo} result.</li>  
 * </ul>
 * 
 * <p>This interface may be implemented for special purpose. Common usage is however to use 
 * some real graph implementation as writer.</p>  
 */
public interface IDirectedGraphWriter {
	
	/** Creates a vertex. */
	public Object createVertex(IUserInfo info);
	
	/** Creates an edge. */
	public Object createEdge(Object source, Object target, IUserInfo info);
	
}