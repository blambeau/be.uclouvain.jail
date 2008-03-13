package be.uclouvain.jail.contribs.lsmi;

import java.io.File;
import java.io.IOException;

import be.uclouvain.jail.algo.fa.rand.RandomDFAInput;
import be.uclouvain.jail.algo.fa.rand.RandomDFAResult;
import be.uclouvain.jail.algo.graph.rand.RandomGraphAlgo;
import be.uclouvain.jail.fa.IDFA;

/**
 * Generates some DFAs.
 * 
 * @author blambeau
 */
public class LSMIGenerateDFA {

	/** DFA sizes. */
	private int[] sizes = new int[]{8};
	private double[] tolerances = new double[]{0.01, 0.01, 0.05, 0.05};
	
	/** Number of DFAs of each size. */
	private int number = 1;

	/** Database to use. */
	private LSMIDatabase db;
	
	/** Creates a DFA generator based on a folder. */
	public LSMIGenerateDFA(File folder) {
		this.db = new LSMIDatabase(folder);
	}
	
	/** Generates a DFA of a given size. */
	private IDFA generate(int size, double tolerance) {
		RandomDFAInput input = new RandomDFAInput();
		input.setAccepting(1.0);
		input.setStateCount(size);
		input.setAlphabetSize(2);
		input.setDepthControl(true);
		input.setMaxTry(1000);
		input.setTolerance(tolerance);
		RandomDFAResult result = new RandomDFAResult();
		
		new RandomGraphAlgo().execute(input,result);
		return (IDFA) result.adapt(IDFA.class);
	}
	
	/** Executes generation. */
	public void execute() throws IOException {
		for (int size=0; size<sizes.length; size++) {
			for (int j=0; j<number; j++) {
				System.out.println("Generating " + j + "-th DFA: " + sizes[size] + " states.");
				IDFA dfa = generate(sizes[size],tolerances[size]);
				db.addDFA(dfa, sizes[size], j);
			}
		}
	}
	
	/** Main method. */
	public static void main(String[] args) throws IOException {
		File folder = null;
		if (args.length == 1) {
			folder = new File(args[0]);
		} else {
			folder = new File(".");
		}
		new LSMIGenerateDFA(folder).execute();
	}
	
}
