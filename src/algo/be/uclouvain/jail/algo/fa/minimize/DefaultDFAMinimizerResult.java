package be.uclouvain.jail.algo.fa.minimize;

import java.util.HashSet;
import java.util.Set;

import be.uclouvain.jail.algo.fa.merge.DefaultDFAMergingResult;
import be.uclouvain.jail.algo.graph.merge.GraphMergingAlgo;
import be.uclouvain.jail.algo.graph.merge.IGraphMergingInput;
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
	
	/** The block structure. */
	private SetBlockStructure<Object> blocks;
	
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

	/** 
	 * Algorithm started event.
	 * 
	 * <p>Initial partition is the classical one: accepting, non
	 * accepting and error states.</p>
	 */
	@SuppressWarnings("unchecked")
	public IBlockStructure<Object> started(IDFAMinimizerInput input) {
		this.input = input;
		IDFA dfa = input.getDFA();
		
		Set[] blocks = new Set[]{new HashSet(), new HashSet(), new HashSet()};
		for (Object state: dfa.getGraph().getVertices()) {
			if (dfa.isError(state)) {
				blocks[2].add(state);
			} else if (dfa.isAccepting(state)) {
				blocks[0].add(state);
			} else {
				blocks[1].add(state);
			}
		}
		
		this.blocks = new SetBlockStructure<Object>(blocks);
		return this.blocks;
	}

	/** Updates the structure to reflect the change. */
	public int refined(Set<Object> block, Set<Object> unreachable) {
		int count = blocks.refine(unreachable);
		//System.out.println("Blocks refined " + blocks.toString(input.getDFA().getGraph().getVerticesTotalOrder()));
		return count;
	}
	
	/** Returns the computed partition. */
	public SetBlockStructure<Object> getStatePartition() {
		return blocks;
	}
	
	/** Returns equivalent minimal DFA. */
	public IDFA getMinimalDFA() {
		IDFA dfa = new GraphDFA(input.getDFA().getAlphabet());
		merging.setDFA(dfa);
		final IBlockStructure<Object> partition = getStatePartition();
		new GraphMergingAlgo().execute(new IGraphMergingInput() {

			/** Returns the graph. */
			public IDirectedGraph getGraph() {
				return input.getDFA().getGraph();
			}

			/** Returns the partition. */
			public IBlockStructure<Object> getVertexPartition() {
				return partition;
			}
			
		}, merging);
		return dfa;
	}

}
