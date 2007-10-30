package be.uclouvain.jail.adapt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import be.uclouvain.jail.adapt.ChainAdapter.AwareAdapter;
import be.uclouvain.jail.algo.graph.shortest.dsp.DSP;
import be.uclouvain.jail.dialect.dot.DOTDirectedGraphPrintable;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.adjacency.AdjacencyDirectedGraph;
import be.uclouvain.jail.graph.deco.DirectedGraph;
import be.uclouvain.jail.graph.deco.GraphUniqueIndex;
import be.uclouvain.jail.graph.utils.DirectedGraphWriter;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.MapUserInfo;
import be.uclouvain.jail.uinfo.functions.NonCommutativeFunction;

/** 
 * Provides a magic adapter that is able to convert many objects by 
 * chaining other adapters.
 * 
 * @author blambeau
 */
public class NetworkAdaptationTool implements IAdaptationTool {

	/** Adaptation network. */
	private DirectedGraph graph;
	
	/** Index by class. */
	private GraphUniqueIndex byClassIndex;

	/** Identity adapter. */
	private IAdapter identity = new IAdapter() {

		/** Returns who. */
		@SuppressWarnings("unchecked")
		public Object adapt(Object who, Class type) {
			if (type.isAssignableFrom(who.getClass())) {
				return who;
			} else {
				throw new IllegalStateException("Bad identity adapter usage.");
			}
		}
		
	};
	
	/** Creates a network adapter. */
	public NetworkAdaptationTool() {
		graph = new DirectedGraph(new AdjacencyDirectedGraph());
		byClassIndex = new GraphUniqueIndex(GraphUniqueIndex.VERTEX,"class",true).installOn(graph);
	}

	/** Returns the adaptation graph. */
	public IDirectedGraph getGraph() {
		return graph;
	}
	
	/** Creates a vertex info. */
	private IUserInfo vertexInfo(Class domain) {
		MapUserInfo info = new MapUserInfo();
		info.setAttribute("class",domain);
		info.setAttribute("label", domain.getSimpleName());
		return info;
	}

	/** Creates an edge info. */
	private IUserInfo edgeInfo(IAdapter adapter, Class target) {
		if (adapter instanceof AwareAdapter == false) {
			adapter = new AwareAdapter(target,adapter);
		}
		MapUserInfo info = new MapUserInfo();
		info.setAttribute("adapter",adapter);
		info.setAttribute("label",adapter.toString());
		return info;
	}

	/** Ensures that a domain has been fully created inside the network. */
	private Object ensureVertex(Class domain) {
		Object vertex = byClassIndex.getVertex(domain);

		// not already created? let recurse on super types
		if (vertex == null) {
			vertex = graph.createVertex(vertexInfo(domain));

			// recurse on implemented interfaces
			for (Class sup: domain.getInterfaces()) {
				Object supVertex = ensureVertex(sup);
				graph.createEdge(vertex, supVertex, edgeInfo(identity,sup));
			}
			
			// recurse on super class 
			Class sup = domain.getSuperclass();
			if (sup != null) {
				Object supVertex = ensureVertex(sup);
				graph.createEdge(vertex, supVertex, edgeInfo(identity,sup));
			}
		}
		
		return vertex;
	}
	
	/** Finds an adapter from source to target. */
	private IAdapter findAdapter(Class source, Class target) {
		// find vertices
		Object sourceVertex = ensureVertex(source);
		Object targetVertex = ensureVertex(target);
		
		// compute shortest path
		DSP dsp = new DSP(graph,sourceVertex);
		dsp.allOneWeights();
		IAdapter adapter = dsp.edgePathCompute(targetVertex, "adapter", new NonCommutativeFunction<IAdapter>() {

			/** Converts to a chain adapter. */
			public IAdapter compute(Iterable<IAdapter> operands) {
				List<AwareAdapter> chain = new ArrayList<AwareAdapter>();
				for (IAdapter adapter: operands) {
					chain.add((AwareAdapter)adapter);
				}
				if (chain.size()==0) {
					return null;
				} if (chain.size()==1) {
					return chain.get(0);
				} else {
					return new ChainAdapter(chain);
				}
			}

		});
		
		// return created adapter
		return adapter;
	}
	
	/** Adapts who to a specified type. */
	public <T> Object adapt(Object who, Class<T> type) {
		if (who == null) {
			throw new IllegalArgumentException("Adapted object may not be null.");
		}
		if (type == null) {
			throw new IllegalArgumentException("Requested type may not be null.");
		}
		
		// check already ok
		if (type.isAssignableFrom(who.getClass())) {
			return who;
		}
		
		// check by component himself
		if (who instanceof IAdaptable) {
			return ((IAdaptable)who).adapt(type);
		} else {
			// find adapter and delegates
			IAdapter adapter = findAdapter(who.getClass(),type); 
			return adapter == null ? null : adapter.adapt(who, type);
		}
	}

	/** Externally adapts who. */
	public <T> Object externalAdapt(IAdaptable who, Class<T> type) {
		if (who == null) {
			throw new IllegalArgumentException("Adapted object may be null.");
		}
		if (type == null) {
			throw new IllegalArgumentException("Requested type may be null.");
		}

		// check already ok
		if (type.isAssignableFrom(who.getClass())) {
			return who;
		}
		
		// find adapter and delegates
		IAdapter adapter = findAdapter(who.getClass(),type); 
		return adapter == null ? null : adapter.adapt(who, type);
	}
	
	/** Registers an external adapter. */
	public void register(Class source, Class target, IAdapter adapter) {
		// find vertices
		Object sourceVertex = ensureVertex(source);
		Object targetVertex = ensureVertex(target);
		
		// create edge
		graph.createEdge(sourceVertex, targetVertex, edgeInfo(adapter,target));
	}

	/** Returns a DSP tree for some adaptation. */
	public IDirectedGraph getAdaptationsOf(Class domain) {
		Object rootVertex = ensureVertex(domain);
		DSP dsp = new DSP(this.graph,rootVertex);
		
		IDirectedGraph result = new AdjacencyDirectedGraph();
		DirectedGraphWriter writer = new DirectedGraphWriter(result);
		writer.getVertexCopier().keepAll();
		writer.getEdgeCopier().keepAll();
		
		// compute spanning tree
		dsp.getSpanningTree(writer);
		return result;
	}

	/** Returns a string expression. */
	public void debugGraph() {
		try {
			new DOTDirectedGraphPrintable(graph).print(System.out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
