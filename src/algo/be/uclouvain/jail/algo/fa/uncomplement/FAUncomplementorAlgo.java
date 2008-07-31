package be.uclouvain.jail.algo.fa.uncomplement;

import be.uclouvain.jail.algo.graph.copy.DirectedGraphCopier;
import be.uclouvain.jail.fa.FAStateKind;
import be.uclouvain.jail.fa.IFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.IDirectedGraphWriter;

/**
 * Uncomplements a DFA, removing avoid/error states.
 * 
 * @author blambeau
 */
public class FAUncomplementorAlgo extends DirectedGraphCopier {

	/** Input dfa. */
	private IFA dfa;
	
	/** Creates an algo instance. */
	public FAUncomplementorAlgo() {
	}
	
	/** Execute the uncompletion. */
	public void execute(IFA dfa, IDirectedGraphWriter writer) {
		this.dfa = dfa;
		super.execute(dfa.getGraph(), writer);
	}
	
	/** Overrided if non AVOID. */
	@Override
	protected Object copyVertex(IDirectedGraph graph, IDirectedGraphWriter output, Object vertex) {
		if (!FAStateKind.AVOID.equals(dfa.getStateKind(vertex))) {
			return super.copyVertex(graph, output, vertex);
		} else {
			return null;
		}
	}

	
	
}
