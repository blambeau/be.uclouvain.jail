package be.uclouvain.jail.uinfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import be.uclouvain.jail.uinfo.functions.BoolAndFunction;
import be.uclouvain.jail.uinfo.functions.BoolOrFunction;
import be.uclouvain.jail.uinfo.functions.FirstFunction;
import be.uclouvain.jail.uinfo.functions.IAggregateFunction;

/**
 * Provides a base class to create IUserInfo instance by aggregations.
 *  
 * @author blambeau
 */
public class UserInfoAggregator implements IUserInfoCreator<Set<IUserInfo>> {

	/** Registered functions. */
	private Map<String[],IAggregateFunction> functions;
	
	/** Creates an aggregator instance. */
	public UserInfoAggregator() {
		functions = new HashMap<String[],IAggregateFunction>();
	}

	/** Registers a function for a given attribute. */
	public void register(String attr, IAggregateFunction function) {
		register(attr,attr,function);
	}
	
	/** Registers a function for a given attribute. */
	public void register(String source, String target, IAggregateFunction function) {
		functions.put(new String[]{source,target}, function);
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
		register(attr,new FirstFunction());
	}
	
	/** Extracts the operands. */
	private List<Object> extractOperands(String attr, Set<IUserInfo> sources) {
		List<Object> operands = new ArrayList<Object>();
		for (IUserInfo info: sources) {
			operands.add(info.getAttribute(attr));
		}
		return operands;
	}
	
	/** Creates a user info instance. */
	@SuppressWarnings("unchecked")
	public IUserInfo create(Set<IUserInfo> info) {
		IUserInfo target = new MapUserInfo();
		for (Map.Entry<String[],IAggregateFunction> entry: functions.entrySet()) {
			String[] attrs = entry.getKey();
			IAggregateFunction function = entry.getValue();
			
			// compute value
			Object value = function.compute(extractOperands(attrs[0],info));
			
			// set as new attr
			target.setAttribute(attrs[1], value);
		}
		return target;
	}

}
