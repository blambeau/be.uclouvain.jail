package be.uclouvain.jail.algo.induct;

import junit.framework.TestCase;
import be.uclouvain.jail.algo.induct.extension.LSMIExtension;
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
 * Tests the BUG 13 on LSMI.
 * 
 * @author blambeau
 */
public class LSMIBug13Test extends TestCase {

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
		assertValidResult(sample,r);
		
		return result;
	}

	/** Tests RPNI algorithm on a sample, with known target. */
	public IDFA testRPNI(ISample<?> sample, IDFA expected) throws Exception {
		// prepare
		DefaultInductionAlgoInput input = new DefaultInductionAlgoInput(sample);
		input.setRepresentorAttr("class");
		input.setUnknown("");
		input.setExtension(new LSMIExtension());
		// test
		return testRPNI(sample,expected,input);
	}
	
	/** Tests on the 20080313 bug. */
	public void testRPNIOn20080313Bug() throws Exception {
		IDFA pta = JailTestUtils.loadDotDFA(JailTestUtils.resource(getClass(), "pta_labels_20080313_bug.dot"));
		ISample<Object> sample = new DefaultSample<Object>(pta);
		AutomatonFacade.show(pta);
		testRPNI(sample,null);
	}
	
	/** Main method. */
	public static void main(String[] args) throws Exception {
		for (int i=0; i<1; i++) {
			System.out.println("Executing -----------------------------------------------------");
			new LSMIBug13Test().testRPNIOn20080313Bug();
		}
	}
	
}
