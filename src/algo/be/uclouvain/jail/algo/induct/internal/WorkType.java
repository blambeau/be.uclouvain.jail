package be.uclouvain.jail.algo.induct.internal;

/** Enumeration with work types. */
public enum WorkType {

	/** Kernel state merge. */
	KStateMerge(0), 
	
	/** Other state merge. */
	OStateMerge(1), 
	
	/** Kernel edge merge. */
	KEdgeMerge(2), 
	
	/** Other edge merge. */ 
	OEdgeMerge(3), 
	
	/** Kernel state gain. */
	KStateGain(4), 
	
	/** Other state gain. */
	OStateGain(5),

	/** Victim state gain. */
	VStateGain(6);
	
	/** Identifier of the work. */
	private int id;

	/** Creates a work instance. */
	private WorkType(int id) {
		this.id = id;
	}

	/** Returns the work id. */
	public int id() {
		return id;
	}

}
