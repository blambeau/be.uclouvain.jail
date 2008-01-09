package be.uclouvain.jail.examples;

import java.net.URL;
import java.util.NoSuchElementException;

/**
 * Provides utilities on Jail examples.
 * 
 * @author blambeau
 */
public final class JailExamples {

	/** Not intended to be instanciated. */
	private JailExamples() {}
	
	/** Returns an URL on a given example. */
	public static URL getExample(String where) {
		URL url = ClassLoader.getSystemClassLoader().getResource(where);
		if (url == null) { throw new NoSuchElementException("Unable to locate " + where); }
		return url;
	}
	
	/** Returns all Jail examples. */
	public static URL[] getAllExamples() {
		return new URL[]{
			getExample("atm/hmsc.jail"),

			getExample("bicycle/commons.jail"),
			getExample("bicycle/pta.jail"),

			getExample("computer/hmsc.jail"),
			getExample("computer/hmscsplit.jail"),
			
			getExample("hopcroft/commons.jail"),
			getExample("hopcroft/determinize.jail"),
			getExample("hopcroft/minimize.jail"),
			getExample("hopcroft/tmoves.jail"),
			getExample("hopcroft/tmoves2.jail"),

			getExample("train/commons.jail"),
			getExample("train/decorate.jail"),
			getExample("train/standard3.jail"),
			getExample("train/rpni.jail"),
		};
	}
	
}
