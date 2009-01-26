package be.uclouvain.jail.fa.constraints;

import java.util.HashSet;
import java.util.Set;

import be.uclouvain.jail.algo.graph.connex.ConnexGraphConstraint;
import be.uclouvain.jail.fa.impl.AttributeGraphFAInformer;
import be.uclouvain.jail.fa.impl.IGraphFAInformer;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.constraints.AbstractGraphConstraint;
import be.uclouvain.jail.uinfo.IUserInfo;

/**
 * Forces/checks that a graph is a DFA.
 * 
 * <p>This constraint checks the following facts on the graph:</p>
 * <ul>
 *     <li>Graph is connex.</i>
 *     <li>Graph contains an initial state (and only one).</li>
 *     <li>accepting, non accepting, error, initial flags are installed
 *         on each vertex (according to the informer).</li>
 *     <li>For each state, no two outgoing edges are labeled with the same
 *         letter.</p>  
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
public class DFAGraphConstraint extends AbstractGraphConstraint {

	/** Informer to use. */
	protected IGraphFAInformer informer;
	
	/** Creates a constraint with a user-defined informer. */
	public DFAGraphConstraint(IGraphFAInformer informer) {
		this.informer = informer;
	}

	/** Creates a constraint with a default informer. */
	public DFAGraphConstraint() {
		this(new AttributeGraphFAInformer());
	}
	
	/** Checks that the constraint is respected by a graph. */
	public boolean isRespectedBy(IDirectedGraph graph) {
		// check that DFA is connex
		boolean connex = new ConnexGraphConstraint().isRespectedBy(graph);
		if (!connex) { return false; }

		// check states
		int initCount = 0;
		Set<Object> seen = new HashSet<Object>();
		for (Object vertex: graph.getVertices()) {
			IUserInfo info = graph.getVertexInfo(vertex);
			
			// only one init state
			try {
				if (informer.isInitial(info)) {
					initCount++;
					if (initCount > 1) {
						return false;
					}
				}
			
				// let informer check that it find what it needs
				informer.getStateKind(info);
			} catch (IllegalStateException s) {
				return false;
			}
			
			// check outgoing edges
			seen.clear();
			for (Object edge: graph.getOutgoingEdges(vertex)) {
				Object letter = informer.edgeLetter(graph.getEdgeInfo(edge));
				if (seen.contains(letter)) {
					return false;
				}
				seen.add(letter);
			}
		}
		
		return initCount==1;
	}

}
