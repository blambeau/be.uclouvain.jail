package be.uclouvain.jail.algo.graph.decorate;

import be.uclouvain.jail.algo.utils.AbstractAlgoInput;
import be.uclouvain.jail.graph.IDirectedGraph;

/**
 * Provides a default implementation of {@link IIdentifyAlgoInput}.
 * 
 * @author blambeau
 */
public class DefaultIdentifyAlgoInput extends AbstractAlgoInput implements IIdentifyAlgoInput {

	/** Graph to identify. */
	private IDirectedGraph g;
	
	/** Edge and vertex attribute. */
	private String eAttr, vAttr;
	
	/** Creates an algo input instance. */
	public DefaultIdentifyAlgoInput(IDirectedGraph g) {
		this.g = g;
	}

	/** Installs the options. */
	@Override
	protected void installOptions() {
		super.installOptions();
		super.addOption("edgeAttr", false, String.class, "index");
		super.addOption("vertexAttr", false, String.class, "index");
	}

	/** Returns the graph to identify. */
	public IDirectedGraph getGraph() {
		return g;
	}

	/** Returns edge attribute. */
	public String getEdgeAttr() {
		return eAttr;
	}

	/** Sets edge attribute. */
	public void setEdgeAttr(String eAttr) {
		this.eAttr = eAttr;
	}
	
	/** Returns state attribute. */
	public String getVertexAttr() {
		return vAttr;
	}

	/** Sets vertex attribute. */
	public void setVertexAttr(String vAttr) {
		this.vAttr = vAttr;
	}
	
}
