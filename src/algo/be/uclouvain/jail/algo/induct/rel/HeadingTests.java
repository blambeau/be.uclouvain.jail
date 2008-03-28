package be.uclouvain.jail.algo.induct.rel;

import junit.framework.TestCase;
import net.chefbe.javautils.robust.tests.EqualsAndHashTestUtils;

/**
 * Tests Heading class.
 * 
 * @author blambeau
 */
public class HeadingTests extends TestCase {

	/** Returns a string array. */
	private String[] names(String...names) {
		if (names == null) { return new String[0]; }
		return names;
	}
	
	/** Returns a type array. */
	private Class[] types(Class...types) {
		if (types == null) { return new Class[0]; }
		return types;
	}
	
	/** Creates a heading. */
	private Heading heading(String[] names, Class[] types) {
		return new Heading(names, types);
	}
	
	/** Tests degree(). */
	public void testDegree() {
		// empty heading
		{
			Heading heading = heading(names(), types());
			assertEquals("Empty heading is of 0 size", 0, heading.degree());
		}
		
		// S# one heading 
		{
			Heading heading = heading(names("S#"), types(String.class));
			assertEquals("S heading is of degree 1", 1, heading.degree());
		}

		// SP heading 
		{
			Heading heading = heading(names("S#","P#"), types(String.class, String.class));
			assertEquals("SP heading is of degree 2", 2, heading.degree());
		}
	}
	
	/** Tests indexOf(). */
	public void testIndexOf() {
		// empty heading
		{
			Heading heading = heading(names(), types());
			assertEquals("indexOf always returns -1",-1, heading.indexOf("target", false));
			try {
				heading.indexOf("target", true);
				assertTrue("Exception has been thrown",false);
			} catch (IllegalArgumentException ex) {}
		}
		
		// S one heading 
		{
			Heading heading = heading(names("S#"), types(String.class));
			assertEquals("indexOf returns 0 for S#",0, heading.indexOf("S#", true));
		}
		
		// SP heading 
		{
			Heading heading = heading(names("S#","P#"), types(String.class, String.class));
			assertEquals("indexOf returns 1 for S#",1, heading.indexOf("S#", true));
			assertEquals("indexOf returns 0 for P#",0, heading.indexOf("P#", true));
		}
	}
	
	/** Tests typeOf(). */
	public void testTypeOf() {
		// empty heading
		{
			Heading heading = heading(names(), types());
			try {
				heading.getTypeOf("target");
				assertTrue("Exception has been thrown",false);
			} catch (IllegalArgumentException ex) {}
		}
		
		// S heading 
		{
			Heading heading = heading(names("S#"), types(String.class));
			assertEquals("String.class if typeOf(S#)",String.class,heading.getTypeOf("S#"));
		}

		// SP heading 
		{
			Heading h = heading(names("S#","P#"), types(String.class, Integer.class));
			assertEquals("String.class if typeOf(S#)",String.class,h.getTypeOf("S#"));
			assertEquals("Integer.class if typeOf(P#)",Integer.class,h.getTypeOf("P#"));
		}

		// SP heading 2
		{
			Heading h = heading(names("P#","S#"), types(Integer.class, String.class));
			assertEquals("String.class if typeOf(S#)",String.class,h.getTypeOf("S#"));
			assertEquals("Integer.class if typeOf(P#)",Integer.class,h.getTypeOf("P#"));
		}
	}
	
	/** Tests equals() and hashCode(). */
	public void testEqualsAndHashCode() {
		EqualsAndHashTestUtils utils = new EqualsAndHashTestUtils();
		Heading empty0 = heading(names(), types());
		Heading empty1 = heading(names(), types());
		utils.block(empty0, empty1);
		
		Heading S0 = heading(names("S#"), types(String.class));
		Heading S1 = heading(names("S#"), types(String.class));
		utils.block(S0,S1);
		
		Heading SP0 = heading(names("S#","P#"), types(String.class, String.class));
		Heading SP1 = heading(names("S#","P#"), types(String.class, String.class));
		Heading SP2 = heading(names("P#","S#"), types(String.class, String.class));
		utils.block(SP0,SP1,SP2);
		
		Heading notSP1 = heading(names("P#","S#"), types(String.class, Integer.class));
		utils.block(notSP1);

		Heading notSP2 = heading(names("S#","P#"), types(String.class, Integer.class));
		utils.block(notSP2);
		
		utils.testEquals();
		utils.testHashCode();
	}
	
}
