package be.uclouvain.jail.algo.graph.copy.match;

import java.util.ArrayList;
import java.util.List;

import net.chefbe.autogram.ast2.parsing.ParseException;
import net.chefbe.autogram.ast2.parsing.active.ActiveParser;
import net.chefbe.autogram.ast2.parsing.peg.PEGParser;
import net.chefbe.autogram.ast2.parsing.peg.Pos;
import be.uclouvain.jail.dialect.commons.UTILSParser;

/** Parser for GMatch grammar. */
public class GMatchParser extends ActiveParser {

	// ----------------------------------------------- External parsers
	/** External grammars. */
	private GMatchParser gm = this;

	private UTILSParser u = new UTILSParser(this);

	/** Creates a master parser. */
	public GMatchParser() {
	}

	/** Creates a child parser. */
	public GMatchParser(PEGParser parent) {
		super(parent);
	}

	/** Returns the parser bound to a prefix. */
	@Override
	public PEGParser getParser(String prefix) {
		if (parent != null) {
			return parent.getParser(prefix);
		} else {
			if ("u".equals(prefix)) {
				return u;
			}
			return this;
		}
	}

	// ----------------------------------------------- Rules
	/** &lt;gm:match_test&gt; */
	public final Object pMatchTest(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == 'e') || (_alt_c == 's'))) {
			Object _rule = null;
			_rule = gm.pGmMatchTestoneUList(pos);
			u.pEof(pos);
			return load(pos, "gm:match_test", new String[] { "rule" },
					new Object[] { _rule });
		} else {
			throw new ParseException("<gm:match_test> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;gm:match_testone&gt; */
	public final Object pMatchTestone(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == 'e') || (_alt_c == 's'))) {
			Object _tor = null;
			_tor = gm.pMatchRule(pos);
			gm.pSy$59USymbol(pos);
			return _tor;
		} else {
			throw new ParseException("<gm:match_testone> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;gm:match_rule&gt; */
	public final Object pMatchRule(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == 'e') || (_alt_c == 's'))) {
			Object _match = null;
			_match = gm.pMatchPath(pos);
			gm.pEx$45$62USymbol(pos);
			Object _do = null;
			_do = gm.pMatchDo(pos);
			return load(pos, "gm:match_rule", new String[] { "match", "do" },
					new Object[] { _match, _do });
		} else {
			throw new ParseException("<gm:match_rule> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;gm:match_path&gt; */
	public final Object pMatchPath(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		_alt_c = pos.charAt();
		if ((_alt_c == 's')) {
			try {
				pos.save();
				Object _expr = null;
				_expr = p$Exact(pos, "state/@*");
				u.pSpacing(pos);
				pos.commit();
				return load(pos, "gm:match_path", new String[] { "expr" },
						new Object[] { _expr });
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if ((_alt_c == 's')) {
			try {
				pos.save();
				Object _expr = null;
				_expr = p$Exact(pos, "state");
				u.pSpacing(pos);
				pos.commit();
				return load(pos, "gm:match_path", new String[] { "expr" },
						new Object[] { _expr });
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if ((_alt_c == 'e')) {
			try {
				pos.save();
				Object _expr = null;
				_expr = p$Exact(pos, "edge/@*");
				u.pSpacing(pos);
				pos.commit();
				return load(pos, "gm:match_path", new String[] { "expr" },
						new Object[] { _expr });
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if ((_alt_c == 'e')) {
			try {
				pos.save();
				Object _expr = null;
				_expr = p$Exact(pos, "edge");
				u.pSpacing(pos);
				pos.commit();
				return load(pos, "gm:match_path", new String[] { "expr" },
						new Object[] { _expr });
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return (Object) pos.error("<gm:match_path> expected, " + pos.charAt()
				+ " found.");
	}

	/** &lt;gm:match_do&gt; */
	public final Object pMatchDo(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '{')) {
			gm.pSy$123USymbol(pos);
			Object _attr = null;
			_alt_c = pos.charAt();
			if ((_alt_c == '@')) {
				try {
					pos.save();
					_attr = gm.pGmMatchDoattrUCommalist(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			gm.pSy$125USymbol(pos);
			return load(pos, "gm:match_do", new String[] { "attr" },
					new Object[] { _attr });
		} else {
			throw new ParseException("<gm:match_do> expected, " + pos.charAt()
					+ " found.", pos.location());
		}
	}

	/** &lt;gm:match_doattr&gt; */
	public final Object pMatchDoattr(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '@')) {
			Object _key = null;
			_key = gm.pAttrExpr(pos);
			gm.pEx$45$62USymbol(pos);
			Object _value = null;
			_value = gm.pAttrValue(pos);
			return load(pos, "gm:match_doattr",
					new String[] { "key", "value" }, new Object[] { _key,
							_value });
		} else {
			throw new ParseException("<gm:match_doattr> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;gm:attr_expr&gt; */
	public final Object pAttrExpr(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '@')) {
			Object _tor = null;
			_tor = gm.pAttrNamedef(pos);
			return _tor;
		} else {
			throw new ParseException("<gm:attr_expr> expected, " + pos.charAt()
					+ " found.", pos.location());
		}
	}

	/** &lt;gm:attr_namedef&gt; */
	public final Object pAttrNamedef(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '@')) {
			Object _name = null;
			_name = gm.pAttrName(pos);
			return load(pos, "gm:attr_namedef", new String[] { "name" },
					new Object[] { _name });
		} else {
			throw new ParseException("<gm:attr_namedef> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;gm:attr_name&gt; */
	public final Object pAttrName(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '@')) {
			p$Char(pos, '@');
			Object _tor = null;
			_tor = u.pQidentifier(pos);
			return _tor;
		} else {
			throw new ParseException("<gm:attr_name> expected, " + pos.charAt()
					+ " found.", pos.location());
		}
	}

	/** &lt;gm:attr_value&gt; */
	public final Object pAttrValue(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		_alt_c = pos.charAt();
		if ((_alt_c == 'w')) {
			try {
				pos.save();
				Object _tor = null;
				_tor = gm.pCaseExpr(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if (((_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			try {
				pos.save();
				Object _tor = null;
				_tor = gm.pFunctionCall(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if ((_alt_c == '@')) {
			try {
				pos.save();
				Object _tor = null;
				_tor = gm.pAttrRef(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if (((_alt_c == '"') || (_alt_c == '\'')
				|| (_alt_c >= '0' && _alt_c <= '9') || (_alt_c == 'f') || (_alt_c == 't'))) {
			try {
				pos.save();
				Object _tor = null;
				_tor = gm.pLiteral(pos);
				_alt_c = pos.charAt();
				if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
						|| (_alt_c == ' ') || (_alt_c == '/'))) {
					try {
						pos.save();
						u.pSpacing(pos);
						pos.commit();
					} catch (ParseException ex) {
						pos.rollback();
					}
				}
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return (Object) pos.error("<gm:attr_value> expected, " + pos.charAt()
				+ " found.");
	}

	/** &lt;gm:attr_ref&gt; */
	public final Object pAttrRef(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '@')) {
			Object _name = null;
			_name = gm.pAttrName(pos);
			return load(pos, "gm:attr_ref", new String[] { "name" },
					new Object[] { _name });
		} else {
			throw new ParseException("<gm:attr_ref> expected, " + pos.charAt()
					+ " found.", pos.location());
		}
	}

	/** &lt;gm:case_expr&gt; */
	public final Object pCaseExpr(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == 'w')) {
			Object _when = null;
			_when = gm.pGmWhenExprUList(pos);
			Object _else = null;
			_alt_c = pos.charAt();
			if ((_alt_c == 'e')) {
				try {
					pos.save();
					_else = gm.pElseExpr(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			return load(pos, "gm:case_expr", new String[] { "when", "else" },
					new Object[] { _when, _else });
		} else {
			throw new ParseException("<gm:case_expr> expected, " + pos.charAt()
					+ " found.", pos.location());
		}
	}

	/** &lt;gm:when_expr&gt; */
	public final Object pWhenExpr(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == 'w')) {
			p$Keyword(pos, "when");
			Object _test = null;
			_test = gm.pBoolExpr(pos);
			p$Keyword(pos, "then");
			Object _value = null;
			_value = gm.pAttrValue(pos);
			return load(pos, "gm:when_expr", new String[] { "test", "value" },
					new Object[] { _test, _value });
		} else {
			throw new ParseException("<gm:when_expr> expected, " + pos.charAt()
					+ " found.", pos.location());
		}
	}

	/** &lt;gm:else_expr&gt; */
	public final Object pElseExpr(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == 'e')) {
			p$Keyword(pos, "else");
			Object _value = null;
			_value = gm.pAttrValue(pos);
			return load(pos, "gm:else_expr", new String[] { "value" },
					new Object[] { _value });
		} else {
			throw new ParseException("<gm:else_expr> expected, " + pos.charAt()
					+ " found.", pos.location());
		}
	}

	/** &lt;gm:function_call&gt; */
	public final Object pFunctionCall(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			Object _name = null;
			_name = u.pIdentifier(pos);
			gm.pSy$40USymbol(pos);
			Object _arg = null;
			_alt_c = pos.charAt();
			if (((_alt_c == '"') || (_alt_c == '\'')
					|| (_alt_c >= '0' && _alt_c <= '9') || (_alt_c == '@')
					|| (_alt_c >= 'A' && _alt_c <= 'Z')
					|| (_alt_c >= 'a' && _alt_c <= 'z') || (_alt_c == 'f')
					|| (_alt_c == 't') || (_alt_c == 'w'))) {
				try {
					pos.save();
					_arg = gm.pGmFunctionArgUCommalist(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			gm.pSy$41USymbol(pos);
			return load(pos, "gm:function_call",
					new String[] { "name", "arg" },
					new Object[] { _name, _arg });
		} else {
			throw new ParseException("<gm:function_call> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;gm:function_arg&gt; */
	public final Object pFunctionArg(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '"') || (_alt_c == '\'')
				|| (_alt_c >= '0' && _alt_c <= '9') || (_alt_c == '@')
				|| (_alt_c >= 'A' && _alt_c <= 'Z')
				|| (_alt_c >= 'a' && _alt_c <= 'z') || (_alt_c == 'f')
				|| (_alt_c == 't') || (_alt_c == 'w'))) {
			Object _tor = null;
			_tor = gm.pAttrValue(pos);
			return _tor;
		} else {
			throw new ParseException("<gm:function_arg> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;gm:literal&gt; */
	public final Object pLiteral(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		_alt_c = pos.charAt();
		if (((_alt_c == '"') || (_alt_c == '\''))) {
			try {
				pos.save();
				Object _value = null;
				_value = u.pString(pos);
				pos.commit();
				return load(pos, "gm:literal", new String[] { "value" },
						new Object[] { _value });
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if (((_alt_c == 'f') || (_alt_c == 't'))) {
			try {
				pos.save();
				Object _value = null;
				_value = u.pBoolean(pos);
				pos.commit();
				return load(pos, "gm:literal", new String[] { "value" },
						new Object[] { _value });
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if ((_alt_c >= '0' && _alt_c <= '9')) {
			try {
				pos.save();
				Object _value = null;
				_value = u.pInteger(pos);
				pos.commit();
				return load(pos, "gm:literal", new String[] { "value" },
						new Object[] { _value });
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return (Object) pos.error("<gm:literal> expected, " + pos.charAt()
				+ " found.");
	}

	/** &lt;gm:bool_expr&gt; */
	public final Object pBoolExpr(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '"') || (_alt_c == '\'') || (_alt_c == '(')
				|| (_alt_c >= '0' && _alt_c <= '9') || (_alt_c == '@')
				|| (_alt_c >= 'A' && _alt_c <= 'Z')
				|| (_alt_c >= 'a' && _alt_c <= 'z') || (_alt_c == 'f')
				|| (_alt_c == 'n') || (_alt_c == 't'))) {
			Object _tor = null;
			_tor = gm.pBoolOrexpr(pos);
			return _tor;
		} else {
			throw new ParseException("<gm:bool_expr> expected, " + pos.charAt()
					+ " found.", pos.location());
		}
	}

	/** &lt;gm:bool_orexpr&gt; */
	public final Object pBoolOrexpr(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		_alt_c = pos.charAt();
		if (((_alt_c == '"') || (_alt_c == '\'') || (_alt_c == '(')
				|| (_alt_c >= '0' && _alt_c <= '9') || (_alt_c == '@')
				|| (_alt_c >= 'A' && _alt_c <= 'Z')
				|| (_alt_c >= 'a' && _alt_c <= 'z') || (_alt_c == 'f')
				|| (_alt_c == 'n') || (_alt_c == 't'))) {
			try {
				pos.save();
				Object _left = null;
				_left = gm.pBoolAndexpr(pos);
				p$Keyword(pos, "or");
				Object _right = null;
				_right = gm.pBoolOrexpr(pos);
				pos.commit();
				return load(pos, "gm:bool_orexpr", new String[] { "left",
						"right" }, new Object[] { _left, _right });
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return gm.pBoolAndexpr(pos);
	}

	/** &lt;gm:bool_andexpr&gt; */
	public final Object pBoolAndexpr(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		_alt_c = pos.charAt();
		if (((_alt_c == '"') || (_alt_c == '\'') || (_alt_c == '(')
				|| (_alt_c >= '0' && _alt_c <= '9') || (_alt_c == '@')
				|| (_alt_c >= 'A' && _alt_c <= 'Z')
				|| (_alt_c >= 'a' && _alt_c <= 'z') || (_alt_c == 'f')
				|| (_alt_c == 'n') || (_alt_c == 't'))) {
			try {
				pos.save();
				Object _left = null;
				_left = gm.pBoolNotexpr(pos);
				p$Keyword(pos, "and");
				Object _right = null;
				_right = gm.pBoolAndexpr(pos);
				pos.commit();
				return load(pos, "gm:bool_andexpr", new String[] { "left",
						"right" }, new Object[] { _left, _right });
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return gm.pBoolNotexpr(pos);
	}

	/** &lt;gm:bool_notexpr&gt; */
	public final Object pBoolNotexpr(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		_alt_c = pos.charAt();
		if ((_alt_c == 'n')) {
			try {
				pos.save();
				p$Keyword(pos, "not");
				gm.pSy$40USymbol(pos);
				Object _expr = null;
				_expr = gm.pBoolExpr(pos);
				gm.pSy$41USymbol(pos);
				pos.commit();
				return load(pos, "gm:bool_notexpr", new String[] { "expr" },
						new Object[] { _expr });
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return gm.pBoolDyadexpr(pos);
	}

	/** &lt;gm:bool_dyadexpr&gt; */
	public final Object pBoolDyadexpr(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		_alt_c = pos.charAt();
		if (((_alt_c == '"') || (_alt_c == '\'')
				|| (_alt_c >= '0' && _alt_c <= '9') || (_alt_c == '@')
				|| (_alt_c >= 'A' && _alt_c <= 'Z')
				|| (_alt_c >= 'a' && _alt_c <= 'z') || (_alt_c == 'f') || (_alt_c == 't'))) {
			try {
				pos.save();
				Object _left = null;
				_left = gm.pBoolDyadlr(pos);
				Object _op = null;
				_op = gm.pBoolDyadop(pos);
				Object _right = null;
				_right = gm.pBoolDyadlr(pos);
				pos.commit();
				return load(pos, "gm:bool_dyadexpr", new String[] { "left",
						"op", "right" }, new Object[] { _left, _op, _right });
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return gm.pBoolTermexpr(pos);
	}

	/** &lt;gm:bool_dyadlr&gt; */
	public final Object pBoolDyadlr(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		_alt_c = pos.charAt();
		if (((_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			try {
				pos.save();
				Object _tor = null;
				_tor = gm.pFunctionCall(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if ((_alt_c == '@')) {
			try {
				pos.save();
				Object _tor = null;
				_tor = gm.pAttrRef(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if (((_alt_c == '"') || (_alt_c == '\'')
				|| (_alt_c >= '0' && _alt_c <= '9') || (_alt_c == 'f') || (_alt_c == 't'))) {
			try {
				pos.save();
				Object _tor = null;
				_tor = gm.pLiteral(pos);
				_alt_c = pos.charAt();
				if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
						|| (_alt_c == ' ') || (_alt_c == '/'))) {
					try {
						pos.save();
						u.pSpacing(pos);
						pos.commit();
					} catch (ParseException ex) {
						pos.rollback();
					}
				}
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return (Object) pos.error("<gm:bool_dyadlr> expected, " + pos.charAt()
				+ " found.");
	}

	/** &lt;gm:bool_dyadop&gt; */
	public final Object pBoolDyadop(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		_alt_c = pos.charAt();
		if ((_alt_c == '!')) {
			try {
				pos.save();
				Object _tor = null;
				_tor = gm.pSy$33$61USymbol(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if ((_alt_c == '>')) {
			try {
				pos.save();
				Object _tor = null;
				_tor = gm.pSy$62$61USymbol(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if ((_alt_c == '>')) {
			try {
				pos.save();
				Object _tor = null;
				_tor = gm.pSy$62USymbol(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if ((_alt_c == '<')) {
			try {
				pos.save();
				Object _tor = null;
				_tor = gm.pSy$60$61USymbol(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if ((_alt_c == '<')) {
			try {
				pos.save();
				Object _tor = null;
				_tor = gm.pSy$60USymbol(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if ((_alt_c == '=')) {
			try {
				pos.save();
				Object _tor = null;
				_tor = gm.pSy$61USymbol(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return (Object) pos.error("<gm:bool_dyadop> expected, " + pos.charAt()
				+ " found.");
	}

	/** &lt;gm:bool_termexpr&gt; */
	public final Object pBoolTermexpr(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		_alt_c = pos.charAt();
		if ((_alt_c == '@')) {
			try {
				pos.save();
				Object _tor = null;
				_tor = gm.pAttrRef(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if (((_alt_c == 'f') || (_alt_c == 't'))) {
			try {
				pos.save();
				Object _tor = null;
				_tor = gm.pBoolBoolterm(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if ((_alt_c == '(')) {
			try {
				pos.save();
				gm.pSy$40USymbol(pos);
				Object _tor = null;
				_tor = gm.pBoolExpr(pos);
				gm.pSy$41USymbol(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return (Object) pos.error("<gm:bool_termexpr> expected, "
				+ pos.charAt() + " found.");
	}

	/** &lt;gm:bool_boolterm&gt; */
	public final Object pBoolBoolterm(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == 'f') || (_alt_c == 't'))) {
			Object _value = null;
			_value = u.pBoolean(pos);
			u.pSpacing(pos);
			return load(pos, "gm:bool_boolterm", new String[] { "value" },
					new Object[] { _value });
		} else {
			throw new ParseException("<gm:bool_boolterm> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;gm:ex$45$62_u_symbol&gt; */
	public final Object pEx$45$62USymbol(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '-')) {
			Object _tor = null;
			_tor = p$Exact(pos, "->");
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
					|| (_alt_c == ' ') || (_alt_c == '/'))) {
				try {
					pos.save();
					u.pSpacing(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			return _tor;
		} else {
			throw new ParseException("<gm:ex$45$62_u_symbol> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;gm:gm_function_arg_u_commalist&gt; */
	public final List<?> pGmFunctionArgUCommalist(Pos pos)
			throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '"') || (_alt_c == '\'')
				|| (_alt_c >= '0' && _alt_c <= '9') || (_alt_c == '@')
				|| (_alt_c >= 'A' && _alt_c <= 'Z')
				|| (_alt_c >= 'a' && _alt_c <= 'z') || (_alt_c == 'f')
				|| (_alt_c == 't') || (_alt_c == 'w'))) {
			Object _f = null;
			_f = gm.pFunctionArg(pos);
			List<Object> _n = new ArrayList<Object>();
			_alt_c = pos.charAt();
			while ((_alt_c == ',')) {
				try {
					pos.save();
					_n.add(gm.pGmFunctionArgUCommalist11(pos));
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
					break;
				}
				_alt_c = pos.charAt();
			}
			return toList(_f, _n);
		} else {
			throw new ParseException(
					"<gm:gm_function_arg_u_commalist> expected, "
							+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;gm:gm_match_doattr_u_commalist&gt; */
	public final List<?> pGmMatchDoattrUCommalist(Pos pos)
			throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '@')) {
			Object _f = null;
			_f = gm.pMatchDoattr(pos);
			List<Object> _n = new ArrayList<Object>();
			_alt_c = pos.charAt();
			while ((_alt_c == ',')) {
				try {
					pos.save();
					_n.add(gm.pGmMatchDoattrUCommalist11(pos));
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
					break;
				}
				_alt_c = pos.charAt();
			}
			return toList(_f, _n);
		} else {
			throw new ParseException(
					"<gm:gm_match_doattr_u_commalist> expected, "
							+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;gm:gm_match_testone_u_list&gt; */
	public final List<?> pGmMatchTestoneUList(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == 'e') || (_alt_c == 's'))) {
			Object _f = null;
			_f = gm.pMatchTestone(pos);
			List<Object> _n = new ArrayList<Object>();
			_alt_c = pos.charAt();
			while (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
					|| (_alt_c == ' ') || (_alt_c == '/') || (_alt_c == 'e') || (_alt_c == 's'))) {
				try {
					pos.save();
					_n.add(gm.pGmMatchTestoneUList11(pos));
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
					break;
				}
				_alt_c = pos.charAt();
			}
			return toList(_f, _n);
		} else {
			throw new ParseException("<gm:gm_match_testone_u_list> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;gm:gm_when_expr_u_list&gt; */
	public final List<?> pGmWhenExprUList(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == 'w')) {
			Object _f = null;
			_f = gm.pWhenExpr(pos);
			List<Object> _n = new ArrayList<Object>();
			_alt_c = pos.charAt();
			while (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
					|| (_alt_c == ' ') || (_alt_c == '/') || (_alt_c == 'w'))) {
				try {
					pos.save();
					_n.add(gm.pGmWhenExprUList11(pos));
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
					break;
				}
				_alt_c = pos.charAt();
			}
			return toList(_f, _n);
		} else {
			throw new ParseException("<gm:gm_when_expr_u_list> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;gm:sy$33$61_u_symbol&gt; */
	public final Object pSy$33$61USymbol(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '!')) {
			Object _tor = null;
			_tor = p$Char(pos, '!');
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
					|| (_alt_c == ' ') || (_alt_c == '/'))) {
				try {
					pos.save();
					u.pSpacing(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			return _tor;
		} else {
			throw new ParseException("<gm:sy$33$61_u_symbol> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;gm:sy$40_u_symbol&gt; */
	public final Object pSy$40USymbol(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '(')) {
			Object _tor = null;
			_tor = p$Char(pos, '(');
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
					|| (_alt_c == ' ') || (_alt_c == '/'))) {
				try {
					pos.save();
					u.pSpacing(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			return _tor;
		} else {
			throw new ParseException("<gm:sy$40_u_symbol> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;gm:sy$41_u_symbol&gt; */
	public final Object pSy$41USymbol(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == ')')) {
			Object _tor = null;
			_tor = p$Char(pos, ')');
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
					|| (_alt_c == ' ') || (_alt_c == '/'))) {
				try {
					pos.save();
					u.pSpacing(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			return _tor;
		} else {
			throw new ParseException("<gm:sy$41_u_symbol> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;gm:sy$44_u_symbol&gt; */
	public final Object pSy$44USymbol(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == ',')) {
			Object _tor = null;
			_tor = p$Char(pos, ',');
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
					|| (_alt_c == ' ') || (_alt_c == '/'))) {
				try {
					pos.save();
					u.pSpacing(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			return _tor;
		} else {
			throw new ParseException("<gm:sy$44_u_symbol> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;gm:sy$59_u_symbol&gt; */
	public final Object pSy$59USymbol(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == ';')) {
			Object _tor = null;
			_tor = p$Char(pos, ';');
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
					|| (_alt_c == ' ') || (_alt_c == '/'))) {
				try {
					pos.save();
					u.pSpacing(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			return _tor;
		} else {
			throw new ParseException("<gm:sy$59_u_symbol> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;gm:sy$60_u_symbol&gt; */
	public final Object pSy$60USymbol(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '<')) {
			Object _tor = null;
			_tor = p$Char(pos, '<');
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
					|| (_alt_c == ' ') || (_alt_c == '/'))) {
				try {
					pos.save();
					u.pSpacing(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			return _tor;
		} else {
			throw new ParseException("<gm:sy$60_u_symbol> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;gm:sy$60$61_u_symbol&gt; */
	public final Object pSy$60$61USymbol(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '<')) {
			Object _tor = null;
			_tor = p$Char(pos, '<');
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
					|| (_alt_c == ' ') || (_alt_c == '/'))) {
				try {
					pos.save();
					u.pSpacing(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			return _tor;
		} else {
			throw new ParseException("<gm:sy$60$61_u_symbol> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;gm:sy$61_u_symbol&gt; */
	public final Object pSy$61USymbol(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '=')) {
			Object _tor = null;
			_tor = p$Char(pos, '=');
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
					|| (_alt_c == ' ') || (_alt_c == '/'))) {
				try {
					pos.save();
					u.pSpacing(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			return _tor;
		} else {
			throw new ParseException("<gm:sy$61_u_symbol> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;gm:sy$62_u_symbol&gt; */
	public final Object pSy$62USymbol(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '>')) {
			Object _tor = null;
			_tor = p$Char(pos, '>');
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
					|| (_alt_c == ' ') || (_alt_c == '/'))) {
				try {
					pos.save();
					u.pSpacing(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			return _tor;
		} else {
			throw new ParseException("<gm:sy$62_u_symbol> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;gm:sy$62$61_u_symbol&gt; */
	public final Object pSy$62$61USymbol(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '>')) {
			Object _tor = null;
			_tor = p$Char(pos, '>');
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
					|| (_alt_c == ' ') || (_alt_c == '/'))) {
				try {
					pos.save();
					u.pSpacing(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			return _tor;
		} else {
			throw new ParseException("<gm:sy$62$61_u_symbol> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;gm:sy$123_u_symbol&gt; */
	public final Object pSy$123USymbol(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '{')) {
			Object _tor = null;
			_tor = p$Char(pos, '{');
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
					|| (_alt_c == ' ') || (_alt_c == '/'))) {
				try {
					pos.save();
					u.pSpacing(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			return _tor;
		} else {
			throw new ParseException("<gm:sy$123_u_symbol> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;gm:sy$125_u_symbol&gt; */
	public final Object pSy$125USymbol(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '}')) {
			Object _tor = null;
			_tor = p$Char(pos, '}');
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
					|| (_alt_c == ' ') || (_alt_c == '/'))) {
				try {
					pos.save();
					u.pSpacing(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			return _tor;
		} else {
			throw new ParseException("<gm:sy$125_u_symbol> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;gm:gm_function_arg_u_commalist_1_1&gt; */
	public final Object pGmFunctionArgUCommalist11(Pos pos)
			throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == ',')) {
			gm.pSy$44USymbol(pos);
			Object _tor = null;
			_tor = gm.pFunctionArg(pos);
			return _tor;
		} else {
			throw new ParseException(
					"<gm:gm_function_arg_u_commalist_1_1> expected, "
							+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;gm:gm_match_doattr_u_commalist_1_1&gt; */
	public final Object pGmMatchDoattrUCommalist11(Pos pos)
			throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == ',')) {
			gm.pSy$44USymbol(pos);
			Object _tor = null;
			_tor = gm.pMatchDoattr(pos);
			return _tor;
		} else {
			throw new ParseException(
					"<gm:gm_match_doattr_u_commalist_1_1> expected, "
							+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;gm:gm_match_testone_u_list_1_1&gt; */
	public final Object pGmMatchTestoneUList11(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
				|| (_alt_c == ' ') || (_alt_c == '/') || (_alt_c == 'e') || (_alt_c == 's'))) {
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
					|| (_alt_c == ' ') || (_alt_c == '/'))) {
				try {
					pos.save();
					u.pSpacing(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			Object _tor = null;
			_tor = gm.pMatchTestone(pos);
			return _tor;
		} else {
			throw new ParseException(
					"<gm:gm_match_testone_u_list_1_1> expected, "
							+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;gm:gm_when_expr_u_list_1_1&gt; */
	public final Object pGmWhenExprUList11(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
				|| (_alt_c == ' ') || (_alt_c == '/') || (_alt_c == 'w'))) {
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
					|| (_alt_c == ' ') || (_alt_c == '/'))) {
				try {
					pos.save();
					u.pSpacing(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			Object _tor = null;
			_tor = gm.pWhenExpr(pos);
			return _tor;
		} else {
			throw new ParseException("<gm:gm_when_expr_u_list_1_1> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

}
