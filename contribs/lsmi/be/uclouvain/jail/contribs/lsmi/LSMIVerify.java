package be.uclouvain.jail.contribs.lsmi;

import java.io.File;
import java.io.IOException;

/** 
 * Checks results of LSMI tests.
 * 
 * @author blambeau
 */
public class LSMIVerify {

	/** Database to use. */
	private LSMIDatabase db;
	
	/** Creates a DFA generator based on a folder. */
	public LSMIVerify(File folder) {
		this.db = new LSMIDatabase(folder);
	}
	
	/** Executes verification. */
	public void execute() {
		
	}
	
	/** Main method. */
	public static void main(String[] args) throws IOException {
		File folder = null;
		if (args.length == 1) {
			folder = new File(args[0]);
		} else {
			folder = new File(".");
		}
		new LSMIVerify(folder).execute();
	}
	
	
	
}
