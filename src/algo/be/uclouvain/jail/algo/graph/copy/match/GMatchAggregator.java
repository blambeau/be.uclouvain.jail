package be.uclouvain.jail.algo.graph.copy.match;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.chefbe.autogram.ast2.IASTNode;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.IUserInfoHelper;
import be.uclouvain.jail.uinfo.IUserInfoPopulator;
import be.uclouvain.jail.uinfo.functions.AggregateFunctionFactory;
import be.uclouvain.jail.uinfo.functions.IAggregateFunction;

/**
 * Populator by GMatch expression.
 * 
 * @author blambeau
 */
public class GMatchAggregator implements IUserInfoPopulator<Collection<IUserInfo>> {

	/** Root node. */
	private IASTNode node;
	
	/** Helper to use. */
	private IUserInfoHelper helper;
	
	/** Creates a match populator. */
	public GMatchAggregator(IASTNode node, IUserInfoHelper helper) {
		if (!GMatchNodes.MATCH_DO.equals(node.type())) {
			throw new IllegalArgumentException("MATCH_DO node expected, " + node.type() + " received.");
		}
		this.node = node;
		this.helper = helper;
	}

	/** Populates a target from a source. */
	public void populate(IUserInfo target, Collection<IUserInfo> source) {
		try {
			node.accept(new GMatchPopulatorCallback<Collection<IUserInfo>>(source,target,helper){

				/** Uses the pick up function to extract the value. */
				@Override
				protected Object extractSourceAttributeValue(Collection<IUserInfo> source, String key) {
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
					
					// find function
					IAggregateFunction func = AggregateFunctionFactory.getAggregateFunction(name);
					if (func == null) {
						return super.FUNCTION_CALL(node);
					}
					
					// check single argument attribute
					IASTNode argNode = node.childFor("arg");
					if (!GMatchNodes.ATTR_REF.equals(argNode.type())) {
						return super.FUNCTION_CALL(node);
					}
					
					// retrieve attribute key
					String attr = argNode.getAttrString("name");
					
					// extracts IUserInfo
					List<Object> values = new ArrayList<Object>();
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
