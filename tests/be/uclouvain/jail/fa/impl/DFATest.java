package be.uclouvain.jail.fa.impl;

import java.net.URL;

import junit.framework.TestCase;
import be.uclouvain.jail.algo.fa.equiv.DFAEquivalence;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.adjacency.AdjacencyDirectedGraph;
import be.uclouvain.jail.graph.adjacency.DefaultGraphComponentFactory;
import be.uclouvain.jail.graph.adjacency.IGraphComponentFactory;
import be.uclouvain.jail.io.dot.DOTDirectedGraphLoader;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.MapUserInfo;

/** Tests some implementations of DFA. */
public class DFATest extends TestCase {

	/** DFA to use to check equivalence. */
	/*
		digraph DFA {
			graph [rankdir="LR"];
			node [shape="circle"];
			0 [label="v0" isInitial=true isAccepting=false isError=false];
			1 [label="v1" isInitial=false isAccepting=true isError=false shape="doublecircle"];
			2 [label="v2" isInitial=false isAccepting=true isError=false shape="doublecircle"];
			3 [label="v3" isInitial=false isAccepting=false isError=false];
			4 [label="v4" isInitial=false isAccepting=false isError=true color="red"];
			0 -> 1 [letter="a"];
			0 -> 2 [letter="b"];
			1 -> 3 [letter="b"];
			2 -> 3 [letter="a"];
			3 -> 1 [letter="a"];
			3 -> 2 [letter="b"];
			1 -> 4 [letter="a"];
			2 -> 4 [letter="b"];	
		}
	 */
	private IDFA reference;

	/** Creates a vertex info. */
	private IUserInfo vInfo(boolean initial, boolean accepting, boolean error) {
		IUserInfo info = new MapUserInfo();
		info.setAttribute("isInitial", initial);
		info.setAttribute("isAccepting", accepting);
		info.setAttribute("isError", error);
		return info;
	}

	/** Creates an edge info. */
	private IUserInfo eInfo(String letter) {
		IUserInfo info = new MapUserInfo();
		info.setAttribute("letter", letter);
		return info;
	}
	
	/** Sets the test up, creating the reference DFA. */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		reference = new GraphDFA();
		IDirectedGraph graph = reference.getGraph();
		
		Object v0 = graph.createVertex(vInfo(true,false,false));
		Object v1 = graph.createVertex(vInfo(false,true,false));
		Object v2 = graph.createVertex(vInfo(false,true,false));
		Object v3 = graph.createVertex(vInfo(false,false,false));
		Object v4 = graph.createVertex(vInfo(false,false,true));
		
		graph.createEdge(v0, v1, eInfo("a"));
		graph.createEdge(v0, v2, eInfo("b"));
		graph.createEdge(v1, v3, eInfo("b"));
		graph.createEdge(v3, v1, eInfo("a"));
		graph.createEdge(v2, v3, eInfo("a"));
		graph.createEdge(v3, v2, eInfo("b"));
		graph.createEdge(v1, v4, eInfo("a"));
		graph.createEdge(v2, v4, eInfo("b"));
	}

	/** Returns a graph URL. */
	public URL getDFAURL() {
		return DFATest.class.getResource("DFA.dot");
	}
	
	/** Loads a dfa from DFA.dot file. */
	public IDFA loadDFA(IDFA dfa) throws Exception {
		DOTDirectedGraphLoader.loadGraph(dfa.getGraph(),getDFAURL());
		return dfa;
	}

	/** Tests default GraphDFA implementation. */
	public void testEfficientDFA() throws Exception {
		AttributeGraphFAInformer informer = new AttributeGraphFAInformer();
		IGraphComponentFactory factory = new DFAComponentFactory();
		IDirectedGraph graph = new AdjacencyDirectedGraph(factory);
		IDFA dfa = loadDFA(new GraphDFA(graph,informer));
		assertTrue(DFAEquivalence.isEquivalent(dfa,reference));
	}
	
	/** Tests default GraphDFA implementation. */
	public void testClassicalDFA() throws Exception {
		AttributeGraphFAInformer informer = new AttributeGraphFAInformer();
		IGraphComponentFactory factory = new DefaultGraphComponentFactory();
		IDirectedGraph graph = new AdjacencyDirectedGraph(factory);
		IDFA dfa = loadDFA(new GraphDFA(graph,informer));
		assertTrue(DFAEquivalence.isEquivalent(dfa,reference));
	}
	
	/** Tests default GraphDFA implementation. */
	public void testDFAWithNFA() throws Exception {
		AttributeGraphFAInformer informer = new AttributeGraphFAInformer();
		IGraphComponentFactory factory = new NFAComponentFactory();
		IDirectedGraph graph = new AdjacencyDirectedGraph(factory);
		IDFA dfa = loadDFA(new GraphDFA(graph,informer));
		assertTrue(DFAEquivalence.isEquivalent(dfa,reference));
	}
	
}
