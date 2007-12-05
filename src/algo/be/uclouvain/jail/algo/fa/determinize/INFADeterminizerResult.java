package be.uclouvain.jail.algo.fa.determinize;

import net.chefbe.javautils.adapt.IAdaptable;
import be.uclouvain.jail.algo.fa.utils.FAEdgeGroup;
import be.uclouvain.jail.algo.fa.utils.FAStateGroup;

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
public interface INFADeterminizerResult extends IAdaptable {

	/** "Algorithm started" event. */
	public void started(INFADeterminizerInput input);

	/** "Algorithm ended" event. */
	public void ended();

	/** Creates a state in the resulting DFA. */
	public void createState(FAStateGroup source);
	
	/** Creates a result transition between two target states. */
	public void createTargetTransitions(FAStateGroup source, FAStateGroup target, FAEdgeGroup edges);

}
