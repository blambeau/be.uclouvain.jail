package be.uclouvain.jail.algo.fa.walk;

import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.graph.IDirectedGraph;

/**
 * Provides a walker on a PTA.
 * 
 * @author blambeau
 */
public class PTADepthFirstWalker {

	/** Visitor interface. */
	public static interface IVisitor {

		/** Fired when a state is entered. */
		boolean entering(Object state, Object edge);

		/** Fired when a state is leaved. */
		boolean leaving(Object state, Object edge, boolean recurse);
		
	}
	
	/** PTA. */
	private IDFA pta;

	/** Directed graph. */
	private IDirectedGraph ptag;
	
	/** Creates a walker instance. */
	public PTADepthFirstWalker(IDFA pta) {
		this.pta = pta;
		this.ptag = pta.getGraph();
	}

	/** Executes using a visitor. */
	public void execute(IVisitor v) {
		Object initial = pta.getInitialState();
		walk(initial,null,v);
	}

	/** Walks from a state. */
	public boolean walk(Object state, Object edge, IVisitor v) {
		boolean recurse = v.entering(state,edge);
		if (recurse) {
			for (Object outEdge: ptag.getOutgoingEdges(state)) {
				Object target = ptag.getEdgeTarget(outEdge);
				if (walk(target,outEdge,v)) {
					return true;
				}
			}
		}
		return v.leaving(state,edge,recurse);
	}
	
}
