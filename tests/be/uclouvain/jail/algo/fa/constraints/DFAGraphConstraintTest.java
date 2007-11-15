package be.uclouvain.jail.algo.fa.constraints;

import junit.framework.TestCase;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.INFA;
import be.uclouvain.jail.fa.constraints.DFAGraphConstraint;
import be.uclouvain.jail.tests.JailTestUtils;

/** Tests DFAGraphConstraint class. */
public class DFAGraphConstraintTest extends TestCase {

	/** Checks that it correctly answers. */
	public void testIsRespectedBy() throws Exception {
		DFAGraphConstraint c = new DFAGraphConstraint();

		// checks that NFAs are correctly recognized
		for (INFA nfa: JailTestUtils.getAllNFAs()) {
			assertFalse(c.isRespectedBy(nfa.getGraph()));
		}
		
		// checks that DFAs are correctly recognized
		for (IDFA dfa: JailTestUtils.getAllDFAs()) {
			assertTrue(c.isRespectedBy(dfa.getGraph()));
		}
	}
	
}
