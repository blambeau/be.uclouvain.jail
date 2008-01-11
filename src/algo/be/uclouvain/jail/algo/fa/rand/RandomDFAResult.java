package be.uclouvain.jail.algo.fa.rand;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import net.chefbe.javautils.collections.set.SetUtils;
import be.uclouvain.jail.algo.fa.determinize.NFADeterminizer;
import be.uclouvain.jail.algo.fa.minimize.DFAMinimizer;
import be.uclouvain.jail.algo.graph.rand.DefaultRandomGraphResult;
import be.uclouvain.jail.algo.graph.rand.IRandomGraphInput;
import be.uclouvain.jail.fa.FAStateKind;
import be.uclouvain.jail.fa.IAlphabet;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.INFA;
import be.uclouvain.jail.fa.impl.AttributeGraphFAInformer;
import be.uclouvain.jail.fa.impl.GraphDFA;
import be.uclouvain.jail.fa.impl.GraphNFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.uinfo.AbstractRandomPopulator;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.UserInfoRandomizer;
import be.uclouvain.jail.uinfo.functions.OnFirstPopulator;

/**
 * Specializes {@link DefaultRandomGraphResult} to generate DFAs.
 * 
 * @author blambeau
 */
public class RandomDFAResult extends DefaultRandomGraphResult {

	/** Minimize automaton? */
	private boolean minimize = true;

	/** Determinize automaton? */
	private boolean determinize = true;
	
	/** Alphabet letters cache. */
	private Set<Object> letters;
	
	/** Resulting dfa. */
	private IDFA dfa;
	
	/** Alphabet to use. */
	protected IAlphabet<?> alphabet;
	
	/** Installs the result. */
	public RandomDFAResult() {
	}

	/** Sets the options. */
	@Override
	protected void installOptions() {
		super.installOptions();
		super.addOption("minimize", false, Boolean.class, null);
		super.addOption("determinize", false, Boolean.class, null);
	}

	/** Minimize automaton at clean? */
	public void setMinimize(boolean minimize) {
		this.minimize = minimize;
	}

	/** Minimize automaton at clean? */
	public void setDeterminize(boolean determinize) {
		this.determinize = determinize;
	}

	/** Install special randomizers. */
	@Override
	public void started(IRandomGraphInput input) {
		super.started(input);
		this.alphabet = ((RandomDFAInput)input).alphabet;
		
		// get randomizers
		UserInfoRandomizer vertexR = super.getVertexRandomizer();

		// install initial function
		vertexR.addPopulator(new OnFirstPopulator<Random>(AttributeGraphFAInformer.STATE_INITIAL_KEY,true,false));
		
		// installs accepting randomizer if required
		final double accepting = ((RandomDFAInput)input).accepting; 
		if (accepting != -1) {
			super.getVertexRandomizer().addPopulator(
					new AbstractRandomPopulator(AttributeGraphFAInformer.STATE_KIND_KEY) {
						@Override
						public Object choose(Random r) {
							if (r.nextDouble() < accepting) {
								return FAStateKind.ACCEPTING;
							} else {
								return FAStateKind.PASSAGE;
							}
						}
					}
			);
		}
	}

	/** Creates an edge info. */
	@Override
	public IUserInfo createEdgeInfo(IDirectedGraph graph, Object source, Object target, Random r) {
		// let info be created by the super class
		IUserInfo info = super.createEdgeInfo(graph, source, target, r);
		
		// When user installs letter, just let do it
		if (info.hasAttribute(AttributeGraphFAInformer.EDGE_LETTER_KEY)) {
			return info;
		}
		
		// lazy letters creation
		if (letters == null) {
			letters = new HashSet<Object>();
			for (Object l: this.alphabet) {
				letters.add(l);
			}
		}
		
		// compute used letters
		Set<Object> used = new HashSet<Object>();
		for (Object edge: graph.getOutgoingEdges(source)) {
			used.add(graph.getEdgeInfo(edge).getAttribute(AttributeGraphFAInformer.EDGE_LETTER_KEY));
		}
		
		// compute available ones
		Set<Object> available = SetUtils.minus(letters, used);
		if (available.isEmpty()) {
			return null;
		} else {
			Object[] letters = available.toArray();
			Object letter = (letters.length==1) ? 
					         letters[0] : 
				             letters[r.nextInt(letters.length)];
			info.setAttribute(AttributeGraphFAInformer.EDGE_LETTER_KEY, letter);
		}
		
		return info;
	}

	/** First clean the graph, the minimize it if required. */ 
	@Override
	public IDirectedGraph clean(IDirectedGraph g) {
		// clean the graph, return false if it fails
		super.setConnex(true);
		g = super.clean(g);
		if (g == null) { return g; }
		
		// check that the initial state has not been removed
		boolean found = false;
		for (Object state: g.getVertices()) {
			IUserInfo info = g.getVertexInfo(state);
			if (info.hasAttribute(AttributeGraphFAInformer.STATE_INITIAL_KEY)) {
				Object attr = info.getAttribute(AttributeGraphFAInformer.STATE_INITIAL_KEY);
				if (Boolean.TRUE.equals(attr)) {
					found = true;
					break;
				}
			}
		}
		if (!found) {
			Object state = g.getVerticesTotalOrder().getElementAt(0);
			IUserInfo info = g.getVertexInfo(state);
			info.setAttribute(AttributeGraphFAInformer.STATE_INITIAL_KEY, Boolean.TRUE);
		}
		
		// minimize DFA if required
		if (minimize) {
			dfa = new GraphDFA(g);
			dfa = new DFAMinimizer(dfa).getMinimalDFA();
			g = dfa.getGraph();
		}

		// determinize DFA is required
		if (determinize)  {
			INFA nfa = new GraphNFA(g);
			dfa = new NFADeterminizer(nfa).getResultingDFA(); 
			g = dfa.getGraph();
		}
		
		super.setDirectedGraph(g);
		return g;
	}

	/** Provides adaptation to a dfa. */
	@Override
	public <T> Object adapt(Class<T> c) {
		if (c.isAssignableFrom(getClass())) {
			return this;
		}

		// provide adaptation to IDFA
		if (IDFA.class.equals(c)) {
			return dfa;
		}
		
		return super.adapt(c);
	}
	
}
