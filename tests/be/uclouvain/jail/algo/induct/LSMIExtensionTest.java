package be.uclouvain.jail.algo.induct;

import be.uclouvain.jail.algo.induct.extension.LSMIExtension;
import be.uclouvain.jail.algo.induct.internal.DefaultInductionAlgoInput;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.ISample;

/** 
 * Tests the LSMI extension.
 * 
 * @author blambeau
 */
public class LSMIExtensionTest extends InductionAlgoTests {

	@Override
	public void testBlueFringeOnInriaSample3_2() throws Exception {
	}

	@Override
	public void testBlueFringeOnTrain() throws Exception {
	}

	@Override
	public void testRPNIOnInriaSample3_2() throws Exception {
	}

	@Override
	public void testRPNIOnTrain() throws Exception {
	}

	/** Tests RPNI algorithm on a sample, with known target. */
	@Override
	public IDFA testRPNI(ISample<?> sample, IDFA expected) throws Exception {
		// prepare
		DefaultInductionAlgoInput input = new DefaultInductionAlgoInput(sample);
		input.setRepresentorAttr("class");
		input.setUnknown("");
		input.setExtension(new LSMIExtension());
		// test
		return testRPNI(sample,expected,input);
	}
	
	/** Tests BlueFringe algorithm. */
	@Override
	public IDFA testBlueFringe(ISample<?> sample, IDFA expected) throws Exception {
		// prepare
		DefaultInductionAlgoInput input = new DefaultInductionAlgoInput(sample);
		input.setRepresentorAttr("class");
		input.setUnknown("");
		input.setExtension(new LSMIExtension());
		// test
		IDFA dfa = testBlueFringe(sample,expected,input);
		return dfa;
	}
	
	/** Main method. */
	public static void main(String[] args) throws Exception {
		new LSMIExtensionTest().testBlueFringeOnRandomPTA();
	}
	
}
