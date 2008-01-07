package be.uclouvain.jail.algo.induct.utils;

import be.uclouvain.jail.algo.induct.internal.Simulation;

/** Membership query. */
public class MembershipQuery {

	/** Current simulation. */
	public Simulation simu;

	/** Prefix of the kernel state. */
	public Object prefix[];

	/** Bridge letter (gained letter). */
	public Object letter;

	/** Suffix of the state victim. */
	public Object suffix[];

	/** Negative of positive query ? */
	public boolean negative;

	/** Creates a query instance. */
	public MembershipQuery() {
	}

	/** Creates a query instance. */
	public MembershipQuery(
			Simulation simu, 
			Object[] prefix, 
			Object letter, 
			Object[] suffix, 
			boolean negative) {
		this.simu = simu;
		this.prefix = prefix;
		this.letter = letter;
		this.suffix = suffix;
		this.negative = negative;
	}
	
}