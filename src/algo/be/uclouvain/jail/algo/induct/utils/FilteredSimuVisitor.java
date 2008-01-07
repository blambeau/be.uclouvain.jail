package be.uclouvain.jail.algo.induct.utils;

import be.uclouvain.jail.algo.induct.internal.IWork;
import be.uclouvain.jail.algo.induct.internal.Simulation;
import be.uclouvain.jail.algo.induct.internal.WorkType;
import be.uclouvain.jail.algo.induct.open.ISimuVisitor;

/** Helper to create visitors interested in some works only. */
public class FilteredSimuVisitor implements ISimuVisitor {

	/** Work interests. */
	private boolean interest[];

	/** Rela visitor to use. */
	private ISimuVisitor realVisitor;
	
	/** Creates a visitor with interests. */
	public FilteredSimuVisitor(WorkType...types) {
		this(null,types);
	}

	public FilteredSimuVisitor(ISimuVisitor realVisitor, WorkType...types) {
		if (types == null || types.length == 0) {
			throw new IllegalArgumentException("Interests cannot be empty.");
		}
		this.realVisitor = realVisitor;
		interest = new boolean[WorkType.values().length];
		WorkType aworktype[] = types;
		int i = 0;
		for (int j = aworktype.length; i < j; i++) {
			WorkType type = aworktype[i];
			interest[type.id()] = true;
		}
	}
	
	/** Filters work, delegated to doOnWork when of interest. */
	public void onWork(Simulation simu, IWork work) {
		WorkType type = work.type();
		if (interest[type.id()]) {
			doOnWork(simu, work, type);
		}
	}

	/** Does the visit on a interesting work. */
	protected void doOnWork(Simulation simulation, IWork iwork, WorkType worktype) {
		if (realVisitor != null) {
			realVisitor.onWork(simulation, iwork);
		}
	}
	
}
