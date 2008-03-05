package be.uclouvain.jail.algo.induct.processor;

import java.util.HashMap;
import java.util.Map;

import be.uclouvain.jail.algo.commons.Unable;
import be.uclouvain.jail.algo.induct.internal.InductionAlgo;
import be.uclouvain.jail.algo.induct.internal.SLPair;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.constraints.PTAGraphConstraint;
import be.uclouvain.jail.graph.IDirectedGraph;

/**
 * Forwards labels.
 * 
 * @author blambeau
 */
public class ForwardLabelProcessor implements IInductionProcessor {

	/** Class attribute to use. */
	private String classAttr;
	
	/** Target attribute to install. */
	private String indexAttr;
	
	/** Next index. */
	private int nextIndex;
	
	/** Indexes by class. */
	private Map<Object, Integer> indexes;
	
	/** Propagation rules. */
	private Map<SLPair, Integer> rules;
	
	/** Creates a processor instance. */
	public ForwardLabelProcessor(String classAttr, String indexAttr) {
		this.classAttr = classAttr;
		this.indexAttr = indexAttr;
	}
	
	/** Process input PTA. */
	public void process(InductionAlgo algo) {
		process(algo.getPTA());
	}
	
	/** Process a PTA. */
	public void process(IDFA pta) {
		nextIndex = 0;
		indexes = new HashMap<Object, Integer>();
		rules = new HashMap<SLPair, Integer>();
		new PTAWalk().walk(pta);
	}

	/** Walks a PTA. */
	class PTAWalk {
		
		/** PTA. */
		private IDFA pta;
		
		/** Graph. */
		private IDirectedGraph ptag; 
		
		/** Walks a PTA. */
		public void walk(IDFA pta) {
			this.pta = pta;
			this.ptag = pta.getGraph();
			assert (new PTAGraphConstraint().isRespectedBy(ptag));

			Object init = pta.getInitialState();
			enter(null,null,init);
		}

		/** Enters a node. */
		protected void enter(Object source, Object letter, Object target) {
			if (source == null) {
				onRoot(target);
			} else {
				visiting(source, letter, target);
			}
			for (Object edge: ptag.getOutgoingEdges(target)) {
				Object nextL = pta.getEdgeLetter(edge);
				Object nextT = ptag.getEdgeTarget(edge);
				enter(target,nextL,nextT);
			}
		}
		
		/** Returns the class of a state. */
		protected Object getClassOf(Object state) {
			return ptag.getVertexInfo(state).getAttribute(classAttr);
		}
		
		/** Returns the index of a state. */
		protected Integer getIndexOf(Object state) {
			Integer index = (Integer) ptag.getVertexInfo(state).getAttribute(indexAttr);
			assert (index != null) : "Index has been set.";
			return index;
		}
		
		/** On root. */
		protected void onRoot(Object root) {
			Object clazz = getClassOf(root);
			int index = nextIndex++;
			if (clazz != null) {
				indexes.put(clazz, index);
			}
			ptag.getUserInfoOf(root).setAttribute(indexAttr, index);
		}
		
		/** When visiting a triple. */ 
		protected void visiting(Object source, Object letter, Object target) {
			Integer index = getIndexOf(source);
			
			// first look at rules
			Integer nextByRule = rules.get(new SLPair(index,letter));

			// next look at class
			Integer nextByClass = null;
			Object clazz = getClassOf(target);
			if (clazz != null) {
				nextByClass = indexes.get(clazz);
			}
			
			// check no inconsistency
			if (nextByRule != null && nextByClass != null && !nextByRule.equals(nextByClass)) {
				throw new Unable("Inconsistency found!");
			}
			
			// get next index
			Integer next = nextByRule == null ? nextByClass : nextByRule;
			if (next == null) { next = nextIndex++; }
			
			// install new index
			ptag.getUserInfoOf(target).setAttribute(indexAttr, next);
			rules.put(new SLPair(index, letter), next);
			if (clazz != null) {
				indexes.put(clazz, next);
			}
		}
		
	}
	
}
