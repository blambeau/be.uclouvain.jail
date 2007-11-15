package be.uclouvain.jail.adapt;

import java.util.List;

/**
 * Encapsulates an adapter by chaining sub adapters.
 * 
 * <p>This class is mainly provided in order to implement the NetworkAdaptationTool 
 * and is not intended to be used directly by the user.</p>
 * 
 * @author blambeau
 */
public class ChainAdapter implements IAdapter {

	/** Aware adapter. */
	static class AwareAdapter implements IAdapter {
		
		/** Known adaptation target. */
		private Class target;

		/** Decorated adapter. */
		private IAdapter adapter;
		
		/** Creates an aware adapter. */
		public AwareAdapter(Class target, IAdapter adapter) {
			this.target = target;
			this.adapter = adapter;
		}

		/** Delegated to the adapter. */
		public Object adapt(Object who, Class type) {
			return adapter.adapt(who,type==null ? target : type);
		}
	
		/** Returns a string representation. */
		public String toString() {
			if (adapter instanceof CreateClassAdapter) {
				return adapter.toString();
			} else {
				return adapter.getClass().getSimpleName();
			}
		}
		
	}
	
	/** Chain. */
	private List<AwareAdapter> chain;
	
	/** Creates a chain instance. */
	public ChainAdapter(List<AwareAdapter> chain) {
		this.chain = chain;
	}

	/** Adapts who to the requested type. */
	public Object adapt(Object who, Class type) {
		for (AwareAdapter adapter: chain) {
			who = adapter.adapt(who, null);
			if (who == null) { return null; }
		}
		return who;
	}

}
