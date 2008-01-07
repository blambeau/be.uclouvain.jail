package be.uclouvain.jail.algo.induct.open;

import be.uclouvain.jail.algo.induct.internal.PTAEdge;
import be.uclouvain.jail.algo.induct.internal.PTAState;

/** 
 * Walks a PTA. 
 * 
 * <p>A walker may be used to make a depth-first visit of an induction PTA. 
 * Walker methods are fired when entering and leaving each state of the PTA 
 * (with the incoming edge provided as first parameter). Please read the 
 * documentation of these two methods in order to understand the semantics
 * of returning boolean flags.</p>
 * 
 * <p>This interface may be implemented.</p>
 */
public interface IWalker {

	/** 
	 * Fired when the walker enters a pta state using a given edge. 
	 * 
	 * @param edge incoming edge of state.
	 * @param state visited state of the PTA.
	 * @return true to visit state children, false otherwise. 
	 */
	public boolean walksLeftOf(PTAEdge edge, PTAState state);

	/** 
	 * Fired when the walker leaves a state initially reached using
	 * a given edge. 
	 * 
	 * <p>This method may return a true value to end the walk quickly.
	 * Please note that the walk is ended directly, that is, rightOf 
	 * calls are not made for state ancestors.</p>
	 * 
	 * @param edge incoming edge of state.
	 * @param state visited state of the PTA.
	 * @param flag value initially returned when walked left. 
	 * @return true to end the walk quickly, false otherwise. 
	 */
	public boolean walksRightOf(PTAEdge edge, PTAState state, boolean flag);
	
}
