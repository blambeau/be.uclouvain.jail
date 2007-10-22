package be.uclouvain.jail.algo.graph.shortest.dsp;

import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.utils.JavaUtils;

/** Weight informer which extracts the distance from an attribute. */  
public class AttributeWeightInformer implements IWeightInformer<Number> {

	/** Distance attribute to use. */
	private String distAttr;
	
	/** Creates a weight informer. */
	public AttributeWeightInformer(String distAttr) {
		this.distAttr = distAttr;
	}

	/** Extract the weight from an attribute. */
	public Number weight(IDirectedGraph graph, Object edge) {
		return (Number) graph.getEdgeInfo(edge).getAttribute(distAttr);
	}

	/** Returns 0. */
	public Number getNullDistance() {
		return 0;
	}

	/** Returns null. */
	public Number getInfinityDistance() {
		return null;
	}

	/** Returns d+e. */
	public Number sum(Number d, Number e) {
		return (d==null||e==null) ? null : JavaUtils.sum(d,e);
	}

	/** Compares two integers. */
	public int compare(Number d,Number e) {
		if (d==null && e==null) { return 0; }
		else if (d==null) { return 1; }
		else if (e==null) { return -1; }
		return JavaUtils.compare(d,e);
	}

}
