package be.uclouvain.jail.vm.autogram;

import java.util.ArrayList;
import java.util.List;

import net.chefbe.autogram.ast2.parsing.ParseException;
import net.chefbe.autogram.ast2.parsing.active.ActiveParser;
import net.chefbe.autogram.ast2.parsing.peg.PEGParser;
import net.chefbe.autogram.ast2.parsing.peg.Pos;
import be.uclouvain.jail.algo.graph.copy.match.GMatchParser;
import be.uclouvain.jail.dialect.utils.UTILSParser;

/** Parser for Jail grammar. */
public class JailParser extends ActiveParser {

	// ----------------------------------------------- External parsers
	/** External grammars. */
	private JailParser jail = this;

	private UTILSParser u = new UTILSParser(this);

	private GMatchParser gm = new GMatchParser(this);

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
			} else if ("gm".equals(prefix)) {
				return gm;
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
				|| (_alt_c == '<') || (_alt_c >= 'A' && _alt_c <= 'Z')
				|| (_alt_c == '\\') || (_alt_c >= 'a' && _alt_c <= 'z') || (_alt_c == 'd'))) {
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
		if ((_alt_c == '\\')) {
			try {
				pos.save();
				Object _tor = null;
				_tor = jail.pSystemc(pos);
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
				_tor = jail.pAffectation(pos);
				jail.pSy$59USymbol(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if ((_alt_c == 'd')) {
			try {
				pos.save();
				Object _tor = null;
				_tor = jail.pDefine(pos);
				jail.pSy$59USymbol(pos);
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
				jail.pSy$59USymbol(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return (Object) pos.error("<jail:command> expected, " + pos.charAt()
				+ " found.");
	}

	/** &lt;jail:systemc&gt; */
	public final Object pSystemc(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		_alt_c = pos.charAt();
		if ((_alt_c == '\\')) {
			try {
				pos.save();
				p$Char(pos, '\\');
				Object _name = null;
				_name = jail.pIdentifier(pos);
				Object _arg = null;
				_alt_c = pos.charAt();
				if (((_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
					try {
						pos.save();
						_arg = jail.pJailSystemargUList(pos);
						pos.commit();
					} catch (ParseException ex) {
						pos.rollback();
					}
				}
				jail.pSy$59USymbol(pos);
				pos.commit();
				return load(pos, "jail:systemc",
						new String[] { "name", "arg" }, new Object[] { _name,
								_arg });
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if ((_alt_c == '\\')) {
			try {
				pos.save();
				p$Char(pos, '\\');
				Object _name = null;
				_name = jail.pIdentifier(pos);
				Object _arg = null;
				_alt_c = pos.charAt();
				if (((_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
					try {
						pos.save();
						_arg = jail.pJailSystemargUList(pos);
						pos.commit();
					} catch (ParseException ex) {
						pos.rollback();
					}
				}
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
				return load(pos, "jail:systemc",
						new String[] { "name", "arg" }, new Object[] { _name,
								_arg });
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return (Object) pos.error("<jail:systemc> expected, " + pos.charAt()
				+ " found.");
	}

	/** &lt;jail:systemarg&gt; */
	public final Object pSystemarg(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			Object _value = null;
			_value = jail.pIdentifier(pos);
			return load(pos, "jail:systemarg", new String[] { "value" },
					new Object[] { _value });
		} else {
			throw new ParseException("<jail:systemarg> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
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
			return load(pos, "jail:show", new String[] { "value" },
					new Object[] { _value });
		} else {
			throw new ParseException("<jail:show> expected, " + pos.charAt()
					+ " found.", pos.location());
		}
	}

	/** &lt;jail:nativedoc&gt; */
	public final Object pNativedoc(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
				|| (_alt_c == ' ') || (_alt_c == '/'))) {
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' '))) {
				try {
					pos.save();
					u.pSpaces(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			List<Object> _doc = new ArrayList<Object>();
			_doc.add(jail.pDoc(pos));
			_alt_c = pos.charAt();
			while ((_alt_c == '/')) {
				try {
					pos.save();
					_doc.add(jail.pDoc(pos));
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
					break;
				}
				_alt_c = pos.charAt();
			}
			u.pEof(pos);
			return load(pos, "jail:nativedoc", new String[] { "doc" },
					new Object[] { _doc });
		} else {
			throw new ParseException("<jail:nativedoc> expected, "
					+ pos.charAt() + " found.", pos.location());
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
				while (!pos.isEof()) {
					char f = pos.charAt();
					if (f == _zz_first && try$Exact(pos, "§>")) {
						break;
					} else {
						pos.more();
						_sb_literal.append(f);
					}
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
		if (((_alt_c == '"') || (_alt_c == '\'') || (_alt_c == '(')
				|| (_alt_c >= '0' && _alt_c <= '9') || (_alt_c == '<')
				|| (_alt_c == 'f') || (_alt_c == 't'))) {
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
		_alt_c = pos.charAt();
		if (((_alt_c == '(') || (_alt_c == '<'))) {
			try {
				pos.save();
				Object _expr = null;
				_expr = jail.pGexpression(pos);
				pos.commit();
				return load(pos, "jail:goperand", new String[] { "expr" },
						new Object[] { _expr });
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if (((_alt_c == '"') || (_alt_c == '\'')
				|| (_alt_c >= '0' && _alt_c <= '9') || (_alt_c == 'f') || (_alt_c == 't'))) {
			try {
				pos.save();
				Object _expr = null;
				_expr = jail.pLiteral(pos);
				pos.commit();
				return load(pos, "jail:goperand", new String[] { "expr" },
						new Object[] { _expr });
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return (Object) pos.error("<jail:goperand> expected, " + pos.charAt()
				+ " found.");
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
		if ((_alt_c == '{')) {
			try {
				pos.save();
				Object _tor = null;
				_tor = jail.pOptmatch(pos);
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
				_tor = jail.pOptliteral(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return (Object) pos.error("<jail:optvalue> expected, " + pos.charAt()
				+ " found.");
	}

	/** &lt;jail:optmatch&gt; */
	public final Object pOptmatch(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '{')) {
			Object _match = null;
			_match = gm.pMatchDo(pos);
			return load(pos, "jail:optmatch", new String[] { "match" },
					new Object[] { _match });
		} else {
			throw new ParseException("<jail:optmatch> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:optliteral&gt; */
	public final Object pOptliteral(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		_alt_c = pos.charAt();
		if (((_alt_c == '"') || (_alt_c == '\''))) {
			try {
				pos.save();
				Object _value = null;
				_value = u.pString(pos);
				pos.commit();
				return load(pos, "jail:optliteral", new String[] { "value" },
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
				return load(pos, "jail:optliteral", new String[] { "value" },
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
				return load(pos, "jail:optliteral", new String[] { "value" },
						new Object[] { _value });
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return (Object) pos.error("<jail:optliteral> expected, " + pos.charAt()
				+ " found.");
	}

	/** &lt;jail:literal&gt; */
	public final Object pLiteral(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		_alt_c = pos.charAt();
		if (((_alt_c == '"') || (_alt_c == '\''))) {
			try {
				pos.save();
				Object _value = null;
				_value = u.pString(pos);
				pos.commit();
				return load(pos, "jail:literal", new String[] { "value" },
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
				return load(pos, "jail:literal", new String[] { "value" },
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
				return load(pos, "jail:literal", new String[] { "value" },
						new Object[] { _value });
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return (Object) pos.error("<jail:literal> expected, " + pos.charAt()
				+ " found.");
	}

	/** &lt;jail:define&gt; */
	public final Object pDefine(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == 'd')) {
			p$Keyword(pos, "define");
			Object _header = null;
			_header = jail.pDefHeader(pos);
			Object _body = null;
			_body = jail.pDefBody(pos);
			return load(pos, "jail:define", new String[] { "header", "body" },
					new Object[] { _header, _body });
		} else {
			throw new ParseException("<jail:define> expected, " + pos.charAt()
					+ " found.", pos.location());
		}
	}

	/** &lt;jail:def_header&gt; */
	public final Object pDefHeader(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '(')) {
			p$Char(pos, '(');
			Object _name = null;
			_name = jail.pIdentifier(pos);
			Object _operand = null;
			_operand = jail.pDefOperands(pos);
			Object _option = null;
			_alt_c = pos.charAt();
			if ((_alt_c == ':')) {
				try {
					pos.save();
					_option = jail.pDefOptions(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			jail.pSy$41USymbol(pos);
			return load(pos, "jail:def_header", new String[] { "name",
					"operand", "option" }, new Object[] { _name, _operand,
					_option });
		} else {
			throw new ParseException("<jail:def_header> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:def_operands&gt; */
	public final Object pDefOperands(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '<')) {
			Object _placeholer = null;
			_placeholer = jail.pJailPholderdefUList(pos);
			return load(pos, "jail:def_operands",
					new String[] { "placeholer" }, new Object[] { _placeholer });
		} else {
			throw new ParseException("<jail:def_operands> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:def_options&gt; */
	public final Object pDefOptions(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == ':')) {
			Object _option = null;
			_option = jail.pJailDefOptionUList(pos);
			return load(pos, "jail:def_options", new String[] { "option" },
					new Object[] { _option });
		} else {
			throw new ParseException("<jail:def_options> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:def_option&gt; */
	public final Object pDefOption(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == ':')) {
			p$Char(pos, ':');
			Object _name = null;
			_name = jail.pIdentifier(pos);
			Object _placeholer = null;
			_placeholer = jail.pPholderdef(pos);
			return load(pos, "jail:def_option", new String[] { "name",
					"placeholer" }, new Object[] { _name, _placeholer });
		} else {
			throw new ParseException("<jail:def_option> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:def_body&gt; */
	public final Object pDefBody(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '(')) {
			Object _op = null;
			_op = jail.pGoperator(pos);
			return load(pos, "jail:def_body", new String[] { "op" },
					new Object[] { _op });
		} else {
			throw new ParseException("<jail:def_body> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:pholderdef&gt; */
	public final Object pPholderdef(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '<')) {
			p$Char(pos, '<');
			Object _name = null;
			_name = jail.pIdentifier(pos);
			jail.pSy$62USymbol(pos);
			return load(pos, "jail:pholderdef", new String[] { "name" },
					new Object[] { _name });
		} else {
			throw new ParseException("<jail:pholderdef> expected, "
					+ pos.charAt() + " found.", pos.location());
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

	/** &lt;jail:doc&gt; */
	public final Object pDoc(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '/')) {
			Object _help = null;
			_help = u.pJavaDocComment(pos);
			u.pSpaces(pos);
			Object _signature = null;
			_signature = jail.pSignature(pos);
			p$Char(pos, ';');
			u.pSpaces(pos);
			return load(pos, "jail:doc", new String[] { "help", "signature" },
					new Object[] { _help, _signature });
		} else {
			throw new ParseException("<jail:doc> expected, " + pos.charAt()
					+ " found.", pos.location());
		}
	}

	/** &lt;jail:signature&gt; */
	public final Object pSignature(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == 'd')) {
			p$Keyword(pos, "define");
			p$Keyword(pos, "native");
			String _tor = null;
			{
				StringBuffer _sb_tor = new StringBuffer();
				char _zz_first = ';';
				int _zz_start = pos.offset();
				while (!pos.isEof()) {
					char f = pos.charAt();
					if (f == _zz_first) {
						break;
					} else {
						pos.more();
						_sb_tor.append(f);
					}
				}
				if (_zz_start == pos.offset()) {
					throw new ParseException("!<symbol> expected, "
							+ pos.charAt() + " found.", pos.location());
				}
				_tor = _sb_tor.toString();
			}
			return _tor;
		} else {
			throw new ParseException("<jail:signature> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:ex$45$62_u_symbol&gt; */
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
			throw new ParseException("<jail:ex$45$62_u_symbol> expected, "
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

	/** &lt;jail:gm_function_arg_u_commalist&gt; */
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
					_n.add(jail.pGmFunctionArgUCommalist11(pos));
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
					"<jail:gm_function_arg_u_commalist> expected, "
							+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:gm_match_doattr_u_commalist&gt; */
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
					_n.add(jail.pGmMatchDoattrUCommalist11(pos));
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
					"<jail:gm_match_doattr_u_commalist> expected, "
							+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:gm_match_testone_u_list&gt; */
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
					_n.add(jail.pGmMatchTestoneUList11(pos));
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
					"<jail:gm_match_testone_u_list> expected, " + pos.charAt()
							+ " found.", pos.location());
		}
	}

	/** &lt;jail:gm_when_expr_u_list&gt; */
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
					_n.add(jail.pGmWhenExprUList11(pos));
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
					break;
				}
				_alt_c = pos.charAt();
			}
			return toList(_f, _n);
		} else {
			throw new ParseException("<jail:gm_when_expr_u_list> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:jail_command_u_list&gt; */
	public final List<?> pJailCommandUList(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '(') || (_alt_c == '<')
				|| (_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c == '\\')
				|| (_alt_c >= 'a' && _alt_c <= 'z') || (_alt_c == 'd'))) {
			Object _f = null;
			_f = jail.pCommand(pos);
			List<Object> _n = new ArrayList<Object>();
			_alt_c = pos.charAt();
			while (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
					|| (_alt_c == ' ') || (_alt_c == '(') || (_alt_c == '/')
					|| (_alt_c == '<') || (_alt_c >= 'A' && _alt_c <= 'Z')
					|| (_alt_c == '\\') || (_alt_c >= 'a' && _alt_c <= 'z') || (_alt_c == 'd'))) {
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

	/** &lt;jail:jail_def_option_u_list&gt; */
	public final List<?> pJailDefOptionUList(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == ':')) {
			Object _f = null;
			_f = jail.pDefOption(pos);
			List<Object> _n = new ArrayList<Object>();
			_alt_c = pos.charAt();
			while (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
					|| (_alt_c == ' ') || (_alt_c == '/') || (_alt_c == ':'))) {
				try {
					pos.save();
					_n.add(jail.pJailDefOptionUList11(pos));
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
					break;
				}
				_alt_c = pos.charAt();
			}
			return toList(_f, _n);
		} else {
			throw new ParseException("<jail:jail_def_option_u_list> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:jail_goperand_u_list&gt; */
	public final List<?> pJailGoperandUList(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '"') || (_alt_c == '\'') || (_alt_c == '(')
				|| (_alt_c >= '0' && _alt_c <= '9') || (_alt_c == '<')
				|| (_alt_c == 'f') || (_alt_c == 't'))) {
			Object _f = null;
			_f = jail.pGoperand(pos);
			List<Object> _n = new ArrayList<Object>();
			_alt_c = pos.charAt();
			while (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
					|| (_alt_c == ' ') || (_alt_c == '"') || (_alt_c == '\'')
					|| (_alt_c == '(') || (_alt_c == '/')
					|| (_alt_c >= '0' && _alt_c <= '9') || (_alt_c == '<')
					|| (_alt_c == 'f') || (_alt_c == 't'))) {
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

	/** &lt;jail:jail_pholderdef_u_list&gt; */
	public final List<?> pJailPholderdefUList(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '<')) {
			Object _f = null;
			_f = jail.pPholderdef(pos);
			List<Object> _n = new ArrayList<Object>();
			_alt_c = pos.charAt();
			while (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
					|| (_alt_c == ' ') || (_alt_c == '/') || (_alt_c == '<'))) {
				try {
					pos.save();
					_n.add(jail.pJailPholderdefUList11(pos));
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
					break;
				}
				_alt_c = pos.charAt();
			}
			return toList(_f, _n);
		} else {
			throw new ParseException("<jail:jail_pholderdef_u_list> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:jail_systemarg_u_list&gt; */
	public final List<?> pJailSystemargUList(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			Object _f = null;
			_f = jail.pSystemarg(pos);
			List<Object> _n = new ArrayList<Object>();
			_alt_c = pos.charAt();
			while (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
					|| (_alt_c == ' ') || (_alt_c == '/')
					|| (_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
				try {
					pos.save();
					_n.add(jail.pJailSystemargUList11(pos));
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
					break;
				}
				_alt_c = pos.charAt();
			}
			return toList(_f, _n);
		} else {
			throw new ParseException("<jail:jail_systemarg_u_list> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:sy$33$61_u_symbol&gt; */
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
			throw new ParseException("<jail:sy$33$61_u_symbol> expected, "
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

	/** &lt;jail:sy$44_u_symbol&gt; */
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
			throw new ParseException("<jail:sy$44_u_symbol> expected, "
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

	/** &lt;jail:sy$60_u_symbol&gt; */
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
			throw new ParseException("<jail:sy$60_u_symbol> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:sy$60$61_u_symbol&gt; */
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
			throw new ParseException("<jail:sy$60$61_u_symbol> expected, "
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

	/** &lt;jail:sy$62_u_symbol&gt; */
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
			throw new ParseException("<jail:sy$62_u_symbol> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:sy$62$61_u_symbol&gt; */
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
			throw new ParseException("<jail:sy$62$61_u_symbol> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:sy$123_u_symbol&gt; */
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
			throw new ParseException("<jail:sy$123_u_symbol> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:sy$125_u_symbol&gt; */
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
			throw new ParseException("<jail:sy$125_u_symbol> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:gm_function_arg_u_commalist_1_1&gt; */
	public final Object pGmFunctionArgUCommalist11(Pos pos)
			throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == ',')) {
			jail.pSy$44USymbol(pos);
			Object _tor = null;
			_tor = gm.pFunctionArg(pos);
			return _tor;
		} else {
			throw new ParseException(
					"<jail:gm_function_arg_u_commalist_1_1> expected, "
							+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:gm_match_doattr_u_commalist_1_1&gt; */
	public final Object pGmMatchDoattrUCommalist11(Pos pos)
			throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == ',')) {
			jail.pSy$44USymbol(pos);
			Object _tor = null;
			_tor = gm.pMatchDoattr(pos);
			return _tor;
		} else {
			throw new ParseException(
					"<jail:gm_match_doattr_u_commalist_1_1> expected, "
							+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:gm_match_testone_u_list_1_1&gt; */
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
					"<jail:gm_match_testone_u_list_1_1> expected, "
							+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:gm_when_expr_u_list_1_1&gt; */
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
			throw new ParseException(
					"<jail:gm_when_expr_u_list_1_1> expected, " + pos.charAt()
							+ " found.", pos.location());
		}
	}

	/** &lt;jail:jail_command_u_list_1_1&gt; */
	public final Object pJailCommandUList11(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
				|| (_alt_c == ' ') || (_alt_c == '(') || (_alt_c == '/')
				|| (_alt_c == '<') || (_alt_c >= 'A' && _alt_c <= 'Z')
				|| (_alt_c == '\\') || (_alt_c >= 'a' && _alt_c <= 'z') || (_alt_c == 'd'))) {
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

	/** &lt;jail:jail_def_option_u_list_1_1&gt; */
	public final Object pJailDefOptionUList11(Pos pos) throws ParseException {
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
			_tor = jail.pDefOption(pos);
			return _tor;
		} else {
			throw new ParseException(
					"<jail:jail_def_option_u_list_1_1> expected, "
							+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:jail_goperand_u_list_1_1&gt; */
	public final Object pJailGoperandUList11(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
				|| (_alt_c == ' ') || (_alt_c == '"') || (_alt_c == '\'')
				|| (_alt_c == '(') || (_alt_c == '/')
				|| (_alt_c >= '0' && _alt_c <= '9') || (_alt_c == '<')
				|| (_alt_c == 'f') || (_alt_c == 't'))) {
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

	/** &lt;jail:jail_pholderdef_u_list_1_1&gt; */
	public final Object pJailPholderdefUList11(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
				|| (_alt_c == ' ') || (_alt_c == '/') || (_alt_c == '<'))) {
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
			_tor = jail.pPholderdef(pos);
			return _tor;
		} else {
			throw new ParseException(
					"<jail:jail_pholderdef_u_list_1_1> expected, "
							+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:jail_systemarg_u_list_1_1&gt; */
	public final Object pJailSystemargUList11(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
				|| (_alt_c == ' ') || (_alt_c == '/')
				|| (_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
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
			_tor = jail.pSystemarg(pos);
			return _tor;
		} else {
			throw new ParseException(
					"<jail:jail_systemarg_u_list_1_1> expected, "
							+ pos.charAt() + " found.", pos.location());
		}
	}

}
