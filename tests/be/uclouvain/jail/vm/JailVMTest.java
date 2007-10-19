package be.uclouvain.jail.vm;

import java.net.URL;

import junit.framework.TestCase;
import be.uclouvain.jail.Jail;

/** Tests JailVM class. */
public class JailVMTest extends TestCase {

	/** Returns a graph URL. */
	public URL getJailURL() {
		return JailVMTest.class.getResource("test.jail");
	}
	
	/** Tests JailVM class. */
	public void testJailVM() throws Exception {
		Jail.install();
		JailVM vm = new JailVM();
		vm.register(new JailCorePlugin());
		vm.execute(getJailURL());
	}
	
	
}
