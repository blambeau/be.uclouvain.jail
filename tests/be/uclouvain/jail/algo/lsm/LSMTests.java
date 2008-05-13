package be.uclouvain.jail.algo.lsm;

import junit.framework.TestCase;
import be.uclouvain.jail.algo.fa.equiv.DFAEquiv;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.ISample;
import be.uclouvain.jail.fa.IString;
import be.uclouvain.jail.fa.IWalkInfo;
import be.uclouvain.jail.fa.utils.DefaultSample;
import be.uclouvain.jail.tests.JailTestUtils;
import be.uclouvain.jail.vm.toolkits.AutomatonFacade;

/**
 * Tests LSM algorithm. 
 * 
 * @author blambeau
 */
public class LSMTests extends TestCase {

	/** Asserts that two DFAs are equivalent. */
	public void assertEquivalent(IDFA dfa1, IDFA dfa2) throws Exception {
		assertTrue(new DFAEquiv(dfa1,dfa2).areEquivalent());
	}

	/** Checks that a result is valid according to a sample. */
	public void assertValidResult(ISample<?> sample, IDFA result) {
		for (IString<?> s: sample) {
			IWalkInfo<?> info = s.walk(result);
			boolean accepted = info.isFullyIncluded() && info.getIncludedPart().isAccepted();
			boolean positive = s.isPositive();
			assertEquals("Accepted iif positive",accepted,positive);
		}
	}

	/** Tests LSM on an input, using an expected result. */
	public void testLSM(IDFA source, IDFA expected) throws Exception {
		LSMAlgoInput input = new LSMAlgoInput(source);
		LSMAlgoResult result = new LSMAlgoResult();
		new LSMAlgo().execute(input, result);
		
		// assert equivalence
		IDFA r = AutomatonFacade.uncomplement(result.resultDFA());
		if (expected != null) {
			assertEquivalent(expected,r);
		}
	}

	/** Tests LSM on an input, using an expected result. */
	public void testLSM(ISample<?> sample, IDFA expected) throws Exception {
		IDFA source = (IDFA) sample.adapt(IDFA.class);
		LSMAlgoInput input = new LSMAlgoInput(source);
		LSMAlgoResult result = new LSMAlgoResult();
		new LSMAlgo().execute(input, result);
		
		// assert equivalence
		IDFA r = AutomatonFacade.uncomplement(result.resultDFA());
		if (expected != null) {
			assertEquivalent(expected,r);
		}
		assertValidResult(sample,r);
	}
	
	/** Tests RPNI on Sample 3.2 from INRIA. */
	public void testLSMOnInriaSample3_2() throws Exception {
		// load expected result and sample
		IDFA expected = JailTestUtils.INRIA_DFA_3_1();
		ISample<String> sample = JailTestUtils.INRIA_SAMPLE_3_2();
		testLSM(sample,expected);
	}
	
	/** Tests RPNI on the train. */
	public void testLSMOnTrain() throws Exception {
		// load expected result and sample
		IDFA expected = JailTestUtils.TRAIN_DFA();
		ISample<String> sample = JailTestUtils.TRAIN_SAMPLE();
		testLSM(sample,expected);
	}
	
	/** Tests RPNI on the random PTA. */
	public void testLSMOnRandomPTA() throws Exception {
		IDFA pta = JailTestUtils.loadDotDFA(JailTestUtils.resource(getClass(), "pta_labels.dot"));
		ISample<Object> sample = new DefaultSample<Object>(pta);
		testLSM(sample,null);
	}
	
}
