package be.uclouvain.jail.algo.induct.extension;

import be.uclouvain.jail.algo.induct.compatibility.PairwiseCompatibility;
import be.uclouvain.jail.algo.induct.compatibility.StateKindCompatibility;
import be.uclouvain.jail.algo.induct.internal.InductionAlgo;
import be.uclouvain.jail.algo.induct.processor.BackPropagateProcessor;

/**
 * Coste & Nicolas back propagation extension.
 * 
 * @author blambeau
 */
public class BackPropagateExtension implements IInductionAlgoExtension {

	/** State kind compatibility. */
	private StateKindCompatibility skc;
	
	/** Pair wise compatibility. */
	private PairwiseCompatibility pc;
	
	/** Installs the extension on the algorithm. */
	public void install(InductionAlgo algo) {
		// create state kind compatibility
		skc = new StateKindCompatibility();
		algo.addCompatibility(skc);
		
		// create pair wise one
		pc = new PairwiseCompatibility();
		algo.addCompatibility(pc);
	}

	/** Initializes the algorithm. */
	public void initialize(InductionAlgo algo) {
		// initializes the two compatibilities layers
		skc.initialize(algo);
		pc.initialize(algo);
		
		// Back propagates incompatibilities
		new BackPropagateProcessor().process(algo);
	}

}
