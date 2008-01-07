package be.uclouvain.jail.vm.toolkits;

import be.uclouvain.jail.dialect.jis.JISGraphDialect;
import be.uclouvain.jail.vm.JailReflectionToolkit;
import be.uclouvain.jail.vm.JailVM;

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
	
	/** Provides adaptations. */
	public Object adapt(Object who, Class type) {
		return null;
	}
	
}