package be.uclouvain.jail.algo.graph.copy.match;

import java.util.Set;

import net.chefbe.autogram.ast2.IASTNode;
import net.chefbe.javautils.collections.set.ListOrderedSet;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.IUserInfoPopulator;
import be.uclouvain.jail.uinfo.functions.AggregateFunctionFactory;
import be.uclouvain.jail.uinfo.functions.IAggregateFunction;
import be.uclouvain.jail.vm.JailVMException;

/**
 * Populator by GMatch expression.
 * 
 * @author blambeau
 */
public class GMatchAggregator implements IUserInfoPopulator<Set<IUserInfo>> {

	/** Root node. */
	private IASTNode node;
	
	/** Creates a match populator. */
	public GMatchAggregator(IASTNode node) {
		if (!GMatchNodes.MATCH_DO.equals(node.type())) {
			throw new IllegalArgumentException("MATCH_DO node expected, " + node.type() + " received.");
		}
		this.node = node;
	}

	/** Populates a target from a source. */
	public void populate(IUserInfo target, Set<IUserInfo> source) {
		try {
			node.accept(new GMatchPopulatorCallback<Set<IUserInfo>>(source,target){

				/** Uses the pick up function to extract the value. */
				@Override
				protected Object extractSourceAttributeValue(Set<IUserInfo> source, String key) {
					for (IUserInfo info: source) {
						Object value = info.getAttribute(key);
						if (value != null) {
							return value;
						}
					}
					return null;
				}
				
				/** Callback method for FUNCTION_CALL nodes. */
				@SuppressWarnings("unchecked")
				public Object FUNCTION_CALL(IASTNode node) throws Exception {
					// function name
					String name = node.getAttrString("name");
					
					// check single argument attribute
					IASTNode argNode = node.childFor("arg");
					if (!GMatchNodes.ATTR_REF.equals(argNode.type())) {
						throw new IllegalStateException("Bad aggregation function usage, ATTR_REF expected.");
					}
					
					// retrieve attribute key
					String attr = argNode.getAttrString("name");
					
					// find function
					IAggregateFunction func = AggregateFunctionFactory.getAggregateFunction(name);
					if (func == null) {
						throw JailVMException.unknownFunction(name);
					}
					
					Set<Object> values = new ListOrderedSet<Object>();
					for (IUserInfo info: source) {
						values.add(info.getAttribute(attr));
					}
					Object result = func.compute(values);
					
					//System.out.println("Result is " + result);
					return result;
				}
				
			});
		} catch (Exception e) {
			throw new IllegalStateException("Error while GMatch populating ...",e);
		}
	}

}
