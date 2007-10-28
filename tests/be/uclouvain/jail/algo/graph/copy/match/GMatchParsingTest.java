package be.uclouvain.jail.algo.graph.copy.match;

import java.net.URL;

import junit.framework.TestCase;
import net.chefbe.autogram.ast2.IASTNode;
import net.chefbe.autogram.ast2.parsing.active.ASTLoader;
import net.chefbe.autogram.ast2.parsing.active.ASTLoader.EnumTypeResolver;
import net.chefbe.autogram.ast2.parsing.peg.Input;
import net.chefbe.autogram.ast2.parsing.peg.Pos;
import net.chefbe.autogram.ast2.utils.DebugVisitor;

/** Tests GMatch parser. */
public class GMatchParsingTest extends TestCase {

	/** Tests the parser on the gmatch expression file. */
	public void testParser() throws Exception {
		URL url = GMatchParsingTest.class.getResource("test.gmatch");
		Pos pos = new Pos(Input.input(url),0);
		GMatchParser parser = new GMatchParser();
		parser.setActiveLoader(new ASTLoader(new EnumTypeResolver<GMatchNodes>(GMatchNodes.class)));
		IASTNode node = (IASTNode) parser.pMatchTest(pos);
		node.accept(new DebugVisitor());
	}
	
}
