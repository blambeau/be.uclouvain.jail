package be.uclouvain.jail.algo.fa.minimize;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import be.uclouvain.jail.algo.fa.utils.FAStateGroup;
import be.uclouvain.jail.algo.graph.utils.GraphPartition;
import be.uclouvain.jail.algo.graph.utils.IGraphMemberGroup;
import be.uclouvain.jail.algo.graph.utils.IGraphPartitionner;
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

	/** The block structure. */
	private GraphPartition blocks;

	/** The list of blocks to explore. */
	private Set<FAStateGroup> toExplore;

	/** Creates an algorithm instance. */
	public DFAMinimizerAlgo() {
	}

	/** Checks if a block can be refined, that is if block contains somes states
	 * of reachable but not all. */
	private boolean canBeRefined(FAStateGroup block, FAStateGroup reachable) {
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
	
	/** Executes algorithm. */
	public void execute(IDFAMinimizerInput input, IDFAMinimizerResult result) {
		// initialization
		this.dfa = input.getDFA();
		IDirectedGraph graph = dfa.getGraph();
		this.toExplore = new HashSet<FAStateGroup>();
		this.blocks = new GraphPartition(graph,graph.getVertices());
		this.blocks.refine(input.getInitPartitionner());
		
		/* adds the initial blocks to toExplore */
		for (IGraphMemberGroup group: blocks) {
			toExplore.add(new FAStateGroup(dfa,group));
		}
		result.started(input);
		
		/* while there is a block to explore */
		while (!toExplore.isEmpty()) {

			// take first block and remove it
			FAStateGroup block = toExplore.iterator().next();
			toExplore.remove(block);
			
			/* for each alphabet letter */
			Iterator<Object> letters = block.getIncomingLetters();
			while (letters.hasNext()) {
				Object letter = letters.next();

				// retrieve closure for this letter
				final FAStateGroup reachable = block.reverseDelta(letter).getSourceStateGroup();
				
				/* iterates each block */
				int size = blocks.size();
				for (int j=0; j<size; j++) {
					FAStateGroup jBlock = new FAStateGroup(dfa,blocks.getGroup(j));

					/* do not retain the block if it contains all states of b,
					 * or not at all */
					if (!canBeRefined(jBlock, reachable)) {
						continue;
					}

					/* separate jBlock into two sub blocks : states that are reachable,
					 * other that are not. */
					blocks.refine(j, new IGraphPartitionner<Object>() {
						public Object getPartitionOf(Object value) {
							if (reachable.contains(value)) {
								return 1;
							} else {
								return 0;
							}
						}
					});

					// retrieve the two groups
					FAStateGroup jRefined = new FAStateGroup(dfa,blocks.getGroup(j));
					FAStateGroup unreachable = new FAStateGroup(dfa,blocks.getGroup(blocks.size()-1));

					// just a check
					if (jRefined.size()==0 || unreachable.size()==0) {
						throw new AssertionError("Refinements leads to non empty sets.");
					}

					/* update toExplore */
					if (toExplore.contains(jRefined)) {
						toExplore.add(unreachable);
					} else if (jRefined.size()<unreachable.size()) {
						toExplore.add(jRefined);
					} else {
						toExplore.add(unreachable);
					}

				}

			}
		}

		result.ended(blocks);
	}

}
