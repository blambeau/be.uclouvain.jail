package be.uclouvain.jail.dialect.utils;

import net.chefbe.autogram.ast2.parsing.ParseException;
import net.chefbe.autogram.ast2.parsing.peg.PEGParser;
import net.chefbe.autogram.ast2.parsing.peg.Pos;

/** Parser for UTILS grammar. */
public class UTILSParser extends PEGParser {

	// ----------------------------------------------- External parsers
	/** External grammars. */
	private UTILSParser u = this;

	/** Creates a master parser. */
	public UTILSParser() {
	}

	/** Creates a child parser. */
	public UTILSParser(PEGParser parent) {
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
	/** &lt;u:d_string_literal&gt; */
	public final String pDStringLiteral(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '"')) {
			Object _b = null;
			_b = p$Char(pos, '"');
			String _i = null;
			{
				StringBuffer _sb_i = new StringBuffer();
				char _zz_first = '"';
				while (!pos.isEof()) {
					char f = pos.charAt();
					if (f == _zz_first && !pos.previousIs('\\')) {
						break;
					} else {
						pos.more();
						_sb_i.append(f);
					}
				}
				_i = _sb_i.toString();
			}
			Object _e = null;
			_e = p$Char(pos, '"');
			return buffer(_b, _i, _e);
		} else {
			throw new ParseException("<u:d_string_literal> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:s_string_literal&gt; */
	public final String pSStringLiteral(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '\'')) {
			Object _n = null;
			_n = p$Char(pos, '\'');
			String _i = null;
			{
				StringBuffer _sb_i = new StringBuffer();
				char _zz_first = '\'';
				while (!pos.isEof()) {
					char f = pos.charAt();
					if (f == _zz_first && !pos.previousIs('\\')) {
						break;
					} else {
						pos.more();
						_sb_i.append(f);
					}
				}
				_i = _sb_i.toString();
			}
			Object _e = null;
			_e = p$Char(pos, '\'');
			return buffer(_n, _i, _e);
		} else {
			throw new ParseException("<u:s_string_literal> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:string_literal&gt; */
	public final Object pStringLiteral(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		_alt_c = pos.charAt();
		if ((_alt_c == '"')) {
			try {
				pos.save();
				Object _tor = null;
				_tor = u.pDStringLiteral(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if ((_alt_c == '\'')) {
			try {
				pos.save();
				Object _tor = null;
				_tor = u.pSStringLiteral(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return (Object) pos.error("<u:string_literal> expected, " + pos.charAt() + " found.");
	}

	/** &lt;u:integer_literal&gt; */
	public final String pIntegerLiteral(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		_alt_c = pos.charAt();
		if ((_alt_c == '0')) {
			try {
				pos.save();
				int start = pos.offset();
				p$Char(pos, '0');
				pos.commit();
				return pos.diff(start);
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
				return pos.diff(start);
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return (String) pos.error("<u:integer_literal> expected, " + pos.charAt() + " found.");
	}

	/** &lt;u:double_literal&gt; */
	public final String pDoubleLiteral(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '0') || (_alt_c >= '1' && _alt_c <= '9'))) {
			int start = pos.offset();
			u.pIntegerLiteral(pos);
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
			return pos.diff(start);
		} else {
			throw new ParseException("<u:double_literal> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:boolean_literal&gt; */
	public final Object pBooleanLiteral(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		_alt_c = pos.charAt();
		if ((_alt_c == 't')) {
			try {
				pos.save();
				Object _tor = null;
				_tor = p$Exact(pos, "true");
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if ((_alt_c == 'f')) {
			try {
				pos.save();
				Object _tor = null;
				_tor = p$Exact(pos, "false");
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return (Object) pos.error("<u:boolean_literal> expected, " + pos.charAt() + " found.");
	}

	/** &lt;u:d_string&gt; */
	public final Object pDString(Pos pos) throws ParseException {
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
			throw new ParseException("<u:d_string> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:s_string&gt; */
	public final Object pSString(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '\'')) {
			p$Char(pos, '\'');
			String _tor = null;
			{
				StringBuffer _sb_tor = new StringBuffer();
				char _zz_first = '\'';
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
			p$Char(pos, '\'');
			return _tor;
		} else {
			throw new ParseException("<u:s_string> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:string&gt; */
	public final Object pString(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		_alt_c = pos.charAt();
		if ((_alt_c == '"')) {
			try {
				pos.save();
				Object _tor = null;
				_tor = u.pDString(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if ((_alt_c == '\'')) {
			try {
				pos.save();
				Object _tor = null;
				_tor = u.pSString(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return (Object) pos.error("<u:string> expected, " + pos.charAt() + " found.");
	}

	/** &lt;u:char&gt; */
	public final Character pChar(Pos pos) throws ParseException {
		Object _c = null;
		_c = u.p$Char(pos);
		return Character.valueOf((Character) _c);
	}

	/** &lt;u:int&gt; */
	public final Integer pInt(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '0') || (_alt_c >= '1' && _alt_c <= '9'))) {
			Object _i = null;
			_i = u.pIntegerLiteral(pos);
			return Integer.parseInt((String) _i);
		} else {
			throw new ParseException("<u:int> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:integer&gt; */
	public final Integer pInteger(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '0') || (_alt_c >= '1' && _alt_c <= '9'))) {
			Object _i = null;
			_i = u.pIntegerLiteral(pos);
			return Integer.valueOf((String) _i);
		} else {
			throw new ParseException("<u:integer> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:double&gt; */
	public final Double pDouble(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '0') || (_alt_c >= '1' && _alt_c <= '9'))) {
			Object _d = null;
			_d = u.pDoubleLiteral(pos);
			return Double.valueOf((String) _d);
		} else {
			throw new ParseException("<u:double> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:bool&gt; */
	public final Boolean pBool(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == 'f') || (_alt_c == 't'))) {
			Object _b = null;
			_b = u.pBooleanLiteral(pos);
			return Boolean.parseBoolean((String) _b);
		} else {
			throw new ParseException("<u:bool> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:boolean&gt; */
	public final Boolean pBoolean(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == 'f') || (_alt_c == 't'))) {
			Object _b = null;
			_b = u.pBooleanLiteral(pos);
			return Boolean.valueOf((String) _b);
		} else {
			throw new ParseException("<u:boolean> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:lowerCase_word&gt; */
	public final String pLowerCaseWord(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c >= 'a' && _alt_c <= 'z')) {
			int start = pos.offset();
			{
				int _zz_start = pos.offset();
				while (!pos.isEof()) {
					char zz = pos.charAt();
					if ((zz >= 'a' && zz <= 'z')) {
						pos.more();
					} else {
						break;
					}
				}
				if (_zz_start == pos.offset()) {
					throw new ParseException("<cpattern> expected.", pos.location());
				}
			}
			return pos.diff(start);
		} else {
			throw new ParseException("<u:lowerCase_word> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:upperCase_word&gt; */
	public final String pUpperCaseWord(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c >= 'A' && _alt_c <= 'Z')) {
			int start = pos.offset();
			{
				int _zz_start = pos.offset();
				while (!pos.isEof()) {
					char zz = pos.charAt();
					if ((zz >= 'A' && zz <= 'Z')) {
						pos.more();
					} else {
						break;
					}
				}
				if (_zz_start == pos.offset()) {
					throw new ParseException("<cpattern> expected.", pos.location());
				}
			}
			return pos.diff(start);
		} else {
			throw new ParseException("<u:upperCase_word> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:alpha_word&gt; */
	public final String pAlphaWord(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			int start = pos.offset();
			{
				int _zz_start = pos.offset();
				while (!pos.isEof()) {
					char zz = pos.charAt();
					if ((zz >= 'a' && zz <= 'z') || (zz >= 'A' && zz <= 'Z')) {
						pos.more();
					} else {
						break;
					}
				}
				if (_zz_start == pos.offset()) {
					throw new ParseException("<cpattern> expected.", pos.location());
				}
			}
			return pos.diff(start);
		} else {
			throw new ParseException("<u:alpha_word> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:alphaNum_word&gt; */
	public final String pAlphaNumWord(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c >= '0' && _alt_c <= '9') || (_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			int start = pos.offset();
			{
				int _zz_start = pos.offset();
				while (!pos.isEof()) {
					char zz = pos.charAt();
					if ((zz >= 'a' && zz <= 'z') || (zz >= 'A' && zz <= 'Z') || (zz >= '0' && zz <= '9')) {
						pos.more();
					} else {
						break;
					}
				}
				if (_zz_start == pos.offset()) {
					throw new ParseException("<cpattern> expected.", pos.location());
				}
			}
			return pos.diff(start);
		} else {
			throw new ParseException("<u:alphaNum_word> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:num_word&gt; */
	public final String pNumWord(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c >= '0' && _alt_c <= '9')) {
			int start = pos.offset();
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
			return pos.diff(start);
		} else {
			throw new ParseException("<u:num_word> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:space&gt; */
	public final Character pSpace(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == ' ')) {
			Object _c = null;
			_c = p$Char(pos, ' ');
			return Character.valueOf((Character) _c);
		} else {
			throw new ParseException("<u:space> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:inline_space&gt; */
	public final Object pInlineSpace(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		_alt_c = pos.charAt();
		if ((_alt_c == ' ')) {
			try {
				pos.save();
				Object _tor = null;
				_tor = u.pSpace(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if ((_alt_c == '\t')) {
			try {
				pos.save();
				Object _tor = null;
				_tor = u.pTab(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return (Object) pos.error("<u:inline_space> expected, " + pos.charAt() + " found.");
	}

	/** &lt;u:eol&gt; */
	public final String pEol(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\n') || (_alt_c == '\r'))) {
			int start = pos.offset();
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
			return pos.diff(start);
		} else {
			throw new ParseException("<u:eol> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:eof&gt; */
	public final Character pEof(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '\0')) {
			Object _c = null;
			_c = p$Char(pos, '\0');
			return Character.valueOf((Character) _c);
		} else {
			throw new ParseException("<u:eof> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:tab&gt; */
	public final Character pTab(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '\t')) {
			Object _c = null;
			_c = p$Char(pos, '\t');
			return Character.valueOf((Character) _c);
		} else {
			throw new ParseException("<u:tab> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:aSpace&gt; */
	public final Object pASpace(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		_alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == ' '))) {
			try {
				pos.save();
				Object _tor = null;
				_tor = u.pInlineSpace(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if (((_alt_c == '\n') || (_alt_c == '\r'))) {
			try {
				pos.save();
				Object _tor = null;
				_tor = u.pEol(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return (Object) pos.error("<u:aSpace> expected, " + pos.charAt() + " found.");
	}

	/** &lt;u:aSpace_string&gt; */
	public final String pASpaceString(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' '))) {
			int start = pos.offset();
			u.pASpace(pos);
			return pos.diff(start);
		} else {
			throw new ParseException("<u:aSpace_string> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:spaces&gt; */
	public final String pSpaces(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' '))) {
			int start = pos.offset();
			u.pASpaceString(pos);
			_alt_c = pos.charAt();
			while (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' '))) {
				try {
					pos.save();
					u.pASpaceString(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
					break;
				}
				_alt_c = pos.charAt();
			}
			return pos.diff(start);
		} else {
			throw new ParseException("<u:spaces> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:eol_spaces&gt; */
	public final String pEolSpaces(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' '))) {
			int start = pos.offset();
			_alt_c = pos.charAt();
			while (((_alt_c == '\t') || (_alt_c == ' '))) {
				try {
					pos.save();
					u.pInlineSpace(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
					break;
				}
				_alt_c = pos.charAt();
			}
			u.pEol(pos);
			return pos.diff(start);
		} else {
			throw new ParseException("<u:eol_spaces> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:comment&gt; */
	public final Object pComment(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		_alt_c = pos.charAt();
		if ((_alt_c == '/')) {
			try {
				pos.save();
				Object _tor = null;
				_tor = u.pEolComment(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if ((_alt_c == '/')) {
			try {
				pos.save();
				Object _tor = null;
				_tor = u.pJavaDocComment(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if ((_alt_c == '/')) {
			try {
				pos.save();
				Object _tor = null;
				_tor = u.pCppComment(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return (Object) pos.error("<u:comment> expected, " + pos.charAt() + " found.");
	}

	/** &lt;u:eol_comment&gt; */
	public final String pEolComment(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '/')) {
			int start = pos.offset();
			p$Exact(pos, "//");

			{
				int _zz_start = pos.offset();
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
				if (_zz_start == pos.offset()) {
					throw new ParseException("!<u:eol> expected, " + pos.charAt() + " found.", pos.location());
				}
			}
			u.pEol(pos);
			return pos.diff(start);
		} else {
			throw new ParseException("<u:eol_comment> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:sql_comment&gt; */
	public final String pSqlComment(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '\'')) {
			int start = pos.offset();
			p$Char(pos, '\'');

			{
				int _zz_start = pos.offset();
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
				if (_zz_start == pos.offset()) {
					throw new ParseException("!<u:eol> expected, " + pos.charAt() + " found.", pos.location());
				}
			}
			u.pEol(pos);
			return pos.diff(start);
		} else {
			throw new ParseException("<u:sql_comment> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:cpp_comment&gt; */
	public final String pCppComment(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '/')) {
			int start = pos.offset();
			p$Exact(pos, "/*");

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
			throw new ParseException("<u:cpp_comment> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:javaDoc_comment&gt; */
	public final String pJavaDocComment(Pos pos) throws ParseException {
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
			throw new ParseException("<u:javaDoc_comment> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:aSpacing&gt; */
	public final Object pASpacing(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		_alt_c = pos.charAt();
		if ((_alt_c == '/')) {
			try {
				pos.save();
				Object _tor = null;
				_tor = u.pComment(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' '))) {
			try {
				pos.save();
				Object _tor = null;
				_tor = u.pASpaceString(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return (Object) pos.error("<u:aSpacing> expected, " + pos.charAt() + " found.");
	}

	/** &lt;u:spacing&gt; */
	public final String pSpacing(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' ') || (_alt_c == '/'))) {
			int start = pos.offset();
			u.pASpacing(pos);
			_alt_c = pos.charAt();
			while (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' ') || (_alt_c == '/'))) {
				try {
					pos.save();
					u.pASpacing(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
					break;
				}
				_alt_c = pos.charAt();
			}
			return pos.diff(start);
		} else {
			throw new ParseException("<u:spacing> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:identifier&gt; */
	public final Object pIdentifier(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			Object _tor = null;
			_tor = u.pIdentifierChars(pos);
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' ') || (_alt_c == '/'))) {
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

	/** &lt;u:identifier_chars&gt; */
	public final String pIdentifierChars(Pos pos) throws ParseException {
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
			throw new ParseException("<u:identifier_chars> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:qidentifier&gt; */
	public final String pQidentifier(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			int start = pos.offset();
			u.pIdentifierChars(pos);
			_alt_c = pos.charAt();
			while ((_alt_c == '.')) {
				try {
					pos.save();
					u.pQidentifier11(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
					break;
				}
				_alt_c = pos.charAt();
			}
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' ') || (_alt_c == '/'))) {
				try {
					pos.save();
					u.pSpacing(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			return pos.trim(start);
		} else {
			throw new ParseException("<u:qidentifier> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;u:qidentifier_1_1&gt; */
	public final Object pQidentifier11(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '.')) {
			p$Char(pos, '.');
			Object _tor = null;
			_tor = u.pIdentifierChars(pos);
			return _tor;
		} else {
			throw new ParseException("<u:qidentifier_1_1> expected, " + pos.charAt() + " found.", pos.location());
		}
	}

}
