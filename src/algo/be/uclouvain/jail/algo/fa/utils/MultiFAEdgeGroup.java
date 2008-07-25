package be.uclouvain.jail.algo.fa.utils;

import be.uclouvain.jail.fa.IFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.utils.ITotalOrder;

/** 
 * Specialization of AbstractGroup that provides useful utilities in case
 * of edge groups.
 * 
 * @author blambeau
 */
public class MultiFAEdgeGroup extends AbstractMultiGroup {

	/** Group informer. */
	private IMultiFAGroupInformer informer;
	
	/** Creates a state group instance. */
	public MultiFAEdgeGroup(int[] components, IMultiFAGroupInformer informer) {
		super();
		this.informer = informer;
		super.setComponents(components);
	}

	/** Creates a state group instance. */
	public MultiFAEdgeGroup(Object[] components, IMultiFAGroupInformer informer) {
		super();
		this.informer = informer;
		super.setComponents(components);
	}

	/** Returns the i-th directed graph. */
	@Override
	public final IDirectedGraph getGraph(int i) {
		return getFA(i).getGraph();
	}

	/** Returns the i-th FA. */
	public final IFA getFA(int i) {
		return informer.getFA(i);
	}

	/** Returns the i-th edges total order. */
	@Override
	public final ITotalOrder<Object> getTotalOrder(int i) {
		return getGraph(i).getEdgesTotalOrder();
	}

	/** Returns the target state group. */
	public MultiFAStateGroup getTargetStateGroup(MultiFAStateGroup resolve) {
		// create target indexes
		int size = size();
		int[] targets = new int[size];
		
		// loop variables
		IDirectedGraph graph = null;
		Object edge = null;
		Object state = null;
		
		// find each target index
		for (int i=0; i<size; i++) {
			graph = getGraph(i);
			edge = getComponent(i);
			if (edge == null) {
				if (resolve == null) {
					return null;
				} else {
					targets[i] = resolve.getComponentIndex(i);
				}
			} else {
				state = graph.getEdgeTarget(edge);
				targets[i] = getVerticesTotalOrder(i).indexOf(state);
			}
		}
		
		// create state group
		return new MultiFAStateGroup(targets,informer);
	}

}
