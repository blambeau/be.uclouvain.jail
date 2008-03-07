package be.uclouvain.jail.algo.graph.copy.match;

import net.chefbe.autogram.ast2.IASTNode;
import net.chefbe.autogram.ast2.parsing.ParseException;
import net.chefbe.autogram.ast2.parsing.active.ASTLoader;
import net.chefbe.autogram.ast2.parsing.active.ASTLoader.EnumTypeResolver;
import net.chefbe.autogram.ast2.utils.BaseLocation;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.IUserInfoHelper;
import be.uclouvain.jail.uinfo.IUserInfoPopulator;

/**
 * Populator by GMatch expression.
 * 
 * @author blambeau
 */
public class GMatchPopulator<T> implements IUserInfoPopulator<T> {

	/** Root node. */
	private IASTNode node;
	
	/** Callback to use. */
	private GMatchPopulatorCallback<T> callback;
	
	/** Creates a match populator. */
	public GMatchPopulator(IASTNode node, IUserInfoHelper helper) {
		if (!GMatchNodes.MATCH_DO.equals(node.type())) {
			throw new IllegalArgumentException("MATCH_DO node expected, " + node.type() + " received.");
		}
		this.node = node;
		this.callback = new GMatchPopulatorCallback<T>(helper) {
			protected Object extractSourceAttributeValue(T source, String key) {
				if (source instanceof IUserInfo) {
					return ((IUserInfo)source).getAttribute(key);
				} else {
					throw new IllegalArgumentException("IUserInfo expected.");
				}
			}
		};
	}

	/** Populates a target from a source. */
	public void populate(IUserInfo target, final T source) {
		try {
			callback.launchOn(node, source, target);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new IllegalStateException("Error while GMatch populating ...",e);
		}
	}
	
	/** Parses a GMatch expression and returns a populator. */
	public static <T> GMatchPopulator<T> parse(String expr, IUserInfoHelper helper) throws ParseException {
		GMatchParser parser = new GMatchParser();
		parser.setActiveLoader(new ASTLoader(new EnumTypeResolver<GMatchNodes>(GMatchNodes.class)));
		IASTNode root = parser.parse(GMatchNodes.MATCH_DO, new BaseLocation(expr));
		return new GMatchPopulator<T>(root,helper); 
	}
	
}
