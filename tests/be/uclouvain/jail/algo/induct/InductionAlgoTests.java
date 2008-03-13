package be.uclouvain.jail.algo.induct;

import junit.framework.TestCase;
import be.uclouvain.jail.algo.fa.equiv.DFAEquiv;
import be.uclouvain.jail.algo.induct.internal.BlueFringeAlgo;
import be.uclouvain.jail.algo.induct.internal.DefaultInductionAlgoInput;
import be.uclouvain.jail.algo.induct.internal.IInductionAlgoInput;
import be.uclouvain.jail.algo.induct.internal.RPNIAlgo;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.ISample;
import be.uclouvain.jail.fa.IString;
import be.uclouvain.jail.fa.IWalkInfo;
import be.uclouvain.jail.fa.utils.DefaultSample;
import be.uclouvain.jail.tests.JailTestUtils;
import be.uclouvain.jail.vm.toolkits.AutomatonFacade;

/**
 * Tests induction algorithms.
 * 
 * @author blambeau
 */
public class InductionAlgoTests extends TestCase {

	/** Asserts that two DFAs are equivalent. */
	public void assertEquivalent(IDFA dfa1, IDFA dfa2) throws Exception {
		assertTrue(new DFAEquiv(dfa1,dfa2).areEquivalent());
	}

	/** Checks that a result is valid according to a sample. */
	public void assertValidResult(ISample<?> sample, IDFA result) {
		for (IString<?> s: sample) {
			IWalkInfo info = s.walk(result);
			boolean accepted = info.isFullyIncluded() && info.getIncludedPart().isAccepted();
			boolean positive = s.isPositive();
			assertEquals("Accepted iif positive",accepted,positive);
		}
	}

	/** Tests an induction algorithm. */
	public IDFA testRPNI(ISample<?> sample, IDFA expected, IInductionAlgoInput input) throws Exception {
		IDFA result = new RPNIAlgo().execute(input);
		
		// assert equivalence
		IDFA r = AutomatonFacade.uncomplement(result);
		if (expected != null) {
			assertEquivalent(expected,r);
		}
		assertValidResult(sample,r);
		
		return result;
	}

	/** Tests an induction algorithm. */
	public IDFA testBlueFringe(ISample<?> sample, IDFA expected, IInductionAlgoInput input) throws Exception {
		IDFA result = new BlueFringeAlgo().execute(input);
		
		// assert equivalence
		IDFA r = AutomatonFacade.uncomplement(result);
		if (expected != null) {
			assertEquivalent(expected,r);
		}
		assertValidResult(sample,r);

		return result;
	}
	
	/** Tests RPNI algorithm on a sample, with known target. */
	public IDFA testRPNI(ISample<?> sample, IDFA expected) throws Exception {
		IInductionAlgoInput input = new DefaultInductionAlgoInput(sample);
		return testRPNI(sample,expected,input);
	}
	
	/** Tests BlueFringe algorithm. */
	public IDFA testBlueFringe(ISample<?> sample, IDFA expected) throws Exception {
		IInductionAlgoInput input = new DefaultInductionAlgoInput(sample);
		return testBlueFringe(sample,expected,input);
	}
	
	/** Tests RPNI on Sample 3.2 from INRIA. */
	public void testRPNIOnInriaSample3_2() throws Exception {
		// load expected result and sample
		IDFA expected = JailTestUtils.INRIA_DFA_3_1();
		ISample<String> sample = JailTestUtils.INRIA_SAMPLE_3_2();
		testRPNI(sample,expected);
	}
	
	/** Tests BlueFringe on Sample 3.2 from INRIA. */
	public void testBlueFringeOnInriaSample3_2() throws Exception {
		// load expected result and sample
		IDFA expected = JailTestUtils.INRIA_DFA_3_1();
		ISample<String> sample = JailTestUtils.INRIA_SAMPLE_3_2();
		testBlueFringe(sample,expected);
	}
	
	/** Tests RPNI on the train. */
	public void testRPNIOnTrain() throws Exception {
		// load expected result and sample
		IDFA expected = JailTestUtils.TRAIN_DFA();
		ISample<String> sample = JailTestUtils.TRAIN_SAMPLE();
		testRPNI(sample,expected);
	}
	
	/** Tests RPNI on the train. */
	public void testBlueFringeOnTrain() throws Exception {
		// load expected result and sample
		IDFA expected = JailTestUtils.TRAIN_DFA();
		ISample<String> sample = JailTestUtils.TRAIN_SAMPLE();
		testBlueFringe(sample,expected);
	}
	
	/** Tests RPNI on the random PTA. */
	public void testRPNIOnRandomPTA() throws Exception {
		IDFA pta = JailTestUtils.loadDotDFA(JailTestUtils.resource(getClass(), "pta_labels.dot"));
		ISample<Object> sample = new DefaultSample<Object>(pta);
		testRPNI(sample,null);
	}
	
	/** Tests BlueFringe on the random PTA. */
	public void testBlueFringeOnRandomPTA() throws Exception {
		IDFA pta = JailTestUtils.loadDotDFA(JailTestUtils.resource(getClass(), "pta_labels.dot"));
		ISample<Object> sample = new DefaultSample<Object>(pta);
		testBlueFringe(sample,null);
	}
	
	/** Main method. */
	public static void main(String[] args) throws Exception {
		new InductionAlgoTests().testRPNIOnInriaSample3_2();
	}
	
}
