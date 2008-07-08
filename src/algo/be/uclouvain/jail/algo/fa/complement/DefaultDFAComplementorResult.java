package be.uclouvain.jail.algo.fa.complement;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.chefbe.javautils.adapt.AdaptUtils;
import be.uclouvain.jail.algo.utils.AbstractAlgoResult;
import be.uclouvain.jail.fa.FAStateKind;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.IFA;
import be.uclouvain.jail.fa.impl.AttributeGraphFAInformer;
import be.uclouvain.jail.fa.impl.GraphDFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.IDirectedGraphWriter;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.IUserInfoHelper;
import be.uclouvain.jail.uinfo.UserInfoHelper;

/**
 * Default implementation of IDFAComplementorResult.
 * 
 * @author blambeau
 */
public class DefaultDFAComplementorResult extends AbstractAlgoResult implements IDFAComplementorResult {

	/** Input of the algorithm. */
	private IDFAComplementorInput input;
	
	/** Input DFA. */
	private IDFA inputDFA;
	
	/** Resulting DFA. */
	private IDirectedGraphWriter result;
	
	/** Helper to use. */
	private IUserInfoHelper helper;
	
	/** Copied states. */
	private Map<Object,Object> states;
	
	/** Error state to use. */
	private Object errorState;

	/** Creates a result instance. */
	public DefaultDFAComplementorResult() {
		this.helper = new UserInfoHelper();
	}
	
	/** Algorithm started event. */
	public void started(IDFAComplementorInput input) {
		states = new HashMap<Object,Object>();
		this.input = input;
		this.inputDFA = input.getDFA();
		this.result = super.getWriter(new GraphDFA());
	}

	/** Algorithm ended event. */
	public void ended() {
	}

	/** Ensures that a state is created. */
	private Object ensure(Object state) {
		if (states.containsKey(state)) {
			return states.get(state);
		} else {
			IUserInfo info = inputDFA.getGraph().getUserInfoOf(state);
			Object copy = result.createVertex(info);
			states.put(state, copy);
			return copy;
		}
	}
	
	/** Copies an edge. */
	public void copyEdge(Object edge) {
		Object source = inputDFA.getGraph().getEdgeSource(edge);
		Object target = inputDFA.getGraph().getEdgeTarget(edge);
		IUserInfo info = inputDFA.getGraph().getUserInfoOf(edge);
		result.createEdge(ensure(source), ensure(target), info);
	}
	
	/** Finds the error state. */
	protected Object findError() {
		if (errorState == null) {
			errorState = result.createVertex(createErrorStateInfo());
		}
		return errorState;
	}

	/** When some letters are missing. */
	public void onMissing(Object state, Set<Object> missing) {
		System.out.println("On missing from " + state + " " + missing);
		Object source = ensure(state);
		Object target = null;
		switch (input.getHeuristic()) {
			case ERROR_STATE:
				target = findError();
				break;
			case SAME_STATE:
				target = source;
				break;
		}
		
		for (Object letter: missing) {
			IUserInfo edgeInfo = createMissingEdgeInfo(letter);
			result.createEdge(source, target, edgeInfo);
		}
	}

	/** Creates a user info for a missing edge. */
	protected IUserInfo createMissingEdgeInfo(Object letter) {
		return helper.keyValue(AttributeGraphFAInformer.EDGE_LETTER_KEY,letter);
	}

	/** Creates a user info for the error state. */
	protected IUserInfo createErrorStateInfo() {
		helper.addKeyValue(AttributeGraphFAInformer.STATE_INITIAL_KEY, false);
		helper.addKeyValue(AttributeGraphFAInformer.STATE_KIND_KEY, FAStateKind.AVOID);
		return helper.install();
	}

	/** Adapts to some types. */
	public <T> Object adapt(Class<T> c) {
		if (c.isAssignableFrom(getClass())) {
			return this;
		}
		
		// natural adaptation to a DFA
		if (IDFA.class.equals(c) || IFA.class.equals(c) || 
			IDirectedGraph.class.equals(c)) {
			return result.adapt(c);
		}
		
		return AdaptUtils.externalAdapt(this,c);
	}

}
