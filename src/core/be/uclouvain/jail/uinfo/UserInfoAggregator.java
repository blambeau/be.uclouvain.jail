package be.uclouvain.jail.uinfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import be.uclouvain.jail.fa.functions.FAStateKindFunction;
import be.uclouvain.jail.uinfo.functions.BoolAndFunction;
import be.uclouvain.jail.uinfo.functions.BoolOrFunction;
import be.uclouvain.jail.uinfo.functions.IAggregateFunction;
import be.uclouvain.jail.uinfo.functions.OnFirstPopulator;
import be.uclouvain.jail.uinfo.functions.PickUpFunction;

/**
 * Provides a base class to create IUserInfo instance by aggregations.
 *  
 * @author blambeau
 */
public class UserInfoAggregator implements IUserInfoCreator<Collection<IUserInfo>> {

	/** Aggregate function as a populator. */
	class AggregateFunctionPopulator implements IUserInfoPopulator<Collection<IUserInfo>> {

		/** Function to use. */
		private IAggregateFunction function;
		
		/** Source and target attributes. */
		private String sourceAttr, targetAttr;
		
		/** Creates a populator instance. */
		public AggregateFunctionPopulator(IAggregateFunction function, String sourceAttr, String targetAttr) {
			this.function = function;
			this.sourceAttr = sourceAttr;
			this.targetAttr = targetAttr;
		}

		@SuppressWarnings("unchecked")
		public void populate(IUserInfo target, Collection<IUserInfo> source) {
			List<Object> values = new ArrayList<Object>();
			for (IUserInfo info: source) {
				values.add(info.getAttribute(sourceAttr));
			}
			target.setAttribute(targetAttr, function.compute(values));
		}
		
	}
	
	/** Populators to use. */
	private List<IUserInfoPopulator<Collection<IUserInfo>>> populators;

	/** Creates an aggregator instance. */
	public UserInfoAggregator() {
		populators = new ArrayList<IUserInfoPopulator<Collection<IUserInfo>>>();
	}

	/** Adds a populator. */
	public UserInfoAggregator addPopulator(IUserInfoPopulator<Collection<IUserInfo>> populator) {
		if (populator == null) {
			throw new IllegalArgumentException("populator cannot be null.");
		}
		this.populators.add(populator);
		return this;
	}
	
	/** Registers a function for a given attribute. */
	public void register(String attr, IAggregateFunction function) {
		register(attr,attr,function);
	}
	
	/** Registers a function for a given attribute. */
	public void register(String source, String target, IAggregateFunction function) {
		addPopulator(new AggregateFunctionPopulator(function,source,target));
	}
	
	/** Registers an onFirst function. */
	public void onFirst(String attr, Object firstValue, Object otherValue) {
		addPopulator(new OnFirstPopulator<Collection<IUserInfo>>(attr, firstValue, otherValue));
	}
	
	/** Registers a Boolean-OR function for a given attribute. */ 
	public void boolOr(String attr) {
		register(attr,new BoolOrFunction());
	}
	
	/** Registers a Boolean-AND function for a given attribute. */ 
	public void boolAnd(String attr) {
		register(attr,new BoolAndFunction());
	}
	
	/** Registers a first function. */
	public void first(String attr) {
		register(attr,new PickUpFunction());
	}
	
	/** Factors a list function. */
	public void stateKind(String attr, int acceptingOp, int errorOp, boolean throwOnAvoid) {
		register(attr,new FAStateKindFunction(acceptingOp,errorOp,throwOnAvoid));
	}

	/** 
	 * Factors a IUserInfo instance.
	 * 
	 * <p>This method creates a MapUserInfo instance by default
	 * and may be overrided to create instances of another class.</p>
	 * 
	 * @return newly created instance of a IUserInfo.
	 */
	protected IUserInfo factor() {
		return new MapUserInfo();
	}
	
	/** Creates a IUserInfo instance from another one. */
	public IUserInfo create(IUserInfo...info) {
		List<IUserInfo> list = new ArrayList<IUserInfo>();
		for (IUserInfo i: info) {
			list.add(i);
		}
		return create(list);
	}
	
	/** Creates a IUserInfo instance from another one. */
	public IUserInfo create(Collection<IUserInfo> info) {
		IUserInfo copy = factor();
		for (IUserInfoPopulator<Collection<IUserInfo>> p : populators) {
			p.populate(copy, info);
		}
		return copy;
	}

}
