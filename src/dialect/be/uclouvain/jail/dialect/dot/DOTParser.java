package be.uclouvain.jail.dialect.dot;

import java.util.ArrayList;
import java.util.List;

import net.chefbe.autogram.ast2.parsing.ParseException;
import net.chefbe.autogram.ast2.parsing.active.ActiveParser;
import net.chefbe.autogram.ast2.parsing.peg.PEGParser;
import net.chefbe.autogram.ast2.parsing.peg.Pos;
import be.uclouvain.jail.dialect.commons.UTILSParser;

/** Parser for DOT grammar. */
public class DOTParser extends ActiveParser {

	// ----------------------------------------------- External parsers
	/** External grammars. */
	private DOTParser dot = this;

	private UTILSParser u = new UTILSParser(this);

	/** Creates a master parser. */
	public DOTParser() {
	}

	/** Creates a child parser. */
	public DOTParser(PEGParser parent) {
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
	/** &lt;dot:graphdef&gt; */
	public final Object pGraphdef(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == 'd')) {
			p$Keyword(pos, "digraph");
			Object _id = null;
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
					|| (_alt_c == ' ') || (_alt_c == '#') || (_alt_c == '$')
					|| (_alt_c == '/') || (_alt_c >= '0' && _alt_c <= '9')
					|| (_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c == '_') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
				try {
					pos.save();
					_id = dot.pIdentifier(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			dot.pSy$123USymbol(pos);
			Object _components = null;
			_components = dot.pDotComponentUList(pos);
			dot.pSy$125USymbol(pos);
			return load(pos, "dot:graphdef",
					new String[] { "id", "components" }, new Object[] { _id,
							_components });
		} else {
			throw new ParseException("<dot:graphdef> expected, " + pos.charAt()
					+ " found.", pos.location());
		}
	}

	/** &lt;dot:component&gt; */
	public final Object pComponent(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		_alt_c = pos.charAt();
		if ((_alt_c == 'g')) {
			try {
				pos.save();
				Object _tor = null;
				_tor = dot.pGraphCommons(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if ((_alt_c == 'n')) {
			try {
				pos.save();
				Object _tor = null;
				_tor = dot.pNodeCommons(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if ((_alt_c == 'e')) {
			try {
				pos.save();
				Object _tor = null;
				_tor = dot.pEdgeCommons(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
				|| (_alt_c == ' ') || (_alt_c == '#') || (_alt_c == '$')
				|| (_alt_c == '-') || (_alt_c == '/')
				|| (_alt_c >= '0' && _alt_c <= '9')
				|| (_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c == '_') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			try {
				pos.save();
				Object _tor = null;
				_tor = dot.pEdgedef(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
				|| (_alt_c == ' ') || (_alt_c == '#') || (_alt_c == '$')
				|| (_alt_c == '/') || (_alt_c >= '0' && _alt_c <= '9')
				|| (_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c == '[')
				|| (_alt_c == '_') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			try {
				pos.save();
				Object _tor = null;
				_tor = dot.pNodedef(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return (Object) pos.error("<dot:component> expected, " + pos.charAt()
				+ " found.");
	}

	/** &lt;dot:graph_commons&gt; */
	public final Object pGraphCommons(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == 'g')) {
			p$Keyword(pos, "graph");
			Object _attr = null;
			_attr = dot.pAttributes(pos);
			dot.pSy$59USymbol(pos);
			return load(pos, "dot:graph_commons", new String[] { "attr" },
					new Object[] { _attr });
		} else {
			throw new ParseException("<dot:graph_commons> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;dot:node_commons&gt; */
	public final Object pNodeCommons(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == 'n')) {
			p$Keyword(pos, "node");
			Object _attr = null;
			_attr = dot.pAttributes(pos);
			dot.pSy$59USymbol(pos);
			return load(pos, "dot:node_commons", new String[] { "attr" },
					new Object[] { _attr });
		} else {
			throw new ParseException("<dot:node_commons> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;dot:edge_commons&gt; */
	public final Object pEdgeCommons(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == 'e')) {
			p$Keyword(pos, "edge");
			Object _attr = null;
			_attr = dot.pAttributes(pos);
			dot.pSy$59USymbol(pos);
			return load(pos, "dot:edge_commons", new String[] { "attr" },
					new Object[] { _attr });
		} else {
			throw new ParseException("<dot:edge_commons> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;dot:nodedef&gt; */
	public final Object pNodedef(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
				|| (_alt_c == ' ') || (_alt_c == '#') || (_alt_c == '$')
				|| (_alt_c == '/') || (_alt_c >= '0' && _alt_c <= '9')
				|| (_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c == '[')
				|| (_alt_c == '_') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			Object _id = null;
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
					|| (_alt_c == ' ') || (_alt_c == '#') || (_alt_c == '$')
					|| (_alt_c == '/') || (_alt_c >= '0' && _alt_c <= '9')
					|| (_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c == '_') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
				try {
					pos.save();
					_id = dot.pIdentifier(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			Object _attr = null;
			_attr = dot.pAttributes(pos);
			dot.pSy$59USymbol(pos);
			return load(pos, "dot:nodedef", new String[] { "id", "attr" },
					new Object[] { _id, _attr });
		} else {
			throw new ParseException("<dot:nodedef> expected, " + pos.charAt()
					+ " found.", pos.location());
		}
	}

	/** &lt;dot:edgedef&gt; */
	public final Object pEdgedef(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
				|| (_alt_c == ' ') || (_alt_c == '#') || (_alt_c == '$')
				|| (_alt_c == '-') || (_alt_c == '/')
				|| (_alt_c >= '0' && _alt_c <= '9')
				|| (_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c == '_') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			Object _src = null;
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
					|| (_alt_c == ' ') || (_alt_c == '#') || (_alt_c == '$')
					|| (_alt_c == '/') || (_alt_c >= '0' && _alt_c <= '9')
					|| (_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c == '_') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
				try {
					pos.save();
					_src = dot.pIdentifier(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			dot.pEx$45$62USymbol(pos);
			Object _trg = null;
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
					|| (_alt_c == ' ') || (_alt_c == '#') || (_alt_c == '$')
					|| (_alt_c == '/') || (_alt_c >= '0' && _alt_c <= '9')
					|| (_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c == '_') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
				try {
					pos.save();
					_trg = dot.pIdentifier(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			Object _attr = null;
			_attr = dot.pAttributes(pos);
			dot.pSy$59USymbol(pos);
			return load(pos, "dot:edgedef",
					new String[] { "src", "trg", "attr" }, new Object[] { _src,
							_trg, _attr });
		} else {
			throw new ParseException("<dot:edgedef> expected, " + pos.charAt()
					+ " found.", pos.location());
		}
	}

	/** &lt;dot:attributes&gt; */
	public final Object pAttributes(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		_alt_c = pos.charAt();
		if ((_alt_c == '[')) {
			try {
				pos.save();
				dot.pSy$91USymbol(pos);
				Object _attr = null;
				_attr = dot.pDotAttributeUList(pos);
				dot.pSy$93USymbol(pos);
				pos.commit();
				return load(pos, "dot:attributes", new String[] { "attr" },
						new Object[] { _attr });
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		_alt_c = pos.charAt();
		if ((_alt_c == '[')) {
			try {
				pos.save();
				dot.pSy$91USymbol(pos);
				Object _attr = null;
				_attr = dot.pDotAttributeUCommalist(pos);
				dot.pSy$93USymbol(pos);
				pos.commit();
				return load(pos, "dot:attributes", new String[] { "attr" },
						new Object[] { _attr });
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return (Object) pos.error("<dot:attributes> expected, " + pos.charAt()
				+ " found.");
	}

	/** &lt;dot:attribute&gt; */
	public final Object pAttribute(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
				|| (_alt_c == ' ') || (_alt_c == '#') || (_alt_c == '$')
				|| (_alt_c == '/') || (_alt_c >= '0' && _alt_c <= '9')
				|| (_alt_c == '=') || (_alt_c >= 'A' && _alt_c <= 'Z')
				|| (_alt_c == '_') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			Object _key = null;
			_alt_c = pos.charAt();
			if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
					|| (_alt_c == ' ') || (_alt_c == '#') || (_alt_c == '$')
					|| (_alt_c == '/') || (_alt_c >= '0' && _alt_c <= '9')
					|| (_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c == '_') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
				try {
					pos.save();
					_key = dot.pIdentifier(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			dot.pSy$61USymbol(pos);
			Object _value = null;
			_value = dot.pValue(pos);
			return load(pos, "dot:attribute", new String[] { "key", "value" },
					new Object[] { _key, _value });
		} else {
			throw new ParseException("<dot:attribute> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;dot:value&gt; */
	public final Object pValue(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
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
		if (((_alt_c >= '0' && _alt_c <= '9')
				|| (_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c == '_') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			try {
				pos.save();
				Object _tor = null;
				_tor = dot.pNospace(pos);
				pos.commit();
				return _tor;
			} catch (ParseException ex) {
				pos.rollback(ex);
			}
		}
		return (Object) pos.error("<dot:value> expected, " + pos.charAt()
				+ " found.");
	}

	/** &lt;dot:nospace&gt; */
	public final String pNospace(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c >= '0' && _alt_c <= '9')
				|| (_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c == '_') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			int start = pos.offset();
			{
				int _zz_start = pos.offset();
				while (!pos.isEof()) {
					char zz = pos.charAt();
					if ((zz >= 'A' && zz <= 'Z') || (zz >= 'a' && zz <= 'z')
							|| (zz >= '0' && zz <= '9') || (zz == '_')) {
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
			return pos.trim(start);
		} else {
			throw new ParseException("<dot:nospace> expected, " + pos.charAt()
					+ " found.", pos.location());
		}
	}

	/** &lt;dot:identifier&gt; */
	public final Object pIdentifier(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
				|| (_alt_c == ' ') || (_alt_c == '#') || (_alt_c == '$')
				|| (_alt_c == '/') || (_alt_c >= '0' && _alt_c <= '9')
				|| (_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c == '_') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			Object _tor = null;
			_alt_c = pos.charAt();
			if (((_alt_c == '#') || (_alt_c == '$')
					|| (_alt_c >= '0' && _alt_c <= '9')
					|| (_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c == '_') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
				try {
					pos.save();
					_tor = dot.pIdentifierChars(pos);
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
			return _tor;
		} else {
			throw new ParseException("<dot:identifier> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;dot:identifier_chars&gt; */
	public final String pIdentifierChars(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '#') || (_alt_c == '$')
				|| (_alt_c >= '0' && _alt_c <= '9')
				|| (_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c == '_') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			int start = pos.offset();
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
			throw new ParseException("<dot:identifier_chars> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;dot:ex$45$62_u_symbol&gt; */
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
			throw new ParseException("<dot:ex$45$62_u_symbol> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;dot:dot_attribute_u_commalist&gt; */
	public final List<?> pDotAttributeUCommalist(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
				|| (_alt_c == ' ') || (_alt_c == '#') || (_alt_c == '$')
				|| (_alt_c == '/') || (_alt_c >= '0' && _alt_c <= '9')
				|| (_alt_c == '=') || (_alt_c >= 'A' && _alt_c <= 'Z')
				|| (_alt_c == '_') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			Object _f = null;
			_f = dot.pAttribute(pos);
			List<Object> _n = new ArrayList<Object>();
			_alt_c = pos.charAt();
			while ((_alt_c == ',')) {
				try {
					pos.save();
					_n.add(dot.pDotAttributeUCommalist11(pos));
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
					"<dot:dot_attribute_u_commalist> expected, " + pos.charAt()
							+ " found.", pos.location());
		}
	}

	/** &lt;dot:dot_attribute_u_list&gt; */
	public final List<?> pDotAttributeUList(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
				|| (_alt_c == ' ') || (_alt_c == '#') || (_alt_c == '$')
				|| (_alt_c == '/') || (_alt_c >= '0' && _alt_c <= '9')
				|| (_alt_c == '=') || (_alt_c >= 'A' && _alt_c <= 'Z')
				|| (_alt_c == '_') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			Object _f = null;
			_f = dot.pAttribute(pos);
			List<Object> _n = new ArrayList<Object>();
			_alt_c = pos.charAt();
			while (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
					|| (_alt_c == ' ') || (_alt_c == '#') || (_alt_c == '$')
					|| (_alt_c == '/') || (_alt_c >= '0' && _alt_c <= '9')
					|| (_alt_c == '=') || (_alt_c >= 'A' && _alt_c <= 'Z')
					|| (_alt_c == '_') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
				try {
					pos.save();
					_n.add(dot.pDotAttributeUList11(pos));
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
					break;
				}
				_alt_c = pos.charAt();
			}
			return toList(_f, _n);
		} else {
			throw new ParseException("<dot:dot_attribute_u_list> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;dot:dot_component_u_list&gt; */
	public final List<?> pDotComponentUList(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
				|| (_alt_c == ' ') || (_alt_c == '#') || (_alt_c == '$')
				|| (_alt_c == '-') || (_alt_c == '/')
				|| (_alt_c >= '0' && _alt_c <= '9')
				|| (_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c == '[')
				|| (_alt_c == '_') || (_alt_c >= 'a' && _alt_c <= 'z')
				|| (_alt_c == 'e') || (_alt_c == 'g') || (_alt_c == 'n'))) {
			Object _f = null;
			_f = dot.pComponent(pos);
			List<Object> _n = new ArrayList<Object>();
			_alt_c = pos.charAt();
			while (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
					|| (_alt_c == ' ') || (_alt_c == '#') || (_alt_c == '$')
					|| (_alt_c == '-') || (_alt_c == '/')
					|| (_alt_c >= '0' && _alt_c <= '9')
					|| (_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c == '[')
					|| (_alt_c == '_') || (_alt_c >= 'a' && _alt_c <= 'z')
					|| (_alt_c == 'e') || (_alt_c == 'g') || (_alt_c == 'n'))) {
				try {
					pos.save();
					_n.add(dot.pDotComponentUList11(pos));
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
					break;
				}
				_alt_c = pos.charAt();
			}
			return toList(_f, _n);
		} else {
			throw new ParseException("<dot:dot_component_u_list> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;dot:sy$44_u_symbol&gt; */
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
			throw new ParseException("<dot:sy$44_u_symbol> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;dot:sy$59_u_symbol&gt; */
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
			throw new ParseException("<dot:sy$59_u_symbol> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;dot:sy$61_u_symbol&gt; */
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
			throw new ParseException("<dot:sy$61_u_symbol> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;dot:sy$91_u_symbol&gt; */
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
			throw new ParseException("<dot:sy$91_u_symbol> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;dot:sy$93_u_symbol&gt; */
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
			throw new ParseException("<dot:sy$93_u_symbol> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;dot:sy$123_u_symbol&gt; */
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
			throw new ParseException("<dot:sy$123_u_symbol> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;dot:sy$125_u_symbol&gt; */
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
			throw new ParseException("<dot:sy$125_u_symbol> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;dot:dot_attribute_u_commalist_1_1&gt; */
	public final Object pDotAttributeUCommalist11(Pos pos)
			throws ParseException {
		char _alt_c = pos.charAt();
		if ((_alt_c == ',')) {
			dot.pSy$44USymbol(pos);
			Object _tor = null;
			_tor = dot.pAttribute(pos);
			return _tor;
		} else {
			throw new ParseException(
					"<dot:dot_attribute_u_commalist_1_1> expected, "
							+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;dot:dot_attribute_u_list_1_1&gt; */
	public final Object pDotAttributeUList11(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
				|| (_alt_c == ' ') || (_alt_c == '#') || (_alt_c == '$')
				|| (_alt_c == '/') || (_alt_c >= '0' && _alt_c <= '9')
				|| (_alt_c == '=') || (_alt_c >= 'A' && _alt_c <= 'Z')
				|| (_alt_c == '_') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
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
			_tor = dot.pAttribute(pos);
			return _tor;
		} else {
			throw new ParseException(
					"<dot:dot_attribute_u_list_1_1> expected, " + pos.charAt()
							+ " found.", pos.location());
		}
	}

	/** &lt;dot:dot_component_u_list_1_1&gt; */
	public final Object pDotComponentUList11(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
				|| (_alt_c == ' ') || (_alt_c == '#') || (_alt_c == '$')
				|| (_alt_c == '-') || (_alt_c == '/')
				|| (_alt_c >= '0' && _alt_c <= '9')
				|| (_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c == '[')
				|| (_alt_c == '_') || (_alt_c >= 'a' && _alt_c <= 'z')
				|| (_alt_c == 'e') || (_alt_c == 'g') || (_alt_c == 'n'))) {
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
			_tor = dot.pComponent(pos);
			return _tor;
		} else {
			throw new ParseException(
					"<dot:dot_component_u_list_1_1> expected, " + pos.charAt()
							+ " found.", pos.location());
		}
	}

}
