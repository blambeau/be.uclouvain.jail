package be.uclouvain.jail.algo.fa.tmoves;

import junit.framework.TestCase;
import be.uclouvain.jail.Jail;
import be.uclouvain.jail.adapt.AdaptUtils;
import be.uclouvain.jail.adapt.CreateClassAdapter;
import be.uclouvain.jail.algo.fa.determinize.NFADeterminizer;
import be.uclouvain.jail.algo.fa.equiv.DFAEquiv;
import be.uclouvain.jail.dialect.IPrintable;
import be.uclouvain.jail.dialect.dot.DOTDirectedGraphLoader;
import be.uclouvain.jail.dialect.dot.DOTDirectedGraphPrintable;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.INFA;
import be.uclouvain.jail.fa.impl.GraphDFA;
import be.uclouvain.jail.graph.IDirectedGraph;

/** Tests the NFA determinizer algorithm. */
public class TauRemoverAlgoTest extends TestCase {

	/** DFA without tau-transitions. */
	private IDFA identity;
	
	/** DFA with tau-transitions. */
	private IDFA dfa;
	
	/** Loads NFA.dot and DFA.dot */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		identity = new GraphDFA();
		DOTDirectedGraphLoader.loadGraph(identity.getGraph(),TauRemoverAlgoTest.class.getResource("IDENTITY.dot"));
		dfa = new GraphDFA();
		DOTDirectedGraphLoader.loadGraph(dfa.getGraph(),TauRemoverAlgoTest.class.getResource("TMOVES.dot"));
	}
	
	/** Debugs a DFA. */
	@SuppressWarnings("unused")
	private void debug(INFA dfa) throws Exception {
		Jail.install();
		IPrintable p = (IPrintable) dfa.getGraph().adapt(IPrintable.class);
		Jail.setProperty("DirectedGraphPrintable.dot.edge.label.uinfo","letter");
		p.print(System.out);
	}
	
	/** Debugs a DFA. */
	@SuppressWarnings("unused")
	private void debug(IDFA dfa) throws Exception {
		AdaptUtils.register(IDirectedGraph.class,IPrintable.class,new CreateClassAdapter(DOTDirectedGraphPrintable.class));
		IPrintable p = (IPrintable) dfa.getGraph().adapt(IPrintable.class);
		Jail.setProperty("DirectedGraphPrintable.dot.edge.label.uinfo","letter");
		p.print(System.out);
	}
	
	/** Tests tau remover on identity. */
	public void testTauRemoverOnIdentity() throws Exception {
		ITauInformer informer = new ITauInformer() {
			public boolean isEpsilon(Object letter) {
				return "tau".equals(letter);
			}
		};
		INFA result = new TauRemover(identity,informer).getResultingNFA();
		IDFA equiv = new NFADeterminizer(result).getResultingDFA();
		assertTrue(new DFAEquiv(identity,equiv).areEquivalent());
	}
	
	/** Tests the determinizer algorithm. */
	public void testTauRemover() throws Exception {
		ITauInformer informer = new ITauInformer() {
			public boolean isEpsilon(Object letter) {
				return "tau".equals(letter);
			}
		};
		INFA result = new TauRemover(dfa,informer).getResultingNFA();
		IDFA equiv = new NFADeterminizer(result).getResultingDFA();
		debug(equiv);
	}
	
	
}
