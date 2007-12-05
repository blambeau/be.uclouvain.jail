package be.uclouvain.jail.algo.fa.kernel;

import net.chefbe.javautils.adapt.AdaptUtils;
import net.chefbe.javautils.adapt.IAdaptable;
import be.uclouvain.jail.algo.fa.determinize.NFADeterminizer;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.INFA;
import be.uclouvain.jail.fa.impl.GraphNFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.IDirectedGraphWriter;
import be.uclouvain.jail.graph.utils.DirectedGraphWriter;
import be.uclouvain.jail.uinfo.UserInfoCopier;

/**
 * Default implementation of {@link IDFAKernelExtractorResult}.
 * 
 * @author blambeau
 */
public class DefaultDFAKernelExtractorResult implements IDFAKernelExtractorResult, IAdaptable {

	/** NFA to keep result. */
	private INFA nfa;
	
	/** Writer on the NFA. */
	private DirectedGraphWriter writer;

	/** Creates a result for an existing NFA. */
	public DefaultDFAKernelExtractorResult(INFA nfa) {
		this.nfa = nfa;
		this.writer = new DirectedGraphWriter(nfa.getGraph());
	}

	/** Creates a result for a default NFA. */
	public DefaultDFAKernelExtractorResult() {
		this(new GraphNFA());
	}

	/** Returns the state copier. */
	public UserInfoCopier getStateCopier() {
		return writer.getVertexCopier();
	}

	/** Returns the edge copier. */
	public UserInfoCopier getEdgeCopier() {
		return writer.getEdgeCopier();
	}
	
	/** Returns the writer. */
	public IDirectedGraphWriter getNFAWriter() {
		return writer;
	}

	/** Adapts to a specific type. */
	public <T> Object adapt(Class<T> c) {
		if (c.isAssignableFrom(getClass())) { return this; }
		
		// my adaptations
		if (INFA.class.equals(c)) {
			return nfa;
		} else if (IDFA.class.equals(c)) {
			return new NFADeterminizer(nfa).getResultingDFA();
		} else if (IDirectedGraph.class.equals(c)) {
			return nfa.getGraph();
		}
		
		// external adaptations
		return AdaptUtils.externalAdapt(this,c);
	}

}
