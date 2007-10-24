package be.uclouvain.jail.fa;

import be.uclouvain.jail.adapt.IAdaptable;
import be.uclouvain.jail.fa.utils.DefaultDFATrace;
import be.uclouvain.jail.graph.IDirectedGraphPath;

/**
 * Encpsulates a trace inside a DFA.
 * 
 * <p>A DFA trace is the DFA equivalent abstraction of a graph path. Because
 * JAIL uses graph decorations for representing automata, an automaton trace
 * provides the underlying graph path. In other words, an automaton trace can
 * be seen as a interesting decorator for graph paths.</p>
 * 
 * <p>Automaton traces are basically language strings. For this reason, a trace
 * is an iterable of letters, not edges as graph paths.</p>
 * 
 * <p>A useful decorator, providing a query API, is implemented by {@link DFATrace} 
 * and can be obtained by adaptation of this interface.</p>
 * 
 * <p>This interface is not intended to be implemented, a default implementation
 * being provided by {@link DefaultDFATrace}.</p>
 */
public interface IDFATrace<T> extends IAdaptable, Iterable<T> {

	/** Returns the underlying graph path. */
	public IDirectedGraphPath getGraphPath();

	/** Returns the DFA from which this trace has been extracted. */
	public IDFA getDFA();
	
}
