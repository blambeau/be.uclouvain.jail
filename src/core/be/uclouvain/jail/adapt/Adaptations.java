package be.uclouvain.jail.adapt;

import java.util.HashMap;
import java.util.Map;

/**
 * Acts as a collection of adaptations, through adapters.
 * 
 * <p>This class is mainly provided to help implementing the {@link IOpenAdaptable}
 * interface. It can also be used to implement an adapter by aggregation as it 
 * declares itself as an IAdapter.</p>
 * 
 * @author blambeau
 */
public class Adaptations implements IAdapter {

	/** Adaptations by type. */
	private Map<Class, IAdapter> adaptations;
	
	/** Creates a collection instance. */
	public Adaptations() {
		adaptations = new HashMap<Class, IAdapter>();
	}

	/** Registers an adapter for some adaptation type. */
	public void register(Class c, IAdapter a) {
		this.adaptations.put(c, a);
	}
	
	/** Adapts who object to the requested type. */
	public Object adapt(Object who, Class c) {
		IAdapter adapter = adaptations.get(c);
		return adapter == null ? null : adapter.adapt(who, c);
	}
	
}
