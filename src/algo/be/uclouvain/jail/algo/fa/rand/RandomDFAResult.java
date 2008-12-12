package be.uclouvain.jail.algo.fa.rand;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import net.chefbe.javautils.collections.set.SetUtils;
import be.uclouvain.jail.algo.fa.determinize.NFADeterminizer;
import be.uclouvain.jail.algo.fa.minimize.DFAMinimizerAlgo;
import be.uclouvain.jail.algo.fa.minimize.DefaultDFAMinimizerInput;
import be.uclouvain.jail.algo.fa.minimize.DefaultDFAMinimizerResult;
import be.uclouvain.jail.algo.graph.rand.DefaultRandomGraphResult;
import be.uclouvain.jail.algo.graph.rand.IRandomGraphInput;
import be.uclouvain.jail.fa.FAStateKind;
import be.uclouvain.jail.fa.IAlphabet;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.INFA;
import be.uclouvain.jail.fa.constraints.AccessibleDFAGraphConstraint;
import be.uclouvain.jail.fa.impl.AttributeGraphFAInformer;
import be.uclouvain.jail.fa.impl.GraphDFA;
import be.uclouvain.jail.fa.impl.GraphNFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.utils.ITotalOrder;
import be.uclouvain.jail.uinfo.AbstractRandomPopulator;
import be.uclouvain.jail.uinfo.ConstantPopulator;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.UserInfoRandomizer;

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
		vertexR.addPopulator(new ConstantPopulator<Random>(AttributeGraphFAInformer.STATE_INITIAL_KEY,false));
		
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
	public IUserInfo createEdgeInfo(IDirectedGraph graph, Object source, Object target) {
		// let info be created by the super class
		IUserInfo info = super.createEdgeInfo(graph, source, target);
		
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
				             letters[randomizer.nextInt(letters.length)];
			info.setAttribute(AttributeGraphFAInformer.EDGE_LETTER_KEY, letter);
		}
		
		return info;
	}

	/** First clean the graph, the minimize it if required. */ 
	@Override
	public IDirectedGraph clean(IDirectedGraph g) {
		Set<Object> reachable = new HashSet<Object>();

		// pick an initial state at random
		ITotalOrder<Object> states = g.getVerticesTotalOrder();
		Object root = states.getElementAt(0);
		IUserInfo info = g.getVertexInfo(root);
		info.setAttribute(AttributeGraphFAInformer.STATE_INITIAL_KEY, Boolean.TRUE);

		//
		dfs(g, root, reachable);
		assert(reachable.size() > 0);
		for (Object state: states.getTotalOrder()) {
			if (!reachable.contains(state)) {
				assert (state != root);
				g.removeVertex(state);
			}
		}
		assert (g.getVerticesTotalOrder().size()>0);
		
		// determinize DFA is required
		if (determinize)  {
			INFA nfa = new GraphNFA(g);
			IDFA dfa = new NFADeterminizer(nfa).getResultingDFA(); 
			g = dfa.getGraph();
		}

		// minimize DFA if required
		if (minimize) {
			IDFA dfa = new GraphDFA(g);
			DefaultDFAMinimizerInput input = new DefaultDFAMinimizerInput(dfa);
			DefaultDFAMinimizerResult result = new DefaultDFAMinimizerResult();
			input.setConnex(false);
			new DFAMinimizerAlgo().execute(input, result);
			assert input.getDFA().getGraph().getVerticesTotalOrder().size()>0;
			dfa = result.getMinimalDFA(); 
			g = dfa.getGraph();
			assert g.getVerticesTotalOrder().size()>0;
		}
		assert new AccessibleDFAGraphConstraint().isRespectedBy(g);
		
		super.setDirectedGraph(g);
		return g;
	}

	/** Do a depth-first search. */
	private void dfs(IDirectedGraph g, Object vertex, Set<Object> reachable) {
		reachable.add(vertex);
		for (Object edge: g.getOutgoingEdges(vertex)) {
			Object target = g.getEdgeTarget(edge);
			if (!reachable.contains(target)) {
				dfs(g, target, reachable);
			}
		}
	}
	
	/** Provides adaptation to a dfa. */
	@Override
	public <T> Object adapt(Class<T> c) {
		if (c.isAssignableFrom(getClass())) {
			return this;
		}

		// provide adaptation to IDFA
		if (IDFA.class.equals(c)) {
			return new GraphDFA(g);
		}
		
		return super.adapt(c);
	}
	
}
