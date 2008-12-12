package be.uclouvain.jail.algo.graph.connex;

import net.chefbe.javautils.adapt.AdaptUtils;
import be.uclouvain.jail.algo.graph.utils.IGraphPartition;

/** Default implementation of IGraphConXDetectorResult. */
public class DefaultGraphConXDetectorResult implements IGraphConXDetectorResult {

	/** Computed partition. */
	private IGraphPartition partition;
	
	/** Algorithm started event. */
	public void started(IGraphConXDetectorInput input) {
	}

	/** Algorithm ended event. */
	public void ended(IGraphPartition partition) {
		this.partition = partition;
	}
	
	public IGraphPartition getPartition() {
		return partition;
	}

	/** Adaptation. */
	public <T> Object adapt(Class<T> c) {
		if (c.isAssignableFrom(getClass())) {
			return this;
		}
		
		// natural adaptation to a partition
		if (IGraphPartition.class.equals(c)) {
			return partition;
		}
		
		return AdaptUtils.externalAdapt(this,c);
	}

}
