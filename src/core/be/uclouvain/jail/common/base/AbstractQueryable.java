package be.uclouvain.jail.common.base;

import java.lang.reflect.InvocationTargetException;

import net.chefbe.javautils.robust.exceptions.CoreException;

import org.apache.commons.beanutils.PropertyUtils;

import be.uclouvain.jail.common.IQueryable;

/**
 * Provides a base implementation for queryable.
 * 
 * @author blambeau
 */
public class AbstractQueryable<T> implements IQueryable {

	/** Queried object. */
	protected T queried;
	
	/** Creates a queryable on an object. */
	public AbstractQueryable(T queried) {
		this.queried = queried;
	}

	/** Returns a property. */
	public Object getProperty(String property) {
		try {
			return PropertyUtils.getProperty(queried,property);
		} catch (IllegalAccessException e) {
			throw new CoreException(e);
		} catch (InvocationTargetException e) {
			throw new CoreException(e);
		} catch (NoSuchMethodException e) {
			throw new CoreException(e);
		}
	}

}
