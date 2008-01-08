package be.uclouvain.jail.fa.constraints;

import be.uclouvain.jail.fa.impl.AttributeGraphFAInformer;
import be.uclouvain.jail.fa.impl.IGraphFAInformer;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.constraints.AbstractGraphConstraint;
import be.uclouvain.jail.uinfo.IUserInfo;

/**
 * Forces/checks that a graph is a FA.
 * 
 * <p>This constraint checks the following facts on the graph:</p>
 * <ul>
 *     <li>accepting, non accepting, error, initial flags are installed
 *         on each vertex (according to the informer).</li>
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
public class FAGraphConstraint extends AbstractGraphConstraint {

	/** Informer to use. */
	private IGraphFAInformer informer;
	
	/** Creates a constraint with a user-defined informer. */
	public FAGraphConstraint(IGraphFAInformer informer) {
		this.informer = informer;
	}

	/** Creates a constraint with a default informer. */
	public FAGraphConstraint() {
		this(new AttributeGraphFAInformer());
	}
	
	/** Checks that the constraint is respected by a graph. */
	public boolean isRespectedBy(IDirectedGraph graph) {
		// check states
		for (Object vertex: graph.getVertices()) {
			IUserInfo info = graph.getVertexInfo(vertex);
			try {
				// let informer check that it find what it needs
				informer.isInitial(info);
				informer.getStateKind(info);
			} catch (IllegalStateException s) {
				return false;
			}
		}
		return true;
	}

}
