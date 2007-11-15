package be.uclouvain.jail.vm;

import java.net.URL;

import junit.framework.TestCase;
import net.chefbe.autogram.ast2.IASTNode;
import net.chefbe.autogram.ast2.ILocation;
import net.chefbe.autogram.ast2.parsing.active.ASTLoader;
import net.chefbe.autogram.ast2.parsing.active.ActiveParser;
import net.chefbe.autogram.ast2.parsing.active.ASTLoader.EnumTypeResolver;
import net.chefbe.autogram.ast2.parsing.peg.Input;
import net.chefbe.autogram.ast2.parsing.peg.Pos;
import net.chefbe.autogram.ast2.utils.BaseLocation;
import be.uclouvain.jail.algo.graph.copy.match.GMatchNodes;
import be.uclouvain.jail.examples.JailExamples;
import be.uclouvain.jail.vm.autogram.JailNodes;
import be.uclouvain.jail.vm.autogram.JailParser;

/** Tests Jail Autogram parser classes. */
public class JailParsingTest extends TestCase {

	/** Creates a JAIL parser instance. */
	private JailParser createParser() throws Exception {
		// create parser and parse
		JailParser parser = new JailParser();
		((ActiveParser)parser.getParser("gm")).setActiveLoader(
			new ASTLoader(new EnumTypeResolver<GMatchNodes>(GMatchNodes.class))
		);
		parser.setActiveLoader(
			new ASTLoader(new EnumTypeResolver<JailNodes>(JailNodes.class))
		);
		return parser;
	}

	/** Creates a Pos instance. */
	private Pos createPos(URL url) throws Exception {
		// create location and pos
		ILocation loc = new BaseLocation(url);
		Pos pos = new Pos(Input.input(loc),0);
		return pos;
	}
	
	/** Tests parsing classes. */
	public void testParsing(URL url) throws Exception {
		JailParser parser = createParser();
		IASTNode root = (IASTNode) parser.pUnit(createPos(url));
		assertNotNull(root);
		//root.accept(new DebugVisitor());
	}
	
	/** Parses all examples. */
	public void testJailExamplesParsing() throws Exception {
		for (URL url: JailExamples.getAllExamples()) {
			testParsing(url);
		}
	}
	
	/** Tests expressions parsing. */
	public void testExpressionsParsing() throws Exception {
		testParsing(JailParsingTest.class.getResource("test.jail"));
	}
	
	/** Tests expressions parsing. */
	public void testSystemcParsing() throws Exception {
		testParsing(JailParsingTest.class.getResource("systemc.jail"));
	}
	
	/** Tests parsing documentations. */
	public void testDocParsing() throws Exception {
		URL url = JailCoreToolkit.class.getResource("JailCoreToolkit.jail");
		JailParser parser = createParser();
		IASTNode root = (IASTNode) parser.pNativedoc(createPos(url));
		assertNotNull(root);
		//root.accept(new DebugVisitor());
	}
	
}
