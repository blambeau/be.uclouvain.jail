package be.uclouvain.jail.dialect.seqp;

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
import be.uclouvain.jail.fa.FAStateKind;
import be.uclouvain.jail.fa.impl.AttributeGraphFAInformer;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.IDirectedGraphWriter;
import be.uclouvain.jail.graph.adjacency.AdjacencyDirectedGraph;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.IUserInfoHelper;

/**
 * Converts a SEQP Abstract Syntax Tree to a IDirectedGraph.
 * 
 * @author blambeau
 */
public class SEQPGraphLoader extends SEQPCallback<Object> {

	/** State label to set. */
	private static final String STATELABEL = "label";
	
	/** Writer to use */
	private IDirectedGraphWriter writer;

	/** Creates vertices. */
	private Map<String, Object> vertices;

	/** Informations. */
	private Map<Object,IUserInfo> infos;
	
	/** Init state. */
	private Object init;
	
	/** Current user info. */
	private IUserInfo info;

	/** Helper to use. */
	private IUserInfoHelper helper;
	
	/** Creates a graph loader. */
	public SEQPGraphLoader(IDirectedGraphWriter writer, IUserInfoHelper helper) {
		this.writer = writer;
		this.helper = helper;
		vertices = new HashMap<String, Object>();
		this.infos = new HashMap<Object,IUserInfo>();
	}

	/** Loads a source inside a graph writer. */
	public static void load(Object source, 
			IDirectedGraphWriter writer, 
			IUserInfoHelper helper) throws IOException, ParseException {
		// parse source
		SEQPParser parser = new SEQPParser();
		parser.setActiveLoader(new ASTLoader(new EnumTypeResolver<SEQPNodes>(SEQPNodes.class)));
		Pos pos = new Pos(Input.input(new BaseLocation(source)), 0);

		// use callback
		IASTNode root = (IASTNode) parser.pSeqpDef(pos);
		try {
			root.accept(new SEQPGraphLoader(writer, helper));
		} catch (Exception e) {
			throw new IllegalStateException("Unexpected error", e);
		}
	}

	/** Loads a directed grah. */
	public static IDirectedGraph load(Object source, IUserInfoHelper helper) throws IOException, ParseException {
		IDirectedGraph graph = new AdjacencyDirectedGraph();
		load(source, graph, helper);
		return graph;
	}

	/** Creates a vertex user info. */
	private IUserInfo vInfo(String label) {
		helper.addKeyValue(STATELABEL, label == null ? "" : label);
		helper.addKeyValue(AttributeGraphFAInformer.STATE_KIND_KEY, 
				           FAStateKind.fromBools(true,false));
		helper.addKeyValue(AttributeGraphFAInformer.STATE_INITIAL_KEY, vertices.size()==0);
		info = helper.install();
		return info;
	}

	/** Creates an edge user info. */
	private IUserInfo eInfo(String letter) {
		info = helper.keyValue(AttributeGraphFAInformer.EDGE_LETTER_KEY, letter);
		return info;
	}

	/** Ensures a vertex in the graph. */
	private Object ensureVertex(String label, boolean create) {
		Object vertex = label == null ? null : vertices.get(label);
		if (vertex == null && create) {
			info = vInfo(label);
			vertex = writer.createVertex(info);
			if (label != null) {
				vertices.put(label, vertex);
			}
			infos.put(vertex, info);
		} else if (vertex != null) {
			info = infos.get(vertex);
		}
		return vertex;
	}

	/** Callback method for SEQP_DEF nodes. */
	@Override
	public Object SEQP_DEF(IASTNode node) throws Exception {
		super.recurseOnChildren(node);
		return null;
	}

	/** Callback method for STATEDEF nodes. */
	@Override
	public Object STATEDEF(IASTNode node) throws Exception {
		// retrieve vertex
		String label = node.getAttrString("label");
		this.init = ensureVertex(label, true);

		// install attributes and vertex continuation path
		recurseOnChildren(node);
		return null;
	}

	/** Callback method for ATTRIBUTES nodes. */
	@Override
	public Object ATTRIBUTES(IASTNode node) throws Exception {
		super.recurseOnChildren(node);
		return null;
	}

	/** Callback method for ATTRDEF nodes. */
	@Override
	public Object ATTRDEF(IASTNode node) throws Exception {
		String key = node.getAttrString("name");
		Object value = node.getAttr("value");
		helper.keys(key);
		helper.install(info, value);
		return null;
	}

	/** Callback method for PATH nodes. */
	@Override
	public Object PATH(IASTNode node) throws Exception {
		Object[] target = (Object[]) makeCall(node.childFor("root"));
		if (target[0] == null) {
			target[0] = "";
		}
		writer.createEdge(init, target[1], eInfo((String) target[0]));
		return null;
	}

	/** Callback method for EVENTREF nodes. */
	@Override
	public Object EVENTREF(IASTNode node) throws Exception {
		/*
		 * Situations: 
		 *       1) ...->me 
		 *       2) ...->me->Y => target=[null,Y] 
		 *       3) ...->me->you->Y => target=[you,Y]
		 * 
		 * 1) create anonymous target state Y, return [me,Y] 
		 * 2) return [me,Y] 
		 * 3) create anonymous target state Z, create Z->you->Y, return [me,Z]
		 */

		// letter on the edge
		String letter = node.getAttrString("letter");

		// destination vertex and letter, {create target}
		Object[] target = null;
		IASTNode to = node.childFor("to");
		if (to != null) {
			// situations 2) and 3)
			target = (Object[]) makeCall(to);
		} else {
			// situation 1)
			Object Y = ensureVertex(null, true);
			//System.out.println("EVENTREF 1): [" + letter + "," + Y + "]");
			return new Object[] { letter, Y };
		}

		if (target[0] == null) {
			// situation 2)
			//System.out.println("EVENTREF 2): [" + letter + "," + target[1] + "]");
			return new Object[] { letter, target[1] };
		} else {
			// situation 3)
			Object Z = ensureVertex(null, true);
			writer.createEdge(Z, target[1], eInfo((String)target[0]));
			//System.out.println("EVENTREF 3): [" + letter + "," + Z + "]");
			return new Object[] { letter, Z };
		}
	}

	/** Callback method for STATEREF nodes. */
	@Override
	public Object STATEREF(IASTNode node) throws Exception {
		/*
		 * Situations: 
		 *     1) ...->ME                => target=[null,ME] 
		 *     2) ...->ME->event->Y->... => target=[event,Y] 
		 *     3) ...->ME->Y             => target=[null,Y]
		 * 
		 * 1) ensure ME, don't create any edge, return [null,ME] 
		 * 2) ensure ME, create ME->target[0]->target[1] 
		 * 3) ensure ME, create ME->''->target[1]
		 * 
		 * in all cases, return [null,ME]
		 */

		// source vertex {ensure ME}
		String label = node.getAttrString("label");
		Object source = ensureVertex(label, true);

		// destination vertex and letter, {create target}
		Object[] target = null;
		IASTNode to = node.childFor("to");
		if (to != null) {
			// situations 2) and 3)
			target = (Object[]) makeCall(to);
		} else {
			// situation 1)
			return new Object[] { null, source };
		}

		// create empty letter if required {situation 3)}
		if (target[0] == null) {
			target[0] = "";
		}

		// create edge
		writer.createEdge(source, target[1], eInfo((String) target[0]));

		return new Object[] { null, source };
	}

}
