package be.uclouvain.jail.vm;

import java.io.IOException;
import java.net.URL;

import junit.framework.TestCase;
import net.chefbe.autogram.ast2.IASTNode;
import net.chefbe.autogram.ast2.ILocation;
import net.chefbe.autogram.ast2.parsing.ParseException;
import net.chefbe.autogram.ast2.parsing.active.ASTLoader;
import net.chefbe.autogram.ast2.parsing.active.ActiveParser;
import net.chefbe.autogram.ast2.parsing.active.ASTLoader.EnumTypeResolver;
import net.chefbe.autogram.ast2.parsing.peg.Input;
import net.chefbe.autogram.ast2.parsing.peg.Pos;
import net.chefbe.autogram.ast2.utils.BaseLocation;
import net.chefbe.autogram.ast2.utils.DebugVisitor;
import be.uclouvain.jail.algo.graph.copy.match.GMatchNodes;
import be.uclouvain.jail.vm.autogram.JailNodes;
import be.uclouvain.jail.vm.autogram.JailParser;

/** Tests Jail Autogram parser classes. */
public class JailParsingTest extends TestCase {

	/** Returns a graph URL. */
	public URL getJailURL() {
		return JailParsingTest.class.getResource("test.jail");
	}
	
	/** Tests parsing classes. */
	public void testParsing() throws Exception {
		try {
			// create location and pos
			ILocation loc = new BaseLocation(getJailURL());
			Pos pos = new Pos(Input.input(loc),0);
			
			// create parser and parse
			JailParser parser = new JailParser();
			((ActiveParser)parser.getParser("gm")).setActiveLoader(
				new ASTLoader(new EnumTypeResolver<GMatchNodes>(GMatchNodes.class))
			);
			parser.setActiveLoader(
				new ASTLoader(new EnumTypeResolver<JailNodes>(JailNodes.class))
			);
			IASTNode root = (IASTNode) parser.pUnit(pos);
			
			// debug parsed grammar
			root.accept(new DebugVisitor());
			
		} catch (IOException ex) {
			throw new Exception("Unable to parse jail file.",ex);
		} catch (ParseException ex) {
			throw new Exception("Jail parsing failed.",ex);
		}
	}
	
	
}
