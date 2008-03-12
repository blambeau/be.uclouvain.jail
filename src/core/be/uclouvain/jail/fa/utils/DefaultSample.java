package be.uclouvain.jail.fa.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import net.chefbe.javautils.adapt.AdaptUtils;
import be.uclouvain.jail.algo.fa.inject.DFAInjectionHelper;
import be.uclouvain.jail.algo.fa.walk.PTADepthFirstWalker;
import be.uclouvain.jail.algo.fa.walk.PTADepthFirstWalker.IVisitor;
import be.uclouvain.jail.fa.FAStateKind;
import be.uclouvain.jail.fa.IAlphabet;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.IFATrace;
import be.uclouvain.jail.fa.IString;
import be.uclouvain.jail.fa.IWalkInfo;
import be.uclouvain.jail.fa.impl.AttributeGraphFAInformer;
import be.uclouvain.jail.fa.impl.GraphDFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.IDirectedGraphPath;
import be.uclouvain.jail.graph.utils.DefaultDirectedGraphPath;
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
		return (dfa.getGraph().getVerticesTotalOrder().size() == 1) ? 0 : -1;
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
		IWalkInfo<L> info = s.walk(dfa);
		if (!info.isFullyIncluded()) {
			// part of the string is not present
			return false;
		} else if (s.isPositive()) {
			return info.getIncludedPart().isAccepted();
		} else {
			// already known as negative?
			IFATrace<L> trace = info.getIncludedPart();
			return trace.isError() || trace.isAvoid();
		}
	}
	
	/** Walks a string inside the sample. */
	public IWalkInfo walk(IString<L> s) {
		return s.walk(dfa);
	}

	/** Checks if a state is marking the end of a state. */
	private boolean isEndOfString(Object state) {
		FAStateKind kind = dfa.getStateKind(state);
		return !FAStateKind.PASSAGE.equals(kind);
	}
	
	/** Returns number of strings. */
	public int size() {
		if (this.size == -1) {
			new PTADepthFirstWalker(this.dfa).execute(new IVisitor() {
	
				/** When entering a state. */
				public boolean entering(Object state, Object edge) {
					if (isEndOfString(state)) { size++; }
					return true;
				}
				
				/** When leaving a state. */
				public boolean leaving(Object state, Object edge, boolean recurse) {
					return false;
				}
			});
		}
		return size;
	}

	/** Returns an iterator on strings. */
	public Iterator<IString<L>> iterator() {
		final List<IString<L>> list = new ArrayList<IString<L>>();
		new PTADepthFirstWalker(this.dfa).execute(new IVisitor() {
			
			/** Directed graph. */
			private IDirectedGraph ptag = dfa.getGraph();
			
			/** Edge stack. */
			private Stack<Object> edges = new Stack<Object>();
			
			/** Flushes a string. */
			private IString<L> flush() {
				//System.out.println("Creating a string " + edges.size());
				if (edges.isEmpty()) {
					IDirectedGraphPath path = new DefaultDirectedGraphPath(ptag,dfa.getInitialState());
					DefaultFATrace<L> trace = new DefaultFATrace<L>(dfa,path);
					return new FATraceString<L>(trace);
				} else {
					List<Object> pathEdges = new ArrayList<Object>();
					pathEdges.addAll(edges);
					IDirectedGraphPath path = new DefaultDirectedGraphPath(ptag,pathEdges);
					DefaultFATrace<L> trace = new DefaultFATrace<L>(dfa,path);
					return new FATraceString<L>(trace);
				}
			}
			
			/** Push edge if not null. */
			public boolean entering(Object state, Object edge) {
				if (edge != null) { 
					edges.push(edge); 
				}
				if (isEndOfString(state)) {
					list.add(flush());
				}
				return true;
			}
			
			/** Flush if ok. */
			public boolean leaving(Object state, Object edge, boolean recurse) {
				if (edge != null) { edges.pop(); }
				return false;
			}
			
		});
		return list.iterator();
	}

	/** Adds a sample string, by writing in the nfa. */
	public boolean addString(IString<L> s) {
		boolean isNew = !contains(s);
		injector.inject(s);
		if (isNew && size != -1) {
			size++;
		}
		return isNew;
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
