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
		if (((_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			Object _command = null;
			_command = jail.pJailCommandUList(pos);
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
		if (((_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			Object _tor = null;
			_tor = jail.pPluginCommand(pos);
			return _tor;
		} else {
			throw new ParseException("<jail:command> expected, " + pos.charAt()
					+ " found.", pos.location());
		}
	}

	/** &lt;jail:plugin_command&gt; */
	public final Object pPluginCommand(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			Object _namespace = null;
			_namespace = u.pIdentifier(pos);
			p$Char(pos, ':');
			Object _name = null;
			_name = u.pIdentifier(pos);
			Object _arg = null;
			_alt_c = pos.charAt();
			if (((_alt_c == '"') || (_alt_c == '\''))) {
				try {
					pos.save();
					_arg = jail.pJailArgumentUList(pos);
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
				}
			}
			return load(pos, "jail:plugin_command", new String[] { "namespace",
					"name", "arg" }, new Object[] { _namespace, _name, _arg });
		} else {
			throw new ParseException("<jail:plugin_command> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:argument&gt; */
	public final Object pArgument(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '"') || (_alt_c == '\''))) {
			Object _value = null;
			_value = u.pString(pos);
			return load(pos, "jail:argument", new String[] { "value" },
					new Object[] { _value });
		} else {
			throw new ParseException("<jail:argument> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:jail_argument_u_list&gt; */
	public final List<?> pJailArgumentUList(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '"') || (_alt_c == '\''))) {
			Object _f = null;
			_f = jail.pArgument(pos);
			List<Object> _n = new ArrayList<Object>();
			_alt_c = pos.charAt();
			while (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
					|| (_alt_c == ' ') || (_alt_c == '"') || (_alt_c == '\'') || (_alt_c == '/'))) {
				try {
					pos.save();
					_n.add(jail.pJailArgumentUList11(pos));
					pos.commit();
				} catch (ParseException ex) {
					pos.rollback();
					break;
				}
				_alt_c = pos.charAt();
			}
			return toList(_f, _n);
		} else {
			throw new ParseException("<jail:jail_argument_u_list> expected, "
					+ pos.charAt() + " found.", pos.location());
		}
	}

	/** &lt;jail:jail_command_u_list&gt; */
	public final List<?> pJailCommandUList(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
			Object _f = null;
			_f = jail.pCommand(pos);
			List<Object> _n = new ArrayList<Object>();
			_alt_c = pos.charAt();
			while (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
					|| (_alt_c == ' ') || (_alt_c == '/')
					|| (_alt_c >= 'A' && _alt_c <= 'Z') || (_alt_c >= 'a' && _alt_c <= 'z'))) {
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

	/** &lt;jail:jail_argument_u_list_1_1&gt; */
	public final Object pJailArgumentUList11(Pos pos) throws ParseException {
		char _alt_c = pos.charAt();
		if (((_alt_c == '\t') || (_alt_c == '\n') || (_alt_c == '\r')
				|| (_alt_c == ' ') || (_alt_c == '"') || (_alt_c == '\'') || (_alt_c == '/'))) {
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
			_tor = jail.pArgument(pos);
			return _tor;
		} else {
			throw new ParseException(
					"<jail:jail_argument_u_list_1_1> expected, " + pos.charAt()
							+ " found.", pos.location());
		}
	}

	/** &lt;jail:jail_command_u_list_1_1&gt; */
	public final Object pJailCommandUList11(Pos pos) throws ParseException {
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
			_tor = jail.pCommand(pos);
			return _tor;
		} else {
			throw new ParseException(
					"<jail:jail_command_u_list_1_1> expected, " + pos.charAt()
							+ " found.", pos.location());
		}
	}

}
