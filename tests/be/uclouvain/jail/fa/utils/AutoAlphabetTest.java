package be.uclouvain.jail.fa.utils;

import junit.framework.TestCase;
import be.uclouvain.jail.fa.IAlphabet;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.impl.GraphDFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.utils.ITotalOrder;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.MapUserInfo;

/**
 * Tests the AutoAlphabet class.
 * 
 * @author blambeau
 */
public class AutoAlphabetTest extends TestCase {

	/** Reference DFA used to check equivalence. */
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
	private IUserInfo vInfo(String id, boolean initial, boolean accepting, boolean error) {
		IUserInfo info = new MapUserInfo();
		info.setAttribute("id", id);
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
		
		Object v0 = graph.createVertex(vInfo("0",true,false,false));
		Object v1 = graph.createVertex(vInfo("1", false,true,false));
		Object v2 = graph.createVertex(vInfo("2", false,true,false));
		Object v3 = graph.createVertex(vInfo("3", false,false,false));
		Object v4 = graph.createVertex(vInfo("4", false,false,true));
		
		graph.createEdge(v0, v1, eInfo("a"));
		graph.createEdge(v0, v2, eInfo("b"));
		graph.createEdge(v1, v3, eInfo("b"));
		graph.createEdge(v3, v1, eInfo("a"));
		graph.createEdge(v2, v3, eInfo("a"));
		graph.createEdge(v3, v2, eInfo("b"));
		graph.createEdge(v1, v4, eInfo("a"));
		graph.createEdge(v2, v4, eInfo("b"));
	}

	/** Tests the correctness of returned alphabet. */
	public void testAutoAlphabet() {
		IAlphabet<String> alphabet = reference.getAlphabet();
		
		// test total order
		ITotalOrder<String> order = alphabet.getLetters();
		assertEquals(2,order.size());
		assertEquals("a",order.getElementAt(0));
		assertEquals("b",order.getElementAt(1));
		assertEquals(0,order.indexOf("a"));
		assertEquals(1,order.indexOf("b"));
		
		// test comparator
		String previous = null;
		for (String letter: alphabet) {
			if (previous != null) {
				assertEquals("b",letter);
				assertTrue(alphabet.compare(previous, letter)<0);
			} else {
				assertEquals("a",letter);
			}
			previous = letter;
		}
		
	}
	
}
