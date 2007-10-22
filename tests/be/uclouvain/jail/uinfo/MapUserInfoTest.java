package be.uclouvain.jail.uinfo;

import java.util.Map;

import junit.framework.TestCase;

/** Tests MapUserInfo class. */
public class MapUserInfoTest extends TestCase {

	/** Info to test. */
	private IUserInfo info;
	
	/** Asserts that some attribute values equals a specified value. */
	private void assertInfoAttrEquals(IUserInfo info, String attr, Object value) {
		assertEquals("Info contains " + value + " mapped to " + attr,value,info.getAttribute(attr));
	}
	
	/** Factors a MapUserInfo instance. */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		info = new MapUserInfo();
		info.setAttribute("number", 1);
		info.setAttribute("label", "some label string");
		info.setAttribute("isValid", true);
	}

	/** Tests setAttribute method. */
	public void testSetAttribute() throws Exception {
		assertInfoAttrEquals(info,"number",1);
		assertInfoAttrEquals(info,"label", "some label string");
		assertInfoAttrEquals(info,"isValid", true);
	}

	/** Test getKeys method. */
	public void testGetKeys() {
		Iterable<String> keys = info.getKeys();
		int count = 0;
		for (String key: keys) {
			if ("number".equals(key) ||
			    "label".equals(key) ||
			    "isValid".equals(key)) {
				count++;
			} else {
				assertFalse("Contains correct labels",true);
			}
		}
		assertEquals("Contains three keys",3,count);
	}
	
	/** Tests getAttributes method. */
	public void testGetAttributes() {
		Map<String,Object> attrs = info.getAttributes();
		assertEquals("contains correct number",1,attrs.get("number"));
		assertEquals("contains correct label","some label string",attrs.get("label"));
		assertEquals("contains correct isValid",true,attrs.get("isValid"));
		assertEquals("Correct size",3,attrs.size());
	}
	
	/** Tests removeAttribute method. */
	public void testRemoveAttribute() {
		info.removeAttribute("isValid");
		Map<String,Object> attrs = info.getAttributes();
		assertEquals("contains correct number",1,attrs.get("number"));
		assertEquals("contains correct label","some label string",attrs.get("label"));
		assertEquals("Correct size",2,attrs.size());
	}
	
	/** Tests copy method. */
	public void testCopy() {
		IUserInfo copy = info.copy();
		assertEquals("Correct map copy",info.getAttributes(),copy.getAttributes());
	}
	
}
