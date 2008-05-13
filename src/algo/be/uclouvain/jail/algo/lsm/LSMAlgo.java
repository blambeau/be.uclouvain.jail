package be.uclouvain.jail.algo.lsm;

import java.io.IOException;
import java.util.Iterator;
import java.util.TreeSet;

import be.uclouvain.jail.algo.commons.Avoid;
import be.uclouvain.jail.algo.fa.utils.FAEdgeGroup;
import be.uclouvain.jail.algo.fa.utils.FAStateGroup;
import be.uclouvain.jail.algo.fa.utils.FAUtils;
import be.uclouvain.jail.algo.graph.utils.UnionFind;
import be.uclouvain.jail.dialect.dot.JDotty;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.graph.utils.ITotalOrder;

/**
 * Implements LSM.
 * 
 * @author blambeau
 */
public class LSMAlgo {

	/** Algorithm result. */
	private LSMAlgoInput input;
	
	/** Algorithm result. */
	private LSMAlgoResult result;
	
	/** States. */
	private ITotalOrder<Object> states;
	
	/** Union find of the source states. */
	private UnionFind<Object> ufds;

	/** Creates an algo instance. */
	public LSMAlgo() {
	}
	
	/** Initializes the algorithm. */
	public void initialize() {
		IDFA source = input.getSource();
		states = new TotalOrderCreator().compute(source);
		this.ufds = new UnionFind<Object>(states);
	}
	
	/** Groups a set of states. */
	private FAStateGroup group(int i) {
		FAStateGroup group = new FAStateGroup(input.getSource());
		group.addStates(ufds.set(i));
		return group;
	}
	
	/** Merges a whole state group. */
	private void merge(FAStateGroup group) {
		if (group.size() == 1) {
			return;
		}
		
		// find representors of the states
		TreeSet<Integer> blocks = new TreeSet<Integer>();
		for (Object state: group) {
			blocks.add(ufds.findi(states.indexOf(state)));
		}

		// merge states of the block
		merge(blocks);
	}
	
	/** Merge some states. */
	private void merge(TreeSet<Integer> states) {
		int master = states.first();
		for (Integer victim: states) {
			// avoid merging master with itself
			if (victim == master) {
				continue;
			}
			
			// victim has been previously merged
			if (!ufds.isMaster(victim)) {
				continue;
			}
			
			merge(victim,master);
		}
	}
	
	/** Merges two states. */
	private void merge(int j, int i) {
		assert (!ufds.inSameBlock(j, i)) : "States are not in the same block.";
		assert (ufds.isMaster(i)) : "i target state is master of its block";
		assert (ufds.isMaster(j)) : "j target state is master of its block";
		assert (j > i) : "J victim state is always greater that target."; 

		// merge the states
		ufds.union(i, j);
		assert (ufds.isMaster(i)) : "i target state is still master of its block";
		assert (!ufds.isMaster(j)) : "j target state is not master of its block";
		assert (ufds.inSameBlock(i, j)) : "states are now in the same block.";
		
		// check compatibility
		if (!compatible(j,i)) {
			throw new Avoid();
		}
		
		FAStateGroup group = group(i);
		Iterator<Object> letters = group.getOutgoingLetters();
		while (letters.hasNext()) {
			Object letter = letters.next();
			FAEdgeGroup edges = group.delta(letter);
			FAStateGroup target = edges.getTargetStateGroup();
			merge(target);
		}
	}
	
	/** Checks compatibility of two states. */
	private boolean compatible(int j, int i) {
		FAStateGroup group = group(i);
		result.getUserInfoHandler().vertexAggregate(group.getUserInfos());
		return true;
	}
	
	/** Executes the algorithm. */
	public void execute(LSMAlgoInput input, LSMAlgoResult result) {
		this.input = input;
		this.result = result;
		initialize();
		int size = states.size();
		
		result.started(input);
		result.setPartition(states, ufds);
		
		//System.out.println(ufds);
		//show();
		
		// For each non consolidated state.
		for (int i=1; i<size; i++) {
			// bypass non master states
			if (!ufds.isMaster(i)) {
				continue;
			}
			
			// for each consolidated state
			for (int j=0; j<i; j++) {
				// bypass non master states
				if (!ufds.isMaster(j)) {
					continue;
				}
				
				ufds.startTransaction();
				try {
					//System.out.print("Trying (" + i + "," + j + ") ");
					merge(i,j);
					//System.out.println(" ok");
					ufds.commit();
					break;
				} catch (Avoid ex) {
					//System.out.println(" KO !");
					ufds.rollback();
				}
			}
			
			
			//System.out.println(ufds);
			//show();
		}
		
	}
	
	JDotty jdotty = new JDotty();
	/** Shows an intermediate result. */
	public void show() {
		try {
			jdotty.present(FAUtils.copyForDot(result.resultDFA()),null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
