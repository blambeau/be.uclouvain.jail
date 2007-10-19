package be.uclouvain.jail.vm.autogram;

import java.net.URL;

import junit.framework.TestCase;
import net.chefbe.autogram.ast2.IASTNode;
import net.chefbe.autogram.ast2.ILocation;
import net.chefbe.autogram.ast2.parsing.active.ASTLoader;
import net.chefbe.autogram.ast2.parsing.active.ASTLoader.EnumTypeResolver;
import net.chefbe.autogram.ast2.parsing.peg.Input;
import net.chefbe.autogram.ast2.parsing.peg.Pos;
import net.chefbe.autogram.ast2.utils.BaseLocation;
import net.chefbe.autogram.ast2.utils.DebugVisitor;

/** Tests JailParser class. */
public class JailParserTest extends TestCase {

	/** Returns a graph URL. */
	public URL getJailURL() {
		return JailParserTest.class.getResource("test.jail");
	}
	
	/** Tests jail parser class. */
	public void testJailParser() throws Exception {
		JailParser parser = new JailParser();
		parser.setActiveLoader(new ASTLoader(new EnumTypeResolver<JailNodes>(JailNodes.class)));
		ILocation loc = new BaseLocation(getJailURL());
		Pos pos = new Pos(Input.input(loc),0);
		IASTNode node = (IASTNode) parser.pUnit(pos);
		node.accept(new DebugVisitor());
	}
	
}
