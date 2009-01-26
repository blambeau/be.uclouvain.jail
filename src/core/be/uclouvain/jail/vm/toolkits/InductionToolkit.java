package be.uclouvain.jail.vm.toolkits;

import net.chefbe.javautils.adapt.IAdapter;
import be.uclouvain.jail.algo.fa.rand.DefaultRandomStringsInput;
import be.uclouvain.jail.algo.fa.rand.DefaultRandomStringsResult;
import be.uclouvain.jail.algo.fa.rand.IRandomStringsResult;
import be.uclouvain.jail.algo.fa.rand.RandomStringsAlgo;
import be.uclouvain.jail.algo.fa.walk.DFARandomWalkInput;
import be.uclouvain.jail.algo.fa.walk.DFARandomWalkResult;
import be.uclouvain.jail.algo.graph.walk.RandomWalkAlgo;
import be.uclouvain.jail.algo.induct.internal.BlueFringeAlgo;
import be.uclouvain.jail.algo.induct.internal.DefaultInductionAlgoInput;
import be.uclouvain.jail.algo.induct.internal.IInductionAlgoInput;
import be.uclouvain.jail.algo.induct.internal.RPNIAlgo;
import be.uclouvain.jail.algo.induct.oracle.AutoQueryTester;
import be.uclouvain.jail.algo.induct.oracle.BLambeauOracle;
import be.uclouvain.jail.algo.induct.oracle.ConsoleQueryTester;
import be.uclouvain.jail.algo.induct.oracle.IMembershipQueryTester;
import be.uclouvain.jail.algo.induct.oracle.IOracle;
import be.uclouvain.jail.algo.lsm.LSMAlgo;
import be.uclouvain.jail.algo.lsm.LSMAlgoInput;
import be.uclouvain.jail.algo.lsm.LSMAlgoResult;
import be.uclouvain.jail.dialect.jis.JISGraphDialect;
import be.uclouvain.jail.fa.IAlphabet;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.ISample;
import be.uclouvain.jail.fa.IString;
import be.uclouvain.jail.fa.IWalkInfo;
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
	
	/** Randomize strings. */
	/*
	public IRandomStringsResult abadingo(JailVMOptions options, JailVM vm) throws JailVMException {
		// create input and set options
		AbadingoRandomStringsInput<Object> input = new AbadingoRandomStringsInput<Object>(); 
		input.setOptions(options);

		// get alphabet, create sample 
		IAlphabet<Object> alphabet = input.getAlphabet();
		ISample<Object> sample = new DefaultSample<Object>(alphabet);
		
		// create algo result and set options
		DefaultRandomStringsResult<Object> result = new DefaultRandomStringsResult<Object>(sample);
		result.setOptions(options);
		
		// execute algorithm
		new RandomStringsAlgo().execute(input,result);
		return result;
	}
	*/

	/** Scores a dfa using a sample. */
	public IDFA score(IDFA dfa, ISample<?> sample) throws JailVMException {
		long size = 0;
		long ok = 0;
		for (IString<?> s: sample) {
			size++;
			IWalkInfo<?> info = s.walk(dfa);
			boolean accepted = info.isFullyIncluded()
			                && info.getIncludedPart().isAccepted();
			boolean positive = s.isPositive();
			if (accepted == positive) {
				ok++;
			}
		}
		System.out.println("Score: " + ((double)ok)/((double)size));
		return dfa;
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
	
	/** Executes ASM. */
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
	/*
	public IDFA classify(IDFA pta, JailVMOptions opt) throws JailVMException {
		ForwardLabelProcessor.Input input = new ForwardLabelProcessor.Input(pta);
		input.setOptions(opt);
		new ForwardLabelProcessor().process(input);
		return pta;
	}
	*/
	
	/** Generates a random DFA. */
	public ISample randsample(IDFA dfa, JailVMOptions options, JailVM vm) throws JailVMException {
		DFARandomWalkInput input = new DFARandomWalkInput(dfa);
		DFARandomWalkResult result = new DFARandomWalkResult();
		input.setOptions(options);
		result.setOptions(options);
		new RandomWalkAlgo().execute(input,result);
		ISample sample = (ISample) result.adapt(ISample.class);
		return sample;
	}
	
	/** Randomize strings. */
	public IRandomStringsResult randstrings(JailVMOptions options, JailVM vm) throws JailVMException {
		// create input and set options
		DefaultRandomStringsInput<Object> input = new DefaultRandomStringsInput<Object>(); 
		input.setOptions(options);

		// get alphabet, create sample 
		IAlphabet<Object> alphabet = input.getAlphabet();
		ISample<Object> sample = new DefaultSample<Object>(alphabet);
		
		// create algo result and set options
		DefaultRandomStringsResult<Object> result = new DefaultRandomStringsResult<Object>(sample);
		result.setOptions(options);
		
		// execute algorithm
		new RandomStringsAlgo().execute(input,result);
		return result;
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