package be.uclouvain.jail.algo.fa.compose;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
	
	/** Explores a single group. */
	private void explore(MultiFAStateGroup source) {
		assert !result.isExplored(source) : "States never explored twice.";
		
		// let result know that source has been pushed
		result.exploring(source);
		
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

					try {
						// reach it (may throw Avoid)
						if (result.reached(source, edge, target)) {
							explore(target);
						}
					} catch (Avoid ex) {}
				}
			} catch (Avoid ex) {
				//System.out.println("Synchronization failed on " + letter + " due to |" + ex.getMessage() + "|");
			}
		}
		
		result.endexplore(source);
	}
	
	/** Execute algorithm. */
	public void execute(IFAComposerInput input, IFAComposerResult result) {
		result.started(input);
		
		this.fas = input.getFAs();
		this.result = result;
		
		// explore each initial state
		final IMultiFAGroupInformer informer = this;
		Object[][] inits = getInitStates();
		ArrayUtils.explode(inits, new IArrayExploder() {
			public void group(Object[] group) {
				explore(new MultiFAStateGroup(group, informer));
			}
		});
		
		result.ended();
	}

}
