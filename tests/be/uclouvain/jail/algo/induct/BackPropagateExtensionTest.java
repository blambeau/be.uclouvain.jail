package be.uclouvain.jail.algo.induct;

import be.uclouvain.jail.algo.induct.extension.BackPropagateExtension;
import be.uclouvain.jail.algo.induct.internal.DefaultInductionAlgoInput;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.ISample;

/** 
 * Tests the LSMI extension.
 * 
 * @author blambeau
 */
public class BackPropagateExtensionTest extends InductionAlgoTests {

	/** Tests RPNI algorithm on a sample, with known target. */
	@Override
	public IDFA testRPNI(ISample<?> sample, IDFA expected) throws Exception {
		// prepare
		DefaultInductionAlgoInput input = new DefaultInductionAlgoInput(sample);
		input.setRepresentorAttr("class");
		input.setUnknown("");
		input.setExtension(new BackPropagateExtension());
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
		input.setExtension(new BackPropagateExtension());
		// test
		return testBlueFringe(sample,expected,input);
	}
	
}
