package be.uclouvain.jail.vm.autogram.v2;

import java.util.ArrayList;
import java.util.List;

import net.chefbe.autogram.ast2.parsing.ParseException;
import net.chefbe.autogram.ast2.parsing.active.ActiveParser;
import net.chefbe.autogram.ast2.parsing.peg.Memoization;
import net.chefbe.autogram.ast2.parsing.peg.PEGParser;
import net.chefbe.autogram.ast2.parsing.peg.Pos;

/** Parser for Jail grammar. */
public class JailParser extends ActiveParser {

	// ----------------------------------------------- External parsers
	/** External grammars. */
	private JailParser jail = this;

	private JailUtilsParser u = new JailUtilsParser(this);

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
		Memoization memo = pos.getMemoization("jail:unit");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pUnit(pos);
				pos.memoize(start, "jail:unit", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:unit", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:unit&gt; */
	public final Object _pUnit(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' ') || (_alt_c == '"')
				|| (_alt_c == '#') || (_alt_c == '\'') || (_alt_c == '(') || (_alt_c == '/') || (_alt_c == '0')
				|| (_alt_c >= '1' && _alt_c <= '9') || (_alt_c == '<') || (_alt_c >= 'A' && _alt_c <= 'Z')
				|| (_alt_c == '[') || (_alt_c == '^') || (_alt_c >= 'a' && _alt_c <= 'z') || (_alt_c == 'd')
				|| (_alt_c == 'f') || (_alt_c == 'n') || (_alt_c == 't') || (_alt_c == 'u') || (_alt_c == '{'))) {
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' ') || (_alt_c == '#'))) {
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
			return load(pos, "jail:unit", new String[] { "command" }, new Object[] { _command });
		} else {
			throw new ParseException("<jail:unit> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:command&gt; */
	public final Object pCommand(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:command");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pCommand(pos);
				pos.memoize(start, "jail:command", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:command", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:command&gt; */
	public final Object _pCommand(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		_alt_c = pos.charAt();
		if ((_alt_c == 'u')) {
			try {
				pos.save();
				Object _tor = null;
				_tor = jail.pUse(pos);
				jail.pSy$59USpaced(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if (((_alt_c == '/') || (_alt_c == 'n'))) {
			try {
				pos.save();
				Object _tor = null;
				_tor = jail.pNative(pos);
				jail.pSy$59USpaced(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if (((_alt_c == '/') || (_alt_c == 'd'))) {
			try {
				pos.save();
				Object _tor = null;
				_tor = jail.pDefine(pos);
				jail.pSy$59USpaced(pos);
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
				_tor = jail.pAffect(pos);
				jail.pSy$59USpaced(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if (((_alt_c == '"') || (_alt_c == '\'') || (_alt_c == '(') || (_alt_c == '0')
				|| (_alt_c >= '1' && _alt_c <= '9') || (_alt_c == '<') || (_alt_c >= 'A' && _alt_c <= 'Z')
				|| (_alt_c == '[') || (_alt_c == '^') || (_alt_c >= 'a' && _alt_c <= 'z') || (_alt_c == 'f')
				|| (_alt_c == 't') || (_alt_c == '{'))) {
			try {
				pos.save();
				Object _tor = null;
				_tor = jail.pEval(pos);
				jail.pSy$59USpaced(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return (Object) pos.error("<jail:command> expected, " + pos.charAt() + " found.");
	}

	/** &lt;jail:use&gt; */
	public final Object pUse(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:use");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pUse(pos);
				pos.memoize(start, "jail:use", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:use", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:use&gt; */
	public final Object _pUse(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == 'u')) {
			p$Keyword(pos, "use");
			Object _class = null;
			_class = u.pDotidentifier(pos);
			p$Keyword(pos, "as");
			Object _namespace = null;
			_namespace = u.pIdentifier(pos);
			return load(pos, "jail:use", new String[] { "class", "namespace" }, new Object[] { _class, _namespace });
		} else {
			throw new ParseException("<jail:use> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:native&gt; */
	public final Object pNative(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:native");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pNative(pos);
				pos.memoize(start, "jail:native", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:native", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:native&gt; */
	public final Object _pNative(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '/') || (_alt_c == 'n'))) {
			Object _doc = null;
			_alt_c = pos.charAt();
			if ((_alt_c == '/')) {
				try {
					pos.save();
					_doc = u.pJaildoc(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			p$Keyword(pos, "native");
			Object _signature = null;
			_signature = jail.pSignature(pos);
			return load(pos, "jail:native", new String[] { "doc", "signature" }, new Object[] { _doc, _signature });
		} else {
			throw new ParseException("<jail:native> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:define&gt; */
	public final Object pDefine(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:define");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pDefine(pos);
				pos.memoize(start, "jail:define", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:define", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:define&gt; */
	public final Object _pDefine(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '/') || (_alt_c == 'd'))) {
			Object _doc = null;
			_alt_c = pos.charAt();
			if ((_alt_c == '/')) {
				try {
					pos.save();
					_doc = u.pJaildoc(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			p$Keyword(pos, "define");
			Object _signature = null;
			_signature = jail.pSignature(pos);
			Object _body = null;
			_body = jail.pExpression(pos);
			return load(pos, "jail:define", new String[] { "doc", "signature", "body" }, new Object[] { _doc,
					_signature, _body });
		} else {
			throw new ParseException("<jail:define> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:affect&gt; */
	public final Object pAffect(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:affect");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pAffect(pos);
				pos.memoize(start, "jail:affect", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:affect", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:affect&gt; */
	public final Object _pAffect(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			Object _var = null;
			_var = u.pIdentifier(pos);
			jail.pSy$61USpaced(pos);
			Object _value = null;
			_value = jail.pExpression(pos);
			return load(pos, "jail:affect", new String[] { "var", "value" }, new Object[] { _var, _value });
		} else {
			throw new ParseException("<jail:affect> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:eval&gt; */
	public final Object pEval(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:eval");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pEval(pos);
				pos.memoize(start, "jail:eval", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:eval", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:eval&gt; */
	public final Object _pEval(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '"') || (_alt_c == '\'') || (_alt_c == '(') || (_alt_c == '0')
				|| (_alt_c >= '1' && _alt_c <= '9') || (_alt_c == '<') || (_alt_c >= 'A' && _alt_c <= 'Z')
				|| (_alt_c == '[') || (_alt_c == '^') || (_alt_c >= 'a' && _alt_c <= 'z') || (_alt_c == 'f')
				|| (_alt_c == 't') || (_alt_c == '{'))) {
			Object _value = null;
			_value = jail.pExpression(pos);
			return load(pos, "jail:eval", new String[] { "value" }, new Object[] { _value });
		} else {
			throw new ParseException("<jail:eval> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:signature&gt; */
	public final Object pSignature(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:signature");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pSignature(pos);
				pos.memoize(start, "jail:signature", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:signature", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:signature&gt; */
	public final Object _pSignature(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '(')) {
			p$Char(pos, '(');
			Object _name = null;
			_name = u.pQidentifier(pos);
			Object _param = null;
			_alt_c = pos.charAt();
			if ((_alt_c == '<')) {
				try {
					pos.save();
					_param = jail.pParameters(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			Object _optparam = null;
			_alt_c = pos.charAt();
			if ((_alt_c == ':')) {
				try {
					pos.save();
					_optparam = jail.pOptparams(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			jail.pSy$41USpaced(pos);
			p$Keyword(pos, "returns");
			Object _returns = null;
			_returns = jail.pParameter(pos);
			return load(pos, "jail:signature", new String[] { "name", "param", "optparam", "returns" }, new Object[] {
					_name, _param, _optparam, _returns });
		} else {
			throw new ParseException("<jail:signature> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:parameters&gt; */
	public final Object pParameters(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:parameters");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pParameters(pos);
				pos.memoize(start, "jail:parameters", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:parameters", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:parameters&gt; */
	public final Object _pParameters(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '<')) {
			Object _param = null;
			_param = jail.pJailParameterUList(pos);
			return load(pos, "jail:parameters", new String[] { "param" }, new Object[] { _param });
		} else {
			throw new ParseException("<jail:parameters> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:parameter&gt; */
	public final Object pParameter(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:parameter");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pParameter(pos);
				pos.memoize(start, "jail:parameter", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:parameter", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:parameter&gt; */
	public final Object _pParameter(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		_alt_c = pos.charAt();
		if ((_alt_c == '<')) {
			try {
				pos.save();
				Object _tor = null;
				_tor = jail.pSingparam(pos);
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
				_tor = jail.pArgsparam(pos);
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
				_tor = jail.pPickparam(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return (Object) pos.error("<jail:parameter> expected, " + pos.charAt() + " found.");
	}

	/** &lt;jail:optparams&gt; */
	public final Object pOptparams(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:optparams");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pOptparams(pos);
				pos.memoize(start, "jail:optparams", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:optparams", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:optparams&gt; */
	public final Object _pOptparams(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == ':')) {
			Object _param = null;
			_param = jail.pJailOptparamUList(pos);
			return load(pos, "jail:optparams", new String[] { "param" }, new Object[] { _param });
		} else {
			throw new ParseException("<jail:optparams> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:optparam&gt; */
	public final Object pOptparam(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:optparam");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pOptparam(pos);
				pos.memoize(start, "jail:optparam", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:optparam", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:optparam&gt; */
	public final Object _pOptparam(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == ':')) {
			p$Char(pos, ':');
			Object _name = null;
			_name = u.pIdchars(pos);
			Object _optional = null;
			_alt_c = pos.charAt();
			if ((_alt_c == '?')) {
				try {
					pos.save();
					_optional = p$Char(pos, '?');
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' ') || (_alt_c == '#'))) {
				try {
					pos.save();
					u.pSpacing(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			Object _param = null;
			_param = jail.pSingparam(pos);
			return load(pos, "jail:optparam", new String[] { "name", "optional", "param" }, new Object[] { _name,
					_optional, _param });
		} else {
			throw new ParseException("<jail:optparam> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:singparam&gt; */
	public final Object pSingparam(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:singparam");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pSingparam(pos);
				pos.memoize(start, "jail:singparam", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:singparam", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:singparam&gt; */
	public final Object _pSingparam(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '<')) {
			p$Char(pos, '<');
			Object _name = null;
			_name = u.pIdchars(pos);
			jail.pSy$62USpaced(pos);
			return load(pos, "jail:singparam", new String[] { "name" }, new Object[] { _name });
		} else {
			throw new ParseException("<jail:singparam> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:argsparam&gt; */
	public final Object pArgsparam(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:argsparam");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pArgsparam(pos);
				pos.memoize(start, "jail:argsparam", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:argsparam", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:argsparam&gt; */
	public final Object _pArgsparam(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '<')) {
			p$Char(pos, '<');
			Object _name = null;
			_name = u.pIdchars(pos);
			jail.pEx$46$46$46$62USpaced(pos);
			return load(pos, "jail:argsparam", new String[] { "name" }, new Object[] { _name });
		} else {
			throw new ParseException("<jail:argsparam> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:pickparam&gt; */
	public final Object pPickparam(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:pickparam");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pPickparam(pos);
				pos.memoize(start, "jail:pickparam", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:pickparam", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:pickparam&gt; */
	public final Object _pPickparam(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '<')) {
			p$Char(pos, '<');
			Object _name = null;
			_name = u.pIdchars(pos);
			p$Char(pos, '[');
			Object _index = null;
			_index = u.pInteger(pos);
			p$Char(pos, ']');
			jail.pSy$62USpaced(pos);
			return load(pos, "jail:pickparam", new String[] { "name", "index" }, new Object[] { _name, _index });
		} else {
			throw new ParseException("<jail:pickparam> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:expression&gt; */
	public final Object pExpression(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:expression");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pExpression(pos);
				pos.memoize(start, "jail:expression", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:expression", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:expression&gt; */
	public final Object _pExpression(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		_alt_c = pos.charAt();
		if ((_alt_c == '(')) {
			try {
				pos.save();
				Object _tor = null;
				_tor = jail.pFunccall(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if (((_alt_c == '"') || (_alt_c == '\'') || (_alt_c == '0') || (_alt_c >= '1' && _alt_c <= '9')
				|| (_alt_c == '<') || (_alt_c == '[') || (_alt_c == 'f') || (_alt_c == 't') || (_alt_c == '{'))) {
			try {
				pos.save();
				Object _tor = null;
				_tor = jail.pLiteral(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if (((_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c == '^') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
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
		return (Object) pos.error("<jail:expression> expected, " + pos.charAt() + " found.");
	}

	/** &lt;jail:funccall&gt; */
	public final Object pFunccall(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:funccall");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pFunccall(pos);
				pos.memoize(start, "jail:funccall", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:funccall", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:funccall&gt; */
	public final Object _pFunccall(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '(')) {
			p$Char(pos, '(');
			Object _name = null;
			_name = u.pQidentifier(pos);
			Object _arg = null;
			_alt_c = pos.charAt();
			if (((_alt_c == '"') || (_alt_c == '\'') || (_alt_c == '(') || (_alt_c == '0')
					|| (_alt_c >= '1' && _alt_c <= '9') || (_alt_c == '<') || (_alt_c >= 'A' && _alt_c <= 'Z')
					|| (_alt_c == '[') || (_alt_c == '^') || (_alt_c >= 'a' && _alt_c <= 'z') || (_alt_c == 'f')
					|| (_alt_c == 't') || (_alt_c == '{'))) {
				try {
					pos.save();
					_arg = jail.pArgs(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			Object _optarg = null;
			_alt_c = pos.charAt();
			if ((_alt_c == ':')) {
				try {
					pos.save();
					_optarg = jail.pOptargs(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			jail.pSy$41USpaced(pos);
			return load(pos, "jail:funccall", new String[] { "name", "arg", "optarg" }, new Object[] { _name, _arg,
					_optarg });
		} else {
			throw new ParseException("<jail:funccall> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:args&gt; */
	public final Object pArgs(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:args");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pArgs(pos);
				pos.memoize(start, "jail:args", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:args", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:args&gt; */
	public final Object _pArgs(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '"') || (_alt_c == '\'') || (_alt_c == '(') || (_alt_c == '0')
				|| (_alt_c >= '1' && _alt_c <= '9') || (_alt_c == '<') || (_alt_c >= 'A' && _alt_c <= 'Z')
				|| (_alt_c == '[') || (_alt_c == '^') || (_alt_c >= 'a' && _alt_c <= 'z') || (_alt_c == 'f')
				|| (_alt_c == 't') || (_alt_c == '{'))) {
			Object _arg = null;
			_arg = jail.pJailArgUList(pos);
			return load(pos, "jail:args", new String[] { "arg" }, new Object[] { _arg });
		} else {
			throw new ParseException("<jail:args> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:arg&gt; */
	public final Object pArg(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:arg");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pArg(pos);
				pos.memoize(start, "jail:arg", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:arg", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:arg&gt; */
	public final Object _pArg(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '"') || (_alt_c == '\'') || (_alt_c == '(') || (_alt_c == '0')
				|| (_alt_c >= '1' && _alt_c <= '9') || (_alt_c == '<') || (_alt_c >= 'A' && _alt_c <= 'Z')
				|| (_alt_c == '[') || (_alt_c == '^') || (_alt_c >= 'a' && _alt_c <= 'z') || (_alt_c == 'f')
				|| (_alt_c == 't') || (_alt_c == '{'))) {
			Object _expr = null;
			_expr = jail.pExpression(pos);
			return load(pos, "jail:arg", new String[] { "expr" }, new Object[] { _expr });
		} else {
			throw new ParseException("<jail:arg> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:optargs&gt; */
	public final Object pOptargs(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:optargs");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pOptargs(pos);
				pos.memoize(start, "jail:optargs", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:optargs", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:optargs&gt; */
	public final Object _pOptargs(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == ':')) {
			Object _arg = null;
			_arg = jail.pJailOptargUList(pos);
			return load(pos, "jail:optargs", new String[] { "arg" }, new Object[] { _arg });
		} else {
			throw new ParseException("<jail:optargs> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:optarg&gt; */
	public final Object pOptarg(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:optarg");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pOptarg(pos);
				pos.memoize(start, "jail:optarg", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:optarg", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:optarg&gt; */
	public final Object _pOptarg(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == ':')) {
			p$Char(pos, ':');
			Object _name = null;
			_name = u.pIdentifier(pos);
			Object _value = null;
			_value = jail.pExpression(pos);
			return load(pos, "jail:optarg", new String[] { "name", "value" }, new Object[] { _name, _value });
		} else {
			throw new ParseException("<jail:optarg> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:literal&gt; */
	public final Object pLiteral(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:literal");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pLiteral(pos);
				pos.memoize(start, "jail:literal", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:literal", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:literal&gt; */
	public final Object _pLiteral(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		_alt_c = pos.charAt();
		if ((_alt_c == '<')) {
			try {
				pos.save();
				Object _tor = null;
				_tor = jail.pDialectlit(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if ((_alt_c == '[')) {
			try {
				pos.save();
				Object _tor = null;
				_tor = jail.pArraylit(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if ((_alt_c == '{')) {
			try {
				pos.save();
				Object _tor = null;
				_tor = jail.pSetlit(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if (((_alt_c == '"') || (_alt_c == '\'') || (_alt_c == '0') || (_alt_c >= '1' && _alt_c <= '9')
				|| (_alt_c == 'f') || (_alt_c == 't'))) {
			try {
				pos.save();
				Object _tor = null;
				_tor = jail.pAtomiclit(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return (Object) pos.error("<jail:literal> expected, " + pos.charAt() + " found.");
	}

	/** &lt;jail:dialectlit&gt; */
	public final Object pDialectlit(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:dialectlit");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pDialectlit(pos);
				pos.memoize(start, "jail:dialectlit", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:dialectlit", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:dialectlit&gt; */
	public final Object _pDialectlit(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '<')) {
			p$Exact(pos, "<§");
			Object _dialect = null;
			_dialect = u.pIdchars(pos);
			u.pAspace(pos);
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
			jail.pEx$167$62USpaced(pos);
			return load(pos, "jail:dialectlit", new String[] { "dialect", "literal" }, new Object[] { _dialect,
					_literal });
		} else {
			throw new ParseException("<jail:dialectlit> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:arraylit&gt; */
	public final Object pArraylit(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:arraylit");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pArraylit(pos);
				pos.memoize(start, "jail:arraylit", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:arraylit", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:arraylit&gt; */
	public final Object _pArraylit(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '[')) {
			Object _value = null;
			_value = jail.pJailExpressionUArray(pos);
			return load(pos, "jail:arraylit", new String[] { "value" }, new Object[] { _value });
		} else {
			throw new ParseException("<jail:arraylit> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:setlit&gt; */
	public final Object pSetlit(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:setlit");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pSetlit(pos);
				pos.memoize(start, "jail:setlit", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:setlit", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:setlit&gt; */
	public final Object _pSetlit(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '{')) {
			Object _value = null;
			_value = jail.pJailExpressionUSet(pos);
			return load(pos, "jail:setlit", new String[] { "value" }, new Object[] { _value });
		} else {
			throw new ParseException("<jail:setlit> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:atomiclit&gt; */
	public final Object pAtomiclit(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:atomiclit");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pAtomiclit(pos);
				pos.memoize(start, "jail:atomiclit", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:atomiclit", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:atomiclit&gt; */
	public final Object _pAtomiclit(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		_alt_c = pos.charAt();
		if (((_alt_c == 'f') || (_alt_c == 't'))) {
			try {
				pos.save();
				Object _value = null;
				_value = jail.pUBooleanUSpaced(pos);
				pos.commit();
				return load(pos, "jail:atomiclit", new String[] { "value" }, new Object[] { _value });
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if ((_alt_c == '\'')) {
			try {
				pos.save();
				Object _value = null;
				_value = jail.pUCharUSpaced(pos);
				pos.commit();
				return load(pos, "jail:atomiclit", new String[] { "value" }, new Object[] { _value });
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if ((_alt_c == '"')) {
			try {
				pos.save();
				Object _value = null;
				_value = jail.pUStringUSpaced(pos);
				pos.commit();
				return load(pos, "jail:atomiclit", new String[] { "value" }, new Object[] { _value });
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if (((_alt_c == '0') || (_alt_c >= '1' && _alt_c <= '9'))) {
			try {
				pos.save();
				Object _value = null;
				_value = jail.pUFloatUSpaced(pos);
				pos.commit();
				return load(pos, "jail:atomiclit", new String[] { "value" }, new Object[] { _value });
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if (((_alt_c == '0') || (_alt_c >= '1' && _alt_c <= '9'))) {
			try {
				pos.save();
				Object _value = null;
				_value = jail.pUIntegerUSpaced(pos);
				pos.commit();
				return load(pos, "jail:atomiclit", new String[] { "value" }, new Object[] { _value });
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return (Object) pos.error("<jail:atomiclit> expected, " + pos.charAt() + " found.");
	}

	/** &lt;jail:varref&gt; */
	public final Object pVarref(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:varref");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pVarref(pos);
				pos.memoize(start, "jail:varref", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:varref", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:varref&gt; */
	public final Object _pVarref(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		_alt_c = pos.charAt();
		if (((_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			try {
				pos.save();
				Object _tor = null;
				_tor = jail.pDirectref(pos);
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
				_tor = jail.pIndexedref(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if ((_alt_c == '^')) {
			try {
				pos.save();
				Object _tor = null;
				_tor = jail.pExtensionref(pos);
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
				_tor = jail.pQualifiedref(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return (Object) pos.error("<jail:varref> expected, " + pos.charAt() + " found.");
	}

	/** &lt;jail:directref&gt; */
	public final Object pDirectref(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:directref");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pDirectref(pos);
				pos.memoize(start, "jail:directref", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:directref", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:directref&gt; */
	public final Object _pDirectref(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			Object _name = null;
			_name = u.pIdentifier(pos);
			return load(pos, "jail:directref", new String[] { "name" }, new Object[] { _name });
		} else {
			throw new ParseException("<jail:directref> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:qualifiedref&gt; */
	public final Object pQualifiedref(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:qualifiedref");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pQualifiedref(pos);
				pos.memoize(start, "jail:qualifiedref", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:qualifiedref", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:qualifiedref&gt; */
	public final Object _pQualifiedref(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			u.pIdchars(pos);
			_alt_c = pos.charAt();
			while ((_alt_c == '.')) {
				try {
					pos.save();
					jail.pQualifiedref11(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
					break;
				}
				_alt_c = pos.charAt();
			}
			return load(pos, "jail:qualifiedref", new String[] {}, new Object[] {});
		} else {
			throw new ParseException("<jail:qualifiedref> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:indexedref&gt; */
	public final Object pIndexedref(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:indexedref");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pIndexedref(pos);
				pos.memoize(start, "jail:indexedref", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:indexedref", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:indexedref&gt; */
	public final Object _pIndexedref(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			Object _name = null;
			_name = u.pIdchars(pos);
			p$Char(pos, '[');
			Object _index = null;
			_index = u.pInteger(pos);
			jail.pSy$93USpaced(pos);
			return load(pos, "jail:indexedref", new String[] { "name", "index" }, new Object[] { _name, _index });
		} else {
			throw new ParseException("<jail:indexedref> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:extensionref&gt; */
	public final Object pExtensionref(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:extensionref");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pExtensionref(pos);
				pos.memoize(start, "jail:extensionref", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:extensionref", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:extensionref&gt; */
	public final Object _pExtensionref(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '^')) {
			p$Char(pos, '^');
			Object _name = null;
			_name = u.pIdentifier(pos);
			return load(pos, "jail:extensionref", new String[] { "name" }, new Object[] { _name });
		} else {
			throw new ParseException("<jail:extensionref> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:identifier&gt; */
	public final Object pIdentifier(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:identifier");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pIdentifier(pos);
				pos.memoize(start, "jail:identifier", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:identifier", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:identifier&gt; */
	public final Object _pIdentifier(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			Object _tor = null;
			_tor = u.pIdentifier(pos);
			return _tor;
		} else {
			throw new ParseException("<jail:identifier> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:qidentifier&gt; */
	public final Object pQidentifier(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:qidentifier");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pQidentifier(pos);
				pos.memoize(start, "jail:qidentifier", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:qidentifier", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:qidentifier&gt; */
	public final Object _pQidentifier(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		_alt_c = pos.charAt();
		if (((_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			try {
				pos.save();
				Object _tor = null;
				_tor = u.pQidentifier(pos);
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
				_tor = u.pIdentifier(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return (Object) pos.error("<jail:qidentifier> expected, " + pos.charAt() + " found.");
	}

	/** &lt;jail:ex$46$46$46$62_u_spaced&gt; */
	public final Object pEx$46$46$46$62USpaced(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:ex$46$46$46$62_u_spaced");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pEx$46$46$46$62USpaced(pos);
				pos.memoize(start, "jail:ex$46$46$46$62_u_spaced", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:ex$46$46$46$62_u_spaced", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:ex$46$46$46$62_u_spaced&gt; */
	public final Object _pEx$46$46$46$62USpaced(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '.')) {
			Object _tor = null;
			_tor = p$Exact(pos, "...>");
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' ') || (_alt_c == '#'))) {
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
			throw new ParseException("<jail:ex$46$46$46$62_u_spaced> expected, " + pos.charAt() + " found.", pos
					.location());
		}
	}

	/** &lt;jail:ex$167$62_u_spaced&gt; */
	public final Object pEx$167$62USpaced(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:ex$167$62_u_spaced");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pEx$167$62USpaced(pos);
				pos.memoize(start, "jail:ex$167$62_u_spaced", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:ex$167$62_u_spaced", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:ex$167$62_u_spaced&gt; */
	public final Object _pEx$167$62USpaced(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '§')) {
			Object _tor = null;
			_tor = p$Exact(pos, "§>");
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' ') || (_alt_c == '#'))) {
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
			throw new ParseException("<jail:ex$167$62_u_spaced> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:jail_arg_u_list&gt; */
	public final List<?> pJailArgUList(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:jail_arg_u_list");
		if (memo == null) {
			int start = pos.offset();
			try {
				List<?> production = _pJailArgUList(pos);
				pos.memoize(start, "jail:jail_arg_u_list", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:jail_arg_u_list", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (List<?>) prod;
			}
		}
	}

	/** &lt;jail:jail_arg_u_list&gt; */
	public final List<?> _pJailArgUList(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '"') || (_alt_c == '\'') || (_alt_c == '(') || (_alt_c == '0')
				|| (_alt_c >= '1' && _alt_c <= '9') || (_alt_c == '<') || (_alt_c >= 'A' && _alt_c <= 'Z')
				|| (_alt_c == '[') || (_alt_c == '^') || (_alt_c >= 'a' && _alt_c <= 'z') || (_alt_c == 'f')
				|| (_alt_c == 't') || (_alt_c == '{'))) {
			Object _f = null;
			_f = jail.pArg(pos);
			List<Object> _n = new ArrayList<Object>();
			_alt_c = pos.charAt();
			while (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' ') || (_alt_c == '"')
					|| (_alt_c == '#') || (_alt_c == '\'') || (_alt_c == '(') || (_alt_c == '0')
					|| (_alt_c >= '1' && _alt_c <= '9') || (_alt_c == '<') || (_alt_c >= 'A' && _alt_c <= 'Z')
					|| (_alt_c == '[') || (_alt_c == '^') || (_alt_c >= 'a' && _alt_c <= 'z') || (_alt_c == 'f')
					|| (_alt_c == 't') || (_alt_c == '{'))) {
				try {
					pos.save();
					_n.add(jail.pJailArgUList11(pos));
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
					break;
				}
				_alt_c = pos.charAt();
			}
			return toList(_f, _n);
		} else {
			throw new ParseException("<jail:jail_arg_u_list> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:jail_command_u_list&gt; */
	public final List<?> pJailCommandUList(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:jail_command_u_list");
		if (memo == null) {
			int start = pos.offset();
			try {
				List<?> production = _pJailCommandUList(pos);
				pos.memoize(start, "jail:jail_command_u_list", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:jail_command_u_list", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (List<?>) prod;
			}
		}
	}

	/** &lt;jail:jail_command_u_list&gt; */
	public final List<?> _pJailCommandUList(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '"') || (_alt_c == '\'') || (_alt_c == '(') || (_alt_c == '/') || (_alt_c == '0')
				|| (_alt_c >= '1' && _alt_c <= '9') || (_alt_c == '<') || (_alt_c >= 'A' && _alt_c <= 'Z')
				|| (_alt_c == '[') || (_alt_c == '^') || (_alt_c >= 'a' && _alt_c <= 'z') || (_alt_c == 'd')
				|| (_alt_c == 'f') || (_alt_c == 'n') || (_alt_c == 't') || (_alt_c == 'u') || (_alt_c == '{'))) {
			Object _f = null;
			_f = jail.pCommand(pos);
			List<Object> _n = new ArrayList<Object>();
			_alt_c = pos.charAt();
			while (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' ') || (_alt_c == '"')
					|| (_alt_c == '#') || (_alt_c == '\'') || (_alt_c == '(') || (_alt_c == '/') || (_alt_c == '0')
					|| (_alt_c >= '1' && _alt_c <= '9') || (_alt_c == '<') || (_alt_c >= 'A' && _alt_c <= 'Z')
					|| (_alt_c == '[') || (_alt_c == '^') || (_alt_c >= 'a' && _alt_c <= 'z') || (_alt_c == 'd')
					|| (_alt_c == 'f') || (_alt_c == 'n') || (_alt_c == 't') || (_alt_c == 'u') || (_alt_c == '{'))) {
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
			throw new ParseException("<jail:jail_command_u_list> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:jail_expression_u_array&gt; */
	public final Object[] pJailExpressionUArray(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:jail_expression_u_array");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object[] production = _pJailExpressionUArray(pos);
				pos.memoize(start, "jail:jail_expression_u_array", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:jail_expression_u_array", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object[]) prod;
			}
		}
	}

	/** &lt;jail:jail_expression_u_array&gt; */
	public final Object[] _pJailExpressionUArray(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '[')) {
			jail.pSy$91USpaced(pos);
			Object _tor = null;
			_alt_c = pos.charAt();
			if (((_alt_c == '"') || (_alt_c == '\'') || (_alt_c == '(') || (_alt_c == '0')
					|| (_alt_c >= '1' && _alt_c <= '9') || (_alt_c == '<') || (_alt_c >= 'A' && _alt_c <= 'Z')
					|| (_alt_c == '[') || (_alt_c == '^') || (_alt_c >= 'a' && _alt_c <= 'z') || (_alt_c == 'f')
					|| (_alt_c == 't') || (_alt_c == '{'))) {
				try {
					pos.save();
					_tor = jail.pJailExpressionUCommalist(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			p$Char(pos, ']');
			return ((List) _tor).toArray();
		} else {
			throw new ParseException("<jail:jail_expression_u_array> expected, " + pos.charAt() + " found.", pos
					.location());
		}
	}

	/** &lt;jail:jail_expression_u_commalist&gt; */
	public final List<?> pJailExpressionUCommalist(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:jail_expression_u_commalist");
		if (memo == null) {
			int start = pos.offset();
			try {
				List<?> production = _pJailExpressionUCommalist(pos);
				pos.memoize(start, "jail:jail_expression_u_commalist", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:jail_expression_u_commalist", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (List<?>) prod;
			}
		}
	}

	/** &lt;jail:jail_expression_u_commalist&gt; */
	public final List<?> _pJailExpressionUCommalist(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '"') || (_alt_c == '\'') || (_alt_c == '(') || (_alt_c == '0')
				|| (_alt_c >= '1' && _alt_c <= '9') || (_alt_c == '<') || (_alt_c >= 'A' && _alt_c <= 'Z')
				|| (_alt_c == '[') || (_alt_c == '^') || (_alt_c >= 'a' && _alt_c <= 'z') || (_alt_c == 'f')
				|| (_alt_c == 't') || (_alt_c == '{'))) {
			Object _f = null;
			_f = jail.pExpression(pos);
			List<Object> _n = new ArrayList<Object>();
			_alt_c = pos.charAt();
			while ((_alt_c == ',')) {
				try {
					pos.save();
					_n.add(jail.pJailExpressionUCommalist11(pos));
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
					break;
				}
				_alt_c = pos.charAt();
			}
			return toList(_f, _n);
		} else {
			throw new ParseException("<jail:jail_expression_u_commalist> expected, " + pos.charAt() + " found.", pos
					.location());
		}
	}

	/** &lt;jail:jail_expression_u_set&gt; */
	public final Object pJailExpressionUSet(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:jail_expression_u_set");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pJailExpressionUSet(pos);
				pos.memoize(start, "jail:jail_expression_u_set", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:jail_expression_u_set", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:jail_expression_u_set&gt; */
	public final Object _pJailExpressionUSet(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '{')) {
			jail.pSy$123USpaced(pos);
			Object _tor = null;
			_alt_c = pos.charAt();
			if (((_alt_c == '"') || (_alt_c == '\'') || (_alt_c == '(') || (_alt_c == '0')
					|| (_alt_c >= '1' && _alt_c <= '9') || (_alt_c == '<') || (_alt_c >= 'A' && _alt_c <= 'Z')
					|| (_alt_c == '[') || (_alt_c == '^') || (_alt_c >= 'a' && _alt_c <= 'z') || (_alt_c == 'f')
					|| (_alt_c == 't') || (_alt_c == '{'))) {
				try {
					pos.save();
					_tor = jail.pJailExpressionUCommalist(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			p$Char(pos, '}');
			return _tor;
		} else {
			throw new ParseException("<jail:jail_expression_u_set> expected, " + pos.charAt() + " found.", pos
					.location());
		}
	}

	/** &lt;jail:jail_optarg_u_list&gt; */
	public final List<?> pJailOptargUList(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:jail_optarg_u_list");
		if (memo == null) {
			int start = pos.offset();
			try {
				List<?> production = _pJailOptargUList(pos);
				pos.memoize(start, "jail:jail_optarg_u_list", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:jail_optarg_u_list", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (List<?>) prod;
			}
		}
	}

	/** &lt;jail:jail_optarg_u_list&gt; */
	public final List<?> _pJailOptargUList(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == ':')) {
			Object _f = null;
			_f = jail.pOptarg(pos);
			List<Object> _n = new ArrayList<Object>();
			_alt_c = pos.charAt();
			while (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' ') || (_alt_c == '#') || (_alt_c == ':'))) {
				try {
					pos.save();
					_n.add(jail.pJailOptargUList11(pos));
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
					break;
				}
				_alt_c = pos.charAt();
			}
			return toList(_f, _n);
		} else {
			throw new ParseException("<jail:jail_optarg_u_list> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:jail_optparam_u_list&gt; */
	public final List<?> pJailOptparamUList(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:jail_optparam_u_list");
		if (memo == null) {
			int start = pos.offset();
			try {
				List<?> production = _pJailOptparamUList(pos);
				pos.memoize(start, "jail:jail_optparam_u_list", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:jail_optparam_u_list", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (List<?>) prod;
			}
		}
	}

	/** &lt;jail:jail_optparam_u_list&gt; */
	public final List<?> _pJailOptparamUList(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == ':')) {
			Object _f = null;
			_f = jail.pOptparam(pos);
			List<Object> _n = new ArrayList<Object>();
			_alt_c = pos.charAt();
			while (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' ') || (_alt_c == '#') || (_alt_c == ':'))) {
				try {
					pos.save();
					_n.add(jail.pJailOptparamUList11(pos));
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
					break;
				}
				_alt_c = pos.charAt();
			}
			return toList(_f, _n);
		} else {
			throw new ParseException("<jail:jail_optparam_u_list> expected, " + pos.charAt() + " found.", pos
					.location());
		}
	}

	/** &lt;jail:jail_parameter_u_list&gt; */
	public final List<?> pJailParameterUList(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:jail_parameter_u_list");
		if (memo == null) {
			int start = pos.offset();
			try {
				List<?> production = _pJailParameterUList(pos);
				pos.memoize(start, "jail:jail_parameter_u_list", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:jail_parameter_u_list", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (List<?>) prod;
			}
		}
	}

	/** &lt;jail:jail_parameter_u_list&gt; */
	public final List<?> _pJailParameterUList(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '<')) {
			Object _f = null;
			_f = jail.pParameter(pos);
			List<Object> _n = new ArrayList<Object>();
			_alt_c = pos.charAt();
			while (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' ') || (_alt_c == '#') || (_alt_c == '<'))) {
				try {
					pos.save();
					_n.add(jail.pJailParameterUList11(pos));
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
					break;
				}
				_alt_c = pos.charAt();
			}
			return toList(_f, _n);
		} else {
			throw new ParseException("<jail:jail_parameter_u_list> expected, " + pos.charAt() + " found.", pos
					.location());
		}
	}

	/** &lt;jail:u_boolean_u_spaced&gt; */
	public final Object pUBooleanUSpaced(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:u_boolean_u_spaced");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pUBooleanUSpaced(pos);
				pos.memoize(start, "jail:u_boolean_u_spaced", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:u_boolean_u_spaced", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:u_boolean_u_spaced&gt; */
	public final Object _pUBooleanUSpaced(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == 'f') || (_alt_c == 't'))) {
			Object _tor = null;
			_tor = u.pBoolean(pos);
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' ') || (_alt_c == '#'))) {
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
			throw new ParseException("<jail:u_boolean_u_spaced> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:u_char_u_spaced&gt; */
	public final Object pUCharUSpaced(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:u_char_u_spaced");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pUCharUSpaced(pos);
				pos.memoize(start, "jail:u_char_u_spaced", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:u_char_u_spaced", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:u_char_u_spaced&gt; */
	public final Object _pUCharUSpaced(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '\'')) {
			Object _tor = null;
			_tor = u.pChar(pos);
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' ') || (_alt_c == '#'))) {
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
			throw new ParseException("<jail:u_char_u_spaced> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:u_float_u_spaced&gt; */
	public final Object pUFloatUSpaced(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:u_float_u_spaced");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pUFloatUSpaced(pos);
				pos.memoize(start, "jail:u_float_u_spaced", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:u_float_u_spaced", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:u_float_u_spaced&gt; */
	public final Object _pUFloatUSpaced(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '0') || (_alt_c >= '1' && _alt_c <= '9'))) {
			Object _tor = null;
			_tor = u.pFloat(pos);
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' ') || (_alt_c == '#'))) {
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
			throw new ParseException("<jail:u_float_u_spaced> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:u_integer_u_spaced&gt; */
	public final Object pUIntegerUSpaced(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:u_integer_u_spaced");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pUIntegerUSpaced(pos);
				pos.memoize(start, "jail:u_integer_u_spaced", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:u_integer_u_spaced", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:u_integer_u_spaced&gt; */
	public final Object _pUIntegerUSpaced(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '0') || (_alt_c >= '1' && _alt_c <= '9'))) {
			Object _tor = null;
			_tor = u.pInteger(pos);
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' ') || (_alt_c == '#'))) {
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
			throw new ParseException("<jail:u_integer_u_spaced> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:u_string_u_spaced&gt; */
	public final Object pUStringUSpaced(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:u_string_u_spaced");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pUStringUSpaced(pos);
				pos.memoize(start, "jail:u_string_u_spaced", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:u_string_u_spaced", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:u_string_u_spaced&gt; */
	public final Object _pUStringUSpaced(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '"')) {
			Object _tor = null;
			_tor = u.pString(pos);
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' ') || (_alt_c == '#'))) {
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
			throw new ParseException("<jail:u_string_u_spaced> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:sy$41_u_spaced&gt; */
	public final Object pSy$41USpaced(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:sy$41_u_spaced");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pSy$41USpaced(pos);
				pos.memoize(start, "jail:sy$41_u_spaced", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:sy$41_u_spaced", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:sy$41_u_spaced&gt; */
	public final Object _pSy$41USpaced(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == ')')) {
			Object _tor = null;
			_tor = p$Char(pos, ')');
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' ') || (_alt_c == '#'))) {
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
			throw new ParseException("<jail:sy$41_u_spaced> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:sy$44_u_spaced&gt; */
	public final Object pSy$44USpaced(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:sy$44_u_spaced");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pSy$44USpaced(pos);
				pos.memoize(start, "jail:sy$44_u_spaced", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:sy$44_u_spaced", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:sy$44_u_spaced&gt; */
	public final Object _pSy$44USpaced(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == ',')) {
			Object _tor = null;
			_tor = p$Char(pos, ',');
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' ') || (_alt_c == '#'))) {
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
			throw new ParseException("<jail:sy$44_u_spaced> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:sy$59_u_spaced&gt; */
	public final Object pSy$59USpaced(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:sy$59_u_spaced");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pSy$59USpaced(pos);
				pos.memoize(start, "jail:sy$59_u_spaced", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:sy$59_u_spaced", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:sy$59_u_spaced&gt; */
	public final Object _pSy$59USpaced(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == ';')) {
			Object _tor = null;
			_tor = p$Char(pos, ';');
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' ') || (_alt_c == '#'))) {
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
			throw new ParseException("<jail:sy$59_u_spaced> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:sy$61_u_spaced&gt; */
	public final Object pSy$61USpaced(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:sy$61_u_spaced");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pSy$61USpaced(pos);
				pos.memoize(start, "jail:sy$61_u_spaced", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:sy$61_u_spaced", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:sy$61_u_spaced&gt; */
	public final Object _pSy$61USpaced(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '=')) {
			Object _tor = null;
			_tor = p$Char(pos, '=');
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' ') || (_alt_c == '#'))) {
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
			throw new ParseException("<jail:sy$61_u_spaced> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:sy$62_u_spaced&gt; */
	public final Object pSy$62USpaced(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:sy$62_u_spaced");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pSy$62USpaced(pos);
				pos.memoize(start, "jail:sy$62_u_spaced", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:sy$62_u_spaced", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:sy$62_u_spaced&gt; */
	public final Object _pSy$62USpaced(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '>')) {
			Object _tor = null;
			_tor = p$Char(pos, '>');
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' ') || (_alt_c == '#'))) {
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
			throw new ParseException("<jail:sy$62_u_spaced> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:sy$91_u_spaced&gt; */
	public final Object pSy$91USpaced(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:sy$91_u_spaced");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pSy$91USpaced(pos);
				pos.memoize(start, "jail:sy$91_u_spaced", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:sy$91_u_spaced", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:sy$91_u_spaced&gt; */
	public final Object _pSy$91USpaced(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '[')) {
			Object _tor = null;
			_tor = p$Char(pos, '[');
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' ') || (_alt_c == '#'))) {
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
			throw new ParseException("<jail:sy$91_u_spaced> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:sy$93_u_spaced&gt; */
	public final Object pSy$93USpaced(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:sy$93_u_spaced");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pSy$93USpaced(pos);
				pos.memoize(start, "jail:sy$93_u_spaced", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:sy$93_u_spaced", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:sy$93_u_spaced&gt; */
	public final Object _pSy$93USpaced(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == ']')) {
			Object _tor = null;
			_tor = p$Char(pos, ']');
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' ') || (_alt_c == '#'))) {
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
			throw new ParseException("<jail:sy$93_u_spaced> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:sy$123_u_spaced&gt; */
	public final Object pSy$123USpaced(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:sy$123_u_spaced");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pSy$123USpaced(pos);
				pos.memoize(start, "jail:sy$123_u_spaced", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:sy$123_u_spaced", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:sy$123_u_spaced&gt; */
	public final Object _pSy$123USpaced(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '{')) {
			Object _tor = null;
			_tor = p$Char(pos, '{');
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' ') || (_alt_c == '#'))) {
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
			throw new ParseException("<jail:sy$123_u_spaced> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:qualifiedref_1_1&gt; */
	public final Object pQualifiedref11(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:qualifiedref_1_1");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pQualifiedref11(pos);
				pos.memoize(start, "jail:qualifiedref_1_1", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:qualifiedref_1_1", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:qualifiedref_1_1&gt; */
	public final Object _pQualifiedref11(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '.')) {
			p$Char(pos, '.');
			Object _tor = null;
			_tor = u.pIdchars(pos);
			return _tor;
		} else {
			throw new ParseException("<jail:qualifiedref_1_1> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:jail_arg_u_list_1_1&gt; */
	public final Object pJailArgUList11(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:jail_arg_u_list_1_1");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pJailArgUList11(pos);
				pos.memoize(start, "jail:jail_arg_u_list_1_1", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:jail_arg_u_list_1_1", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:jail_arg_u_list_1_1&gt; */
	public final Object _pJailArgUList11(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' ') || (_alt_c == '"')
				|| (_alt_c == '#') || (_alt_c == '\'') || (_alt_c == '(') || (_alt_c == '0')
				|| (_alt_c >= '1' && _alt_c <= '9') || (_alt_c == '<') || (_alt_c >= 'A' && _alt_c <= 'Z')
				|| (_alt_c == '[') || (_alt_c == '^') || (_alt_c >= 'a' && _alt_c <= 'z') || (_alt_c == 'f')
				|| (_alt_c == 't') || (_alt_c == '{'))) {
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' ') || (_alt_c == '#'))) {
				try {
					pos.save();
					u.pSpacing(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			Object _tor = null;
			_tor = jail.pArg(pos);
			return _tor;
		} else {
			throw new ParseException("<jail:jail_arg_u_list_1_1> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:jail_command_u_list_1_1&gt; */
	public final Object pJailCommandUList11(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:jail_command_u_list_1_1");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pJailCommandUList11(pos);
				pos.memoize(start, "jail:jail_command_u_list_1_1", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:jail_command_u_list_1_1", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:jail_command_u_list_1_1&gt; */
	public final Object _pJailCommandUList11(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' ') || (_alt_c == '"')
				|| (_alt_c == '#') || (_alt_c == '\'') || (_alt_c == '(') || (_alt_c == '/') || (_alt_c == '0')
				|| (_alt_c >= '1' && _alt_c <= '9') || (_alt_c == '<') || (_alt_c >= 'A' && _alt_c <= 'Z')
				|| (_alt_c == '[') || (_alt_c == '^') || (_alt_c >= 'a' && _alt_c <= 'z') || (_alt_c == 'd')
				|| (_alt_c == 'f') || (_alt_c == 'n') || (_alt_c == 't') || (_alt_c == 'u') || (_alt_c == '{'))) {
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' ') || (_alt_c == '#'))) {
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
			throw new ParseException("<jail:jail_command_u_list_1_1> expected, " + pos.charAt() + " found.", pos
					.location());
		}
	}

	/** &lt;jail:jail_expression_u_commalist_1_1&gt; */
	public final Object pJailExpressionUCommalist11(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:jail_expression_u_commalist_1_1");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pJailExpressionUCommalist11(pos);
				pos.memoize(start, "jail:jail_expression_u_commalist_1_1", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:jail_expression_u_commalist_1_1", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:jail_expression_u_commalist_1_1&gt; */
	public final Object _pJailExpressionUCommalist11(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == ',')) {
			jail.pSy$44USpaced(pos);
			Object _tor = null;
			_tor = jail.pExpression(pos);
			return _tor;
		} else {
			throw new ParseException("<jail:jail_expression_u_commalist_1_1> expected, " + pos.charAt() + " found.",
					pos.location());
		}
	}

	/** &lt;jail:jail_optarg_u_list_1_1&gt; */
	public final Object pJailOptargUList11(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:jail_optarg_u_list_1_1");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pJailOptargUList11(pos);
				pos.memoize(start, "jail:jail_optarg_u_list_1_1", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:jail_optarg_u_list_1_1", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:jail_optarg_u_list_1_1&gt; */
	public final Object _pJailOptargUList11(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' ') || (_alt_c == '#') || (_alt_c == ':'))) {
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' ') || (_alt_c == '#'))) {
				try {
					pos.save();
					u.pSpacing(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			Object _tor = null;
			_tor = jail.pOptarg(pos);
			return _tor;
		} else {
			throw new ParseException("<jail:jail_optarg_u_list_1_1> expected, " + pos.charAt() + " found.", pos
					.location());
		}
	}

	/** &lt;jail:jail_optparam_u_list_1_1&gt; */
	public final Object pJailOptparamUList11(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:jail_optparam_u_list_1_1");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pJailOptparamUList11(pos);
				pos.memoize(start, "jail:jail_optparam_u_list_1_1", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:jail_optparam_u_list_1_1", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:jail_optparam_u_list_1_1&gt; */
	public final Object _pJailOptparamUList11(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' ') || (_alt_c == '#') || (_alt_c == ':'))) {
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' ') || (_alt_c == '#'))) {
				try {
					pos.save();
					u.pSpacing(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			Object _tor = null;
			_tor = jail.pOptparam(pos);
			return _tor;
		} else {
			throw new ParseException("<jail:jail_optparam_u_list_1_1> expected, " + pos.charAt() + " found.", pos
					.location());
		}
	}

	/** &lt;jail:jail_parameter_u_list_1_1&gt; */
	public final Object pJailParameterUList11(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("jail:jail_parameter_u_list_1_1");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pJailParameterUList11(pos);
				pos.memoize(start, "jail:jail_parameter_u_list_1_1", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "jail:jail_parameter_u_list_1_1", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (Object) prod;
			}
		}
	}

	/** &lt;jail:jail_parameter_u_list_1_1&gt; */
	public final Object _pJailParameterUList11(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' ') || (_alt_c == '#') || (_alt_c == '<'))) {
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' ') || (_alt_c == '#'))) {
				try {
					pos.save();
					u.pSpacing(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			Object _tor = null;
			_tor = jail.pParameter(pos);
			return _tor;
		} else {
			throw new ParseException("<jail:jail_parameter_u_list_1_1> expected, " + pos.charAt() + " found.", pos
					.location());
		}
	}

}
