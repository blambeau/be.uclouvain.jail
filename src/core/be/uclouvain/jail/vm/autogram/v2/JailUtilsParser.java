package be.uclouvain.jail.vm.autogram.v2;

import net.chefbe.autogram.ast2.parsing.ParseException;
import net.chefbe.autogram.ast2.parsing.peg.Memoization;
import net.chefbe.autogram.ast2.parsing.peg.PEGParser;
import net.chefbe.autogram.ast2.parsing.peg.Pos;

/** Parser for JailUtils grammar. */
public class JailUtilsParser extends PEGParser {

	// ----------------------------------------------- External parsers
	/** External grammars. */
	private JailUtilsParser u = this;

	/** Creates a master parser. */
	public JailUtilsParser() {
	}

	/** Creates a child parser. */
	public JailUtilsParser(PEGParser parent) {
		super(parent);
	}

	/** Returns the parser bound to a prefix. */
	@Override
	public PEGParser getParser(String prefix) {
		if (parent != null) {
			return parent.getParser(prefix);
		} else {
			return this;
		}
	}

	// ----------------------------------------------- Rules
	/** &lt;u:boolean&gt; */
	public final Boolean pBoolean(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		_alt_c = pos.charAt();
		if ((_alt_c == 't')) {
			try {
				pos.save();
				Object _b = null;
				_b = p$Exact(pos, "true");
				pos.commit();
				return Boolean.parseBoolean((String) _b);
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if ((_alt_c == 'f')) {
			try {
				pos.save();
				Object _b = null;
				_b = p$Exact(pos, "false");
				pos.commit();
				return Boolean.parseBoolean((String) _b);
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return (Boolean) pos.error("<u:boolean> expected, " + pos.charAt() + " found.");
	}

	/** &lt;u:char&gt; */
	public final Character pChar(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '\'')) {
			p$Char(pos, '\'');
			Object _c = null;
			_c = u.p$Char(pos);
			p$Char(pos, '\'');
			return Character.valueOf((Character) _c);
		} else {
			throw new ParseException("<u:char> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:string&gt; */
	public final Object pString(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("u:string");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pString(pos);
				pos.memoize(start, "u:string", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "u:string", ex);
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

	/** &lt;u:string&gt; */
	public final Object _pString(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '"')) {
			p$Char(pos, '"');
			String _tor = null;
			{
				StringBuffer _sb_tor = new StringBuffer();
				char _zz_first = '"';
				while (!pos.isEof()) {
					char f = pos.charAt();
					if (f == _zz_first && !pos.previousIs('\\')) {
						break;
					} else {
						pos.more();
						_sb_tor.append(f);
					}
				}
				_tor = _sb_tor.toString();
			}
			p$Char(pos, '"');
			return _tor;
		} else {
			throw new ParseException("<u:string> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:integer&gt; */
	public final Integer pInteger(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		_alt_c = pos.charAt();
		if ((_alt_c == '0')) {
			try {
				pos.save();
				int start = pos.offset();
				p$Char(pos, '0');
				pos.commit();
				return Integer.valueOf(pos.diff(start));
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if ((_alt_c >= '1' && _alt_c <= '9')) {
			try {
				pos.save();
				int start = pos.offset();
				{
					char zz = pos.charAt();
					if ((zz >= '1' && zz <= '9')) {
						pos.more();
					} else {
						throw new ParseException("<cpattern> expected.", pos.location());
					}
				}

				{
					while (!pos.isEof()) {
						char zz = pos.charAt();
						if ((zz >= '0' && zz <= '9')) {
							pos.more();
						} else {
							break;
						}
					}
				}
				pos.commit();
				return Integer.valueOf(pos.diff(start));
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return (Integer) pos.error("<u:integer> expected, " + pos.charAt() + " found.");
	}

	/** &lt;u:float&gt; */
	public final Float pFloat(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		_alt_c = pos.charAt();
		if (((_alt_c == '0') || (_alt_c >= '1' && _alt_c <= '9'))) {
			try {
				pos.save();
				int start = pos.offset();
				u.pInteger(pos);
				p$Char(pos, '.');

				{
					int _zz_start = pos.offset();
					while (!pos.isEof()) {
						char zz = pos.charAt();
						if ((zz >= '0' && zz <= '9')) {
							pos.more();
						} else {
							break;
						}
					}
					if (_zz_start == pos.offset()) {
						throw new ParseException("<cpattern> expected.", pos.location());
					}
				}
				pos.commit();
				return Float.valueOf(pos.diff(start));
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if (((_alt_c == '0') || (_alt_c >= '1' && _alt_c <= '9'))) {
			try {
				pos.save();
				int start = pos.offset();
				u.pInteger(pos);
				pos.commit();
				return Float.valueOf(pos.diff(start));
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return (Float) pos.error("<u:float> expected, " + pos.charAt() + " found.");
	}

	/** &lt;u:space&gt; */
	public final Object pSpace(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == ' ')) {
			p$Char(pos, ' ');
			return null;
		} else {
			throw new ParseException("<u:space> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:tab&gt; */
	public final Object pTab(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '\t')) {
			p$Char(pos, '\t');
			return null;
		} else {
			throw new ParseException("<u:tab> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:eol&gt; */
	public final Object pEol(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\n') || (_alt_c == '\r'))) {
			_alt_c = pos.charAt();
			if ((_alt_c == '\r')) {
				try {
					pos.save();
					p$Char(pos, '\r');
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			p$Char(pos, '\n');
			return null;
		} else {
			throw new ParseException("<u:eol> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:eof&gt; */
	public final Object pEof(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '\0')) {
			p$Char(pos, '\0');
			return null;
		} else {
			throw new ParseException("<u:eof> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:aspace&gt; */
	public final Object pAspace(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' '))) {

			{
				char zz = pos.charAt();
				if ((zz == ' ') || (zz == '\t') || (zz == '\r') || (zz == '\n')) {
					pos.more();
				} else {
					throw new ParseException("<cpattern> expected.", pos.location());
				}
			}
			return null;
		} else {
			throw new ParseException("<u:aspace> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:spaces&gt; */
	public final Object pSpaces(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' '))) {

			{
				int _zz_start = pos.offset();
				while (!pos.isEof()) {
					char zz = pos.charAt();
					if ((zz == ' ') || (zz == '\t') || (zz == '\r') || (zz == '\n')) {
						pos.more();
					} else {
						break;
					}
				}
				if (_zz_start == pos.offset()) {
					throw new ParseException("<cpattern> expected.", pos.location());
				}
			}
			return null;
		} else {
			throw new ParseException("<u:spaces> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:inlinespace&gt; */
	public final Object pInlinespace(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == ' '))) {

			{
				char zz = pos.charAt();
				if ((zz == ' ') || (zz == '\t')) {
					pos.more();
				} else {
					throw new ParseException("<cpattern> expected.", pos.location());
				}
			}
			return null;
		} else {
			throw new ParseException("<u:inlinespace> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:eolspaces&gt; */
	public final Object pEolspaces(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' '))) {

			{
				while (!pos.isEof()) {
					char zz = pos.charAt();
					if ((zz == ' ') || (zz == '\t')) {
						pos.more();
					} else {
						break;
					}
				}
			}
			u.pEol(pos);
			return null;
		} else {
			throw new ParseException("<u:eolspaces> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:comment&gt; */
	public final Object pComment(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '#')) {
			p$Char(pos, '#');

			{
				while (!pos.isEof()) {
					// Optimization AG4
					_alt_c = pos.charAt();
					if (((_alt_c == '\n') || (_alt_c == '\r'))) {
						try {
							pos.save();
							u.pEol(pos);
							pos.rollback();
							break;
						} catch (ParseException ex) {
							pos.rollback();
							pos.more();
						}
					} else {
						pos.more();
					}
				}
			}
			u.pEol(pos);
			return null;
		} else {
			throw new ParseException("<u:comment> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:aspacing&gt; */
	public final Object pAspacing(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		_alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' '))) {
			try {
				pos.save();
				u.pSpaces(pos);
				pos.commit();
				return null;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if ((_alt_c == '#')) {
			try {
				pos.save();
				u.pComment(pos);
				pos.commit();
				return null;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return (Object) pos.error("<u:aspacing> expected, " + pos.charAt() + " found.");
	}

	/** &lt;u:spacing&gt; */
	public final Object pSpacing(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' ') || (_alt_c == '#'))) {
			_alt_c = pos.charAt();
			while (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' ') || (_alt_c == '#'))) {
				try {
					pos.save();
					u.pAspacing(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
					break;
				}
				_alt_c = pos.charAt();
			}
			return null;
		} else {
			throw new ParseException("<u:spacing> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:ajaildoc&gt; */
	public final String pAjaildoc(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("u:ajaildoc");
		if (memo == null) {
			int start = pos.offset();
			try {
				String production = _pAjaildoc(pos);
				pos.memoize(start, "u:ajaildoc", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "u:ajaildoc", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (String) prod;
			}
		}
	}

	/** &lt;u:ajaildoc&gt; */
	public final String _pAjaildoc(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '/')) {
			int start = pos.offset();
			p$Exact(pos, "/**");

			{
				char _zz_first = "*/".charAt(0);
				int _zz_start = pos.offset();
				while (!pos.isEof()) {
					char f = pos.charAt();
					if (f == _zz_first && try$Exact(pos, "*/")) {
						break;
					} else {
						pos.more();
					}
				}
				if (_zz_start == pos.offset()) {
					throw new ParseException("!*/ expected, " + pos.charAt() + " found.", pos.location());
				}
			}
			p$Exact(pos, "*/");
			return pos.diff(start);
		} else {
			throw new ParseException("<u:ajaildoc> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:jaildoc&gt; */
	public final Object pJaildoc(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("u:jaildoc");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pJaildoc(pos);
				pos.memoize(start, "u:jaildoc", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "u:jaildoc", ex);
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

	/** &lt;u:jaildoc&gt; */
	public final Object _pJaildoc(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '/')) {
			Object _tor = null;
			_tor = u.pAjaildoc(pos);
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
			throw new ParseException("<u:jaildoc> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:idchars&gt; */
	public final String pIdchars(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("u:idchars");
		if (memo == null) {
			int start = pos.offset();
			try {
				String production = _pIdchars(pos);
				pos.memoize(start, "u:idchars", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "u:idchars", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (String) prod;
			}
		}
	}

	/** &lt;u:idchars&gt; */
	public final String _pIdchars(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			int start = pos.offset();
			{
				char zz = pos.charAt();
				if ((zz >= 'a' && zz <= 'z') || (zz >= 'A' && zz <= 'Z')) {
					pos.more();
				} else {
					throw new ParseException("<cpattern> expected.", pos.location());
				}
			}

			{
				while (!pos.isEof()) {
					char zz = pos.charAt();
					if ((zz >= 'a' && zz <= 'z') || (zz >= 'A' && zz <= 'Z') || (zz >= '0' && zz <= '9') || (zz == '_')
							|| (zz == '$') || (zz == '#')) {
						pos.more();
					} else {
						break;
					}
				}
			}
			return pos.diff(start);
		} else {
			throw new ParseException("<u:idchars> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:identifier&gt; */
	public final Object pIdentifier(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("u:identifier");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pIdentifier(pos);
				pos.memoize(start, "u:identifier", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "u:identifier", ex);
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

	/** &lt;u:identifier&gt; */
	public final Object _pIdentifier(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			Object _tor = null;
			_tor = u.pIdchars(pos);
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
			throw new ParseException("<u:identifier> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:qidchars&gt; */
	public final String pQidchars(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("u:qidchars");
		if (memo == null) {
			int start = pos.offset();
			try {
				String production = _pQidchars(pos);
				pos.memoize(start, "u:qidchars", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "u:qidchars", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (String) prod;
			}
		}
	}

	/** &lt;u:qidchars&gt; */
	public final String _pQidchars(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			int start = pos.offset();
			u.pIdchars(pos);
			p$Char(pos, ':');
			u.pIdchars(pos);
			return pos.diff(start);
		} else {
			throw new ParseException("<u:qidchars> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:qidentifier&gt; */
	public final Object pQidentifier(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("u:qidentifier");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pQidentifier(pos);
				pos.memoize(start, "u:qidentifier", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "u:qidentifier", ex);
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

	/** &lt;u:qidentifier&gt; */
	public final Object _pQidentifier(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			Object _tor = null;
			_tor = u.pQidchars(pos);
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
			throw new ParseException("<u:qidentifier> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:dotidchars&gt; */
	public final String pDotidchars(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("u:dotidchars");
		if (memo == null) {
			int start = pos.offset();
			try {
				String production = _pDotidchars(pos);
				pos.memoize(start, "u:dotidchars", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "u:dotidchars", ex);
				throw ex;
			}
		} else {
			Object prod = memo.getProduction();
			if (prod instanceof ParseException) {
				throw (ParseException) prod;
			} else {
				pos.jump(memo);
				return (String) prod;
			}
		}
	}

	/** &lt;u:dotidchars&gt; */
	public final String _pDotidchars(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			int start = pos.offset();
			u.pIdchars(pos);
			_alt_c = pos.charAt();
			while ((_alt_c == '.')) {
				try {
					pos.save();
					u.pDotidchars11(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
					break;
				}
				_alt_c = pos.charAt();
			}
			return pos.diff(start);
		} else {
			throw new ParseException("<u:dotidchars> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:dotidentifier&gt; */
	public final Object pDotidentifier(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("u:dotidentifier");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pDotidentifier(pos);
				pos.memoize(start, "u:dotidentifier", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "u:dotidentifier", ex);
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

	/** &lt;u:dotidentifier&gt; */
	public final Object _pDotidentifier(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			Object _tor = null;
			_tor = u.pDotidchars(pos);
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
			throw new ParseException("<u:dotidentifier> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:dotidchars_1_1&gt; */
	public final Object pDotidchars11(Pos pos) throws ParseException {
		Memoization memo = pos.getMemoization("u:dotidchars_1_1");
		if (memo == null) {
			int start = pos.offset();
			try {
				Object production = _pDotidchars11(pos);
				pos.memoize(start, "u:dotidchars_1_1", production);
				return production;
			} catch (ParseException ex) {
				pos.memoize(start, "u:dotidchars_1_1", ex);
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

	/** &lt;u:dotidchars_1_1&gt; */
	public final Object _pDotidchars11(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '.')) {
			p$Char(pos, '.');
			Object _tor = null;
			_tor = u.pIdchars(pos);
			return _tor;
		} else {
			throw new ParseException("<u:dotidchars_1_1> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

}
