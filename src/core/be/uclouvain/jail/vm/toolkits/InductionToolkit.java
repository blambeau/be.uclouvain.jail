package be.uclouvain.jail.vm.toolkits;

import net.chefbe.javautils.adapt.IAdapter;
import be.uclouvain.jail.algo.induct.internal.BlueFringeAlgo;
import be.uclouvain.jail.algo.induct.internal.DefaultInductionAlgoInput;
import be.uclouvain.jail.algo.induct.internal.IInductionAlgoInput;
import be.uclouvain.jail.algo.induct.internal.RPNIAlgo;
import be.uclouvain.jail.algo.induct.open.IMembershipQueryTester;
import be.uclouvain.jail.algo.induct.open.IOracle;
import be.uclouvain.jail.algo.induct.sample.IExtensibleSample;
import be.uclouvain.jail.algo.induct.sample.ISample;
import be.uclouvain.jail.algo.induct.sample.MCASample;
import be.uclouvain.jail.algo.induct.utils.AutoQueryTester;
import be.uclouvain.jail.algo.induct.utils.BLambeauOracle;
import be.uclouvain.jail.algo.induct.utils.ConsoleQueryTester;
import be.uclouvain.jail.dialect.jis.JISGraphDialect;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.INFA;
import be.uclouvain.jail.fa.constraints.MCAGraphConstraint;
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
				INFA nfa = (INFA) who;
				if (new MCAGraphConstraint().isRespectedBy(nfa.getGraph())) {
					return new MCASample(nfa);
				} else {
					return null;
				}
			}
		};
		vm.registerAdaptation(INFA.class, ISample.class, toSample);
		vm.registerAdaptation(INFA.class, IExtensibleSample.class, toSample);		
	}
	
	/** Executes RPNI. */
	public IDFA rpni(IDFA dfa, JailVMOptions opt) throws JailVMException {
		IInductionAlgoInput input = new DefaultInductionAlgoInput(dfa);
		input.setOptions(opt);
		return new RPNIAlgo().execute(input);
	}
	
	/** Executes RPNI. */
	public IDFA bluefringe(IDFA dfa, JailVMOptions opt) throws JailVMException {
		DefaultInductionAlgoInput input = new DefaultInductionAlgoInput(dfa);
		input.setOptions(opt);
		return new BlueFringeAlgo().execute(input);
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