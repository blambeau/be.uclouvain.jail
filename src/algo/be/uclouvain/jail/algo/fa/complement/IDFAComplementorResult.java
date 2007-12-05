package be.uclouvain.jail.algo.fa.complement;

import java.util.Set;

import net.chefbe.javautils.adapt.IAdaptable;

/**
 * Abstracts result of DFAComplementorAlgo.
 * 
 * @author blambeau
 */
public interface IDFAComplementorResult extends IAdaptable {

	/** Algorithm started event. */
	public void started(IDFAComplementorInput input);

	/** Copies an edge of the input DFA. */
	public void copyEdge(Object edge);

	/** Fired when a state miss some outgoing letters. */
	public void onMissing(Object state, Set<Object> missing);

	/** Algorithm ended event. */
	public void ended();

}
