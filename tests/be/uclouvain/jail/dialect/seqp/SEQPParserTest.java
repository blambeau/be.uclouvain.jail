package be.uclouvain.jail.dialect.seqp;

import java.io.IOException;
import java.net.URL;

import junit.framework.TestCase;
import net.chefbe.autogram.ast2.IASTNode;
import net.chefbe.autogram.ast2.parsing.ParseException;
import net.chefbe.autogram.ast2.parsing.active.ASTLoader;
import net.chefbe.autogram.ast2.parsing.active.ASTLoader.EnumTypeResolver;
import net.chefbe.autogram.ast2.parsing.peg.Input;
import net.chefbe.autogram.ast2.parsing.peg.Pos;

/** Tests the SEQP parser on expressions. */
public class SEQPParserTest extends TestCase {

	/** Returns an URL to the expression file. */
	public URL getURL() {
		return SEQPParserTest.class.getResource("expressions.seqp");
	}
	
	/** Tests the seqp parser. */
	public void testSEQPParser() throws IOException, ParseException {
		URL url = getURL();
		
		SEQPParser parser = new SEQPParser();
		parser.setActiveLoader(
			new ASTLoader(new EnumTypeResolver<SEQPNodes>(SEQPNodes.class))
		);
		Pos pos = new Pos(Input.input(url),0);
		IASTNode root = (IASTNode) parser.pTestunit(pos);
		assertNotNull(root);
		//root.accept(new DebugVisitor());
	}
	
}
