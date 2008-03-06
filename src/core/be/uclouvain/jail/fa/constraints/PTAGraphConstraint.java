package be.uclouvain.jail.fa.constraints;

import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.impl.AttributeGraphFAInformer;
import be.uclouvain.jail.fa.impl.IGraphFAInformer;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.constraints.AbstractGraphConstraint;
import be.uclouvain.jail.uinfo.IUserInfo;

/**
 * Forces/checks that a graph is a PTA.
 * 
 * <p>This constraint checks the following facts on the graph:</p>
 * <ul>
 *     <li>Graph is connex.</i>
 *     <li>Graph contains an initial state (and only one).</li>
 *     <li>accepting, non accepting, error, initial flags are installed
 *         on each vertex (according to the informer).</li>
 *     <li>For each state, no two outgoing edges are labeled with the same
 *         letter.</p>
 *     <li>Each state contains one and only one incoming edge, excepted the
 *         initial state which has no incoming edge.</li>  
 * </ul>
 * 
 * <p>This constraint must be constructed with an {@link IGraphFAInformer}
 * which provides necessary informations about state flags and edge letters.<p> 
 * 
 * <p>Please note that this constraint does not implement install() and 
 * uninstall() for now ; it is only intended to be used as a checking 
 * predicate.</p> 
 * 
 * @author blambeau
 */
public class PTAGraphConstraint extends AbstractGraphConstraint {

	/** Informer to use. */
	private IGraphFAInformer informer;
	
	/** Creates a constraint with a user-defined informer. */
	public PTAGraphConstraint(IGraphFAInformer informer) {
		this.informer = informer;
	}

	/** Creates a constraint with a default informer. */
	public PTAGraphConstraint() {
		this(new AttributeGraphFAInformer());
	}
	
	/** Checks that the constraint is respected by a graph. */
	public boolean isRespectedBy(IDirectedGraph graph) {
		// check that its at least a DFA
		boolean dfa = new DFAGraphConstraint().isRespectedBy(graph);
		if (!dfa) { return false; }

		// check states
		for (Object vertex: graph.getVertices()) {
			int inCount = graph.getIncomingEdges(vertex).size();
			IUserInfo info = graph.getVertexInfo(vertex);
			if (informer.isInitial(info)) {
				if (inCount != 0) { 
					return false; 
				}
			} else if (inCount != 1) {
				return false;
			}
		}
		
		return true;
	}
	
	/** Checks that the constraint is respected by a DFA. */
	public boolean isRespectedBy(IDFA dfa) {
		return isRespectedBy(dfa.getGraph());
	}

}
