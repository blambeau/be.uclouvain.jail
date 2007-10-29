package be.uclouvain.jail.algo.fa.compose;

import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.utils.ITotalOrder;

/** 
 * Specialization of AbstractGroup that provides useful utilities in case
 * of state groups.
 * 
 * @author blambeau
 */
public class DFAEdgeGroup extends AbstractGroup {

	/** Group informer. */
	private IDFAGroupInformer informer;
	
	/** Creates a state group instance. */
	public DFAEdgeGroup(int[] components, IDFAGroupInformer informer) {
		super(components);
		this.informer = informer;
	}

	/** Creates a state group instance. */
	public DFAEdgeGroup(Object[] components, IDFAGroupInformer informer) {
		super();
		this.informer = informer;
		super.setComponents(components);
	}

	/** Returns the i-th directed graph. */
	@Override
	public final IDirectedGraph getGraph(int i) {
		return getDFA(i).getGraph();
	}

	/** Returns the i-th DFA. */
	public final IDFA getDFA(int i) {
		return informer.getDFA(i);
	}

	/** Returns the i-th edges total order. */
	@Override
	public final ITotalOrder<Object> getTotalOrder(int i) {
		return getGraph(i).getEdgesTotalOrder();
	}

	/** Returns the target state group. */
	public DFAStateGroup getTargetStateGroup(DFAStateGroup resolve) {
		// create target indexes
		int size = size();
		int[] targets = new int[size];
		
		// i-th graph
		IDirectedGraph graph;
		
		// find each target index
		for (int i=0; i<size; i++) {
			graph = getGraph(i);
			Object edge = getComponent(i);
			if (edge == null) {
				if (resolve == null) {
					return null;
				} else {
					targets[i] = resolve.getComponentIndex(i);
				}
			} else {
				Object state = graph.getEdgeTarget(edge);
				targets[i] = getVerticesTotalOrder(i).indexOf(state);
			}
		}
		
		// create state group
		return new DFAStateGroup(targets,informer);
	}

}
