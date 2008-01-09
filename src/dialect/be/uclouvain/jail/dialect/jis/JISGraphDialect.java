/**
 * 
 */
package be.uclouvain.jail.dialect.jis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import net.chefbe.autogram.ast2.ILocation;
import net.chefbe.autogram.ast2.parsing.ParseException;
import net.chefbe.autogram.ast2.utils.BaseLocation;
import be.uclouvain.jail.algo.induct.sample.DefaultSample;
import be.uclouvain.jail.algo.induct.sample.DefaultSampleString;
import be.uclouvain.jail.algo.induct.sample.ISample;
import be.uclouvain.jail.dialect.commons.AbstractGraphDialect;

/** Installs the DOT graph dialect. */
public class JISGraphDialect extends AbstractGraphDialect {

	/** Loads source in dot format. */
	public Object load(Object source, String format) throws IOException, ParseException {
		if ("jis".equals(format)) {
			return parse(source);
		} else {
			throw new IllegalStateException("Unknown format (not jail .jis): " + format);
		}
	}

	/** Prints source in dot format. */
	public void print(Object source, String format, PrintWriter stream) throws IOException {
		throw new UnsupportedOperationException("JIS dialect does not implement print.");
	}

	/** Parses a JIS source. */
	protected ISample<String> parse(Object source) throws IOException, ParseException {
		// get a reader 
		ILocation loc = new BaseLocation(source);
		BufferedReader br = new BufferedReader(loc.reader());

		// create target graph
		DefaultSample<String> sample = new DefaultSample<String>();
		
		// read all lines
		String line = null;
		while ((line = br.readLine()) != null) { 
			line = line.trim();
			
			// bypass empty and comment lines
			char start = line.charAt(0);
			if ("".equals(line)) { continue; }
			if (start == '#') { continue; }
			
			// convert to letters, robust to empty string
			line = line.substring(1).trim();
			String[] letters = null;
			if ("".equals(line)) { 
				letters = new String[0];
			} else {
				letters = line.split("\\s");
			}
			
			// convert it to a sample string
			if (start == '+') {
				sample.addSampleString(new DefaultSampleString<String>(true,letters));
			} else if (start == '-') {
				sample.addSampleString(new DefaultSampleString<String>(false,letters));
			} else {
				throw new ParseException("JIS lines must start with + or -");
			}
		}
		
		// return created sample
		return sample;
	}
	
}