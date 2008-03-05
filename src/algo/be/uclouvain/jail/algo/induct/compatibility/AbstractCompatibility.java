package be.uclouvain.jail.algo.induct.compatibility;

import be.uclouvain.jail.algo.induct.internal.InductionAlgo;
import be.uclouvain.jail.graph.IDirectedGraph;

/**
 * Provides a base to implement {@link ICompatibility}.
 * 
 * @author blambeau
 */
public abstract class AbstractCompatibility implements ICompatibility {

	/** Graph underlying PTA. */
	private IDirectedGraph ptag;
	
	/** Returns a state attribute. */
	public Object getUserInfoAttr(Object s, String attr) {
		return ptag.getVertexInfo(s).getAttribute(attr);
	}
	
	/** Initializes the layer. */
	public void initialize(InductionAlgo algo) {
		this.ptag = algo.getPTA().getGraph();
	}

	/** Returns true by default. */
	public boolean isCompatible(Object s, Object t) {
		return true;
	}

	/** Returns false. */
	public boolean isExtensible() {
		return false;
	}

	/** Throws an UnsupportedOperationException. */
	public void markAsIncompatible(Object s, Object t) {
		throw new UnsupportedOperationException("This compatibility layer is not extensible.");
	}

}
