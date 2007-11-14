package be.uclouvain.jail.algo.fa.minimize;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.graph.IDirectedGraph;

/**
 * Provides an implementation of the standard DFA minimization algorithm.
 * (Hopcroft 1971).
 * 
 * @author LAMBEAU Bernard
 */
public class DFAMinimizerAlgo {

	/** Non minimal source DFA. */
	private IDFA dfa;

	/** Underlying graph. */
	private IDirectedGraph graph;
	
	/** The block structure. */
	private IBlockStructure<Object> blocks;

	/** The list of blocks to explore. */
	private TreeSet<Integer> toExplore;

	/** Creates an algorithm instance. */
	public DFAMinimizerAlgo() {
	}

	/** Computes the reverse delta function on a set of states. */
	private Map<Object,Set<Object>> reverseDelta(Set<Object> states) {
		Map<Object,Set<Object>> delta = new HashMap<Object,Set<Object>>();
		
		// for each state
		for (Object state: states) {
			// for each incoming edge
			for (Object edge: graph.getIncomingEdges(state)) {
				// take letter
				Object letter = dfa.getEdgeLetter(edge);
				
				// retrieve set of states, or create it
				Set<Object> sources = delta.get(letter);
				if (sources == null) {
					sources = new HashSet<Object>();
					delta.put(letter, sources);
				}
				
				// add source state of the edge
				sources.add(graph.getEdgeSource(edge));
			}
		}
		return delta;
	}

	/** Checks if a block can be refined, that is if block contains somes states
	 * of reachable but not all. */
	private boolean canBeRefined(Set<Object> block, Set<Object> reachable) {
		// avoid trying to refine one state only blocks
		if (block.size() <= 1) {
			return false;
		}
		
		// block can be refined if it contains at least one state
		// of reachable
		int count = 0;
		for (Object reach: block) {
			if (reachable.contains(reach)) {
				count++;
			}
		}
		return count != 0 && count != block.size();
	}
	
	/** Debugs a block. */
	@SuppressWarnings("unused")
	private String debugBlock(Set<Object> block) {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		int i=0;
		for (Object state: block) {
			if (i++ != 0) {sb.append(",");}
			sb.append(graph.getVerticesTotalOrder().indexOf(state));
		}
		sb.append("}");
		return sb.toString();
	}
	
	/** Executes algorithm. */
	public void execute(IDFAMinimizerInput input, IDFAMinimizerResult result) {
		this.dfa = input.getDFA();
		this.graph = dfa.getGraph();
		this.toExplore = new TreeSet<Integer>();
		this.blocks = result.started(input);
		
		/* adds the initial blocks to toExplore */
		int size = blocks.size();
		for (int i=0; i<size; i++) {
			toExplore.add(i);
		}

		/* while there is a block to explore */
		while (!toExplore.isEmpty()) {

			// take first block and remove it
			Integer lIndex = toExplore.first();
			toExplore.remove(lIndex);
			
			// compute reverse delta
			Set<Object> block = blocks.getBlock(lIndex);
			Map<Object,Set<Object>> delta = reverseDelta(block);
			
			//System.out.println("On iBlock: "+ debugBlock(block));
			
			/* for each alphabet letter */
			for (Object letter: delta.keySet()) {

				// retrieve closure for this letter
				Set<Object> reachable = delta.get(letter);
				
				//System.out.println(debugBlock(block) + "<-" + letter + "<-" + debugBlock(reachable));
				
				/* iterates each block */
				for (int j=0; j<blocks.size(); j++) {
					Set<Object> jBlock = blocks.getBlock(j);
					
					//System.out.print("On jBlock: "+ debugBlock(jBlock));
					
					/* do not retain the block if it contains all states of b,
					 * or not at all */
					if (!canBeRefined(jBlock, reachable)) {
						//System.out.println(" ... not refinable.");
						continue;
					} else {
						//System.out.println(" ... refinable!");
					}

					/* separate jBlock into two sub blocks : states that are reachable,
					 * other that are not. */
					Set<Object> unreachable = new HashSet<Object>();
					for (Object state: jBlock) {
						if (!reachable.contains(state)) {
							unreachable.add(state);
						}
					}
					jBlock.removeAll(unreachable);

					// just a check
					if (jBlock.size()==0 || unreachable.size()==0) {
						System.out.println(reachable.containsAll(jBlock));
						throw new AssertionError("Refinements leads to non empty sets.");
					}

					// let result now that the jBlock has been refined
					int k = result.refined(jBlock,unreachable);
					
					/* update toExplore */
					if (toExplore.contains(j)) {
						toExplore.add(k);
					} else if (jBlock.size()<unreachable.size()) {
						toExplore.add(j);
					} else {
						toExplore.add(k);
					}

				}

			}
		}
	}

}
