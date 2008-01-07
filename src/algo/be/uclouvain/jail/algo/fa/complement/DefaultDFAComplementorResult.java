package be.uclouvain.jail.algo.fa.complement;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.chefbe.javautils.adapt.AdaptUtils;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.impl.AttributeGraphFAInformer;
import be.uclouvain.jail.fa.impl.GraphDFA;
import be.uclouvain.jail.graph.utils.DirectedGraphWriter;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.MapUserInfo;
import be.uclouvain.jail.uinfo.UserInfoCopier;

/**
 * Default implementation of IDFAComplementorResult.
 * 
 * @author blambeau
 */
public class DefaultDFAComplementorResult implements IDFAComplementorResult {

	/** Input of the algorithm. */
	private IDFAComplementorInput input;
	
	/** Input DFA. */
	private IDFA inputDFA;
	
	/** Resulting DFA. */
	private IDFA result;
	
	/** Graph writer. */
	private DirectedGraphWriter writer;
	
	/** Copied states. */
	private Map<Object,Object> states;
	
	/** Error state to use. */
	private Object errorState;

	/** Creates a result instance. */
	public DefaultDFAComplementorResult() {
		result = new GraphDFA();
		writer = new DirectedGraphWriter(result.getGraph());
	}
	
	/** Returns the state copier. */
	public UserInfoCopier getStateCopier() {
		return writer.getVertexCopier();
	}

	/** Returns the edge copier. */
	public UserInfoCopier getEdgeCopier() {
		return writer.getEdgeCopier();
	}

	/** Algorithm started event. */
	public void started(IDFAComplementorInput input) {
		states = new HashMap<Object,Object>();
		this.input = input;
		this.inputDFA = input.getDFA();
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
			Object copy = writer.createVertex(info);
			states.put(state, copy);
			return copy;
		}
	}
	
	/** Copies an edge. */
	public void copyEdge(Object edge) {
		Object source = inputDFA.getGraph().getEdgeSource(edge);
		Object target = inputDFA.getGraph().getEdgeTarget(edge);
		IUserInfo info = inputDFA.getGraph().getUserInfoOf(edge);
		writer.createEdge(ensure(source), ensure(target), info);
	}
	
	/** Finds the error state. */
	protected Object findError() {
		if (errorState == null) {
			errorState = writer.createVertex(createErrorStateInfo());
		}
		return errorState;
	}

	/** When some letters are missing. */
	public void onMissing(Object state, Set<Object> missing) {
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
			writer.createEdge(source, target, edgeInfo);
		}
	}

	/** Creates a user info for a missing edge. */
	protected IUserInfo createMissingEdgeInfo(Object letter) {
		return MapUserInfo.factor(AttributeGraphFAInformer.EDGE_LETTER_KEY,letter);
	}

	/** Creates a user info for the error state. */
	protected IUserInfo createErrorStateInfo() {
		IUserInfo info = new MapUserInfo();
		info.setAttribute(AttributeGraphFAInformer.STATE_INITIAL_KEY, false);
		info.setAttribute(AttributeGraphFAInformer.STATE_ACCEPTING_KEY, false);
		info.setAttribute(AttributeGraphFAInformer.STATE_ERROR_KEY, true);
		return info;
	}

	/** Adapts to some types. */
	public <T> Object adapt(Class<T> c) {
		if (c.isAssignableFrom(getClass())) {
			return this;
		}
		
		// natural adaptation to a DFA
		if (IDFA.class.equals(c)) {
			return result;
		}
		
		return AdaptUtils.externalAdapt(this,c);
	}

}
