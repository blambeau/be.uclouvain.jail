package be.uclouvain.jail.algo.induct.processor;

import java.util.HashMap;
import java.util.Map;

import be.uclouvain.jail.algo.commons.Unable;
import be.uclouvain.jail.algo.induct.internal.InductionAlgo;
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
public class ForwardLabelProcessor implements IInductionProcessor {

	/** Source attribute to use by default. */
	public static final String DEFAULT_SOURCE_ATTR = "ForwardLabelProcessorSourceAttr";
	
	/** Target attribute to use by default. */
	public static final String DEFAULT_TARGET_ATTR = "ForwardLabelProcessorTargetAttr";
	
	/** Input of the algorithm. */
	public static class Input extends AbstractAlgoInput {
		
		/** Input PTA. */
		protected IDFA pta;
		
		/** Class attribute to use. */
		protected String sourceAttr;
		
		/** Target attribute to install. */
		protected String targetAttr;

		/** Unknown value. */
		protected Object unknown;
		
		/** Creates an input with source and target attributes. */
		public Input(IDFA pta, String sourceAttr, String targetAttr) {
			assert (new PTAGraphConstraint().isRespectedBy(pta));
			this.pta = pta;
			this.sourceAttr = sourceAttr;
			this.targetAttr = targetAttr;
		}

		/** Creates an input with default attributes. */
		public Input(IDFA pta) {
			this(pta, DEFAULT_SOURCE_ATTR, DEFAULT_TARGET_ATTR);
		}
		
		/** Returns the PTA to labelize. */
		public IDFA getPTA() {
			return pta;
		}

		/** Installs options. */
		@Override
		protected void installOptions() {
			super.installOptions();
			super.addOption("source", "sourceAttr", false, String.class, DEFAULT_SOURCE_ATTR);
			super.addOption("target", "targetAttr", false, String.class, DEFAULT_TARGET_ATTR);
			super.addOption("unknown", "unknown", false, Object.class, null);
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

		/** Returns unknown value. */
		public Object getUnknown() {
			return unknown;
		}

		/** Sets unknown value. */
		public void setUnknown(Object unknown) {
			this.unknown = unknown;
		}
		
	}
	
	/** Algorithm input. */
	private Input input;

	/** PTA graph. */
	private IDirectedGraph ptag;
	
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
		return ptag.getVertexInfo(state).getAttribute(input.sourceAttr);
	}
	
	/** Returns the index of a state. */
	private Integer getIndexOf(Object state) {
		Integer index = (Integer) ptag.getVertexInfo(state).getAttribute(input.targetAttr);
		assert (index != null) : "Index has been set.";
		return index;
	}
	
	/** Process an induction algorithm. */
	public void process(InductionAlgo algo) {
		String repAttr = algo.getInfo().getRepresentorAttr();
		assert (repAttr != null) : "Representor attribute has been set.";
		Input input = new Input(
				algo.getPTA(), 
				repAttr,
				DEFAULT_TARGET_ATTR);
		input.setUnknown(algo.getInfo().getUnknown());
		process(input);
	}

	/** Processes an input. */
	public void process(final Input input) {
		// install options
		this.input = input;
		this.ptag = input.getPTA().getGraph();
		
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
				if (clazz != null && !clazz.equals(input.unknown)) {
					indexes.put(clazz, index);
				}
				ptag.getUserInfoOf(root).setAttribute(input.targetAttr, index);
			}
			
			/** When visiting a triple. */ 
			protected void visiting(Object source, Object letter, Object target) {
				Integer sourceIndex = getIndexOf(source);
				
				// first look at rules
				Integer nextByRule = rules.get(new SLPair(sourceIndex,letter));

				// next look at class
				Integer nextByClass = null;
				Object clazz = getClassOf(target);
				if (clazz != null && !clazz.equals(input.unknown)) {
					nextByClass = indexes.get(clazz);
				}
				
				// check no inconsistency
				if (nextByRule != null && nextByClass != null && !nextByRule.equals(nextByClass)) {
					throw new Unable("Inconsistency found!");
				}
				
				// get next index
				Integer targetIndex = nextByRule == null ? nextByClass : nextByRule;
				if (targetIndex == null) { targetIndex = nextIndex++; }
				
				// install new index
				ptag.getUserInfoOf(target).setAttribute(input.targetAttr, targetIndex);
				rules.put(new SLPair(sourceIndex, letter), targetIndex);
				if (clazz != null && !clazz.equals(input.unknown)) {
					indexes.put(clazz, targetIndex);
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
