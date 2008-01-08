package be.uclouvain.jail.vm;

import java.net.URL;

import junit.framework.TestCase;
import be.uclouvain.jail.dialect.dot.JDotty;

/** Tests examples. */
public class JailExampleTest extends TestCase {

	/** Returns a graph URL. */
	public URL[] getJailURLs() {
		return new URL[] {
				/*
			ClassLoader.getSystemResource("train/commons.jail"),
			ClassLoader.getSystemResource("train/decorate.jail"),
			ClassLoader.getSystemResource("train/kernel.jail"),
			ClassLoader.getSystemResource("train/ptaconstr.jail"),
			ClassLoader.getSystemResource("train/standard3.jail"),
			ClassLoader.getSystemResource("bdd/expressions.jail"),
			*/
		};
	}
	
	/** Just forces JDotty to be silent. */
	@Override
	protected void setUp() throws Exception {
		JDotty.silent();
	}

	/** Tests JailVM class. */
	public void testJailVM() throws Exception {
		int index = 0;
		for (URL url: getJailURLs()) {
			assertNotNull(index++ + "-th URL is not null.",url);
			JailVM vm = new JailVM();
			vm.execute(url);
		}
	}
	
	public static void main(String[] args) throws Exception {
		new JailExampleTest().testJailVM();
	}
	
}
