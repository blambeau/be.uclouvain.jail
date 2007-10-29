package be.uclouvain.jail.algo.graph.copy.match;

import net.chefbe.autogram.ast2.IASTNode;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.IUserInfoPopulator;

/**
 * Populator by GMatch expression.
 * 
 * @author blambeau
 */
public class GMatchPopulator implements IUserInfoPopulator<IUserInfo> {

	/** Root node. */
	private IASTNode node;
	
	/** Creates a match populator. */
	public GMatchPopulator(IASTNode node) {
		if (!GMatchNodes.MATCH_DO.equals(node.type())) {
			throw new IllegalArgumentException("MATCH_DO node expected, " + node.type() + " received.");
		}
		this.node = node;
	}

	/** Populates a target from a source. */
	public void populate(IUserInfo target, final IUserInfo source) {
		try {
			node.accept(new GMatchPopulatorCallback<IUserInfo>(source,target){

				/** Extracts from source. */
				@Override
				protected Object extractSourceAttributeValue(IUserInfo source, String key) {
					return source.getAttribute(key);
				}
				
				/** Callback method for FUNCTION_CALL nodes. */
				public Object FUNCTION_CALL(IASTNode node) throws Exception {
					throw new UnsupportedOperationException("Function calls non implemented yet!");
				}
				
			});
		} catch (Exception e) {
			throw new IllegalStateException("Error while GMatch populating ...",e);
		}
	}
	
}
