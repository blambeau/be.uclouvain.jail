package be.uclouvain.jail.dialect.seqp;

import java.util.ArrayList;
import java.util.List;

import net.chefbe.autogram.ast2.parsing.ParseException;
import net.chefbe.autogram.ast2.parsing.active.ActiveParser;
import net.chefbe.autogram.ast2.parsing.peg.PEGParser;
import net.chefbe.autogram.ast2.parsing.peg.Pos;
import be.uclouvain.jail.dialect.utils.UTILSParser;

/** Parser for SEQP grammar. */
public class SEQPParser extends ActiveParser {

	// ----------------------------------------------- External parsers
	/** External grammars. */
	private SEQPParser seqp = this;

	private UTILSParser u = new UTILSParser(this);

	/** Creates a master parser. */
	public SEQPParser() {
	}

	/** Creates a child parser. */
	public SEQPParser(PEGParser parent) {
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
	/** &lt;seqp:testunit&gt; */
	public final Object pTestunit(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
				|| (_alt_c == ' ') || (_alt_c == '/') || (_alt_c >= 'A' && _alt_c <= 'Z'))) {
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
			Object _def = null;
			_def = seqp.pSeqpSeqpDefUList(pos);
			u.pEof(pos);
			return load(pos, "seqp:testunit", new String[] { "def" },
					new Object[] { _def });
		} else {
			throw new ParseException("<seqp:testunit> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;seqp:seqp_def&gt; */
	public final Object pSeqpDef(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c >= 'A' && _alt_c <= 'Z')) {
			Object _state = null;
			_state = seqp.pSeqpStatedefUCommalist(pos);
			seqp.pSy$46USymbol(pos);
			return load(pos, "seqp:seqp_def", new String[] { "state" },
					new Object[] { _state });
		} else {
			throw new ParseException("<seqp:seqp_def> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;seqp:statedef&gt; */
	public final Object pStatedef(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		_alt_c = pos.charAt();
		if ((_alt_c >= 'A' && _alt_c <= 'Z')) {
			try {
				pos.save();
				Object _label = null;
				_label = seqp.pStatename(pos);
				Object _attr = null;
				_alt_c = pos.charAt();
				if ((_alt_c == '[')) {
					try {
						pos.save();
						_attr = seqp.pAttributes(pos);
						pos.commit();
					} catch (ParseException ex) {
						pos.rollback();
					}
				}
				seqp.pSy$61USymbol(pos);
				Object _def = null;
				_def = seqp.pSeqpPathUBarlist(pos);
				pos.commit();
				return load(pos, "seqp:statedef", new String[] { "label",
						"attr", "def" }, new Object[] { _label, _attr, _def });
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if ((_alt_c >= 'A' && _alt_c <= 'Z')) {
			try {
				pos.save();
				Object _label = null;
				_label = seqp.pStatename(pos);
				Object _attr = null;
				_alt_c = pos.charAt();
				if ((_alt_c == '[')) {
					try {
						pos.save();
						_attr = seqp.pAttributes(pos);
						pos.commit();
					} catch (ParseException ex) {
						pos.rollback();
					}
				}
				pos.commit();
				return load(pos, "seqp:statedef", new String[] { "label",
						"attr" }, new Object[] { _label, _attr });
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return (Object) pos.error("<seqp:statedef> expected, " + pos.charAt()
				+ " found.");
	}

	/** &lt;seqp:attributes&gt; */
	public final Object pAttributes(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '[')) {
			seqp.pSy$91USymbol(pos);
			Object _attr = null;
			_attr = seqp.pSeqpAttrdefUList(pos);
			seqp.pSy$93USymbol(pos);
			return load(pos, "seqp:attributes", new String[] { "attr" },
					new Object[] { _attr });
		} else {
			throw new ParseException("<seqp:attributes> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;seqp:attrdef&gt; */
	public final Object pAttrdef(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '@')) {
			p$Char(pos, '@');
			Object _name = null;
			_name = seqp.pAttrname(pos);
			seqp.pSy$61USymbol(pos);
			Object _value = null;
			_value = seqp.pLiteral(pos);
			return load(pos, "seqp:attrdef", new String[] { "name", "value" },
					new Object[] { _name, _value });
		} else {
			throw new ParseException("<seqp:attrdef> expected, " + pos.charAt()
					+ " found.", pos.location());
		}
	}

	/** &lt;seqp:attrname&gt; */
	public final Object pAttrname(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			Object _tor = null;
			_tor = u.pIdentifier(pos);
			return _tor;
		} else {
			throw new ParseException("<seqp:attrname> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;seqp:literal&gt; */
	public final Object pLiteral(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
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
		if ((_alt_c >= '0' && _alt_c <= '9')) {
			try {
				pos.save();
				Object _tor = null;
				_tor = u.pInteger(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return (Object) pos.error("<seqp:literal> expected, " + pos.charAt()
				+ " found.");
	}

	/** &lt;seqp:path&gt; */
	public final Object pPath(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			Object _root = null;
			_root = seqp.pPathcomp(pos);
			return load(pos, "seqp:path", new String[] { "root" },
					new Object[] { _root });
		} else {
			throw new ParseException("<seqp:path> expected, " + pos.charAt()
					+ " found.", pos.location());
		}
	}

	/** &lt;seqp:pathcomp&gt; */
	public final Object pPathcomp(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		_alt_c = pos.charAt();
		if ((_alt_c >= 'a' && _alt_c <= 'z')) {
			try {
				pos.save();
				Object _tor = null;
				_tor = seqp.pEventref(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if ((_alt_c >= 'A' && _alt_c <= 'Z')) {
			try {
				pos.save();
				Object _tor = null;
				_tor = seqp.pStateref(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return (Object) pos.error("<seqp:pathcomp> expected, " + pos.charAt()
				+ " found.");
	}

	/** &lt;seqp:eventref&gt; */
	public final Object pEventref(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		_alt_c = pos.charAt();
		if ((_alt_c >= 'a' && _alt_c <= 'z')) {
			try {
				pos.save();
				Object _letter = null;
				_letter = seqp.pEventname(pos);
				seqp.pEx$45$62USymbol(pos);
				Object _to = null;
				_to = seqp.pPathcomp(pos);
				pos.commit();
				return load(pos, "seqp:eventref",
						new String[] { "letter", "to" }, new Object[] {
								_letter, _to });
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if ((_alt_c >= 'a' && _alt_c <= 'z')) {
			try {
				pos.save();
				Object _letter = null;
				_letter = seqp.pEventname(pos);
				pos.commit();
				return load(pos, "seqp:eventref", new String[] { "letter" },
						new Object[] { _letter });
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return (Object) pos.error("<seqp:eventref> expected, " + pos.charAt()
				+ " found.");
	}

	/** &lt;seqp:stateref&gt; */
	public final Object pStateref(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		_alt_c = pos.charAt();
		if ((_alt_c >= 'A' && _alt_c <= 'Z')) {
			try {
				pos.save();
				Object _label = null;
				_label = seqp.pStatename(pos);
				seqp.pEx$45$62USymbol(pos);
				Object _to = null;
				_to = seqp.pPathcomp(pos);
				pos.commit();
				return load(pos, "seqp:stateref",
						new String[] { "label", "to" }, new Object[] { _label,
								_to });
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if ((_alt_c >= 'A' && _alt_c <= 'Z')) {
			try {
				pos.save();
				Object _label = null;
				_label = seqp.pStatename(pos);
				pos.commit();
				return load(pos, "seqp:stateref", new String[] { "label" },
						new Object[] { _label });
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return (Object) pos.error("<seqp:stateref> expected, " + pos.charAt()
				+ " found.");
	}

	/** &lt;seqp:eventname&gt; */
	public final Object pEventname(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c >= 'a' && _alt_c <= 'z')) {
			Object _tor = null;
			_tor = seqp.pEventnameWord(pos);
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
			throw new ParseException("<seqp:eventname> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;seqp:eventname_word&gt; */
	public final String pEventnameWord(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c >= 'a' && _alt_c <= 'z')) {
			int start = pos.offset();
			{
				char zz = pos.charAt();
				if ((zz >= 'a' && zz <= 'z')) {
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
			throw new ParseException("<seqp:eventname_word> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;seqp:statename&gt; */
	public final Object pStatename(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c >= 'A' && _alt_c <= 'Z')) {
			Object _tor = null;
			_tor = seqp.pStatenameWord(pos);
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
			throw new ParseException("<seqp:statename> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;seqp:statename_word&gt; */
	public final String pStatenameWord(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c >= 'A' && _alt_c <= 'Z')) {
			int start = pos.offset();
			{
				char zz = pos.charAt();
				if ((zz >= 'A' && zz <= 'Z')) {
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
			throw new ParseException("<seqp:statename_word> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;seqp:ex$45$62_u_symbol&gt; */
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
			throw new ParseException("<seqp:ex$45$62_u_symbol> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;seqp:seqp_attrdef_u_list&gt; */
	public final List<?> pSeqpAttrdefUList(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '@')) {
			Object _f = null;
			_f = seqp.pAttrdef(pos);
			List<Object> _n = new ArrayList<Object>();
			_alt_c = pos.charAt();
			while (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
					|| (_alt_c == ' ') || (_alt_c == '/') || (_alt_c == '@'))) {
				try {
					pos.save();
					_n.add(seqp.pSeqpAttrdefUList11(pos));
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
					break;
				}
				_alt_c = pos.charAt();
			}
			return toList(_f, _n);
		} else {
			throw new ParseException("<seqp:seqp_attrdef_u_list> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;seqp:seqp_path_u_barlist&gt; */
	public final List<?> pSeqpPathUBarlist(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			Object _f = null;
			_f = seqp.pPath(pos);
			List<Object> _n = new ArrayList<Object>();
			_alt_c = pos.charAt();
			while ((_alt_c == '|')) {
				try {
					pos.save();
					_n.add(seqp.pSeqpPathUBarlist11(pos));
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
					break;
				}
				_alt_c = pos.charAt();
			}
			return toList(_f, _n);
		} else {
			throw new ParseException("<seqp:seqp_path_u_barlist> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;seqp:seqp_seqp_def_u_list&gt; */
	public final List<?> pSeqpSeqpDefUList(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c >= 'A' && _alt_c <= 'Z')) {
			Object _f = null;
			_f = seqp.pSeqpDef(pos);
			List<Object> _n = new ArrayList<Object>();
			_alt_c = pos.charAt();
			while (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
					|| (_alt_c == ' ') || (_alt_c == '/') || (_alt_c >= 'A' && _alt_c <= 'Z'))) {
				try {
					pos.save();
					_n.add(seqp.pSeqpSeqpDefUList11(pos));
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
					break;
				}
				_alt_c = pos.charAt();
			}
			return toList(_f, _n);
		} else {
			throw new ParseException("<seqp:seqp_seqp_def_u_list> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;seqp:seqp_statedef_u_commalist&gt; */
	public final List<?> pSeqpStatedefUCommalist(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c >= 'A' && _alt_c <= 'Z')) {
			Object _f = null;
			_f = seqp.pStatedef(pos);
			List<Object> _n = new ArrayList<Object>();
			_alt_c = pos.charAt();
			while ((_alt_c == ',')) {
				try {
					pos.save();
					_n.add(seqp.pSeqpStatedefUCommalist11(pos));
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
					"<seqp:seqp_statedef_u_commalist> expected, "
							+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;seqp:sy$44_u_symbol&gt; */
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
			throw new ParseException("<seqp:sy$44_u_symbol> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;seqp:sy$46_u_symbol&gt; */
	public final Object pSy$46USymbol(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '.')) {
			Object _tor = null;
			_tor = p$Char(pos, '.');
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
			throw new ParseException("<seqp:sy$46_u_symbol> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;seqp:sy$61_u_symbol&gt; */
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
			throw new ParseException("<seqp:sy$61_u_symbol> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;seqp:sy$91_u_symbol&gt; */
	public final Object pSy$91USymbol(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '[')) {
			Object _tor = null;
			_tor = p$Char(pos, '[');
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
			throw new ParseException("<seqp:sy$91_u_symbol> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;seqp:sy$93_u_symbol&gt; */
	public final Object pSy$93USymbol(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == ']')) {
			Object _tor = null;
			_tor = p$Char(pos, ']');
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
			throw new ParseException("<seqp:sy$93_u_symbol> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;seqp:sy$124_u_symbol&gt; */
	public final Object pSy$124USymbol(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '|')) {
			Object _tor = null;
			_tor = p$Char(pos, '|');
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
			throw new ParseException("<seqp:sy$124_u_symbol> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;seqp:seqp_attrdef_u_list_1_1&gt; */
	public final Object pSeqpAttrdefUList11(Pos pos) throws ParseException {
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
			_tor = seqp.pAttrdef(pos);
			return _tor;
		} else {
			throw new ParseException(
					"<seqp:seqp_attrdef_u_list_1_1> expected, " + pos.charAt()
							+ " found.", pos.location());
		}
	}

	/** &lt;seqp:seqp_path_u_barlist_1_1&gt; */
	public final Object pSeqpPathUBarlist11(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == '|')) {
			seqp.pSy$124USymbol(pos);
			Object _tor = null;
			_tor = seqp.pPath(pos);
			return _tor;
		} else {
			throw new ParseException(
					"<seqp:seqp_path_u_barlist_1_1> expected, " + pos.charAt()
							+ " found.", pos.location());
		}
	}

	/** &lt;seqp:seqp_seqp_def_u_list_1_1&gt; */
	public final Object pSeqpSeqpDefUList11(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
				|| (_alt_c == ' ') || (_alt_c == '/') || (_alt_c >= 'A' && _alt_c <= 'Z'))) {
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
			_tor = seqp.pSeqpDef(pos);
			return _tor;
		} else {
			throw new ParseException(
					"<seqp:seqp_seqp_def_u_list_1_1> expected, " + pos.charAt()
							+ " found.", pos.location());
		}
	}

	/** &lt;seqp:seqp_statedef_u_commalist_1_1&gt; */
	public final Object pSeqpStatedefUCommalist11(Pos pos)
			throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == ',')) {
			seqp.pSy$44USymbol(pos);
			Object _tor = null;
			_tor = seqp.pStatedef(pos);
			return _tor;
		} else {
			throw new ParseException(
					"<seqp:seqp_statedef_u_commalist_1_1> expected, "
							+ pos.charAt() + " found.", pos.location());
		}
	}

}
