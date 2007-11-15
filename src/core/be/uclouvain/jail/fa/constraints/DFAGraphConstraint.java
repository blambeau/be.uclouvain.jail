package be.uclouvain.jail.fa.constraints;

import java.util.HashSet;
import java.util.Set;

import be.uclouvain.jail.algo.graph.connex.ConnexGraphConstraint;
import be.uclouvain.jail.fa.impl.AttributeGraphFAInformer;
import be.uclouvain.jail.fa.impl.IGraphFAInformer;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.deco.DirectedGraph;
import be.uclouvain.jail.graph.deco.GraphConstraintViolationException;
import be.uclouvain.jail.graph.deco.IGraphConstraint;
import be.uclouvain.jail.uinfo.IUserInfo;

/**
 * Forces/checks that a graph is a DFA.
 *  
 * @author blambeau
 */
public class DFAGraphConstraint implements IGraphConstraint {

	/** Informer to use. */
	private IGraphFAInformer informer;
	
	/** Creates a constraint with a user-defined informer. */
	public DFAGraphConstraint(IGraphFAInformer informer) {
		this.informer = informer;
	}

	/** Creates a constraint with a default informer. */
	public DFAGraphConstraint() {
		this(new AttributeGraphFAInformer());
	}
	
	/** Installs on a graph. */
	public <T extends IGraphConstraint> T installOn(DirectedGraph graph)
			throws GraphConstraintViolationException {
		throw new UnsupportedOperationException();
	}

	/** Unistall on the graph. */
	public void uninstall() {
		throw new UnsupportedOperationException();
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
				informer.isError(info);
				informer.isAccepting(info);
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
		
		return true;
	}

}
