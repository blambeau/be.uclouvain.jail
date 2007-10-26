package be.uclouvain.jail.algo.fa.determinize;

import java.util.Set;

import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.INFA;
import be.uclouvain.jail.uinfo.IUserInfo;

/**
 * Abstracts the notion of result of a determinization.
 * 
 * <p>This contract is an abstraction of the {@link NFADeterminizerAlgo} result. It can be
 * implemented to construct a resulting DFA, to listen or debug the algorithm, or something 
 * else.</p> 
 * 
 * <p>Pseudo code of the algorithm as seen by this abstraction is described in 
 * {@link NFADeterminizerAlgo} javadoc documentation.</p>
 * 
 * @author LAMBEAU Bernard
 */
public interface INFADeterminizerResult {

	/**
	 * "Algorithm started" event.
	 * 
	 * @param source the source NFA which is being determinized.
	 */
	public void started(INFA nfa);

	/** "Algorithm ended" event. */
	public void ended();

	/**
	 * Creates a result state from a definition (a collection of source NFA states).
	 * 
	 * <p>This method may return an object identifying the state created in the equivalent DFA
	 * under construction. When creating the DFA on the fly, returning such an identifier is a 
	 * efficient memory solution, as the algorithm must keep the <code>def</code> for internal
	 * implementation reasons.</p>
	 * 
	 * @param def a definition of result state.
	 * @return an identifier of the resulting state in the equivalent DFA. 
	 */
	public Object createTargetState(Set<IUserInfo> def);

	/**
	 * Creates a result transition between two target states.
	 * 
	 * @param source the source of the transition 
	 * (a result state identifier, previously returned by {@link #createTargetState(Set<Object>)} method). 
	 * @param edges the set of NFA edge infos that merge.
	 * @param target the target of the transition
	 * (a result state identifier, previously returned by {@link #createTargetState(Set<Object>)} method). 
	 */
	public void createTargetTransitions(Object source, Object target, Set<IUserInfo> edges);

	/** Returns the computed DFA. */
	public IDFA getResultingDFA();

}
