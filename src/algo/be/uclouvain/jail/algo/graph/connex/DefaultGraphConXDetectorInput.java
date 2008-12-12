package be.uclouvain.jail.algo.graph.connex;

import be.uclouvain.jail.algo.utils.AbstractAlgoInput;
import be.uclouvain.jail.graph.IDirectedGraph;

/** Default implementation of IGraphConXDetectorInput. */
public class DefaultGraphConXDetectorInput extends AbstractAlgoInput implements IGraphConXDetectorInput {

	/** Input graph. */
	private IDirectedGraph graph;
	
	/** Outgoing edges only? */
	private boolean outgoingOnly = false;
	
	/** Creates an input instance. */
	public DefaultGraphConXDetectorInput(IDirectedGraph graph) {
		this.graph = graph;
	}

	/** Install options. */
	@Override
	protected void installOptions() {
		super.installOptions();
		super.addOption("outgoingOnly", false, Boolean.class, null);
	}
	
	/** {@inheritDoc} */
	public boolean outgoingOnly() {
		return outgoingOnly;
	}

	/** Sets outgoing only. */
	public void setOutgoingOnly(boolean outgoingOnly) {
		this.outgoingOnly = outgoingOnly;
	}

	/** Returns the input graph. */
	public IDirectedGraph getGraph() {
		return graph;
	}

}
