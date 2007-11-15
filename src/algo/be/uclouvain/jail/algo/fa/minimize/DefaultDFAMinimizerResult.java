package be.uclouvain.jail.algo.fa.minimize;

import be.uclouvain.jail.adapt.AdaptUtils;
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
		for (IGraphMemberGroup group: partition) {
			if (group.size()==0) {
				throw new AssertionError("Does not generate empty groups.");
			}
		}
		this.partition = partition;
	}

	/** Returns equivalent minimal DFA. */
	private IDFA getMinimalDFA() {
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
		return dfa;
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
