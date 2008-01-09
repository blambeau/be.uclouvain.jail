package be.uclouvain.jail.dialect.upv2;

import java.util.ArrayList;
import java.util.List;

import net.chefbe.autogram.ast2.parsing.ParseException;
import net.chefbe.autogram.ast2.parsing.active.ActiveParser;
import net.chefbe.autogram.ast2.parsing.peg.PEGParser;
import net.chefbe.autogram.ast2.parsing.peg.Pos;

/** Parser for UPV2 grammar. */
public class UPV2Parser extends ActiveParser {

	// ----------------------------------------------- External parsers
	/** External grammars. */
	private UPV2Parser upv2 = this;

	/** Creates a master parser. */
	public UPV2Parser() {
	}

	/** Creates a child parser. */
	public UPV2Parser(PEGParser parent) {
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
	/** &lt;upv2:graphdef&gt; */
	public final Object pGraphdef(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
				|| (_alt_c == ' ') || (_alt_c == '%') || (_alt_c == 'n'))) {
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
					|| (_alt_c == ' ') || (_alt_c == '%'))) {
				try {
					pos.save();
					upv2.pFreespace(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			Object _name = null;
			_name = upv2.pGraphname(pos);
			Object _nbstates = null;
			_nbstates = upv2.pNbstates(pos);
			Object _nbedges = null;
			_nbedges = upv2.pNbedges(pos);
			List<Object> _line = new ArrayList<Object>();
			_alt_c = pos.charAt();
			while (((_alt_c == '\t') || (_alt_c == ' ') || (_alt_c == '0')
					|| (_alt_c >= '1' && _alt_c <= '9') || (_alt_c == 's'))) {
				try {
					pos.save();
					_line.add(upv2.pLine(pos));
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
					break;
				}
				_alt_c = pos.charAt();
			}
			return load(pos, "upv2:graphdef", new String[] { "name",
					"nbstates", "nbedges", "line" }, new Object[] { _name,
					_nbstates, _nbedges, _line });
		} else {
			throw new ParseException("<upv2:graphdef> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;upv2:graphname&gt; */
	public final Object pGraphname(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == ' ') || (_alt_c == 'n'))) {
			upv2.pExNameUpv2Spaced(pos);
			Object _tor = null;
			_tor = upv2.pUpv2IdentifierUpv2Toeol(pos);
			return _tor;
		} else {
			throw new ParseException("<upv2:graphname> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;upv2:nbstates&gt; */
	public final Object pNbstates(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == ' ') || (_alt_c == 'n'))) {
			upv2.pExNumStatesUpv2Spaced(pos);
			Object _tor = null;
			_tor = upv2.pUpv2IntegerUpv2Toeol(pos);
			return _tor;
		} else {
			throw new ParseException("<upv2:nbstates> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;upv2:nbedges&gt; */
	public final Object pNbedges(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == ' ') || (_alt_c == 'n'))) {
			upv2.pExNumEdgesUpv2Spaced(pos);
			Object _tor = null;
			_tor = upv2.pUpv2IntegerUpv2Toeol(pos);
			return _tor;
		} else {
			throw new ParseException("<upv2:nbedges> expected, " + pos.charAt()
					+ " found.", pos.location());
		}
	}

	/** &lt;upv2:line&gt; */
	public final Object pLine(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		_alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == ' ') || (_alt_c == 's'))) {
			try {
				pos.save();
				Object _tor = null;
				_tor = upv2.pStateLine(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == ' ') || (_alt_c == '0') || (_alt_c >= '1' && _alt_c <= '9'))) {
			try {
				pos.save();
				Object _tor = null;
				_tor = upv2.pEdgeLine(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return (Object) pos.error("<upv2:line> expected, " + pos.charAt()
				+ " found.");
	}

	/** &lt;upv2:state_line&gt; */
	public final Object pStateLine(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == ' ') || (_alt_c == 's'))) {
			upv2.pExStateUpv2Spaced(pos);
			Object _id = null;
			_id = upv2.pUpv2IntegerUpv2Spaced(pos);
			Object _attr = null;
			_alt_c = pos.charAt();
			if (((_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
				try {
					pos.save();
					_attr = upv2.pUpv2AttributesUpv2Toeol(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			return load(pos, "upv2:state_line", new String[] { "id", "attr" },
					new Object[] { _id, _attr });
		} else {
			throw new ParseException("<upv2:state_line> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;upv2:edge_line&gt; */
	public final Object pEdgeLine(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == ' ') || (_alt_c == '0') || (_alt_c >= '1' && _alt_c <= '9'))) {
			Object _from = null;
			_from = upv2.pUpv2IntegerUpv2Spaced(pos);
			Object _to = null;
			_to = upv2.pUpv2IntegerUpv2Spaced(pos);
			Object _letter = null;
			_letter = upv2.pUpv2LiteralUpv2Spaced(pos);
			Object _attr = null;
			_alt_c = pos.charAt();
			if (((_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
				try {
					pos.save();
					_attr = upv2.pUpv2AttributesUpv2Toeol(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			return load(pos, "upv2:edge_line", new String[] { "from", "to",
					"letter", "attr" }, new Object[] { _from, _to, _letter,
					_attr });
		} else {
			throw new ParseException("<upv2:edge_line> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;upv2:attributes&gt; */
	public final Object pAttributes(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			upv2.pUpv2AttributeUpv2List(pos);
			return load(pos, "upv2:attributes", new String[] {},
					new Object[] {});
		} else {
			throw new ParseException("<upv2:attributes> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;upv2:attribute&gt; */
	public final Object pAttribute(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			Object _key = null;
			_key = upv2.pIdentifier(pos);
			upv2.pSy$61Upv2Spaced(pos);
			Object _value = null;
			_value = upv2.pUpv2LiteralUpv2Spaced(pos);
			return load(pos, "upv2:attribute", new String[] { "key", "value" },
					new Object[] { _key, _value });
		} else {
			throw new ParseException("<upv2:attribute> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;upv2:string&gt; */
	public final Object pString(Pos pos) throws ParseException {
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
			throw new ParseException("<upv2:string> expected, " + pos.charAt()
					+ " found.", pos.location());
		}
	}

	/** &lt;upv2:integer&gt; */
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
						throw new ParseException("<cpattern> expected.", pos
								.location());
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
		return (Integer) pos.error("<upv2:integer> expected, " + pos.charAt()
				+ " found.");
	}

	/** &lt;upv2:double&gt; */
	public final Double pDouble(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		_alt_c = pos.charAt();
		if (((_alt_c == '0') || (_alt_c >= '1' && _alt_c <= '9'))) {
			try {
				pos.save();
				int start = pos.offset();
				upv2.pInteger(pos);
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
						throw new ParseException("<cpattern> expected.", pos
								.location());
					}
				}
				pos.commit();
				return Double.valueOf(pos.diff(start));
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if (((_alt_c == '0') || (_alt_c >= '1' && _alt_c <= '9'))) {
			try {
				pos.save();
				int start = pos.offset();
				upv2.pInteger(pos);
				pos.commit();
				return Double.valueOf(pos.diff(start));
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return (Double) pos.error("<upv2:double> expected, " + pos.charAt()
				+ " found.");
	}

	/** &lt;upv2:boolean&gt; */
	public final Boolean pBoolean(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		_alt_c = pos.charAt();
		if ((_alt_c == 't')) {
			try {
				pos.save();
				Object _b = null;
				_b = p$Exact(pos, "true");
				pos.commit();
				return Boolean.valueOf((String) _b);
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
				return Boolean.valueOf((String) _b);
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return (Boolean) pos.error("<upv2:boolean> expected, " + pos.charAt()
				+ " found.");
	}

	/** &lt;upv2:literal&gt; */
	public final Object pLiteral(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		_alt_c = pos.charAt();
		if ((_alt_c == '"')) {
			try {
				pos.save();
				Object _tor = null;
				_tor = upv2.pString(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if (((_alt_c == '0') || (_alt_c >= '1' && _alt_c <= '9'))) {
			try {
				pos.save();
				Object _tor = null;
				_tor = upv2.pInteger(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if (((_alt_c == '0') || (_alt_c >= '1' && _alt_c <= '9'))) {
			try {
				pos.save();
				Object _tor = null;
				_tor = upv2.pDouble(pos);
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
				_tor = upv2.pBoolean(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return (Object) pos.error("<upv2:literal> expected, " + pos.charAt()
				+ " found.");
	}

	/** &lt;upv2:space&gt; */
	public final Object pSpace(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == ' '))) {

			{
				char zz = pos.charAt();
				if ((zz == ' ') || (zz == '\t')) {
					pos.more();
				} else {
					throw new ParseException("<cpattern> expected.", pos
							.location());
				}
			}
			return null;
		} else {
			throw new ParseException("<upv2:space> expected, " + pos.charAt()
					+ " found.", pos.location());
		}
	}

	/** &lt;upv2:spaces&gt; */
	public final Object pSpaces(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == ' '))) {

			{
				int _zz_start = pos.offset();
				while (!pos.isEof()) {
					char zz = pos.charAt();
					if ((zz == ' ') || (zz == '\t')) {
						pos.more();
					} else {
						break;
					}
				}
				if (_zz_start == pos.offset()) {
					throw new ParseException("<cpattern> expected.", pos
							.location());
				}
			}
			return null;
		} else {
			throw new ParseException("<upv2:spaces> expected, " + pos.charAt()
					+ " found.", pos.location());
		}
	}

	/** &lt;upv2:eol&gt; */
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
			throw new ParseException("<upv2:eol> expected, " + pos.charAt()
					+ " found.", pos.location());
		}
	}

	/** &lt;upv2:eof&gt; */
	public final Object pEof(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '\0')) {
			p$Char(pos, '\0');
			return null;
		} else {
			throw new ParseException("<upv2:eof> expected, " + pos.charAt()
					+ " found.", pos.location());
		}
	}

	/** &lt;upv2:emptyline&gt; */
	public final Object pEmptyline(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' '))) {
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == ' '))) {
				try {
					pos.save();
					upv2.pSpaces(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			upv2.pEol(pos);
			return null;
		} else {
			throw new ParseException("<upv2:emptyline> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;upv2:comment&gt; */
	public final Object pComment(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == ' ') || (_alt_c == '%'))) {
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == ' '))) {
				try {
					pos.save();
					upv2.pSpaces(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			p$Char(pos, '%');

			{
				int _zz_start = pos.offset();
				while (!pos.isEof()) {
					// Optimization AG4
					_alt_c = pos.charAt();
					if (((_alt_c == '\n') || (_alt_c == '\r'))) {
						try {
							pos.save();
							upv2.pEol(pos);
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
					throw new ParseException("!<upv2:eol> expected, "
							+ pos.charAt() + " found.", pos.location());
				}
			}
			upv2.pEol(pos);
			return null;
		} else {
			throw new ParseException("<upv2:comment> expected, " + pos.charAt()
					+ " found.", pos.location());
		}
	}

	/** &lt;upv2:freeline&gt; */
	public final Object pFreeline(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		_alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r') || (_alt_c == ' '))) {
			try {
				pos.save();
				upv2.pEmptyline(pos);
				pos.commit();
				return null;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == ' ') || (_alt_c == '%'))) {
			try {
				pos.save();
				upv2.pComment(pos);
				pos.commit();
				return null;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return (Object) pos.error("<upv2:freeline> expected, " + pos.charAt()
				+ " found.");
	}

	/** &lt;upv2:freespace&gt; */
	public final Object pFreespace(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
				|| (_alt_c == ' ') || (_alt_c == '%'))) {
			upv2.pFreeline(pos);
			_alt_c = pos.charAt();
			while (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
					|| (_alt_c == ' ') || (_alt_c == '%'))) {
				try {
					pos.save();
					upv2.pFreeline(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
					break;
				}
				_alt_c = pos.charAt();
			}
			return null;
		} else {
			throw new ParseException("<upv2:freespace> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;upv2:idchars&gt; */
	public final String pIdchars(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			int start = pos.offset();
			{
				char zz = pos.charAt();
				if ((zz >= 'a' && zz <= 'z') || (zz >= 'A' && zz <= 'Z')) {
					pos.more();
				} else {
					throw new ParseException("<cpattern> expected.", pos
							.location());
				}
			}

			{
				while (!pos.isEof()) {
					char zz = pos.charAt();
					if ((zz >= 'a' && zz <= 'z') || (zz >= 'A' && zz <= 'Z')
							|| (zz >= '0' && zz <= '9') || (zz == '_')
							|| (zz == '$') || (zz == '#')) {
						pos.more();
					} else {
						break;
					}
				}
			}
			return pos.diff(start);
		} else {
			throw new ParseException("<upv2:idchars> expected, " + pos.charAt()
					+ " found.", pos.location());
		}
	}

	/** &lt;upv2:identifier&gt; */
	public final String pIdentifier(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			Object _id = null;
			_id = upv2.pIdchars(pos);
			return (String) _id;
		} else {
			throw new ParseException("<upv2:identifier> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;upv2:exName_upv2_spaced&gt; */
	public final Object pExNameUpv2Spaced(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == ' ') || (_alt_c == 'n'))) {
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == ' '))) {
				try {
					pos.save();
					upv2.pSpaces(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			Object _tor = null;
			_tor = p$Exact(pos, "Name");
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == ' '))) {
				try {
					pos.save();
					upv2.pSpaces(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			return _tor;
		} else {
			throw new ParseException("<upv2:exName_upv2_spaced> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;upv2:exNumEdges_upv2_spaced&gt; */
	public final Object pExNumEdgesUpv2Spaced(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == ' ') || (_alt_c == 'n'))) {
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == ' '))) {
				try {
					pos.save();
					upv2.pSpaces(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			Object _tor = null;
			_tor = p$Exact(pos, "NumEdges");
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == ' '))) {
				try {
					pos.save();
					upv2.pSpaces(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			return _tor;
		} else {
			throw new ParseException("<upv2:exNumEdges_upv2_spaced> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;upv2:exNumStates_upv2_spaced&gt; */
	public final Object pExNumStatesUpv2Spaced(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == ' ') || (_alt_c == 'n'))) {
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == ' '))) {
				try {
					pos.save();
					upv2.pSpaces(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			Object _tor = null;
			_tor = p$Exact(pos, "NumStates");
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == ' '))) {
				try {
					pos.save();
					upv2.pSpaces(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			return _tor;
		} else {
			throw new ParseException(
					"<upv2:exNumStates_upv2_spaced> expected, " + pos.charAt()
							+ " found.", pos.location());
		}
	}

	/** &lt;upv2:exState_upv2_spaced&gt; */
	public final Object pExStateUpv2Spaced(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == ' ') || (_alt_c == 's'))) {
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == ' '))) {
				try {
					pos.save();
					upv2.pSpaces(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			Object _tor = null;
			_tor = p$Exact(pos, "State");
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == ' '))) {
				try {
					pos.save();
					upv2.pSpaces(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			return _tor;
		} else {
			throw new ParseException("<upv2:exState_upv2_spaced> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;upv2:upv2_attribute_upv2_list&gt; */
	public final List<?> pUpv2AttributeUpv2List(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			Object _f = null;
			_f = upv2.pAttribute(pos);
			List<Object> _n = new ArrayList<Object>();
			_alt_c = pos.charAt();
			while (((_alt_c == '\t') || (_alt_c == ' ')
					|| (_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
				try {
					pos.save();
					_n.add(upv2.pUpv2AttributeUpv2List11(pos));
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
					"<upv2:upv2_attribute_upv2_list> expected, " + pos.charAt()
							+ " found.", pos.location());
		}
	}

	/** &lt;upv2:upv2_attributes_upv2_toeol&gt; */
	public final Object pUpv2AttributesUpv2Toeol(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			Object _tor = null;
			_tor = upv2.pAttributes(pos);
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == ' '))) {
				try {
					pos.save();
					upv2.pSpaces(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			upv2.pEol(pos);
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
					|| (_alt_c == ' ') || (_alt_c == '%'))) {
				try {
					pos.save();
					upv2.pFreespace(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			return _tor;
		} else {
			throw new ParseException(
					"<upv2:upv2_attributes_upv2_toeol> expected, "
							+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;upv2:upv2_identifier_upv2_toeol&gt; */
	public final Object pUpv2IdentifierUpv2Toeol(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			Object _tor = null;
			_tor = upv2.pIdentifier(pos);
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == ' '))) {
				try {
					pos.save();
					upv2.pSpaces(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			upv2.pEol(pos);
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
					|| (_alt_c == ' ') || (_alt_c == '%'))) {
				try {
					pos.save();
					upv2.pFreespace(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			return _tor;
		} else {
			throw new ParseException(
					"<upv2:upv2_identifier_upv2_toeol> expected, "
							+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;upv2:upv2_integer_upv2_spaced&gt; */
	public final Object pUpv2IntegerUpv2Spaced(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == ' ') || (_alt_c == '0') || (_alt_c >= '1' && _alt_c <= '9'))) {
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == ' '))) {
				try {
					pos.save();
					upv2.pSpaces(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			Object _tor = null;
			_tor = upv2.pInteger(pos);
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == ' '))) {
				try {
					pos.save();
					upv2.pSpaces(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			return _tor;
		} else {
			throw new ParseException(
					"<upv2:upv2_integer_upv2_spaced> expected, " + pos.charAt()
							+ " found.", pos.location());
		}
	}

	/** &lt;upv2:upv2_integer_upv2_toeol&gt; */
	public final Object pUpv2IntegerUpv2Toeol(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '0') || (_alt_c >= '1' && _alt_c <= '9'))) {
			Object _tor = null;
			_tor = upv2.pInteger(pos);
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == ' '))) {
				try {
					pos.save();
					upv2.pSpaces(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			upv2.pEol(pos);
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
					|| (_alt_c == ' ') || (_alt_c == '%'))) {
				try {
					pos.save();
					upv2.pFreespace(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			return _tor;
		} else {
			throw new ParseException(
					"<upv2:upv2_integer_upv2_toeol> expected, " + pos.charAt()
							+ " found.", pos.location());
		}
	}

	/** &lt;upv2:upv2_literal_upv2_spaced&gt; */
	public final Object pUpv2LiteralUpv2Spaced(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == ' ') || (_alt_c == '"')
				|| (_alt_c == '0') || (_alt_c >= '1' && _alt_c <= '9')
				|| (_alt_c == 'f') || (_alt_c == 't'))) {
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == ' '))) {
				try {
					pos.save();
					upv2.pSpaces(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			Object _tor = null;
			_tor = upv2.pLiteral(pos);
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == ' '))) {
				try {
					pos.save();
					upv2.pSpaces(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			return _tor;
		} else {
			throw new ParseException(
					"<upv2:upv2_literal_upv2_spaced> expected, " + pos.charAt()
							+ " found.", pos.location());
		}
	}

	/** &lt;upv2:sy$61_upv2_spaced&gt; */
	public final Object pSy$61Upv2Spaced(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == ' ') || (_alt_c == '='))) {
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == ' '))) {
				try {
					pos.save();
					upv2.pSpaces(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			Object _tor = null;
			_tor = p$Char(pos, '=');
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == ' '))) {
				try {
					pos.save();
					upv2.pSpaces(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			return _tor;
		} else {
			throw new ParseException("<upv2:sy$61_upv2_spaced> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;upv2:upv2_attribute_upv2_list_1_1&gt; */
	public final Object pUpv2AttributeUpv2List11(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == ' ')
				|| (_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == ' '))) {
				try {
					pos.save();
					upv2.pSpaces(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			Object _tor = null;
			_tor = upv2.pAttribute(pos);
			return _tor;
		} else {
			throw new ParseException(
					"<upv2:upv2_attribute_upv2_list_1_1> expected, "
							+ pos.charAt() + " found.", pos.location());
		}
	}

}
