package be.uclouvain.jail.dialect.dot;

import java.io.IOException;

import net.chefbe.autogram.ast2.IASTNode;
import net.chefbe.autogram.ast2.parsing.ParseException;
import net.chefbe.autogram.ast2.parsing.active.ASTLoader;
import net.chefbe.autogram.ast2.parsing.active.ASTLoader.EnumTypeResolver;
import net.chefbe.autogram.ast2.parsing.peg.Input;
import net.chefbe.autogram.ast2.parsing.peg.Pos;
import net.chefbe.autogram.ast2.utils.BaseLocation;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.adjacency.AdjacencyDirectedGraph;
import be.uclouvain.jail.graph.constraints.AbstractGraphConstraint;
import be.uclouvain.jail.graph.constraints.GraphUniqueIndex;
import be.uclouvain.jail.graph.deco.DirectedGraph;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.IUserInfoHelper;
import be.uclouvain.jail.uinfo.UserInfoHelper;
import be.uclouvain.jail.vm.JailVMOptions;

/** Loads a DirectedGraph from a .dot file. */
public class DOTDirectedGraphLoader {

	/** Creates a callback instance. */
	private DOTDirectedGraphLoader() {
	}

	/** Loads a graph. */
	public static void loadGraph(IDirectedGraph graph, 
			                     Object source, 
			                     DOTGraphDialect dialect,
			                     JailVMOptions options) throws ParseException, IOException {
		// create parser instance and set loader
		DOTParser parser = new DOTParser();
		parser.setActiveLoader(new ASTLoader(new EnumTypeResolver<DOTNodes>(DOTNodes.class)));

		// create parsing position
		Pos pos = new Pos(Input.input(new BaseLocation(source)), 0);

		// parse graph
		IASTNode node = (IASTNode) parser.pGraphdef(pos);

		// load graph using this callback
		try {
			node.accept(new LoaderCallback(graph, dialect, options));
		} catch (Exception e) {
			throw new RuntimeException("Unexpected exception.", e);
		}
	}

	/** Loads a graph from some source. */
	public static IDirectedGraph loadGraph(Object source, 
			                               DOTGraphDialect dialect, 
			                               JailVMOptions options) throws ParseException, IOException {
		IDirectedGraph graph = new AdjacencyDirectedGraph();
		loadGraph(graph, source, dialect, options);
		return graph;
	}

	/** Loads a graph from some source. */
	public static void loadGraph(IDirectedGraph graph, Object source) 
		throws ParseException, IOException {
		loadGraph(graph,source,null,null);
	}
	
	/** Loads a graph from some source. */
	public static IDirectedGraph loadGraph(Object source) throws ParseException, IOException {
		return loadGraph(source,null,null);
	}
	
	/** Callback class to load graph from AST. */
	static class LoaderCallback extends DOTCallback<Object> {

		/** Loaded graph. */
		private DirectedGraph graph;

		/** Requesting dialect. */ 
		private DOTGraphDialect dialect;
		
		/** Helper to use. */
		private IUserInfoHelper helper;
		
		/** Graph unique index. */
		private GraphUniqueIndex index;

		/** Last info created. */
		private IUserInfo info;

		/** Creates a loader to fill the specified graph. */
		public LoaderCallback(IDirectedGraph graph, 
				              DOTGraphDialect dialect, 
				              JailVMOptions options) {
			this.graph = (DirectedGraph) graph.adapt(DirectedGraph.class);
			this.helper = UserInfoHelper.instance();
			this.dialect = dialect;
		}

		/** Creates a user info instance. */
		private IUserInfo vInfo(String id) {
			info = helper.keyValue("id", id);
			return info;
		}

		/** Creates a user info instance. */
		private IUserInfo eInfo() {
			info = helper.install();
			return info;
		}

		/** Callback method for GRAPHDEF nodes. */
		public Object GRAPHDEF(IASTNode node) throws Exception {
			index = new GraphUniqueIndex(AbstractGraphConstraint.VERTEX, "id",true).installOn(graph);
			super.recurseOnChildren(node);
			index.uninstall();
			return null;
		}

		/** Callback method for GRAPH_COMMONS nodes. */
		public Object GRAPH_COMMONS(IASTNode node) throws Exception {
			return null;
		}

		/** Callback method for NODE_COMMONS nodes. */
		public Object NODE_COMMONS(IASTNode node) throws Exception {
			return null;
		}

		/** Callback method for EDGE_COMMONS nodes. */
		public Object EDGE_COMMONS(IASTNode node) throws Exception {
			return null;
		}

		/** Callback method for NODEDEF nodes. */
		public Object NODEDEF(IASTNode node) throws Exception {
			String id = node.getAttrString("id");
			
			// create basic vertex info and apply attributes
			vInfo(id);
			super.recurseOnChildren(node);
			
			// rewrite it and create vertex
			if (dialect != null) { info = dialect.reloadVertexInfo(info); }
			graph.createVertex(info);
			
			return null;
		}

		/** Callback method for EDGEDEF nodes. */
		public Object EDGEDEF(IASTNode node) throws Exception {
			String src = node.getAttrString("src");
			String trg = node.getAttrString("trg");
			Object srcV = index.getVertex(src);
			Object trgV = index.getVertex(trg);
			
			// ensure basic edge info and apply attributes
			eInfo();
			super.recurseOnChildren(node);
			
			// create edge, rewriting info
			if (dialect != null) { info = dialect.reloadEdgeInfo(info); }
			graph.createEdge(srcV, trgV, info);
			return null;
		}

		/** Callback method for ATTRIBUTES nodes. */
		public Object ATTRIBUTES(IASTNode node) throws Exception {
			return super.recurseOnChildren(node);
		}

		/** Callback method for ATTRIBUTE nodes. */
		public Object ATTRIBUTE(IASTNode node) throws Exception {
			String key = node.getAttrString("key");
			Object value = node.getAttr("value");
			helper.addKeyValue(key,value);
			helper.install(info);
			return null;
		}

	}

}
