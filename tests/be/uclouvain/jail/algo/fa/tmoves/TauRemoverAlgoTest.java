package be.uclouvain.jail.algo.fa.tmoves;

import junit.framework.TestCase;
import be.uclouvain.jail.algo.fa.determinize.NFADeterminizer;
import be.uclouvain.jail.algo.fa.equiv.DFAEquiv;
import be.uclouvain.jail.dialect.dot.DOTDirectedGraphLoader;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.INFA;
import be.uclouvain.jail.fa.impl.GraphDFA;

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
		assertNotNull(equiv);
	}
	
	
}
