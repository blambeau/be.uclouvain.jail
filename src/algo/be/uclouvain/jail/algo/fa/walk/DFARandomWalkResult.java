package be.uclouvain.jail.algo.fa.walk;

import be.uclouvain.jail.algo.commons.Unable;
import be.uclouvain.jail.algo.fa.determinize.NFADeterminizer;
import be.uclouvain.jail.algo.graph.copy.match.GMatchPopulator;
import be.uclouvain.jail.algo.graph.walk.DefaultRandomWalkResult;
import be.uclouvain.jail.algo.graph.walk.IRandomWalkInput;
import be.uclouvain.jail.algo.induct.sample.ISample;
import be.uclouvain.jail.algo.induct.sample.MCASample;
import be.uclouvain.jail.fa.FAStateKind;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.INFA;
import be.uclouvain.jail.fa.impl.AttributeGraphFAInformer;
import be.uclouvain.jail.fa.impl.GraphNFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.IDirectedGraphPath;
import be.uclouvain.jail.graph.utils.DirectedGraphWriter;
import be.uclouvain.jail.uinfo.IUserInfo;

/**
 * Specialization of {@link DefaultRandomWalkResult} for DFA walks.
 * 
 * @author blambeau
 */
public class DFARandomWalkResult extends DefaultRandomWalkResult {

	/** Associated input. */
	private DFARandomWalkInput input;
	
	/** Tries. */
	private int tries = 0;
	
	/** When started ... */
	@Override
	public void started(IRandomWalkInput input) {
		if (input instanceof DFARandomWalkInput == false) {
			throw new Unable("DFARandomWalkResult expects a DFARandomWalkInput as associated input");
		}
		this.input = (DFARandomWalkInput) input;
		super.started(input);
	}

	/** Install options. */
	@Override
	protected void installOptions() {
		super.installOptions();
		super.addOption("state", "vertexPopulator", false, GMatchPopulator.class, null);
	}

	/** Flushes the path inside a writer. */
	@Override
	protected void flushPath(IDirectedGraphPath path, DirectedGraphWriter writer) {
		Object[] vertices = path.flush(writer);
		for (int i=0; i<vertices.length; i++) {
			Object vertex = vertices[i];
			IUserInfo info = writer.getVertexInfo(vertex);
			
			// set initial
			info.setAttribute(AttributeGraphFAInformer.STATE_INITIAL_KEY,i==0);
			
			// set kind
			String attr = AttributeGraphFAInformer.STATE_KIND_KEY;
			boolean last = (i==vertices.length-1);
			FAStateKind oldKind = (FAStateKind) info.getAttribute(attr);
			if (FAStateKind.PASSAGE.equals(oldKind)) {
				info.setAttribute(attr,last ? FAStateKind.ERROR : FAStateKind.PASSAGE);
			} else if (FAStateKind.ACCEPTING.equals(oldKind)) {
				info.setAttribute(attr,last ? FAStateKind.ACCEPTING : FAStateKind.PASSAGE);
			} else if (FAStateKind.ERROR.equals(oldKind)) {
				info.setAttribute(attr,last ? FAStateKind.ERROR : FAStateKind.ERROR);
			} else if (FAStateKind.AVOID.equals(oldKind)) {
				info.setAttribute(attr,FAStateKind.AVOID);
			}
		}
	}

	/** Checks path length. */
	@Override
	public void addWalkPath(IDirectedGraphPath path) {
	    // when ok, test tolerance and max tries
		boolean isOk = true;
        if (input.isStopOnPathLength() && input.tolerance != -1) {
			double wish = input.getPathLength();
			double actual = path.size();
			double diff = Math.abs(wish-actual)/wish;
			isOk = diff<input.tolerance;
			if (!isOk && (++tries > input.maxTry)) { 
				throw new Unable();
			}
        }

        // add path when ok
		if (isOk) {
			super.addWalkPath(path);
		}
	}

	/** Provides adaptations. */
	@Override
	public <T> Object adapt(Class<T> c) {
		if (c.isAssignableFrom(getClass())) {
			return this;
		}

		if (INFA.class.equals(c)) {
			return new GraphNFA((IDirectedGraph)super.adapt(IDirectedGraph.class));
		} else if (IDFA.class.equals(c)) {
			INFA nfa = new GraphNFA((IDirectedGraph)super.adapt(IDirectedGraph.class));
			return new NFADeterminizer(nfa).getResultingDFA();
		} else if (ISample.class.equals(c)) {
			INFA nfa = new GraphNFA((IDirectedGraph)super.adapt(IDirectedGraph.class));
			return new MCASample(nfa);
		}
		
		return super.adapt(c);
	}

}
