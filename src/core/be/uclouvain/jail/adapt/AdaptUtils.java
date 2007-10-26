package be.uclouvain.jail.adapt;

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

	/** Used tool. */
	private static IAdaptationTool tool = new NetworkAdaptationTool();
	
	/** Returns used adaptation tool. */
	public static IAdaptationTool getAdaptationTool() {
		return tool;
	}
	
	/** Sets the adaptation tool to use. */
	public static void setAdaptationTool(IAdaptationTool tool) {
		AdaptUtils.tool = tool;
	}
	
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
		return AdaptUtils.tool.externalAdapt(who, type);
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
		return AdaptUtils.tool.adapt(who, type);
	}
	
	/** 
	 * Registers an external adaptation. 
	 * 
	 * @param src source type of the provided adaptation.
	 * @param target target type of the provided adaptation.
	 * @param adapter adapter which provides the adaptation. 
	 */
	public static void register(Class src, Class target, IAdapter adapter) {
		AdaptUtils.tool.register(src, target, adapter);
	}
	
}
