package be.uclouvain.jail.algo.fa.minimize;

import be.uclouvain.jail.algo.fa.merge.DefaultDFAMergingResult;
import be.uclouvain.jail.algo.graph.merge.GraphMergingAlgo;
import be.uclouvain.jail.algo.graph.merge.IGraphMergingInput;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.impl.GraphDFA;
import be.uclouvain.jail.graph.IDirectedGraph;

/**
 * Provides a user-friendly API on top of the minimizer algorithm.
 * 
 * @author blambeau
 */
public class DFAMinimizer {

	/** Algorithm input. */ 
	private IDFAMinimizerInput input;
	
	/** Algorithm result. */ 
	private IDFAMinimizerResult result;
	
	/** Algorithm has been executed? */
	private boolean executed = false;
	
	/** Creates an algorithm instance. */
	public DFAMinimizer(IDFA dfa) {
		this.input = new DefaultDFAMinimizerInput(dfa);
		this.result = new DefaultDFAMinimizerResult();
	}
	
	/** Returns computed partition. */
	public IBlockStructure<Object> getStatePartition() {
		if (!executed) {execute();}
		return result.getStatePartition();
	}
	
	/** Computed the minimal DFA. */
	public IDFA getMinimalDFA() {
		IDFA dfa = new GraphDFA(input.getDFA().getAlphabet());
		flushMinimalDFA(dfa);
		return dfa;
	}
	
	/** Flushes minimization inside a existing dfa. */
	public void flushMinimalDFA(IDFA dfa) {
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
			
		}, new DefaultDFAMergingResult(dfa));
	}
	
	/** Executes the algorithm. */
	private void execute() {
		new DFAMinimizerAlgo().execute(input,result);
		executed = true;
	}
	
}
