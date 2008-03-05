package be.uclouvain.jail.algo.induct.processor;

import java.util.ArrayList;
import java.util.List;

import be.uclouvain.jail.algo.induct.internal.InductionAlgo;

/**
 * Provides a collection of processors.
 * 
 * @author blambeau
 */
public class InductionProcessors implements IInductionProcessor {

	/** Processor list. */
	private List<IInductionProcessor> processors;
	
	/** Creates a collection instance. */
	public InductionProcessors() {
		this.processors = new ArrayList<IInductionProcessor>();
	}

	/** Adds a processor in the chain. */
	public void addProcessor(IInductionProcessor p) {
		this.processors.add(p);
	}
	
	/** Delegates to each installed processor. */
	public void process(InductionAlgo algo) {
		for (IInductionProcessor p : processors) {
			p.process(algo);
		}
	}

}
