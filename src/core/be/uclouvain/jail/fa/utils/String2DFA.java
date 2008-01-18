package be.uclouvain.jail.fa.utils;

import net.chefbe.javautils.adapt.IAdapter;
import be.uclouvain.jail.fa.FAStateKind;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.IFATrace;
import be.uclouvain.jail.fa.IString;
import be.uclouvain.jail.fa.impl.AttributeGraphFAInformer;
import be.uclouvain.jail.fa.impl.GraphDFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.utils.DefaultDirectedGraphPath;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.IUserInfoHelper;
import be.uclouvain.jail.uinfo.UserInfoHelper;

/**
 * Converts a word to a DFA.
 * 
 * @author blambeau
 */
public class String2DFA implements IAdapter {

	/** Factors a state info. */
	private static IUserInfo sInfo(boolean initial, boolean accepting, boolean error) {
		IUserInfoHelper helper = UserInfoHelper.instance();
		helper.addKeyValue(AttributeGraphFAInformer.STATE_INITIAL_KEY, initial);
		helper.addKeyValue(AttributeGraphFAInformer.STATE_KIND_KEY, 
				FAStateKind.fromBools(accepting,error));
		return helper.install();
	}
	
	/** Creates an edge info. */
	private static IUserInfo eInfo(Object letter) {
		IUserInfoHelper helper = UserInfoHelper.instance();
		return helper.keyValue(AttributeGraphFAInformer.EDGE_LETTER_KEY, letter);
	}
	
	/** Flushes a word to a writer. */
	private static <L> IFATrace<L> flush(IString<L> word, IDFA dfa, boolean enableTrace) {
		IDirectedGraph g = dfa.getGraph();
		DefaultDirectedGraphPath path = null;
		
		// create initial state
		IUserInfo sInfo = sInfo(true,false,false);
		Object current = g.createVertex(sInfo);
		if (enableTrace) {
			path = new DefaultDirectedGraphPath(g, current);
		}
		
		// create next states
		int i=1;
		for (L letter: word) {
			// create edge and state info
			IUserInfo eInfo = eInfo(letter);
			sInfo = sInfo(false,false,false);
			
			// create next state and connect
			Object next = g.createVertex(sInfo);
			Object edge = g.createEdge(current, next, eInfo);
			if (enableTrace) {
				path.addEdge(edge);
			}
			
			// current becomes next
			current = next;
			i++;
		}
		
		return enableTrace ? new DefaultFATrace<L>(dfa, path) : null;
	}
	
	/** Converts a word to a DFA. */
	public static <L> IDFA word2dfa(IString<L> word) {
		IDFA dfa = new GraphDFA();
		flush(word,dfa,false);
		return dfa;
	}

	/** Converts a word to a trace. */
	public static <L> IFATrace<L> toTrace(IString<L> s) {
		IFATrace<L> trace = flush(s,new GraphDFA(),true);
		Object last = trace.getLastState();
		IUserInfo lastInfo = trace.getFA().getGraph().getVertexInfo(last);
		if (s.isPositive()) {
			lastInfo.setAttribute(AttributeGraphFAInformer.STATE_KIND_KEY, FAStateKind.ACCEPTING);
		} else {
			lastInfo.setAttribute(AttributeGraphFAInformer.STATE_KIND_KEY, FAStateKind.ERROR);
		}
		return trace;
	}

	/** Provide adaptations. */
	public Object adapt(Object arg0, Class<?> arg1) {
		if (IFATrace.class.equals(arg1)) {
			if (arg0 instanceof IString) {
				return toTrace((IString<?>)arg0);
			} else if (arg0 instanceof IString) {
				return toTrace((IString<?>)arg0);
			}
		}
		return null;
	}
	
}
