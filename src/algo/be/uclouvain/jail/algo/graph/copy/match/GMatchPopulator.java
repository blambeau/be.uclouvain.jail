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

	/** Continue in WHEN/ELSE expressions. */
	private static Object contin = new Object();
	
	/** Root node. */
	private IASTNode node;
	
	/** Source user info. */
	private IUserInfo source;
	
	/** Target user info. */
	private IUserInfo target;
	
	/** Creates a match populator. */
	public GMatchPopulator(IASTNode node) {
		if (!GMatchNodes.MATCH_DO.equals(node.type())) {
			throw new IllegalArgumentException("MATCH_DO node expected, " + node.type() + " received.");
		}
		this.node = node;
	}

	/** Populates a target from a source. */
	public void populate(IUserInfo target, IUserInfo source) {
		this.source = source;
		this.target = target;
		try {
			node.accept(new GMatchPopulatorCallback());
		} catch (Exception e) {
			throw new IllegalStateException("Error while GMatch populating ...",e);
		}
	}

	/** A match callback. */
	class GMatchPopulatorCallback extends GMatchCallback<Object> {
		
		/** Callback method for MATCH_DO nodes. */
		public Object MATCH_DO(IASTNode node) throws Exception {
			// apply each attribute rule
			return super.recurseOnChildren(node);
		}

		/** Callback method for MATCH_DOATTR nodes. */
		public Object MATCH_DOATTR(IASTNode node) throws Exception {
			// evaluate attribute key
			Object key = super.makeCall(node.childFor("key"));
			if (key == null) { throw new IllegalStateException("No key returned by <gm:attr_expr>"); }
			String keyAttr = key.toString();
			
			// evaluate attrute value
			Object value = super.makeCall(node.childFor("value"));
			
			// set pair
			if (value != null) {
				target.setAttribute(keyAttr, value);
			} else {
				System.err.println("Warning: ignoring null value on attribute set @" + key);
			}
			return null;
		}

		/** Callback method for ATTR_NAMEDEF nodes. */
		public Object ATTR_NAMEDEF(IASTNode node) throws Exception {
			return node.getAttrString("name");
		}

		/** Callback method for ATTR_REF nodes. */
		public Object ATTR_REF(IASTNode node) throws Exception {
			String key = node.getAttrString("name");
			return source.getAttribute(key);
		}

		/** Callback method for FUNCTION_CALL nodes. */
		public Object FUNCTION_CALL(IASTNode node) throws Exception {
			throw new UnsupportedOperationException("Function calls non implemented yet!");
		}

		/** Callback method for LITERAL nodes. */
		public Object LITERAL(IASTNode node) throws Exception {
			return node.getAttr("value");
		}

		/** Callback method for CASE_EXPR nodes. */
		public Object CASE_EXPR(IASTNode node) throws Exception {
			for (IASTNode child: node.children()) {
				Object childValue = makeCall(child);
				if (!contin.equals(childValue)) {
					return childValue;
				}
			}
			return null;
		}

		/** Callback method for WHEN_EXPR nodes. */
		public Object WHEN_EXPR(IASTNode node) throws Exception {
			Object test = makeCall(node.childFor("test"));
			if (test instanceof Boolean == false) {
				throw new IllegalStateException("WHEN_EXPR with non boolean test.");
			}
			
			if (Boolean.TRUE.equals(test)) {
				return node.getAttr("value");
			} else {
				return contin;
			}
		}

		/** Callback method for ELSE_EXPR nodes. */
		public Object ELSE_EXPR(IASTNode node) throws Exception {
			return node.getAttr("value");
		}

		/** Callback method for BOOL_OREXPR nodes. */
		public Object BOOL_OREXPR(IASTNode node) throws Exception {
			Object left = makeCall(node.childFor("left"));
			if (left instanceof Boolean == false) {
				throw new IllegalStateException("BOOL_OREXPR with non boolean left part.");
			}
			
			if (Boolean.TRUE.equals(left)) { return Boolean.TRUE; }
			else return makeCall(node.childFor("right"));
		}

		/** Callback method for BOOL_ANDEXPR nodes. */
		public Object BOOL_ANDEXPR(IASTNode node) throws Exception {
			Object left = makeCall(node.childFor("left"));
			if (left instanceof Boolean == false) {
				throw new IllegalStateException("BOOL_ANDEXPR with non boolean left part.");
			}
			
			if (Boolean.FALSE.equals(left)) { return Boolean.FALSE; }
			else return makeCall(node.childFor("right"));
		}

		/** Callback method for BOOL_NOTEXPR nodes. */
		public Object BOOL_NOTEXPR(IASTNode node) throws Exception {
			Object expr = makeCall(node.childFor("expr"));
			if (expr instanceof Boolean == false) {
				throw new IllegalStateException("BOOL_NOTEXPR with non boolean sub expression.");
			}
			return !((Boolean)expr);
		}

		/** Callback method for BOOL_DYADEXPR nodes. */
		@SuppressWarnings("unchecked")
		public Object BOOL_DYADEXPR(IASTNode node) throws Exception {
			Object left = makeCall(node.childFor("left"));
			Object right = makeCall(node.childFor("right"));
			String op = node.getAttrString("op");
			
			if ("=".equals(op)) {
				return (left==null && right == null) || (left != null && left.equals(right));
			} else if ("<=".equals(op)) {
				Comparable lComp = toComparable(right);
				return lComp.compareTo(right) <= 0;
			} else if ("<".equals(op)) {
				Comparable lComp = toComparable(right);
				return lComp.compareTo(right) < 0;
			} else if (">=".equals(op)) {
				Comparable lComp = toComparable(right);
				return lComp.compareTo(right) >= 0;
			} else if (">".equals(op)) {
				Comparable lComp = toComparable(right);
				return lComp.compareTo(right) > 0;
			} else if ("!=".equals(op)) {
				return (left==null && right!=null) ||
				       (left!=null && right==null) ||
				       (left!=null && !left.equals(right));
			} else {
				throw new IllegalStateException("Unknown dyadic operator: " + op);
			}
		}

		/** Verify that o is comparable. */
		private Comparable toComparable(Object o) {
			if (o instanceof Comparable == false) {
				throw new IllegalStateException("Unable to convert " + o + " to a Comparable");
			}
			return (Comparable) o;
		}
		
		/** Callback method for BOOL_BOOLTERM nodes. */
		public Object BOOL_BOOLTERM(IASTNode node) throws Exception {
			return node.getAttr("value");
		}
		
	}
	
}
