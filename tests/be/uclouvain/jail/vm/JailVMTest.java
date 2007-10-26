package be.uclouvain.jail.vm;

import java.net.URL;

import junit.framework.TestCase;
import be.uclouvain.jail.adapt.AdaptUtils;
import be.uclouvain.jail.adapt.NetworkAdaptationTool;

/** Tests JailVM class. */
public class JailVMTest extends TestCase {

	/** Returns a graph URL. */
	public URL getJailURL() {
		return JailVMTest.class.getResource("test.jail");
	}
	
	/** Tests JailVM class. */
	public void testJailVM() throws Exception {
		JailVM vm = new JailVM();
		((NetworkAdaptationTool)AdaptUtils.getAdaptationTool()).debugGraph();
		vm.execute(getJailURL());
		//vm.debugMemory();
	}
	
	
}
