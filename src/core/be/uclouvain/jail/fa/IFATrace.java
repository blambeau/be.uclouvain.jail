package be.uclouvain.jail.fa;

import java.util.List;

import net.chefbe.javautils.adapt.IAdaptable;
import be.uclouvain.jail.fa.utils.DefaultFATrace;
import be.uclouvain.jail.graph.IDirectedGraphPath;
import be.uclouvain.jail.graph.IDirectedGraphWriter;
import be.uclouvain.jail.graph.IDirectedGraphPath.IVisitor;

/**
 * Encpsulates a trace inside a FA.
 * 
 * <p>A FA trace is the FA equivalent abstraction of a graph path. Because
 * JAIL uses graph decorations for representing automata, an automaton trace
 * provides the underlying graph path. In other words, an automaton trace can
 * be seen as a interesting decorator for graph paths.</p>
 * 
 * <p>Automaton traces are basically language strings. For this reason, a trace
 * is an iterable of letters, not edges as graph paths.</p>
 * 
 * <p>A useful decorator, providing a query API, is implemented by {@link FATrace} 
 * and can be obtained by adaptation of this interface.</p>
 * 
 * <p>This interface is not intended to be implemented, a default implementation
 * being provided by {@link DefaultFATrace}.</p>
 */
public interface IFATrace<T> extends IAdaptable, Iterable<T> {

	/** Returns the underlying graph path. */
	public IDirectedGraphPath getGraphPath();

	/** Returns the FA from which this trace has been extracted. */
	public IFA getFA();
	
	/** Returns root state of the path. */
	public Object getRootState();
	
	/** Returns the last state. */
	public Object getLastState();
	
	/** Returns true if this trace is accepted by the DFA. Acceptation is
	 * defined as the fact that the trace ends in an accepting state. */
	public boolean isAccepted();
	
	/** Returns true if this trace ends in an passage state. */
	public boolean isPassage();
	
	/** Returns true if this trace ends in an error state. */
	public boolean isError();
	
	/** Returns true if this trace ends in an avoid state. */
	public boolean isAvoid();
	
	/** Returns the size of the path, defined as the number of used edges in 
	 * the path. The number of visited vertices is equal to getPathSize()+1
	 * by definition. */
	public int size();

	/** Returns an iterator on path edges. */
	public List<Object> edges();

	/** Returns an iterator on path states. */
	public List<Object> states();

	/** Appends an edge in the trace. */
	public void append(Object edge);
	
	/** Accepts a visitor. */
	public void accept(IVisitor visitor);

	/** Walks a FA and returns walk information. */
	public IWalkInfo<T> walk(IDFA fa);

	/** Creates a sub-trace. */
	public IFATrace<T> subTrace(int start, int length);

	/** Appends with another trace by copy. */
	public IFATrace<T> append(IFATrace<T> trace);
	
	/** Flushes in a writer and return the equivalent trace. */
	public IFATrace<T> flush(IDirectedGraphWriter writer);
	
}
