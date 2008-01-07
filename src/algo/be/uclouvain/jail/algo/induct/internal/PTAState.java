package be.uclouvain.jail.algo.induct.internal;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import net.chefbe.javautils.collections.set.SetUtils;
import be.uclouvain.jail.algo.commons.Avoid;
import be.uclouvain.jail.algo.induct.open.IWalker;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.uinfo.IUserInfo;

/** Provides a state of the decorated PTA. */
public class PTAState {

	/** Induction algorithm. */
	private InductionAlgo algo;

	/** Attached values. */
	private IUserInfo values;

	/** Associated states in the real PTA. */
	private Object representor;

	/** Delta function. */
	private Map<Object,PTAEdge> delta;

	@SuppressWarnings("unchecked")
	public PTAState(InductionAlgo algo, final IDFA pta, Object root) {
		this.algo = algo;
		IDirectedGraph g = pta.getGraph();

		// initialize me
		this.representor = root;
		values = pta.getGraph().getVertexInfo(root);

		// prepare collection of outgoing edges
		delta = new TreeMap<Object,PTAEdge>(pta.getAlphabet());
		for (Object edge : g.getOutgoingEdges(root)) {
			Object letter = pta.getEdgeLetter(edge);
			delta.put(letter, new PTAEdge(algo, pta, edge));
		}
	}

	/** Returns representor. */
	public Object representor() {
		return representor;
	}

	/** Returns outgoing letters. */
	public Set<Object> letters() {
		return delta.keySet();
	}

	/** Returns outgoing edge for a letter. */
	public PTAEdge edge(Object letter) {
		return (PTAEdge) delta.get(letter);
	}

	/** Returns attached values. */
	protected IUserInfo getUserInfo() {
		return values;
	}
	
	/** Sets attached values. */
	protected void setUserInfo(IUserInfo values) {
		this.values = values;
	}

	/** Adds an outgoing edge. */
	protected void addOutEdge(PTAEdge edge) {
		Object letter = edge.letter();
		assert (!delta.containsKey(letter)) : "Gain => not in delta.";
		delta.put(letter, edge);
	}

	/** Consolidates the state. */
	protected Object consolidate(InductionAlgo algo) {
		// create state in DFA
		IDFA dfa = algo.getDFA();
		IDirectedGraph dfag = dfa.getGraph();
		Object state = dfag.createVertex(getUserInfo());
		
		// mark representor
		MappingUtils.updSRepresentor(algo, state, representor());
		MappingUtils.updPRepresentor(algo, state, this);
		
		// update fringe
		for (PTAEdge edge : delta.values()) {
			algo.getFringe().add(state, edge);
		}
		
		// return created state
		return state;
	}

	/** Prepare merge with the target kernel state. */
	protected void prepare(InductionAlgo algo, Simulation simu, Object tkState) throws Avoid {
		assert (tkState instanceof PTAState == false) : "Real tkState.";
		
		IDFA dfa = algo.getDFA();
		Fringe fringe = algo.getFringe();
		
		// merge me with the target kernel state (KStateMerge)
		simu.addKStateMerge(this, tkState);
		
		// check outgoing transitions
		for (Object letter: delta.keySet()) {
			PTAEdge ptaEdge = (PTAEdge) delta.get(letter);
			
			// 1) check real edge on the DFA
			Object tkEdge = dfa.getOutgoingEdge(tkState, letter);
			
			if (tkEdge != null) {
				// merge edge when found, by delegation
				ptaEdge.prepare(algo, simu, tkEdge);
			} else {
				// no such edge ...
				
				// 2) check previous gain of the same letter on kernel state
				PTAEdge fEdge = simu.getKStateGain(tkState, letter);
				if (fEdge == null) {
					// 3) check if the letter leads to the fringe
					fEdge = fringe.fringeEdge(tkState, letter);
				}
				
				if (fEdge != null) {
					// edge found, merge it by delegation
					ptaEdge.prepare(algo, simu, fEdge);
				} else {
					// no edge at all, mark as a KStateGain
					simu.addKStateGain(tkState, ptaEdge);
				}
			}
		}

		// Compute victim state gains.
		IDFA pta = algo.getPTA();
		Object ptaTkState = MappingUtils.sRepresentor(algo, tkState);
		Collection<Object> ptaTkLetters = pta.getOutgoingLetters(ptaTkState);
		Set<Object> diff = SetUtils.minus(ptaTkLetters,delta.keySet());
		for (Object letter : diff) {
			simu.addVStateGain(this,pta.getOutgoingEdge(ptaTkState, letter));
		}
	}

	/** Prepate merge with another (white) state. */
	protected void prepare(InductionAlgo algo, Simulation work, PTAState oState) throws Avoid {
		// merge me with the target state (OStateMerge)
		work.addOStateMerge(this, oState);
		
		// check outgoing transitions
		for (Object letter: delta.keySet()) {
			PTAEdge edge = (PTAEdge) delta.get(letter);
			
			// 1) check real edge on target state
			PTAEdge oEdge = (PTAEdge) oState.delta.get(letter);
			if (oEdge == null) {
				// 2) check previous gain
				oEdge = work.getOStateGain(oState, letter);
			}
			
			if (oEdge != null) {
				// edge found, merge it by delegation
				edge.prepare(algo, work, oEdge);
			} else {
				// no edge at all, mark as a OStateGain
				work.addOStateGain(oState, edge);
			}
		}

	}

	/** Accepts a walker. */
	public void accept(IWalker walker) {
		accept(null, walker);
	}

	/** Accepts a walker. */
	protected boolean accept(PTAEdge edge, IWalker walker) {
		boolean visitChildren = walker.walksLeftOf(edge, this);
		if (!visitChildren) {
			return walker.walksRightOf(edge, this, false);
		}
		for (PTAEdge cEdge : delta.values()) {
			boolean end = cEdge.target().accept(cEdge, walker);
			if (end) {
				return true;
			}
		}
		return walker.walksRightOf(edge, this, true);
	}

	/** Returns a string representation. */
	public String toString() {
		IDFA pta = algo.getPTA();
		return Integer.toString(pta.getGraph().getVerticesTotalOrder().indexOf(representor()));
	}

}
