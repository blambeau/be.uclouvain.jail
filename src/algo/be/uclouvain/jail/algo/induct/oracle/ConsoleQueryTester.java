package be.uclouvain.jail.algo.induct.oracle;

import java.io.IOException;
import java.io.PrintWriter;

import jline.ConsoleReader;
import be.uclouvain.jail.Jail;
import be.uclouvain.jail.algo.commons.Unable;

/**
 * Tests queries using the user, through the console.
 * 
 * @author blambeau
 */
public class ConsoleQueryTester implements IMembershipQueryTester {

	/** Sends the query to the user and waits it's answer. */
	public <T> boolean accept(MembershipQuery<T> query) {
		try {
			PrintWriter writer = Jail.instance().getConsoleWriter();
			ConsoleReader reader = Jail.instance().getConsoleReader();

			// writes the query
			writer.append("Accept query (yes, no, stop) ?\n")
			      .append(query.toString())
			      .append("\n")
			      .flush();
			
			// read result
			while (true) {
				String answer = reader.readLine().trim();
				if ("yes".equalsIgnoreCase(answer) || "y".equalsIgnoreCase(answer)) {
					return true;
				} else if ("no".equalsIgnoreCase(answer) || "n".equalsIgnoreCase(answer)) {
					return false;
				} else if ("stop".equalsIgnoreCase(answer) || "s".equalsIgnoreCase(answer)) {
					throw new Unable("Algorithm stopped by user.");
				}
			}
		} catch (IOException ex) {
			throw new Unable(ex.getMessage());
		}
	}

}
