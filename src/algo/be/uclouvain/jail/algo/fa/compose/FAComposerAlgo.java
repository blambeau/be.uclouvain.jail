package be.uclouvain.jail.algo.fa.compose;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.chefbe.javautils.collections.arrays.ArrayUtils;
import net.chefbe.javautils.collections.arrays.ArrayUtils.IArrayExploder;
import be.uclouvain.jail.algo.commons.Avoid;
import be.uclouvain.jail.algo.fa.utils.IMultiFAGroupInformer;
import be.uclouvain.jail.algo.fa.utils.MultiFAEdgeGroup;
import be.uclouvain.jail.algo.fa.utils.MultiFAStateGroup;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.IFA;
import be.uclouvain.jail.fa.INFA;

/**
 * Computes the synchronous product of FAs.
 * 
 * @author blambeau
 */
public class FAComposerAlgo implements IMultiFAGroupInformer {

	/** Composed automata. */
	private IFA[] fas;

	/** Result. */
	private IFAComposerResult result;
	
	/** Explored states. */
	private Set<MultiFAStateGroup> explored;
	
	/** States to explore. */
	private Set<MultiFAStateGroup> toExplore;
	
	/** Returns i-th FA. */
	public IFA getFA(int i) {
		return fas[i];
	}

	/** Computes initial states. */
	private Object[][] getInitStates() {
		Object[][] inits = new Object[fas.length][];
		for (int i=0; i<fas.length; i++) {
			if (fas[i] instanceof IDFA) {
				// single initial state
				Object init = ((IDFA)fas[i]).getInitialState(); 
				inits[i] = new Object[]{init};
			} else if (fas[i] instanceof INFA) {
				List<Object> states = new ArrayList<Object>();
				for (Object state: ((INFA)fas[i]).getInitialStates()) {
					states.add(state);
				}
				inits[i] = states.toArray();
			} else {
				throw new IllegalStateException("Not a DFA nor a NFA");
			}
		}
		return inits;
	}
	
	/** Checks if a state has already been explored. */
	private boolean isExplored(MultiFAStateGroup group) {
		return explored.contains(group);
	}
	
	/** Marks a state has to be explored. */
	private void markAsToExplore(MultiFAStateGroup group) {
		assert (!isExplored(group)) : "Group not explored yet";
		toExplore.add(group);
	}

	/** Marks a state has explored. */ 
	private void markAsExplored(MultiFAStateGroup group) {
		assert (!toExplore.contains(group)) : "Group is not to be explored.";
		explored.add(group);
	}

	/** Mark target as reached from source, using edge. */
	private void markAsReached(MultiFAStateGroup source, MultiFAEdgeGroup edge, MultiFAStateGroup target) {
		result.stateReached(source, edge, target);
	}

	/** Explores a single group. */
	private void explore(MultiFAStateGroup source) {
		//System.out.println("Exploring " + source);
		
		// mark as explored
		markAsExplored(source);
		
		// all outgoing letters
		Iterator<Object> letters = source.getOutgoingLetters();
		while (letters.hasNext()) {
			Object letter = letters.next();
			
			// try to synchronize on this letter
			try {
				// find each group of out edges on that letter
				List<MultiFAEdgeGroup> edges = source.delta(letter);
				for (MultiFAEdgeGroup edge: edges) {
					
					// find target state group
					MultiFAStateGroup target = edge.getTargetStateGroup(source);
					boolean toBeMarked = !isExplored(target) && !toExplore.contains(target);
					
					try {
						markAsReached(source, edge, target);
						if (toBeMarked) {
							markAsToExplore(target);
							toBeMarked = false;
						}
					} catch (Avoid ex) {
						//System.out.println("Synchronization failed on " + letter + " due to |" + ex.getMessage() + "|");
					}
				}
			} catch (Avoid ex) {
				//System.out.println("Synchronization failed on " + letter + " due to |" + ex.getMessage() + "|");
			}
		}
	}
	
	/** Execute algorithm. */
	public void execute(IFAComposerInput input, IFAComposerResult result) {
		result.started(input);
		
		this.fas = input.getFAs();
		this.result = result;
		this.toExplore = new HashSet<MultiFAStateGroup>();
		this.explored = new HashSet<MultiFAStateGroup>();
		
		final IMultiFAGroupInformer informer = this;
		Object[][] inits = getInitStates();

		// put initial states in toExplore
		ArrayUtils.explode(inits, new IArrayExploder() {
			public void group(Object[] group) {
				markAsToExplore(new MultiFAStateGroup(group, informer));
			}
		});
		
		// explore until no more
		while (!toExplore.isEmpty()) {
			MultiFAStateGroup group = toExplore.iterator().next();
			toExplore.remove(group);
			explore(group);
		}
		
		this.toExplore = null;
		this.explored = null;
		result.ended();
	}

}
