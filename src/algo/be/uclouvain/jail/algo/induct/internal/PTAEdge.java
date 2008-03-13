package be.uclouvain.jail.algo.induct.internal;

import java.util.LinkedList;

import be.uclouvain.jail.algo.commons.Avoid;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.uinfo.IUserInfo;

/** Provides an edge of the decorated PTA. */
public class PTAEdge {

	/** Attached values. */
	private IUserInfo values;

	/** PTA edge. */
	private Object edge;
	
	/** Attached letter. */
	private Object letter;

	/** Source state. */
	private PTAState source;
	
	/** Target state. */
	private PTAState target;

	/** Source kernel state. */ 
	private Object skState;

	/** Creates an edge instance. */
	public PTAEdge(InductionAlgo algo, final IDFA pta, Object edge, PTAState source) {
		this.edge = edge;
		this.source = source;
		letter = pta.getEdgeLetter(edge);
		
		// prepare edges collection
		IDirectedGraph g = pta.getGraph();
		values = g.getEdgeInfo(edge);
		target = new PTAState(algo, pta, g.getEdgeTarget(edge), this);
	}

	/** Returns edge representor. */
	public Object representor() {
		return edge;
	}
	
	/** Returns attached letter. */
	public Object letter() {
		return letter;
	}

	/** Returns source PTA state. */
	public PTAState source() {
		return source;
	}

	/** Returns target PTA state. */
	public PTAState target() {
		return target;
	}

	/** Returns attached values. */
	public IUserInfo getUserInfo() {
		return values;
	}

	/** Sets attached values. */
	protected void setUserInfo(IUserInfo values) {
		this.values = values;
	}

	/** Sets source kernel state (when edge is added to the fringe). */
	protected void setSourceKernelState(Object kState) {
		assert (kState != null) : "Hooked to a non null state.";
		skState = kState;
		return;
	}

	/** Returns source kernel state. */
	public Object getSourceKernelState() {
		return skState;
	}

	/** Consolidates the edge. */
	protected Object consolidate(Simulation simu) {
		assert (skState != null) : "Edge has been hooked.";
		IDFA dfa = simu.getKernelDFA();
		IDirectedGraph dfag = dfa.getGraph();
		
		// consolidate target state
		Object kTarget = simu.consolidate(target);
		
		// add this edge in the target DFA 
		Object edge = dfag.createEdge(skState, kTarget, values);
		
		// return created edge
		return edge;
	}

	/** Prepare merging with a target kernel edge. */
	protected void merge(Simulation simu, Object tkEdge) throws Avoid {
		assert (skState == null) : "Edge has not been hooked.";
		assert (tkEdge instanceof PTAEdge == false) : "Real kernel edge.";
		
		// merge with kernel edge (KEdgeMerge)
		simu.merge(this, tkEdge);
		
		// find target state in the DFA
		IDFA dfa = simu.getKernelDFA();
		Object tkState = dfa.getGraph().getEdgeTarget(tkEdge);
		
		// prepare merge of the PTA target and DFA target
		target.merge(simu, tkState);
	}

	/** Prepare merging with a white PTA edge. */
	protected void merge(Simulation simu, PTAEdge other) throws Avoid {
		assert (skState == null) : "Other edge has not been hooked.";
		
		// merge with other edge
		simu.merge(this, other);
		
		// prepare merge of targets
		PTAState oState = other.target();
		target.merge(simu, oState);
	}

	/** Returns a string representation. */
	public String toString() {
		return (new StringBuilder(":")).append(letter).append("->").append(
				target.toString()).toString();
	}

	/** Participates to short prefix construction. */
	protected void getShortPrefix(LinkedList<Object> edges) {
		edges.addFirst(representor());
		source.getShortPrefix(edges);
	}
	
}
