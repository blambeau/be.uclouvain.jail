package be.uclouvain.jail.fa.utils;

import java.util.Iterator;
import java.util.NoSuchElementException;

import net.chefbe.javautils.adapt.AdaptUtils;
import be.uclouvain.jail.algo.fa.inject.DFAInjectionHelper;
import be.uclouvain.jail.fa.FAStateKind;
import be.uclouvain.jail.fa.IAlphabet;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.IFATrace;
import be.uclouvain.jail.fa.IString;
import be.uclouvain.jail.fa.IWalkInfo;
import be.uclouvain.jail.fa.impl.AttributeGraphFAInformer;
import be.uclouvain.jail.fa.impl.GraphDFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.uinfo.IUserInfoHelper;
import be.uclouvain.jail.uinfo.UserInfoHelper;

/**
 * Decorates a PTA as a sample.
 * 
 * @author blambeau
 * @param <L>
 */
public class DefaultSample<L> extends AbstractSample<L> {

	/** Decorated dfa. */
	private IDFA dfa;
	
	/** Injector to use. */
	private DFAInjectionHelper injector;
	
	/** Size of the PTA. */
	private int size = -1;
	
	/** Creates a sample instance. */
	@SuppressWarnings("unchecked")
	public DefaultSample(IDFA dfa) {
		super((IAlphabet<L>)dfa.getAlphabet());
		this.dfa = dfa;
		this.size = handleSize(dfa);
		this.injector = new DFAInjectionHelper(dfa,getUserInfoHandler());
	}
	
	/** Creates an empty sample. */
	public DefaultSample(IAlphabet<L> alphabet) {
		this(createEmptyDFA(alphabet));
	}

	/** Handles size of the initial DFA. */
	private int handleSize(IDFA dfa) {
		return (dfa.getGraph().getVerticesTotalOrder().size() == 1) ?
			   0 : -1;
	}
	
	/** Creates an empty DFA. */
	private static final <L> IDFA createEmptyDFA(IAlphabet<L> alphabet) {
		IUserInfoHelper helper = new UserInfoHelper();
		IDFA dfa = new GraphDFA(alphabet);
		IDirectedGraph graph = dfa.getGraph();
		helper.addKeyValue(AttributeGraphFAInformer.STATE_INITIAL_KEY, true);
		helper.addKeyValue(AttributeGraphFAInformer.STATE_KIND_KEY, FAStateKind.PASSAGE);
		graph.createVertex(helper.install());
		return dfa;
	}
	
	/** Returns true if the sample contains a given string. */
	public boolean contains(IString<L> s) {
		return FAWalkUtils.walk(dfa,s) != null;
	}
	
	/** Walks a string inside the sample. */
	public IWalkInfo walk(IString<L> s) {
		return s.walk(dfa);
	}

	/** Returns a depth first instance. */
	private PTADepthFirst depthFirst() {
		return new PTADepthFirst(dfa);
	}
	
	/** Checks if a state is the end of a string. */
	private boolean isEndOfString(Object state) {
		FAStateKind kind = dfa.getStateKind(state);
		return !FAStateKind.PASSAGE.equals(kind); 
	}
	
	/** Returns number of strings. */
	public int size() {
		int size = 0;
		PTADepthFirst depthFirst = depthFirst();
		while (depthFirst.hasNext()) {
			if (isEndOfString(depthFirst.next())) {
				size++;
			}
		}
		return size;
	}

	/** Returns an iterator on strings. */
	public Iterator<IString<L>> iterator() {
		return new Iterator<IString<L>>() {
			
			/** Depth first visit. */
			private PTADepthFirst depthFirst = depthFirst();
			
			/** Next string to return. */
			private IString<L> next = findNext();
			
			/** Finds the next string. */
			private IString<L> findNext() {
				while (depthFirst.hasNext()) {
					Object next = depthFirst.next();
					if (isEndOfString(next)) {
						IFATrace<L> trace = depthFirst.flushTrace();
						return new FATraceString<L>(trace);
					}
				}
				return null;
			}
			
			/** Has a next string? */
			public boolean hasNext() {
				return next != null;
			}

			/** Returns next string. */
			public IString<L> next() {
				if (!hasNext()) { throw new NoSuchElementException(); }
				IString<L> toReturn  = next;
				next = findNext();
				return toReturn;
			}
			
			/** Throws an exception. */
			public void remove() {
				throw new UnsupportedOperationException();
			}

		};
	}

	/** Adds a sample string, by writing in the nfa. */
	public void addString(IString<L> s) {
		if (injector.inject(s) && size != -1) {
			size++;
		}
	}

	/** Provides adaptations. */
	public <T> Object adapt(Class<T> c) {
		if (c.isAssignableFrom(getClass())) {
			return this;
		}
		
		// provide classical adaptations
		if (IDirectedGraph.class.equals(c)) {
			return dfa.getGraph();
		} else if (IDFA.class.equals(c)) {
			return dfa;
		}
		 
		// allow external adapters to do their work
		return AdaptUtils.externalAdapt(this,c);
	}

}
