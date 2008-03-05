package be.uclouvain.jail.algo.fa.induct;

import java.io.PrintWriter;

import junit.framework.TestCase;
import be.uclouvain.jail.algo.fa.equiv.DFAEquiv;
import be.uclouvain.jail.algo.induct.compatibility.PairwiseCompatibility;
import be.uclouvain.jail.algo.induct.compatibility.StateKindCompatibility;
import be.uclouvain.jail.algo.induct.internal.BlueFringeAlgo;
import be.uclouvain.jail.algo.induct.internal.DefaultInductionAlgoInput;
import be.uclouvain.jail.algo.induct.internal.IInductionAlgoInput;
import be.uclouvain.jail.algo.induct.internal.RPNIAlgo;
import be.uclouvain.jail.algo.induct.processor.BackPropagateProcessor;
import be.uclouvain.jail.dialect.dot.DOTGraphDialect;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.ISample;
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

	/** Debugs the DFA with dot. */
	private void dotDebug(IDFA dfa) throws Exception {
		PrintWriter w = new PrintWriter(System.out);
		new DOTGraphDialect().print(dfa.getGraph(), w, null);
		w.flush();
	}
	
	/** Tests RPNI on Sample 3.2 from INRIA. */
	public void testRPNIOnInriaSample3_2() throws Exception {
		// load expected result and sample
		IDFA expected = JailTestUtils.INRIA_DFA_3_1();
		ISample<String> sample = JailTestUtils.INRIA_SAMPLE_3_2();

		AutomatonFacade.show((IDFA)sample.adapt(IDFA.class));
		
		// execute RPNI
		IInductionAlgoInput input = new DefaultInductionAlgoInput(sample);
		IDFA result = new RPNIAlgo().execute(input);

		// assert equivalence
		assertEquivalent(expected,result);
		
		dotDebug(result);
	}
	
	/** Tests BlueFringe on Sample 3.2 from INRIA. */
	public void testBlueFringeOnInriaSample3_2() throws Exception {
		// load expected result and sample
		IDFA expected = JailTestUtils.INRIA_DFA_3_1();
		ISample<String> sample = JailTestUtils.INRIA_SAMPLE_3_2();

		// execute BlueFringe
		IInductionAlgoInput input = new DefaultInductionAlgoInput(sample);
		IDFA result = new BlueFringeAlgo().execute(input);

		// assert equivalence
		assertEquivalent(expected,result);
		
		dotDebug(result);
	}
	
	/** Tests RPNI on the train. */
	public void testRPNIOnTrain() throws Exception {
		// load expected result and sample
		IDFA expected = JailTestUtils.TRAIN_DFA();
		ISample<String> sample = JailTestUtils.TRAIN_SAMPLE();
		
		// execute RPNI
		IInductionAlgoInput input = new DefaultInductionAlgoInput(sample);
		IDFA result = new RPNIAlgo().execute(input);
		result = AutomatonFacade.uncomplement(result);
		
		// assert equivalence
		assertEquivalent(expected,result);
	}
	
	/** Tests RPNI on the train. */
	public void testBlueFringeOnTrain() throws Exception {
		// load expected result and sample
		IDFA expected = JailTestUtils.TRAIN_DFA();
		ISample<String> sample = JailTestUtils.TRAIN_SAMPLE();
		
		// execute RPNI
		IInductionAlgoInput input = new DefaultInductionAlgoInput(sample);
		IDFA result = new BlueFringeAlgo().execute(input);
		result = AutomatonFacade.uncomplement(result);
		
		// assert equivalence
		assertEquivalent(expected,result);
	}
	
	/** Tests RPNI on Sample 3.2 from INRIA. */
	public void testBackPropagationOnInriaSample3_2() throws Exception {
		// load expected result and sample
		IDFA expected = JailTestUtils.INRIA_DFA_3_1();
		ISample<String> sample = JailTestUtils.INRIA_SAMPLE_3_2();

		// execute RPNI
		DefaultInductionAlgoInput input = new DefaultInductionAlgoInput(sample);
		input.addCompatibility(new PairwiseCompatibility());
		input.addCompatibility(new StateKindCompatibility());
		input.addPreProcessor(new BackPropagateProcessor());
		IDFA result = new RPNIAlgo().execute(input);

		// assert equivalence
		assertEquivalent(expected,result);
	}
	
	/** Main method. */
	public static void main(String[] args) throws Exception {
		new InductionAlgoTests().testBackPropagationOnInriaSample3_2();
	}
	
}
