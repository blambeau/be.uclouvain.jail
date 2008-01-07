package be.uclouvain.jail.algo.induct.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.chefbe.javautils.collections.arrays.ArrayUtils;
import be.uclouvain.jail.algo.commons.Avoid;
import be.uclouvain.jail.algo.commons.Restart;
import be.uclouvain.jail.algo.induct.internal.IWork;
import be.uclouvain.jail.algo.induct.internal.Simulation;
import be.uclouvain.jail.algo.induct.internal.WorkType;
import be.uclouvain.jail.algo.induct.open.IOracle;

/** Mermbership queries oracle. 
 * <p>
 * This class is a helper to create real oracles that relay on membership 
 * queries extraction. It is not intended to be instanciated as a real oracle.
 * Instead, this class is intended to be overrided ; subclasses are expected
 * to override the {@link #query(MembershipQuery)} method.  
 * </p>
 * */
public class MQueryOracle implements IOracle {
	
	/** Creates an oracle. */
	public MQueryOracle() {
	}

	/** Extracts queries and send them to real oracle. */
	public boolean accept(Simulation simu) throws Avoid, Restart {
		// create a list of decorators with KStateGainS
		final List<KStateGainD> gains = new ArrayList<KStateGainD>();
		simu.accept(new FilteredSimuVisitor(WorkType.KStateGain) {
			public void doOnWork(Simulation simu, IWork work, WorkType type) {
				KStateGainD d = new KStateGainD(work);
				gains.add(d);
			}
		});
		
		// send queries for such gains
		for (KStateGainD gain : gains) {
			Object letter = gain.letter();
			Object prefix[] = gain.shortPrefix();
			for (Iterator suffixes = gain.suffixes(); suffixes.hasNext();) {
				Suffix suffix = (Suffix) suffixes.next();
				MembershipQuery query = new MembershipQuery();
				query.simu = simu;
				query.letter = letter;
				query.prefix = prefix;
				query.suffix = suffix.suffix;
				query.negative = suffix.negative;
				if (!query(query)) {
					throw new Avoid();
				}
			}
		}

		return true;
	}

	/** Send a query to real oracle.
	 * <p> 
	 * This method debugs the query and returns true. It is intended to be
	 * overrided by real oracles implemented by subclasses.
	 * </p> 
	 */
	protected boolean query(MembershipQuery query) throws Avoid, Restart {
		StringBuffer sb = new StringBuffer();
		sb.append("MQuery: ")
		  .append(ArrayUtils.toString(query.prefix, ","))
		  .append(" -> ")
		  .append(query.letter)
		  .append(" -> ")
		  .append(ArrayUtils.toString(query.suffix, ","))
		  .append(" ")
		  .append(query.negative).toString();
		System.out.println(sb.toString());
		return true;
	}
}
