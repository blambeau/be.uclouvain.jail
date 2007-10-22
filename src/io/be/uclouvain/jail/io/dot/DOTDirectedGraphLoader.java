package be.uclouvain.jail.io.dot;

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
import be.uclouvain.jail.graph.deco.DirectedGraph;
import be.uclouvain.jail.graph.deco.GraphUniqueIndex;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.MapUserInfo;

/** Loads a DirectedGraph from a .dot file. */
public class DOTDirectedGraphLoader {

	/** Creates a callback instance. */
	private DOTDirectedGraphLoader() {
	}

	/** Loads a graph from some source. */
	public static IDirectedGraph loadGraph(Object source) throws ParseException, IOException {
		// create parser instance and set loader
		DOTParser parser = new DOTParser();
		parser.setActiveLoader(new ASTLoader(new EnumTypeResolver<DOTNodes>(DOTNodes.class)));
		
		// create parsing position
		Pos pos = new Pos(Input.input(new BaseLocation(source)),0);
		
		// parse graph
		IASTNode node = (IASTNode) parser.pGraphdef(pos);
		
		// load graph using this callback
		try {
			IDirectedGraph graph = (IDirectedGraph) node.accept(new LoaderCallback());
			return graph;
		} catch (Exception e) {
			throw new RuntimeException("Unexpected exception.",e);
		}
	}
	
	/** Callback class to load graph from AST. */
	static class LoaderCallback extends DOTCallback<Object> { 
	
		/** Loaded graph. */
		private DirectedGraph graph;
		
		/** Graph unique index. */
		private GraphUniqueIndex index;
		
		/** Last info created. */
		private IUserInfo info;
		
		/** Creates a user info instance. */
		private IUserInfo vInfo(String id) {
			info = new MapUserInfo();
			info.setAttribute("id",id);
			return info;
		}
		
		/** Creates a user info instance. */
		private IUserInfo eInfo() {
			info = new MapUserInfo();
			return info;
		}
		
		/** Callback method for GRAPHDEF nodes. */
		public Object GRAPHDEF(IASTNode node) throws Exception {
			graph = (DirectedGraph) new AdjacencyDirectedGraph().adapt(DirectedGraph.class);
			index = new GraphUniqueIndex(GraphUniqueIndex.VERTEX,"id",true).installOn(graph);
			super.recurseOnChildren(node);
			return graph;
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
			graph.createVertex(vInfo(id));
			super.recurseOnChildren(node);
			return null;
		}
	
		/** Callback method for EDGEDEF nodes. */
		public Object EDGEDEF(IASTNode node) throws Exception {
			String src = node.getAttrString("src");
			String trg = node.getAttrString("trg");
			Object srcV = index.getVertex(src);
			Object trgV = index.getVertex(trg);
			graph.createEdge(srcV, trgV, eInfo());
			return null;
		}
	
		/** Callback method for ATTRIBUTES nodes. */
		public Object ATTRIBUTES(IASTNode node) throws Exception {
			super.recurseOnChildren(node);
			return null;
		}
	
		/** Callback method for ATTRIBUTE nodes. */
		public Object ATTRIBUTE(IASTNode node) throws Exception {
			String key = node.getAttrString("key");
			Object value = node.getAttr("value");
			info.setAttribute(key, value);
			return null;
		}

	}
	
}
