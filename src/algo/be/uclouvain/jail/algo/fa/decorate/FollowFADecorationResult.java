package be.uclouvain.jail.algo.fa.decorate;

import java.util.ArrayList;
import java.util.List;

import be.uclouvain.jail.algo.fa.utils.FAStateGroup;
import be.uclouvain.jail.algo.graph.copy.match.GMatchAggregator;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.uinfo.IUserInfo;

/**
 * Follows DFAs. 
 * 
 * @author blambeau
 */
public class FollowFADecorationResult extends AbstractFADecorationResult {

	/** Decoration attribute. */
	private static final String DECORATION_ATTR = "FollowDFADecorationResult.groups";
	
	/** DFAs to follow. */
	private IDFA[] dfas;

	/** Creates a result instance. */
	public FollowFADecorationResult(IDFA[] dfas) {
		this.dfas = dfas;
	}

	/** Installs the result options. */
	@Override
	protected void installOptions() {
		super.installOptions();
		super.addOption("state", "stateAggregator", false, GMatchAggregator.class, null);
	}

	/** Sets a state aggregator. */
	public void setStateAggregator(GMatchAggregator a) {
		super.addVertexAggregator(a);
	}
	
	/** Cleans the DFA. */
	@Override
	public void ended() {
		for (Object state: super.graph.getVertices()) {
			List<FAStateGroup> deco = getDecoration(state);

			// take all user infos
			List<IUserInfo> infos = new ArrayList<IUserInfo>();
			for (FAStateGroup d: deco) {
				infos.addAll(d.getUserInfos());
			}
			
			// merge computed with info
			IUserInfo info = super.graph.getVertexInfo(state);
			IUserInfo added = super.getUserInfoHandler().vertexAggregate(infos);
			for (String key: added.getKeys()) {
				info.setAttribute(key, added.getAttribute(key));
			}
			
			// remove decoration attribute
			info.removeAttribute(DECORATION_ATTR);
		}
		super.ended();
	}

	/** Extracts a decoration from a state. */
	@SuppressWarnings("unchecked")
	private List<FAStateGroup> getDecoration(Object source) {
		return (List<FAStateGroup>) super.getStateAttr(source, DECORATION_ATTR);
	}
	
	/** Sets the decoration of a state. */
	private void setDecoration(Object source, List<FAStateGroup> deco) {
		super.setStateAttr(source, DECORATION_ATTR, deco);
	}
	
	/** Merges an old decoration with a propagated one. */
	private FAStateGroup merge(FAStateGroup old, FAStateGroup pd) {
		FAStateGroup merged = new FAStateGroup(old.getFA());
		merged.addMembers(old.getMembers());
		merged.addMembers(pd.getMembers());
		return merged;
	}
	
	/** Computes initial decoration. */
	@Override
	public boolean initDeco(Object state, boolean isInitial) {
		// compute initial state groups
		List<FAStateGroup> initDeco = new ArrayList<FAStateGroup>(dfas.length);
		for (IDFA dfa: dfas) {
			FAStateGroup initState = new FAStateGroup(dfa);
			if (isInitial) {
				initState.addState(dfa.getInitialState());
			}
			initDeco.add(initState);
		}
		
		// set the attribute
		setDecoration(state, initDeco);
		
		// mark as to explore
		return isInitial;
	}

	/** Propagates a decoration. */
	@Override
	public boolean propagate(Object source, Object edge, Object target) {
		// letter to propagate on
		Object letter = super.getEdgeLetter(edge);
		
		// source decoration
		List<FAStateGroup> sourceDeco = getDecoration(source);
		assert (sourceDeco != null) : "Source has been previously decorated.";
		
		// propagation
		int size = dfas.length;
		boolean propagate = false;
		List<FAStateGroup> targetDeco = getDecoration(target);
		for (int i=0; i<size; i++) {
			// source decoration on i-th DFA
			FAStateGroup sd = sourceDeco.get(i);
			
			// compute propagated decoration
			FAStateGroup pd = sd.getFA().getAlphabet().contains(letter) ?
				              sd.delta(letter).getTargetStateGroup() :
				              sd;
				              
		    // extract old one
		    FAStateGroup old = targetDeco.get(i);
		    
		    // compute new one
		    FAStateGroup thenew = merge(pd, old);

		    // sets it
		    targetDeco.set(i,thenew);
		    
		    // has changes ?
		    if (!propagate) { propagate = !thenew.equals(old); }
		}
		
		return propagate;
	}

}
