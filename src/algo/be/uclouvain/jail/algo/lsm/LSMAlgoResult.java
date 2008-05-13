package be.uclouvain.jail.algo.lsm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import be.uclouvain.jail.algo.fa.utils.FAEdgeGroup;
import be.uclouvain.jail.algo.fa.utils.FAStateGroup;
import be.uclouvain.jail.algo.graph.utils.UnionFind;
import be.uclouvain.jail.algo.utils.AbstractAlgoResult;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.functions.FAStateKindFunction;
import be.uclouvain.jail.fa.impl.AttributeGraphFAInformer;
import be.uclouvain.jail.fa.impl.GraphDFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.utils.ITotalOrder;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.IUserInfoHandler;
import be.uclouvain.jail.uinfo.UserInfoAggregator;

/**
 * LSM algorithm result.
 */
public class LSMAlgoResult extends AbstractAlgoResult {

	/** State order used. */
	private ITotalOrder<Object> order;
	
	/** Union find. */
	private UnionFind<Object> ufds;
	
	/** Algorithm input. */
	private LSMAlgoInput input;
	
	/** Creates a result instance. */
	public LSMAlgoResult() {
		UserInfoAggregator stateAggregator = getUserInfoHandler().getVertexAggregator();
		stateAggregator.boolOr(AttributeGraphFAInformer.STATE_INITIAL_KEY);
		stateAggregator.stateKind(AttributeGraphFAInformer.STATE_KIND_KEY,FAStateKindFunction.OR,FAStateKindFunction.OR,true);
		
		UserInfoAggregator edgeAggregator = getUserInfoHandler().getEdgeAggregator();
		edgeAggregator.first(AttributeGraphFAInformer.EDGE_LETTER_KEY);
	}

	/** Algorithm started event. */
	public void started(LSMAlgoInput input) {
		this.input = input;
	}
	
	/** Installs the resulting partition. */
	public void setPartition(ITotalOrder<Object> order, UnionFind<Object> ufds) {
		this.order = order;
		this.ufds = ufds;
	}
	
	/** Groups a set of states. */
	private FAStateGroup group(int i) {
		FAStateGroup group = new FAStateGroup(input.getSource());
		group.addStates(ufds.set(i));
		return group;
	}
	
	/** Creates a resulting DFA. */
	public IDFA resultDFA() {
		IUserInfoHandler handler = getUserInfoHandler();
		int size = ufds.size();

		// create target DFA
		IDFA newdfa = new GraphDFA();
		IDirectedGraph g = newdfa.getGraph();
		
		// from block representor to new state
		Map<Integer,Object> states = new HashMap<Integer,Object>();
		
		// create states
		for (int i=0; i<size; i++) {
			if (!ufds.isMaster(i)) {
				continue;
			}
			
			// merge state info
			FAStateGroup group = group(i);
			IUserInfo info = handler.vertexAggregate(group.getUserInfos());
			info.setAttribute("label", i);
			
			// create state and remember it
			Object state = g.createVertex(info);
			states.put(i, state);
		}
		
		// create edges
		for (int i=0; i<size; i++) {
			if (!ufds.isMaster(i)) {
				continue;
			}
			
			// merge state info
			FAStateGroup sources = group(i);
			Object source = states.get(i);
			assert (source != null) : "Source has been created for group " + sources;
			
			Iterator<Object> letters = sources.getOutgoingLetters();
			while (letters.hasNext()) {
				Object letter = letters.next();
				FAEdgeGroup edges = sources.delta(letter);
				
				// merge edge info
				IUserInfo info = handler.edgeAggregate(edges.getUserInfos());
				
				// find targets
				FAStateGroup targets = edges.getTargetStateGroup();
				Object first = targets.iterator().next();
				int index = ufds.findi(order.indexOf(first));
				
				Object target = states.get(index);
				assert (target != null) : "Target has been created for group " + targets;
				
				g.createEdge(source, target, info);
			}
		}
		
		return newdfa;
	}
	
}
