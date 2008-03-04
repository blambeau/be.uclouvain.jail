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
import be.uclouvain.jail.dialect.utils.AbstractGraphDialect;
import be.uclouvain.jail.fa.IAlphabet;
import be.uclouvain.jail.fa.ISample;
import be.uclouvain.jail.fa.utils.AutoAlphabet;
import be.uclouvain.jail.fa.utils.DefaultSample;
import be.uclouvain.jail.fa.utils.DefaultString;
import be.uclouvain.jail.vm.JailVMOptions;

/** Installs the DOT graph dialect. */
public class JISGraphDialect extends AbstractGraphDialect {

	/** Loads source in dot format. */
	public Object load(Object source, JailVMOptions options) throws IOException, ParseException {
		return parse(source);
	}

	/** Prints source in dot format. */
	public void print(Object source, PrintWriter stream, JailVMOptions options) throws IOException {
		throw new UnsupportedOperationException("JIS dialect does not implement print.");
	}

	/** Parses a JIS source. */
	public ISample<String> parse(Object source) throws IOException, ParseException {
		// create target sample
		IAlphabet<String> alphabet = new AutoAlphabet<String>();
		DefaultSample<String> sample = new DefaultSample<String>(alphabet);

		// delegate
		return parse(source,sample);
	}
	
	/** Parses an put strings inside an existing sample. */
	public ISample<String> parse(Object source, ISample<String> sample) throws IOException, ParseException {
		// get a reader 
		ILocation loc = new BaseLocation(source);
		BufferedReader br = new BufferedReader(loc.reader());

		// get the alphabet
		IAlphabet<String> alphabet = sample.getAlphabet();
		
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
				sample.addString(new DefaultString<String>(alphabet,letters,true));
			} else if (start == '-') {
				sample.addString(new DefaultString<String>(alphabet,letters,false));
			} else {
				throw new ParseException("JIS lines must start with + or -");
			}
		}
		
		// return created sample
		return sample;
	}
	
}