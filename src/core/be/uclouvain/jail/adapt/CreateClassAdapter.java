package be.uclouvain.jail.adapt;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.ConstructorUtils;

/** 
 * Provides simple adaptation scheme like <code>new AdaptedType(who)</code>.
 * 
 * <p>This adapter may be used when the adaptation of some instance <code>c</code> of 
 * class <code>C</code> to some instance <code>d</code> of class <code>D</code> may
 * simply be provided by <code>d = new D(c)</code>. This adaptation scheme is typical
 * of adaptibility by decoration.</p>
 */
public class CreateClassAdapter implements IAdapter {

	/** Decorator class. */
	private Class decoratorClass;
	
	/** Creates an adapter instance with decorator class. */
	public CreateClassAdapter(Class decoratorClass) {
		this.decoratorClass = decoratorClass;
	}

	/** 
	 * Adapts the who object to another type instance. 
	 * 
	 * @param who object which requests the adaptibility.
	 * @param type target adaptation type requested.
	 * @return an instance of class <code>type</code> (or one of its subclass), 
	 * null if the adaptation cannot be provided. 
	 */
	public Object adapt(Object who, Class type) {
		try {
			return ConstructorUtils.invokeConstructor(decoratorClass, who);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return null;
	}

	/** Returns a string representation. */
	public String toString() {
		return decoratorClass.getSimpleName();
	}
	
}
