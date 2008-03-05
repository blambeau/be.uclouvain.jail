package be.uclouvain.jail.algo.induct.oracle;

import java.util.LinkedList;
import java.util.List;

import be.uclouvain.jail.algo.commons.Avoid;
import be.uclouvain.jail.algo.commons.Restart;
import be.uclouvain.jail.algo.induct.internal.IWork;
import be.uclouvain.jail.algo.induct.internal.PTAEdge;
import be.uclouvain.jail.algo.induct.internal.PTAState;
import be.uclouvain.jail.algo.induct.internal.Simulation;
import be.uclouvain.jail.algo.induct.internal.WorkType;
import be.uclouvain.jail.algo.induct.utils.FilteredSimuVisitor;
import be.uclouvain.jail.algo.induct.utils.IGainD;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.graph.utils.DefaultDirectedGraphPath;

/**
 * Bernard Lambeau's Oracle.
 *  
 * @author blambeau
 */
public class BLambeauOracle extends AbstractMembershipOracle {

	/** Current gained edge. */
	private PTAEdge gainedEdge;
	
	/** Creates an oracle instance. */
	public BLambeauOracle() {
		super.setExtractor(new LongSuffixExtractor() {
			@Override
			protected DefaultDirectedGraphPath factorPath(IDFA pta, PTAState root) {
				// overrided because we miss the gained edge otherwise
				List<Object> edges = new LinkedList<Object>();
				edges.add(gainedEdge.representor());
				return new DefaultDirectedGraphPath(pta.getGraph(),edges);
			}
		});
	}

	/** Sends queries. */
	@Override
	public boolean doAccept(Simulation simulation) throws Avoid, Restart {
		simulation.accept(new FilteredSimuVisitor(WorkType.KStateGain, WorkType.OStateGain) {
			@Override
			protected void doOnWork(Simulation simulation, IWork iwork, WorkType worktype) {
				IGainD gain = (IGainD) iwork.decorate();
				gainedEdge = gain.edgeGain();
				PTAState target = gain.targetInPTA();
				assert (target != null) : "Target state in PTA is not null.";
				PTAState gained = gain.stateGain();
				assert (gained != null) : "Gained state is not null.";
				sendQueries(target,gained);
			}
		});
		return true;
	}

}
