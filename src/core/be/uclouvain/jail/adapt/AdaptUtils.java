package be.uclouvain.jail.adapt;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides utilities for adaptability. 
 * 
 * <p>This class provides the facade to all adaptability requests. Users may indeed invoke
 * the {@link #adapt(Object, Class)} on any object, even when the first is not IAdaptable.
 * External adapters may also be registered/unregistered here.</p>
 * 
 * @author blambeau
 */
public class AdaptUtils {

	/** A class pair. */
	static class ClassPair {
		
		/** Pair of classes. */
		private Class[] pair;
		
		/** Creates a pair instance. */
		public ClassPair(Class c, Class d) {
			this.pair = new Class[]{c,d};
		}
		
		/** Equals overriding. */
		public boolean equals(Object o) {
			if (o instanceof ClassPair == false) {
				return false;
			}
			return Arrays.equals(pair, ((ClassPair)o).pair);
		}

		/** hashCode overriding. */
		public int hashCode() {
			return Arrays.hashCode(pair);
		}
		
	}
	
	/** Adapters by class pairs. */
	private static Map<ClassPair,IAdapter> adapters = new HashMap<ClassPair,IAdapter>();

	/** 
	 * Tries to adapt <code>who</code> to <code>type</code> using external adapters.
	 * 
	 * <p>This method is provided for {@link IAdaptable} implementations to allow
	 * external adapters to try adaptation when not natural for who. It should never
	 * be used in other contexts than @{link #IAdaptable.adapt(Class)}.</p>
	 * 
	 * @param who adaptable object which requests the external adaptibility.
	 * @param type target adaptation type requested.
	 */ 
	public static <T> Object externalAdapt(IAdaptable who, Class<T> type) {
		if (who == null) {
			throw new IllegalArgumentException("Adapted object may be null.");
		}
		if (type == null) {
			throw new IllegalArgumentException("Requested type may be null.");
		}

		// check already ok
		if (type.isAssignableFrom(who.getClass())) {
			return who;
		}
		
		// find adapter and delegates
		ClassPair pair = new ClassPair(who.getClass(),type);
		IAdapter adapter = adapters.get(pair); 
		if (adapter == null) {
			return null;
		}
		return adapter.adapt(who, type);
	}
	
	/** 
	 * Tries to adapt <code>who</code> to <code>type</code>. 
	 * 
	 * <p>This method allows requesting adaptability on any object, even if it 
	 * does not implement IAdaptable. The implementation is such that when an 
	 * IAdaptable is provided as first argument this invocation is equivalent 
	 * to @{link #IAdaptable.adapt(Class)}.</p> 
	 * 
	 * @param who object which requests the adaptibility.
	 * @param type target adaptation type requested.
	 */
	public static <T> Object adapt(Object who, Class<T> type) {
		if (who == null) {
			throw new IllegalArgumentException("Adapted object may be null.");
		}
		if (type == null) {
			throw new IllegalArgumentException("Requested type may be null.");
		}
		
		// check already ok
		if (type.isAssignableFrom(who.getClass())) {
			return who;
		}
		
		// check by component himself
		if (who instanceof IAdaptable) {
			return ((IAdaptable)who).adapt(type);
		} else {
			// find adapter and delegates
			ClassPair pair = new ClassPair(who.getClass(),type);
			IAdapter adapter = adapters.get(pair); 
			if (adapter == null) {
				return null;
			}
			return adapter.adapt(who, type);
		}
	}
	
	/** 
	 * Registers an external adaptation. 
	 * 
	 * @param src source type of the provided adaptation.
	 * @param target target type of the provided adaptation.
	 * @param adapter adapter which provides the adaptation. 
	 */
	public static void register(Class src, Class target, IAdapter adapter) {
		if (src == null) {
			throw new IllegalArgumentException("Source type may be null.");
		}
		if (target == null) {
			throw new IllegalArgumentException("Target type may be null.");
		}
		if (adapter == null) {
			throw new IllegalArgumentException("Adapter may be null.");
		}

		ClassPair pair = new ClassPair(src,target);
		adapters.put(pair,adapter);
	}
	
}
