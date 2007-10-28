package be.uclouvain.jail.algo.graph.copy.match;

import net.chefbe.autogram.ast2.IASTNode;
import net.chefbe.autogram.ast2.utils.CallbackBase;

/** Callback tool GMatch grammar. */
public class GMatchCallback<T> extends CallbackBase<T> {

	/** Creates a callback instance. */
	public GMatchCallback() {
		super(GMatchNodes.values(), GMatchCallback.class);
	}

	/** Callback method for MATCH_TEST nodes. */
	public T MATCH_TEST(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for MATCH_RULE nodes. */
	public T MATCH_RULE(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for MATCH_PATH nodes. */
	public T MATCH_PATH(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for MATCH_DO nodes. */
	public T MATCH_DO(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for MATCH_DOATTR nodes. */
	public T MATCH_DOATTR(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for ATTR_NAMEDEF nodes. */
	public T ATTR_NAMEDEF(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for ATTR_REF nodes. */
	public T ATTR_REF(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for CASE_EXPR nodes. */
	public T CASE_EXPR(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for WHEN_EXPR nodes. */
	public T WHEN_EXPR(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for ELSE_EXPR nodes. */
	public T ELSE_EXPR(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for FUNCTION_CALL nodes. */
	public T FUNCTION_CALL(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for LITERAL nodes. */
	public T LITERAL(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for BOOL_OREXPR nodes. */
	public T BOOL_OREXPR(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for BOOL_ANDEXPR nodes. */
	public T BOOL_ANDEXPR(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for BOOL_NOTEXPR nodes. */
	public T BOOL_NOTEXPR(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for BOOL_DYADEXPR nodes. */
	public T BOOL_DYADEXPR(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

	/** Callback method for BOOL_BOOLTERM nodes. */
	public T BOOL_BOOLTERM(IASTNode node) throws Exception {
		return nonOverrided(node);
	}

}
