package be.uclouvain.jail.graph.adjacency;

import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.UserInfoCapable;

/**
 * <p>Implementation of the Edge contract, using a Map to implement edge attributes. This map 
 * allows null key and value that can thus be seen as particular attribute keys and/or values.
 * This map is returned by getAttributes() and can be used as external way to write attributes.
 * </p>
 * 
 * @author LAMBEAU Bernard
 */
public class DefaultEdge extends UserInfoCapable implements IEdge {

	/* instance variables SECTION ------------------------------------------------------------------- */
	/** Source vertex. */
	protected IVertex source;

	/** Target vertex. */
	protected IVertex target;

	/* CONSTRUCTORS SECTION ---------------------------------------------------------------------- */
	/** Empty constructor. Installs a MapUserInfo. */
	public DefaultEdge() {
		super();
	}

	/** Constructor with specific user info. */
	public DefaultEdge(IUserInfo info) {
		super(info);
	}

	/* source PROPERTY SECTION ------------------------------------------------------------------ */
	/** Returns edge source. */
	public IVertex getSource() {
		return this.source;
	}

	/** Sets edge source. */
	public void setSource(IVertex source) {
		this.source = source;
	}

	/** Checks if the vertex is source of this edge. */
	public boolean isSource(IVertex s) {
		return source.equals(s);
	}

	/* target PROPERTY SECTION ------------------------------------------------------------------ */
	/** Returns edge target. */
	public IVertex getTarget() {
		return this.target;
	}

	/** Sets edge target. */
	public void setTarget(IVertex target) {
		this.target = target;
	}

	/** Checks if the vertex is the target is this edge. */
	public boolean isTarget(IVertex s) {
		return target.equals(s);
	}

}
