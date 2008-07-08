package be.uclouvain.jail.vm.toolkits;

import net.chefbe.javautils.adapt.IAdapter;
import be.uclouvain.jail.algo.induct.internal.BlueFringeAlgo;
import be.uclouvain.jail.algo.induct.internal.DefaultInductionAlgoInput;
import be.uclouvain.jail.algo.induct.internal.IInductionAlgoInput;
import be.uclouvain.jail.algo.induct.internal.RPNIAlgo;
import be.uclouvain.jail.algo.induct.oracle.AutoQueryTester;
import be.uclouvain.jail.algo.induct.oracle.BLambeauOracle;
import be.uclouvain.jail.algo.induct.oracle.ConsoleQueryTester;
import be.uclouvain.jail.algo.induct.oracle.IMembershipQueryTester;
import be.uclouvain.jail.algo.induct.oracle.IOracle;
import be.uclouvain.jail.algo.induct.processor.ForwardLabelProcessor;
import be.uclouvain.jail.algo.lsm.LSMAlgo;
import be.uclouvain.jail.algo.lsm.LSMAlgoInput;
import be.uclouvain.jail.algo.lsm.LSMAlgoResult;
import be.uclouvain.jail.dialect.jis.JISGraphDialect;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.ISample;
import be.uclouvain.jail.fa.utils.DefaultSample;
import be.uclouvain.jail.vm.JailReflectionToolkit;
import be.uclouvain.jail.vm.JailVM;
import be.uclouvain.jail.vm.JailVMException;
import be.uclouvain.jail.vm.JailVMOptions;

/**
 * Provides induction tools and algorithms.
 * 
 * @author blambeau
 */
public class InductionToolkit extends JailReflectionToolkit {

	/** Installs the toolkit on the virtual machine. */
	public void install(JailVM vm) {
		vm.registerDialectLoader("jis", new JISGraphDialect());
		
		// finds oracle by their name
		vm.registerAdaptation(String.class, IOracle.class, new IAdapter() {
			public Object adapt(Object arg0, Class<?> arg1) {
				if ("blambeau".equals(arg0)) {
					return new BLambeauOracle();
				}
				return null;
			}
		});
		
		// finds query testers by their name
		vm.registerAdaptation(String.class, IMembershipQueryTester.class, new IAdapter() {
			public Object adapt(Object arg0, Class<?> arg1) {
				if ("console".equals(arg0)) {
					return new ConsoleQueryTester();
				}
				return null;
			}
		});
		
		// adapts a DFA as an automatic query tester
		vm.registerAdaptation(IDFA.class, IMembershipQueryTester.class, new IAdapter() {
			public Object adapt(Object arg0, Class<?> arg1) {
				return new AutoQueryTester((IDFA)arg0);
			}
		});
		
		// converts MCA as samples
		IAdapter toSample = new IAdapter() {
			public Object adapt(Object who, Class<?> c) {
				return new DefaultSample((IDFA)who);
			}
		};
		vm.registerAdaptation(IDFA.class, ISample.class, toSample);
	}
	
	/** Executes RPNI. */
	public IDFA rpni(ISample<?> in, JailVMOptions opt) throws JailVMException {
		IInductionAlgoInput input = new DefaultInductionAlgoInput(in);
		input.setOptions(opt);
		return new RPNIAlgo().execute(input);
	}
	
	/** Executes RPNI. */
	public IDFA bluefringe(ISample<?> in, JailVMOptions opt) throws JailVMException {
		DefaultInductionAlgoInput input = new DefaultInductionAlgoInput(in);
		input.setOptions(opt);
		return new BlueFringeAlgo().execute(input);
	}
	
	public IDFA asm(IDFA in, JailVMOptions opt) throws JailVMException {
		// prepare algorithm
		LSMAlgoInput input = new LSMAlgoInput(in);
		LSMAlgoResult result = new LSMAlgoResult();
		result.setOptions(opt);
		
		// execute ASM
		new LSMAlgo().execute(input,result);
		
		// return result
		return result.resultDFA();
	}
	
	/** Classify a PTA. */
	public IDFA classify(IDFA pta, JailVMOptions opt) throws JailVMException {
		ForwardLabelProcessor.Input input = new ForwardLabelProcessor.Input(pta);
		input.setOptions(opt);
		new ForwardLabelProcessor().process(input);
		return pta;
	}
	
	/** Provides adaptations. */
	public Object adapt(Object who, Class type) {
		return null;
	}
	
	/** Returns name of the toolkit. */
	public String toString() {
		return "InductionToolkit";
	}

}