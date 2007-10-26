package be.uclouvain.jail.adapt;

/**
 * Provides a way to get all external adaptations under specific control. 
 * 
 * @author blambeau
 */
public interface IAdaptationTool {

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
	public <T> Object externalAdapt(IAdaptable who, Class<T> type);
	
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
	public <T> Object adapt(Object who, Class<T> type);
	
	/** 
	 * Registers an external adaptation. 
	 * 
	 * @param src source type of the provided adaptation.
	 * @param target target type of the provided adaptation.
	 * @param adapter adapter which provides the adaptation. 
	 */
	public void register(Class src, Class target, IAdapter adapter);
	
}
