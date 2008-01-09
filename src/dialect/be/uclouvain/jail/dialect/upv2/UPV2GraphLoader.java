package be.uclouvain.jail.dialect.upv2;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.chefbe.autogram.ast2.IASTNode;
import net.chefbe.autogram.ast2.parsing.ParseException;
import net.chefbe.autogram.ast2.parsing.active.ASTLoader;
import net.chefbe.autogram.ast2.parsing.active.ASTLoader.EnumTypeResolver;
import net.chefbe.autogram.ast2.parsing.peg.Input;
import net.chefbe.autogram.ast2.parsing.peg.Pos;
import net.chefbe.autogram.ast2.utils.BaseLocation;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.adjacency.AdjacencyDirectedGraph;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.IUserInfoHelper;
import be.uclouvain.jail.uinfo.UserInfoHelper;
import be.uclouvain.jail.vm.JailVMOptions;

/**
 * Provides graph loading for UPV2 dialect.
 * 
 * @author blambeau
 */
public class UPV2GraphLoader extends UPV2Callback<Object> {

	/** Writer to use */
	private IDirectedGraph writer;

	/** Requesting dialect. */
	private UPV2GraphDialect dialect;

	/** Helper to use. */
	private IUserInfoHelper helper;

	/** Empty user info. */
	private IUserInfo emptyInfo;
	
	/** Map for vertices. */
	private Map<Integer, Object> vertices;
	
	/** Creates a graph loader. */
	public UPV2GraphLoader(IDirectedGraph writer, UPV2GraphDialect dialect, JailVMOptions options) {
		this.writer = writer;
		this.dialect = dialect;
		this.helper = UserInfoHelper.instance();
	}

	/** Loads a source inside a graph writer. */
	public static void load(Object source, IDirectedGraph writer, UPV2GraphDialect dialect, JailVMOptions options)
			throws IOException, ParseException {
		// parse source
		UPV2Parser parser = new UPV2Parser();
		parser.setActiveLoader(new ASTLoader(new EnumTypeResolver<UPV2Nodes>(UPV2Nodes.class)));
		Pos pos = new Pos(Input.input(new BaseLocation(source)), 0);

		// use callback
		IASTNode root = (IASTNode) parser.pGraphdef(pos);
		try {
			root.accept(new UPV2GraphLoader(writer, dialect, options));
		} catch (Exception e) {
			throw new IllegalStateException("Unexpected error", e);
		}
	}

	/** Loads a directed grah. */
	public static IDirectedGraph load(Object source, UPV2GraphDialect dialect, JailVMOptions options)
			throws IOException, ParseException {
		IDirectedGraph graph = new AdjacencyDirectedGraph();
		load(source, graph, dialect, options);
		return graph;
	}

	/** Loads a directed grah. */
	public static IDirectedGraph load(Object source) throws IOException, ParseException {
		return load(source, null, null);
	}

	/** Loads a directed grah. */
	public static void load(Object source, IDirectedGraph writer) throws IOException, ParseException {
		load(source, writer, null, null);
	}

	/** Callback method for GRAPHDEF nodes. */
	public Object GRAPHDEF(IASTNode node) throws Exception {
		this.vertices = new HashMap<Integer, Object>();
		this.emptyInfo = helper.install();
		super.recurseOnChildren(node);
		this.vertices = null;
		return null;
	}

	/** Ensures a vertex for an id. */
	private Object ensureVertex(Integer id) {
		if (!vertices.containsKey(id)) {
			Object state = writer.createVertex(emptyInfo);
			vertices.put(id, state);
		}
		return vertices.get(id);
	}
	
	/** Callback method for STATE_LINE nodes. */
	public Object STATE_LINE(IASTNode node) throws Exception {
		Integer id = (Integer) node.getAttr("id");
		
		// ensure vertex
		Object vertex = ensureVertex(id);
		
		// install attributes
		helper.addKeyValue("id", id);
		super.recurseOnChildren(node);
		
		// set user infos with rewriting
		IUserInfo info = helper.install();
		if (dialect != null) { info = dialect.reloadVertexInfo(info); }
		writer.setVertexInfo(vertex, info);
		
		return null;
	}

	/** Callback method for EDGE_LINE nodes. */
	public Object EDGE_LINE(IASTNode node) throws Exception {
		Integer from = (Integer) node.getAttr("from");
		Integer to = (Integer) node.getAttr("to");
		Object letter = node.getAttr("letter");
		
		// install attributes
		helper.addKeyValue("letter", letter);
		super.recurseOnChildren(node);
		
		// set user infos with rewriting
		IUserInfo info = helper.install();
		if (dialect != null) { info = dialect.reloadEdgeInfo(info); }
		
		// create edge
		writer.createEdge(ensureVertex(from), ensureVertex(to), info);
		
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
		helper.addKeyValue(key, value);
		return null;
	}

}
