package be.uclouvain.jail.fa.utils;

import be.uclouvain.jail.fa.FAStateKind;
import be.uclouvain.jail.fa.IFATrace;
import be.uclouvain.jail.fa.IString;
import be.uclouvain.jail.fa.IWalkInfo;
import be.uclouvain.jail.fa.impl.AttributeGraphFAInformer;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.utils.DirectedGraphWriter;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.IUserInfoHandler;
import be.uclouvain.jail.uinfo.IUserInfoHelper;
import be.uclouvain.jail.uinfo.UserInfoHelper;

/** Split info. */
public class DefaultWalkInfo<L> implements IWalkInfo<L> {

	/** Input word. */
	private IString<L> word;
	
	/** Accepted trace. */
	private IFATrace<L> included;

	/** Rejected word. */
	private IString<L> excluded;
	
	/** Creates a split info instance. */
	public DefaultWalkInfo(IString<L> word, IFATrace<L> included, IString<L> excluded) {
		this.included = included;
		this.excluded = excluded;
	}
	
	/** Creates a split info instance. */
	@SuppressWarnings("unchecked")
	public DefaultWalkInfo(IString<L> word, IFATrace<L> included, IFATrace<L> excluded) {
		this.included = included;
		this.excluded = (IString<L>) excluded.adapt(IString.class);
	}
	
	/** Returns input word. */
	public IString<L> getTestedString() {
		return word;
	}
	
	/** Fully accepted info? */
	public boolean isFullyIncluded() {
		return excluded.size() == 0;
	}
	
	/** Fully rejected info? */
	public boolean isFullyExcluded() {
		return included.size() == 0;
	}
	
	/** Returns the accepted part. */
	public IFATrace<L> getIncludedPart() {
		return included;
	}
	
	/** Returns rejected part. */
	public IString<L> getExcludedString() {
		return excluded;
	}

	/** Appends the trace. */
	private void append(IFATrace<L> copy, FAStateKind kind, IUserInfoHandler handler) {
		assert (excluded.size() != 0) : "Excluded is not empty on append.";
		IUserInfoHelper helper = new UserInfoHelper();
		IDirectedGraph graph = copy.getFA().getGraph();
		
		// create new last state
		helper.addKeyValue(AttributeGraphFAInformer.STATE_INITIAL_KEY, false);
		helper.addKeyValue(AttributeGraphFAInformer.STATE_KIND_KEY, kind);
		IUserInfo info = handler.vertexCopy(helper.install());
		info.setAttribute(AttributeGraphFAInformer.STATE_KIND_KEY, kind);
		Object last = graph.createVertex(info);

		// previous state
		Object prev = copy.getLastState();
		
		// create edge
		L letter = excluded.iterator().next();
		IUserInfo edgeInfo = helper.keyValue(AttributeGraphFAInformer.EDGE_LETTER_KEY, letter);
		edgeInfo = handler.edgeCopy(edgeInfo);
		
		Object edge = graph.createEdge(prev, last, edgeInfo);
		copy.append(edge);
	}
	
	/** Changes the last state of the trace. */
	private void change(IFATrace<L> copy, FAStateKind kind, IUserInfoHandler handler) {
		IDirectedGraph graph = copy.getFA().getGraph();
		Object last = copy.getLastState();
		IUserInfo info = graph.getVertexInfo(last);
		info.setAttribute(AttributeGraphFAInformer.STATE_KIND_KEY, kind);
	}
	
	/** Normalizes an accepted copy. */
	private IFATrace<L> normalize(IFATrace<L> copy, IUserInfoHandler handler) {
		if (included.isAccepted() && !isFullyIncluded()) {
			append(copy, FAStateKind.AVOID, handler);
		} else if (included.isAccepted()) {
			assert (isFullyIncluded()) : "Fully included in this case.";
			change(copy, FAStateKind.ACCEPTING, handler);
		} else if (included.isPassage()) {
			assert (!included.isAccepted()) : "Included is not accepting";
			change(copy, FAStateKind.ERROR, handler);
		} else if (included.isError()) {
			change(copy, FAStateKind.ERROR, handler);
		} else if (included.isAvoid()) {
			change(copy, FAStateKind.AVOID, handler);
		} else {
			throw new AssertionError("All cases covered.");
		}
		return copy;
	}
	
	/** Returns a normalized string. */
	public IFATrace<L> normalize(IUserInfoHandler handler) {
		// initialize a writer with handler and copy accepted
		DirectedGraphWriter writer = new DirectedGraphWriter(handler);
		IFATrace<L> copy = included.flush(writer);
		return normalize(copy, handler);
	}
	
}