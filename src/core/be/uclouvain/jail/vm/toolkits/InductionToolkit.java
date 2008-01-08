package be.uclouvain.jail.vm.toolkits;

import be.uclouvain.jail.algo.induct.internal.BlueFringeAlgo;
import be.uclouvain.jail.algo.induct.internal.DefaultInductionAlgoInput;
import be.uclouvain.jail.algo.induct.internal.IInductionAlgoInput;
import be.uclouvain.jail.algo.induct.internal.RPNIAlgo;
import be.uclouvain.jail.dialect.jis.JISGraphDialect;
import be.uclouvain.jail.fa.IDFA;
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
	}
	
	/** Executes RPNI. */
	public IDFA rpni(IDFA dfa, JailVMOptions opt) throws JailVMException {
		IInductionAlgoInput input = new DefaultInductionAlgoInput(dfa);
		return new RPNIAlgo().execute(input);
	}
	
	/** Executes RPNI. */
	public IDFA bluefringe(IDFA dfa, JailVMOptions opt) throws JailVMException {
		DefaultInductionAlgoInput input = new DefaultInductionAlgoInput(dfa);
		
		if (opt.hasOption("treshold")) {
			int t = opt.getOptionValue("treshold", Integer.class, 0);
			input.setConsolidationThreshold(t);
		}
		
		return new BlueFringeAlgo().execute(input);
	}
	
	/** Provides adaptations. */
	public Object adapt(Object who, Class type) {
		return null;
	}
	
}