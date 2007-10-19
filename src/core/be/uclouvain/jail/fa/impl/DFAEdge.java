package be.uclouvain.jail.fa.impl;

import be.uclouvain.jail.graph.adjacency.DefaultEdge;
import be.uclouvain.jail.uinfo.IUserInfo;

/** DFA edge. */
public class DFAEdge extends DefaultEdge {

	/** Empty constructor. Installs a MapUserInfo. */
	public DFAEdge() {
		super();
	}

	/** Constructor with specific user info. */
	public DFAEdge(IUserInfo info) {
		super(info);
	}
	
	/** Returns DFA edge info. */
	private IDFAEdgeInfo toDFAEdgeInfo() {
		IUserInfo uInfo = getUserInfo();
		IDFAEdgeInfo info = uInfo instanceof IDFAEdgeInfo ?
				            (IDFAEdgeInfo) uInfo :
				            (IDFAEdgeInfo) getUserInfo().adapt(IDFAEdgeInfo.class);
		if (info == null) {
			throw new IllegalArgumentException("Edge infos must be IDFAEdgeInfo adaptable to be used in a DFA.");
		} else {
			return info;
		}
	}

	/** Returns the letter on the edge. */
	public Object letter() {
		return toDFAEdgeInfo().letter();
	}
	
}
