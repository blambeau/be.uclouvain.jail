package be.uclouvain.jail.uinfo;

import junit.framework.TestCase;

/** Tests UserInfoCreator class. */
public class UserInfoCopierTest extends TestCase {

	/** Info used to test. */
	private IUserInfo info;

	/** Asserts that some attribute values equals a specified value. */
	private void assertInfoAttrEquals(IUserInfo info, String attr, Object value) {
		assertEquals("Info contains " + value + " mapped to " + attr, value,
				info.getAttribute(attr));
	}

	/** Asserts that some info is equivalent to the following inline map. */
	private void assertEqualsTo(IUserInfo info, String[] attrs,
			Object... values) {
		int i = 0;
		for (String attr : attrs) {
			assertInfoAttrEquals(info, attr, values[i++]);
		}
		assertEquals("Correct size", info.getAttributes().size(), attrs.length);
	}

	/** Factors a MapUserInfo instance. */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		info = new MapUserInfo();
		info.setAttribute("number", 1);
		info.setAttribute("label", "some label string");
		info.setAttribute("isValid", true);
		assertEqualsTo(info, new String[] { "number", "label", "isValid" }, 1,
				"some label string", true);
	}

	/** Tests addRename method. */
	public void testRename() {
		IUserInfo copy = new UserInfoCopier()
		               .rename("number","theNumber").create(info);
		assertEqualsTo(copy, new String[]{"theNumber"}, 1);
		assertEqualsTo(copy, new String[]{"number"}, new Object[]{null});
	}

	/** Tests addThrowAway method. */
	public void testKeepAllBut() {
		IUserInfo copy = new UserInfoCopier()
						.keepAllBut("label").create(info);
		assertEqualsTo(copy, new String[]{"number","isValid"}, 1,true);

	
		copy = new UserInfoCopier()
						.keepAllBut("label","isValid").create(info);
		assertEqualsTo(copy, new String[]{"number"}, new Object[]{1});
	}
	
	/** Tests keep method. */
	public void testKeep() {
		IUserInfo copy = new UserInfoCopier()
						.keep("number","label","isValid").create(info);
		assertFalse("Not a pointer copy", copy == info);
		assertEqualsTo(copy, new String[]{"number","label","isValid"}, 1,"some label string",true);


		copy = new UserInfoCopier().keep("number").create(info);
		assertEqualsTo(copy, new String[] {"number"}, 1);

		copy = new UserInfoCopier()
						.keep("number","isValid").create(info);
		assertEqualsTo(copy, new String[]{"number","isValid"}, 1,true);
	}
	
	/** Tests keepAll method. */
	public void testKeepAll() {
		IUserInfo copy = new UserInfoCopier().keepAll().create(info);
		assertEqualsTo(copy, new String[]{"number","label","isValid"}, 1,"some label string",true);
	}
	
	/** Tests some intuitive configuration. */
	public void testAllAtOnce() {
		IUserInfo copy = new UserInfoCopier()
			.keepAllBut("number")
			.rename("number", "theNumber")
			.create(info);
		assertEqualsTo(copy, new String[]{"theNumber","label","isValid"}, 1,"some label string",true);
		
	}
	
}
