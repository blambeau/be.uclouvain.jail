package be.uclouvain.jail.algo.induct.internal;

import java.lang.reflect.Method;

import be.uclouvain.jail.fa.IDFA;

/** Provide utilities to manipulate simulation works. */ 
public class WorkUtils {

	/** Not intended to be instanciated. */
	private WorkUtils() {
	}

	/** Returns the running algorithm attached to a work. */ 
	public static InductionAlgo getRunningAlgo(IWork work) {
		return work.simulation().getRunningAlgo();
	}

	/** Returns the DFA under construction. */
	public static IDFA getKernelDFA(IWork work) {
		return work.simulation().getKernelDFA();
	}

	/** Returns the source PTA. */
	public static IDFA getSourcePTA(IWork work) {
		return work.simulation().getSourcePTA();
	}

	/** Computes the short prefix of a PTA state. */
	public static Object[] shortPrefix(Object ptaState) {
		return null;
	}
	
	/** Returns a string representation of a work. */
	public static String toString(IWork subWork) {
		try {
			Method m = WorkUtils.class.getMethod("__toString", new Class[] { subWork.getClass() });
			return m.invoke(null, new Object[] { subWork }).toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return subWork.toString();
	}

	/** Converts a KStateMerge to a string. */
	public static String __toString(Simulation.KStateMerge subWork) {
		return (new StringBuilder("KStateMerge(")).append(subWork.state)
				.append(",").append(subWork.tkState).append(")").toString();
	}

	/** Converts a KEdgeMerge to a string. */
	public static String __toString(Simulation.KEdgeMerge subWork) {
		return "KEdgeMerge()";
	}

	/** Converts an OStateMerge to a string. */
	public static String __toString(Simulation.OStateMerge subWork) {
		return (new StringBuilder("OStateMerge(")).append(subWork.victim)
				.append(",").append(subWork.target).append(")").toString();
	}

	/** Converts an OStateMerge to a string. */
	public static String __toString(Simulation.OEdgeMerge subWork) {
		return "OEdgeMerge()";
	}

	/** Converts an KStateGain to a string. */
	public static String __toString(Simulation.KStateGain subWork) {
		return (new StringBuilder("KStateGain(")).append(subWork.tkState)
				.append(",").append(subWork.ptaEdge).append(")").toString();
	}

	/** Converts an OStateGain to a string. */
	public static String __toString(Simulation.OStateGain subWork) {
		return (new StringBuilder("OStateGain(")).append(subWork.state).append(
				",").append(subWork.edge).append(")").toString();
	}

}
