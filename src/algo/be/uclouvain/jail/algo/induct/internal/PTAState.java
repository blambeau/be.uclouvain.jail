package be.uclouvain.jail.algo.induct.internal;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import be.uclouvain.jail.algo.commons.Avoid;
import be.uclouvain.jail.algo.induct.open.IWalker;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.IFATrace;
import be.uclouvain.jail.fa.utils.DefaultFATrace;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.utils.DefaultDirectedGraphPath;
import be.uclouvain.jail.uinfo.IUserInfo;

/** Provides a state of the decorated PTA. */
public class PTAState {

	/** Mapping. */
	private Map<Object, Object> mapping = new HashMap<Object,Object>();

	/** Keeps a listener information. */
	public void keep(Object key, Object value) {
		this.mapping.put(key, value);
	}

	/** Retrieves a listener information. */
	public Object retrieve(Object key) {
		return this.mapping.get(key);
	}
	/** Keeps a listener information. */
	public Object forget(Object key) {
		return this.mapping.remove(key);
	}
	
	/** Induction algorithm. */
	private InductionAlgo algo;

	/** Attached values. */
	private IUserInfo values;

	/** Parent edge. */
	private PTAEdge parent;
	
	/** Associated state in the real PTA. */
	private Object representor;

	/** Delta function. */
	private Map<Object,PTAEdge> delta;

	/** Creates a PTAState. */
	@SuppressWarnings("unchecked")
	public PTAState(InductionAlgo algo, final IDFA pta, Object representor, PTAEdge parent) {
		this.algo = algo;
		IDirectedGraph g = pta.getGraph();

		// initialize me
		this.representor = representor;
		this.parent = parent;
		values = pta.getGraph().getVertexInfo(representor);

		// prepare collection of outgoing edges
		delta = new TreeMap<Object,PTAEdge>(pta.getAlphabet());
		for (Object edge : g.getOutgoingEdges(representor)) {
			Object letter = pta.getEdgeLetter(edge);
			delta.put(letter, new PTAEdge(algo, pta, edge, this));
		}
	}

	/** Returns induction algo. */
	public InductionAlgo getRunningAlgo() {
		return algo;
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

	/** Returns out edges. */
	public Iterable<PTAEdge> outEdges() {
		return delta.values();
	}
	
	/** Returns attached values. */
	public IUserInfo getUserInfo() {
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
	protected Object consolidate(Simulation simu) {
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
	protected void merge(Simulation simu, Object tkState) throws Avoid {
		assert (tkState != null) : "Not null tkState.";
		assert (tkState instanceof PTAState == false) : "Real tkState.";
		
		IDFA dfa = algo.getDFA();
		Fringe fringe = algo.getFringe();
		
		// merge me with the target kernel state (KStateMerge)
		simu.merge(this, tkState);
		
		// check outgoing transitions
		for (Object letter: delta.keySet()) {
			PTAEdge ptaEdge = (PTAEdge) delta.get(letter);
			
			// 1) check real edge on the DFA
			Object tkEdge = dfa.getOutgoingEdge(tkState, letter);
			
			if (tkEdge != null) {
				// merge edge when found, by delegation
				ptaEdge.merge(simu, tkEdge);
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
					ptaEdge.merge(simu, fEdge);
				} else {
					// no edge at all, mark as a KStateGain
					simu.gain(tkState, ptaEdge);
				}
			}
		}
	}

	/** Prepate merge with another (white) state. */
	protected void merge(Simulation work, PTAState oState) throws Avoid {
		// merge me with the target state (OStateMerge)
		work.merge(this, oState);
		
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
				edge.merge(work, oEdge);
			} else {
				// no edge at all, mark as a OStateGain
				work.gain(oState, edge);
			}
		}

	}

	/** Returns the short prefix of the state. */
	public <T> IFATrace<T> getShortPrefix() {
		IDFA pta = getRunningAlgo().getPTA();
		IDirectedGraph graph = pta.getGraph();

		// I'm the root ?
		if (parent == null) {
			// YES. create an empty path
			DefaultDirectedGraphPath path = new DefaultDirectedGraphPath(graph,representor());
			return new DefaultFATrace<T>(pta,path); 
		} else {
			// NO. build edges and create path
			LinkedList<Object> list = new LinkedList<Object>();
			getShortPrefix(list);
			DefaultDirectedGraphPath path = new DefaultDirectedGraphPath(graph,list);
			return new DefaultFATrace<T>(pta,path);
		}
	}
	
	/** Participates to short prefix construction. */
	protected void getShortPrefix(LinkedList<Object> edges) {
		if (parent != null) {
			parent.getShortPrefix(edges);
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
