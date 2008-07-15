package be.uclouvain.jail.algo.fa.decorate;

/**
 * Result abstraction of {@link DFADecorationAlgo}.
 * 
 * @author blambeau
 */
public interface IDFADecorationResult {

	/** Fired when the algorithm starts. */
	public void started(IDFADecorationInput input);

	/** Fired when the algorithm stops. */
	public void ended();

	/** Forces the initial decoration of a state. */
	public boolean initDeco(Object state, boolean isInitial);

	/** Propagates a decoration through an edge. */
	public boolean propagate(Object source, Object edge, Object target);

}
