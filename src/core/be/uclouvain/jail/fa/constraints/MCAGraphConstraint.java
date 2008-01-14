package be.uclouvain.jail.fa.constraints;

import be.uclouvain.jail.fa.impl.AttributeGraphFAInformer;
import be.uclouvain.jail.fa.impl.IGraphFAInformer;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.constraints.AbstractGraphConstraint;
import be.uclouvain.jail.uinfo.IUserInfo;

/**
 * Forces/checks that a graph is a MCA.
 * 
 * <p>This constraint checks the following facts on the graph:</p>
 * <ul>
 *     <li>Graph is connex.</i>
 *     <li>Graph contains initial state(s).</li>
 *     <li>accepting, non accepting, error, initial flags are installed
 *         on each vertex (according to the informer).</li>
 *     <li>Each state contains at most one incoming and one outgoing edge.</p>
 *     <li>The graph contains no cycle.</li>  
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
public class MCAGraphConstraint extends AbstractGraphConstraint {

	/** Informer to use. */
	private IGraphFAInformer informer;
	
	/** Creates a constraint with a user-defined informer. */
	public MCAGraphConstraint(IGraphFAInformer informer) {
		this.informer = informer;
	}

	/** Creates a constraint with a default informer. */
	public MCAGraphConstraint() {
		this(new AttributeGraphFAInformer());
	}
	
	/** Checks that the constraint is respected by a graph. */
	public boolean isRespectedBy(IDirectedGraph graph) {
		// check that its at least a FA
		boolean fa = new FAGraphConstraint().isRespectedBy(graph);
		if (!fa) { return false; }

		// check states
		for (Object vertex: graph.getVertices()) {
			IUserInfo info = graph.getVertexInfo(vertex);

			// check incoming edges count
			int inCount = graph.getIncomingEdges(vertex).size();
			if (informer.isInitial(info)) {
				if (inCount != 0) { 
					return false; 
				}
			} else if (inCount != 1) {
				return false;
			}
			
			// check outgoing edges count
			int outCount = graph.getOutgoingEdges(vertex).size();
			if (outCount > 1) {
				return false;
			}
			
		}
		
		return true;
	}

}
