package be.uclouvain.jail.algo.fa.equiv;

import java.net.URL;

import junit.framework.TestCase;
import be.uclouvain.jail.dialect.dot.DOTDirectedGraphLoader;
import be.uclouvain.jail.fa.FAStateKind;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.impl.AttributeGraphFAInformer;
import be.uclouvain.jail.fa.impl.GraphDFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.IUserInfoHelper;
import be.uclouvain.jail.uinfo.UserInfoHelper;

/** Tests DFA equivalent algorithm. */
public class DFAEquivTest extends TestCase {

	/** Reference DFA used to check equivalence. */
	/*
		digraph DFA {
			graph [rankdir="LR"];
			node [shape="circle"];
			0 [label="v0" isInitial=true kind='PASSAGE'];
			1 [label="v1" isInitial=false kind='ACCEPTING' shape="doublecircle"];
			2 [label="v2" isInitial=false kind='ACCEPTING' shape="doublecircle"];
			3 [label="v3" isInitial=false kind='PASSAGE'];
			4 [label="v4" isInitial=false kind='AVOID' color="red"];
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

	/** Default helper instance. */
	private IUserInfoHelper helper = UserInfoHelper.instance();
	
	/** Creates a vertex info. */
	private IUserInfo vInfo(String id, boolean initial, boolean accepting, boolean error) {
		helper.keys("id",
				    AttributeGraphFAInformer.STATE_INITIAL_KEY,
				    AttributeGraphFAInformer.STATE_KIND_KEY);
		return helper.install(null, id, initial, 
				FAStateKind.fromBools(accepting,error));
	}

	/** Creates an edge info. */
	private IUserInfo eInfo(String letter) {
		helper.addKeyValue("letter", letter);
		return helper.install();
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

	/** Tests trivial equivalence of reference with himself. */
	public void testTrivialEquivalence() {
		assertTrue(DFAEquiv.isEquivalentTo(reference, reference));
	}
	
	/** Loads a dfa from DFA.dot file. */
	public IDFA loadDFA(URL url) throws Exception {
		IDFA dfa = new GraphDFA();
		DOTDirectedGraphLoader.loadGraph(dfa.getGraph(),url);
		return dfa;
	}

	/** Tests equivalence of reference with REFERENCE.dot. */
	public void testReferenceEquivalence() throws Exception {
		IDFA dfa = loadDFA(DFAEquivTest.class.getResource("REFERENCE.dot"));
		assertTrue(DFAEquiv.isEquivalentTo(dfa, reference));
	}
	
	/** Tests equivalence of reference with SUPER.dot. */
	public void testSuperEquivalence() throws Exception {
		IDFA dfa = loadDFA(DFAEquivTest.class.getResource("SUPER.dot"));

		// without counter example
		assertFalse(DFAEquiv.isEquivalentTo(dfa, reference));
		
		// with counter example
		DFAEquiv equiv = new DFAEquiv(dfa,reference);
		equiv.setCounterExampleEnabled(true);
		assertFalse(equiv.areEquivalent());
		
		assertNotNull(equiv.getCounterExampleKind());
		assertNotNull(equiv.getCounterExample());
	}
	
}
