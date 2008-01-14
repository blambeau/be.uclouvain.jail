package be.uclouvain.jail.algo.induct.sample;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import net.chefbe.javautils.adapt.AdaptUtils;
import be.uclouvain.jail.algo.fa.determinize.NFADeterminizer;
import be.uclouvain.jail.fa.IAlphabet;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.IFATrace;
import be.uclouvain.jail.fa.INFA;
import be.uclouvain.jail.fa.constraints.MCAGraphConstraint;
import be.uclouvain.jail.fa.utils.DefaultFATrace;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.utils.DefaultDirectedGraphPath;

/**
 * Decorates a MCA as a sample.
 * 
 * @author blambeau
 * @param <L>
 */
public class MCASample<L> implements IExtensibleSample<L> {

	/** Decorated nfa. */
	private INFA nfa;
	
	/** Initial states. */
	private List<Object> inits;

	/** Creates a sample instance. */
	public MCASample(INFA nfa) {
		assert (new MCAGraphConstraint().isRespectedBy(nfa.getGraph())) : "Valid MCA.";
		this.nfa = nfa;
		this.inits = new ArrayList<Object>();
		for (Object init: nfa.getInitialStates()) {
			inits.add(init);
		}
	}
	
	/** Returns the sample alphabet. */
	public IAlphabet<L> getAlphabet() {
		return nfa.getAlphabet();
	}

	/** Returns number of strings. */
	public int size() {
		return inits.size();
	}

	/** Returns an iterator on strings. */
	public Iterator<ISampleString<L>> iterator() {
		return new Iterator<ISampleString<L>>() {

			/** Iterator on init states. */
			private Iterator<Object> it = inits.iterator();
			
			/** Returns true if a next string exists. */
			public boolean hasNext() {
				return it.hasNext();
			}

			/** Extracts a trace from an init state. */
			private IFATrace<L> extractTrace(Object init) {
				IDirectedGraph graph = nfa.getGraph();
				DefaultDirectedGraphPath path = new DefaultDirectedGraphPath(graph,init);
				
				// fill the path
				Collection<Object> outs = graph.getOutgoingEdges(init);
				while (!outs.isEmpty()) {
					
					// we expect one outgoing edge only by state on the MCA
					if (outs.size() != 1) { 
						throw new AssertionError("One outgoing edge expected.");
					}
					
					// take edge, add it to the path
					Object edge = outs.iterator().next();
					path.addEdge(edge);
					
					// continue with reaching state
					init = graph.getEdgeTarget(edge);
				}
				
				return new DefaultFATrace<L>(nfa,path);
			}
			
			/** Returns the next string. */
			public ISampleString<L> next() {
				if (!hasNext()) { throw new NoSuchElementException(); }
				Object init = it.next();
				IFATrace<L> trace = extractTrace(init);
				return new FATraceSampleString<L>(trace);
			}

			/** Throws Unsupported. */
			public void remove() {
				throw new UnsupportedOperationException();
			}
			
		};
	}

	/** Adds a sample string, by writing in the nfa. */
	public void addSampleString(ISampleString<L> string) {
		Object[] states = null;
		if (string instanceof IFAAwareString) {
			states = ((IFAAwareString<L>)string).fill(nfa.getGraph());
		} else {
			DefaultSampleString<L> s2 = new DefaultSampleString<L>(string);
			states = s2.fill(nfa.getGraph());
		}
		inits.add(states[0]);
	}

	/** Provides adaptations. */
	public <T> Object adapt(Class<T> c) {
		if (c.isAssignableFrom(getClass())) {
			return this;
		}
		
		// provide classical adaptations
		if (IDirectedGraph.class.equals(c)) {
			return nfa.getGraph();
		} else if (INFA.class.equals(c)) {
			return nfa;
		} else if (IDFA.class.equals(c)) {
			return new NFADeterminizer(nfa).getResultingDFA();
		}
		 
		// allow external adapters to do their work
		return AdaptUtils.externalAdapt(this,c);
	}

}
