package be.uclouvain.jail.algo.fa.minimize;

import java.util.HashSet;
import java.util.Set;

import net.chefbe.javautils.adapt.AdaptUtils;
import be.uclouvain.jail.algo.fa.merge.DefaultDFAMergingResult;
import be.uclouvain.jail.algo.fa.utils.FAEdgeLetterPartitionner;
import be.uclouvain.jail.algo.graph.merge.GraphMergingAlgo;
import be.uclouvain.jail.algo.graph.merge.IGraphMergingInput;
import be.uclouvain.jail.algo.graph.utils.IGraphMemberGroup;
import be.uclouvain.jail.algo.graph.utils.IGraphPartition;
import be.uclouvain.jail.algo.graph.utils.IGraphPartitionner;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.impl.GraphDFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.uinfo.UserInfoAggregator;

/**
 * Provides a default implementation of {@link IDFAMinimizerResult}.
 * 
 * @author blambeau
 */
public class DefaultDFAMinimizerResult implements IDFAMinimizerResult {

	/** Input. */
	private IDFAMinimizerInput input;
	
	/** Resulting partition. */
	private IGraphPartition partition;
	
	/** Merging result. */
	private DefaultDFAMergingResult merging = new DefaultDFAMergingResult();
	
	/** Returns the state aggregator. */
	public UserInfoAggregator getStateAggregator() {
		return merging.getStateAggregator();
	}

	/** Returns the edge aggregator. */
	public UserInfoAggregator getEdgeAggregator() {
		return merging.getEdgeAggregator();
	}

	/** Algorithm started event. */
	public void started(IDFAMinimizerInput input) {
		this.input = input;
	}

	/** Algorithm ended event. */
	public void ended(IGraphPartition partition) {
		assert partition.size()>0: "At least one block.";
		for (IGraphMemberGroup group: partition) {
			if (group.size()==0) {
				throw new AssertionError("Does not generate empty groups.");
			}
		}
		this.partition = partition;
	}

	/** Returns equivalent minimal DFA. */
	public IDFA getMinimalDFA() {
		IDFA dfa = new GraphDFA(input.getDFA().getAlphabet());
		merging.setDFA(dfa);
		new GraphMergingAlgo().execute(new IGraphMergingInput() {

			/** Returns the graph. */
			public IDirectedGraph getGraph() {
				return input.getDFA().getGraph();
			}

			/** Returns edge partitionner. */
			public IGraphPartitionner<Object> getEdgePartitionner() {
				return new FAEdgeLetterPartitionner(input.getDFA());
			}

			/** Returns vertex partitionner. */
			public IGraphPartitionner<Object> getVertexPartitionner() {
				return partition;
			}
			
		}, merging);
		assert (dfa.getGraph().getVerticesTotalOrder().size()>0) : "At least initial state kept.";
		
		if (input.connex()) {
			Set<Object> reachable = new HashSet<Object>();
			IDirectedGraph graph = dfa.getGraph();
			Object init = dfa.getInitialState();
			dfs(graph, init, reachable);
			for (Object vertex: graph.getVerticesTotalOrder().getTotalOrder()) {
				if (!reachable.contains(vertex)) {
					graph.removeVertex(vertex);
				}
			}
		}
		
		return dfa;
	}

	/** Do a depth-first search. */
	private void dfs(IDirectedGraph g, Object vertex, Set<Object> reachable) {
		reachable.add(vertex);
		for (Object edge: g.getOutgoingEdges(vertex)) {
			Object target = g.getEdgeTarget(edge);
			if (!reachable.contains(target)) {
				dfs(g, target, reachable);
			}
		}
	}
	
	/** Adapts to some types. */
	public <T> Object adapt(Class<T> c) {
		if (c.isAssignableFrom(getClass())) {
			return this;
		}
		
		// natural adaptation to a DFA
		if (IDFA.class.equals(c)) {
			return getMinimalDFA();
		}

		// natural adaptation to a partition
		if (IGraphPartition.class.equals(c)) {
			return partition;
		}
		
		return AdaptUtils.externalAdapt(this,c);
	}

}
