package be.uclouvain.jail.algo.induct.processor;

import java.util.HashMap;
import java.util.Map;

import be.uclouvain.jail.algo.commons.Unable;
import be.uclouvain.jail.algo.induct.internal.SLPair;
import be.uclouvain.jail.algo.utils.AbstractAlgoInput;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.constraints.PTAGraphConstraint;
import be.uclouvain.jail.graph.IDirectedGraph;

/**
 * Forwards labels.
 * 
 * @author blambeau
 */
public class ForwardLabelProcessor {

	/** Input of the algorithm. */
	public static class Input extends AbstractAlgoInput {
		
		/** Input PTA. */
		private IDFA pta;
		
		/** Class attribute to use. */
		private String sourceAttr;
		
		/** Target attribute to install. */
		private String targetAttr;

		/** Creates an input with source and target attributes. */
		public Input(IDFA pta, String sourceAttr, String targetAttr) {
			assert (new PTAGraphConstraint().isRespectedBy(pta));
			this.pta = pta;
			this.sourceAttr = sourceAttr;
			this.targetAttr = targetAttr;
		}

		/** Creates an input with default attributes. */
		public Input(IDFA pta) {
			this(pta, "class", "label");
		}
		
		/** Returns the PTA to labelize. */
		public IDFA getPTA() {
			return pta;
		}

		/** Installs options. */
		@Override
		protected void installOptions() {
			super.installOptions();
			super.addOption("source", "sourceAttr", false, String.class, "class");
			super.addOption("target", "targetAttr", false, String.class, "label");
		}

		/** Returns class attribute to use. */
		public String getSourceAttr() {
			return sourceAttr;
		}

		/** Sets class attribute to use. */
		public void setSourceAttr(String classAttr) {
			this.sourceAttr = classAttr;
		}

		/** Returns index attribute to use. */
		public String getTargetAttr() {
			return targetAttr;
		}

		/** Sets index attribute to use. */
		public void setTargetAttr(String indexAttr) {
			this.targetAttr = indexAttr;
		}
		
	}
	
	/** PTA graph. */
	private IDirectedGraph ptag;
	
	/** Source and target attributes. */
	private String classAttr, indexAttr;
	
	/** Next index. */
	private int nextIndex;
	
	/** Indexes by class. */
	private Map<Object, Integer> indexes;
	
	/** Propagation rules. */
	private Map<SLPair, Integer> rules;
	
	/** Creates a processor instance. */
	public ForwardLabelProcessor() {
	}
	
	/** Returns the class of a state. */
	private Object getClassOf(Object state) {
		return ptag.getVertexInfo(state).getAttribute(classAttr);
	}
	
	/** Returns the index of a state. */
	private Integer getIndexOf(Object state) {
		Integer index = (Integer) ptag.getVertexInfo(state).getAttribute(indexAttr);
		assert (index != null) : "Index has been set.";
		return index;
	}
	
	/** Processes an input. */
	public void process(Input input) {
		// install options
		this.ptag = input.getPTA().getGraph();
		this.classAttr = input.getSourceAttr();
		this.indexAttr = input.getTargetAttr();
		
		// initialize data structures
		nextIndex = 0;
		indexes = new HashMap<Object, Integer>();
		rules = new HashMap<SLPair, Integer>();
		
		// walk the PTA 
		new PTADepthFirstWalk() {
			
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
			
		}.walk(input.getPTA());
	}
	
	/** Walks a PTA. */
	static abstract class PTADepthFirstWalk {
		
		/** PTA. */
		private IDFA pta;
		
		/** Graph. */
		private IDirectedGraph ptag; 
		
		/** Walks a PTA. */
		public void walk(IDFA pta) {
			assert (new PTAGraphConstraint().isRespectedBy(pta)) : "Valid input PTA.";
			this.pta = pta;
			this.ptag = pta.getGraph();
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
		
		/** When visiting the root state. */
		protected abstract void onRoot(Object root);
		
		/** When visiting the target. */
		protected abstract void visiting(Object source, Object letter, Object target);
		
	}
	
}
