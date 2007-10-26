package be.uclouvain.jail.vm.autogram;

import java.util.ArrayList;
import java.util.List;

import net.chefbe.autogram.ast2.parsing.ParseException;
import net.chefbe.autogram.ast2.parsing.active.ActiveParser;
import net.chefbe.autogram.ast2.parsing.peg.PEGParser;
import net.chefbe.autogram.ast2.parsing.peg.Pos;
import be.uclouvain.jail.dialect.commons.UTILSParser;

/** Parser for Jail grammar. */
public class JailParser extends ActiveParser {

	// ----------------------------------------------- External parsers
	/** External grammars. */
	private JailParser jail = this;

	private UTILSParser u = new UTILSParser(this);

	/** Creates a master parser. */
	public JailParser() {
	}

	/** Creates a child parser. */
	public JailParser(PEGParser parent) {
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
	/** &lt;jail:unit&gt; */
	public final Object pUnit(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
				|| (_alt_c == ' ') || (_alt_c == '(') || (_alt_c == '/')
				|| (_alt_c == '<') || (_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
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
			Object _command = null;
			_command = jail.pJailCommandUList(pos);
			u.pEof(pos);
			return load(pos, "jail:unit", new String[] { "command" },
					new Object[] { _command });
		} else {
			throw new ParseException("<jail:unit> expected, " + pos.charAt()
					+ " found.", pos.location());
		}
	}

	/** &lt;jail:command&gt; */
	public final Object pCommand(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		_alt_c = pos.charAt();
		if (((_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			try {
				pos.save();
				Object _tor = null;
				_tor = jail.pAffectation(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if (((_alt_c == '(') || (_alt_c == '<'))) {
			try {
				pos.save();
				Object _tor = null;
				_tor = jail.pShow(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return (Object) pos.error("<jail:command> expected, " + pos.charAt()
				+ " found.");
	}

	/** &lt;jail:affectation&gt; */
	public final Object pAffectation(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			Object _var = null;
			_var = jail.pIdentifier(pos);
			jail.pSy$61USymbol(pos);
			Object _value = null;
			_value = jail.pGexpression(pos);
			jail.pSy$59USymbol(pos);
			return load(pos, "jail:affectation",
					new String[] { "var", "value" }, new Object[] { _var,
							_value });
		} else {
			throw new ParseException("<jail:affectation> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:show&gt; */
	public final Object pShow(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '(') || (_alt_c == '<'))) {
			Object _value = null;
			_value = jail.pGexpression(pos);
			jail.pSy$59USymbol(pos);
			return load(pos, "jail:show", new String[] { "value" },
					new Object[] { _value });
		} else {
			throw new ParseException("<jail:show> expected, " + pos.charAt()
					+ " found.", pos.location());
		}
	}

	/** &lt;jail:gexpression&gt; */
	public final Object pGexpression(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		_alt_c = pos.charAt();
		if ((_alt_c == '(')) {
			try {
				pos.save();
				Object _tor = null;
				_tor = jail.pGoperator(pos);
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
				_tor = jail.pGliteral(pos);
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
				Object _tor = null;
				_tor = jail.pVarref(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return (Object) pos.error("<jail:gexpression> expected, "
				+ pos.charAt() + " found.");
	}

	/** &lt;jail:gliteral&gt; */
	public final Object pGliteral(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '<')) {
			p$Exact(pos, "<§");
			Object _format = null;
			_format = jail.pIdentifier(pos);
			String _literal = null;
			{
				StringBuffer _sb_literal = new StringBuffer();
				char _zz_first = "§>".charAt(0);
				int _zz_start = pos.offset();
				while (!pos.isEof()) {
					char f = pos.charAt();
					if (f == _zz_first && try$Exact(pos, "§>")) {
						break;
					} else {
						pos.more();
						_sb_literal.append(f);
					}
				}
				if (_zz_start == pos.offset()) {
					throw new ParseException("!§> expected, " + pos.charAt()
							+ " found.", pos.location());
				}
				_literal = _sb_literal.toString();
			}
			jail.pEx$167$62USymbol(pos);
			return load(pos, "jail:gliteral", new String[] { "format",
					"literal" }, new Object[] { _format, _literal });
		} else {
			throw new ParseException("<jail:gliteral> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:goperator&gt; */
	public final Object pGoperator(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '(')) {
			p$Char(pos, '(');
			Object _name = null;
			_name = jail.pIdentifier(pos);
			Object _operands = null;
			_operands = jail.pGoperands(pos);
			Object _options = null;
			_alt_c = pos.charAt();
			if ((_alt_c == ':')) {
				try {
					pos.save();
					_options = jail.pOptions(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			jail.pSy$41USymbol(pos);
			return load(pos, "jail:goperator", new String[] { "name",
					"operands", "options" }, new Object[] { _name, _operands,
					_options });
		} else {
			throw new ParseException("<jail:goperator> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:goperands&gt; */
	public final Object pGoperands(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '(') || (_alt_c == '<'))) {
			Object _operand = null;
			_operand = jail.pJailGoperandUList(pos);
			return load(pos, "jail:goperands", new String[] { "operand" },
					new Object[] { _operand });
		} else {
			throw new ParseException("<jail:goperands> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:goperand&gt; */
	public final Object pGoperand(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '(') || (_alt_c == '<'))) {
			Object _expr = null;
			_expr = jail.pGexpression(pos);
			return load(pos, "jail:goperand", new String[] { "expr" },
					new Object[] { _expr });
		} else {
			throw new ParseException("<jail:goperand> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:options&gt; */
	public final Object pOptions(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == ':')) {
			Object _option = null;
			_option = jail.pJailOptionUList(pos);
			return load(pos, "jail:options", new String[] { "option" },
					new Object[] { _option });
		} else {
			throw new ParseException("<jail:options> expected, " + pos.charAt()
					+ " found.", pos.location());
		}
	}

	/** &lt;jail:option&gt; */
	public final Object pOption(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == ':')) {
			p$Char(pos, ':');
			Object _name = null;
			_name = jail.pIdentifier(pos);
			Object _value = null;
			_value = jail.pOptvalue(pos);
			return load(pos, "jail:option", new String[] { "name", "value" },
					new Object[] { _name, _value });
		} else {
			throw new ParseException("<jail:option> expected, " + pos.charAt()
					+ " found.", pos.location());
		}
	}

	/** &lt;jail:optvalue&gt; */
	public final Object pOptvalue(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		_alt_c = pos.charAt();
		if ((_alt_c == '/')) {
			try {
				pos.save();
				Object _tor = null;
				_tor = jail.pAttrsExpr(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if (((_alt_c == '"') || (_alt_c == '\''))) {
			try {
				pos.save();
				Object _tor = null;
				_tor = u.pString(pos);
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
				_tor = u.pBoolean(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return (Object) pos.error("<jail:optvalue> expected, " + pos.charAt()
				+ " found.");
	}

	/** &lt;jail:attrs_expr&gt; */
	public final Object pAttrsExpr(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '/')) {
			jail.pSy$47USymbol(pos);
			Object _affectation = null;
			_affectation = jail.pJailAttrAffectUList(pos);
			jail.pSy$47USymbol(pos);
			return load(pos, "jail:attrs_expr", new String[] { "affectation" },
					new Object[] { _affectation });
		} else {
			throw new ParseException("<jail:attrs_expr> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:attr_affect&gt; */
	public final Object pAttrAffect(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '@')) {
			Object _name = null;
			_name = jail.pAttrname(pos);
			jail.pSy$61USymbol(pos);
			Object _expr = null;
			_expr = jail.pAttrExpr(pos);
			return load(pos, "jail:attr_affect",
					new String[] { "name", "expr" }, new Object[] { _name,
							_expr });
		} else {
			throw new ParseException("<jail:attr_affect> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:attr_expr&gt; */
	public final Object pAttrExpr(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '@')) {
			Object _tor = null;
			_tor = jail.pAttrref(pos);
			return _tor;
		} else {
			throw new ParseException("<jail:attr_expr> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:attrname&gt; */
	public final Object pAttrname(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '@')) {
			p$Char(pos, '@');
			Object _tor = null;
			_tor = jail.pIdentifier(pos);
			return _tor;
		} else {
			throw new ParseException("<jail:attrname> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:attrref&gt; */
	public final Object pAttrref(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '@')) {
			Object _name = null;
			_name = jail.pAttrname(pos);
			return load(pos, "jail:attrref", new String[] { "name" },
					new Object[] { _name });
		} else {
			throw new ParseException("<jail:attrref> expected, " + pos.charAt()
					+ " found.", pos.location());
		}
	}

	/** &lt;jail:varref&gt; */
	public final Object pVarref(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '(')) {
			jail.pSy$40USymbol(pos);
			Object _var = null;
			_var = jail.pIdentifier(pos);
			jail.pSy$41USymbol(pos);
			return load(pos, "jail:varref", new String[] { "var" },
					new Object[] { _var });
		} else {
			throw new ParseException("<jail:varref> expected, " + pos.charAt()
					+ " found.", pos.location());
		}
	}

	/** &lt;jail:identifier&gt; */
	public final Object pIdentifier(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			Object _tor = null;
			_tor = u.pIdentifier(pos);
			return _tor;
		} else {
			throw new ParseException("<jail:identifier> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:ex$167$62_u_symbol&gt; */
	public final Object pEx$167$62USymbol(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '§')) {
			Object _tor = null;
			_tor = p$Exact(pos, "§>");
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
			throw new ParseException("<jail:ex$167$62_u_symbol> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:jail_attr_affect_u_list&gt; */
	public final List<?> pJailAttrAffectUList(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '@')) {
			Object _f = null;
			_f = jail.pAttrAffect(pos);
			List<Object> _n = new ArrayList<Object>();
			_alt_c = pos.charAt();
			while (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
					|| (_alt_c == ' ') || (_alt_c == '/') || (_alt_c == '@'))) {
				try {
					pos.save();
					_n.add(jail.pJailAttrAffectUList11(pos));
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
					"<jail:jail_attr_affect_u_list> expected, " + pos.charAt()
							+ " found.", pos.location());
		}
	}

	/** &lt;jail:jail_command_u_list&gt; */
	public final List<?> pJailCommandUList(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '(') || (_alt_c == '<')
				|| (_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			Object _f = null;
			_f = jail.pCommand(pos);
			List<Object> _n = new ArrayList<Object>();
			_alt_c = pos.charAt();
			while (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
					|| (_alt_c == ' ') || (_alt_c == '(') || (_alt_c == '/')
					|| (_alt_c == '<') || (_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
				try {
					pos.save();
					_n.add(jail.pJailCommandUList11(pos));
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
					break;
				}
				_alt_c = pos.charAt();
			}
			return toList(_f, _n);
		} else {
			throw new ParseException("<jail:jail_command_u_list> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:jail_goperand_u_list&gt; */
	public final List<?> pJailGoperandUList(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '(') || (_alt_c == '<'))) {
			Object _f = null;
			_f = jail.pGoperand(pos);
			List<Object> _n = new ArrayList<Object>();
			_alt_c = pos.charAt();
			while (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
					|| (_alt_c == ' ') || (_alt_c == '(') || (_alt_c == '/') || (_alt_c == '<'))) {
				try {
					pos.save();
					_n.add(jail.pJailGoperandUList11(pos));
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
					break;
				}
				_alt_c = pos.charAt();
			}
			return toList(_f, _n);
		} else {
			throw new ParseException("<jail:jail_goperand_u_list> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:jail_option_u_list&gt; */
	public final List<?> pJailOptionUList(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == ':')) {
			Object _f = null;
			_f = jail.pOption(pos);
			List<Object> _n = new ArrayList<Object>();
			_alt_c = pos.charAt();
			while (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
					|| (_alt_c == ' ') || (_alt_c == '/') || (_alt_c == ':'))) {
				try {
					pos.save();
					_n.add(jail.pJailOptionUList11(pos));
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
					break;
				}
				_alt_c = pos.charAt();
			}
			return toList(_f, _n);
		} else {
			throw new ParseException("<jail:jail_option_u_list> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:sy$40_u_symbol&gt; */
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
			throw new ParseException("<jail:sy$40_u_symbol> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:sy$41_u_symbol&gt; */
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
			throw new ParseException("<jail:sy$41_u_symbol> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:sy$47_u_symbol&gt; */
	public final Object pSy$47USymbol(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '/')) {
			Object _tor = null;
			_tor = p$Char(pos, '/');
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
			throw new ParseException("<jail:sy$47_u_symbol> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:sy$59_u_symbol&gt; */
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
			throw new ParseException("<jail:sy$59_u_symbol> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:sy$61_u_symbol&gt; */
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
			throw new ParseException("<jail:sy$61_u_symbol> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:jail_attr_affect_u_list_1_1&gt; */
	public final Object pJailAttrAffectUList11(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
				|| (_alt_c == ' ') || (_alt_c == '/') || (_alt_c == '@'))) {
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
			_tor = jail.pAttrAffect(pos);
			return _tor;
		} else {
			throw new ParseException(
					"<jail:jail_attr_affect_u_list_1_1> expected, "
							+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:jail_command_u_list_1_1&gt; */
	public final Object pJailCommandUList11(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
				|| (_alt_c == ' ') || (_alt_c == '(') || (_alt_c == '/')
				|| (_alt_c == '<') || (_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
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
			_tor = jail.pCommand(pos);
			return _tor;
		} else {
			throw new ParseException(
					"<jail:jail_command_u_list_1_1> expected, " + pos.charAt()
							+ " found.", pos.location());
		}
	}

	/** &lt;jail:jail_goperand_u_list_1_1&gt; */
	public final Object pJailGoperandUList11(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
				|| (_alt_c == ' ') || (_alt_c == '(') || (_alt_c == '/') || (_alt_c == '<'))) {
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
			_tor = jail.pGoperand(pos);
			return _tor;
		} else {
			throw new ParseException(
					"<jail:jail_goperand_u_list_1_1> expected, " + pos.charAt()
							+ " found.", pos.location());
		}
	}

	/** &lt;jail:jail_option_u_list_1_1&gt; */
	public final Object pJailOptionUList11(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
				|| (_alt_c == ' ') || (_alt_c == '/') || (_alt_c == ':'))) {
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
			_tor = jail.pOption(pos);
			return _tor;
		} else {
			throw new ParseException("<jail:jail_option_u_list_1_1> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

}
