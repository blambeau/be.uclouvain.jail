package be.uclouvain.jail.algo.induct;

import be.uclouvain.jail.algo.induct.extension.LSMIExtension;
import be.uclouvain.jail.algo.induct.internal.DefaultInductionAlgoInput;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.ISample;
import be.uclouvain.jail.fa.utils.DefaultSample;
import be.uclouvain.jail.tests.JailTestUtils;
import be.uclouvain.jail.vm.toolkits.AutomatonFacade;

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
	public void testRPNIOnInriaSample3_2() throws Exception {
	}

	@Override
	public void testRPNIOnTrain() throws Exception {
		IDFA pta = JailTestUtils.loadDotDFA(JailTestUtils.resource(getClass(), "train_labels.dot"));
		ISample<Object> sample = new DefaultSample<Object>(pta);

		IDFA rpni = testRPNI(sample,null);
		IDFA bluefringe = testRPNI(sample,null);
		
		AutomatonFacade.show(pta,rpni,bluefringe);
	}
	
	/** Tests RPNI with an allsame constraint. */
	public void testRPNIWithAllSame() throws Exception {
		IDFA pta = JailTestUtils.loadDotDFA(JailTestUtils.resource(getClass(), "train_labels.dot"));
		ISample<Object> sample = new DefaultSample<Object>(pta);
		
		// prepare
		DefaultInductionAlgoInput input = new DefaultInductionAlgoInput(sample);
		input.getInput().getUserInfoHandler().getVertexAggregator().allsame("class");
		
		testRPNI(sample,null,input);
		//IDFA result = testRPNI(sample,null,input);
		//AutomatonFacade.show(pta,result);
	}

	/** Tests BlueFringe with an allsame constraint. */
	public void testBlueFringeWithAllSame() throws Exception {
		IDFA pta = JailTestUtils.loadDotDFA(JailTestUtils.resource(getClass(), "train_labels.dot"));
		ISample<Object> sample = new DefaultSample<Object>(pta);
		
		// prepare
		DefaultInductionAlgoInput input = new DefaultInductionAlgoInput(sample);
		input.getInput().getUserInfoHandler().getVertexAggregator().allsame("class");
		
		testBlueFringe(sample,null,input);
		//IDFA result = testRPNI(sample,null,input);
		//AutomatonFacade.show(pta,result);
	}

	@Override
	public void testBlueFringeOnTrain() throws Exception {
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
		new LSMIExtensionTest().testRPNIWithAllSame();
	}
	
}
