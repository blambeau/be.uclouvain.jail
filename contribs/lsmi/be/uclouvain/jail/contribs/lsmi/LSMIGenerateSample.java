package be.uclouvain.jail.contribs.lsmi;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import net.chefbe.javautils.adapt.AdaptUtils;
import net.chefbe.javautils.adapt.IAdapter;
import be.uclouvain.jail.algo.commons.Unable;
import be.uclouvain.jail.algo.fa.rand.AbadingoRandomStringsInput;
import be.uclouvain.jail.algo.fa.rand.DefaultRandomStringsResult;
import be.uclouvain.jail.algo.fa.rand.RandomStringsAlgo;
import be.uclouvain.jail.fa.IAlphabet;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.ISample;
import be.uclouvain.jail.fa.impl.GraphDFA;
import be.uclouvain.jail.fa.utils.DefaultSample;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.vm.toolkits.InductionFacade;

/**
 * Generate samples from DFAs. 
 * 
 * @author blambeau
 */
public class LSMIGenerateSample {

	/** Number of samples of each size. */
	private int number = 1;

	/** Database to use. */
	private LSMIDatabase db;
	
	/** Creates a DFA generator based on a folder. */
	public LSMIGenerateSample(File folder) {
		this.db = new LSMIDatabase(folder);
		
		AdaptUtils.register(IDirectedGraph.class, IDFA.class, new IAdapter() {
			public Object adapt(Object who, Class<?> type) {
				return new GraphDFA((IDirectedGraph)who);
			}
		});
	}
	
	/** Generates a sample. */
	private ISample<String> generate(IDFA dfa) {
		int stcount = db.getRequestedSize(dfa);
		
		// create input and set options
		IAlphabet<String> alphabet = dfa.getAlphabet();
		AbadingoRandomStringsInput<String> input = new AbadingoRandomStringsInput<String>(); 
		input.setAlphabet(alphabet);
		input.setMaxTry(100000);
		
		// 2*n^2 strings
		input.setNbStrings(stcount*stcount);

		// length of the strings : 2*log(n)+3
		// where n is the number of states and log is in base 2
		long depth = Math.round(2*(Math.log(stcount)/Math.log(2)) + 3);
		input.setWordLength(new Long(depth).intValue());

		// create sample recipient 
		ISample<String> sample = new DefaultSample<String>(alphabet);
		
		// create algo result and set options
		DefaultRandomStringsResult<String> result = new DefaultRandomStringsResult<String>(sample);
		result.setLabeler(dfa);
		
		// execute algorithm and return sample
		new RandomStringsAlgo().execute(input,result);
		return sample;
	}

	/** Executes generation. */
	private void execute() throws IOException {
		// for each DFA
		Iterator<IDFA> dfas = db.getAllDFAs();
		while (dfas.hasNext()) {
			IDFA dfa = dfas.next();

			// generate number samples
			for (int i=0; i<number; i++) {
				System.out.print("Generating " + i + "-th sample for " + 
						db.getAttribute(dfa,"DFA_size") + " " + 
						db.getAttribute(dfa,"DFA_lid") + " ...");
				
				// generate the sample
				ISample<String> sample = null;
				try { sample = generate(dfa); }
				catch (Unable ex) { 
					System.out.println(" failed!"); 
					continue;
				}
				System.out.println(" ok.");
				
				// split it
				List<ISample<String>> samples = InductionFacade.split(sample,0.5);
				
				// add sample to database
				db.addSample(dfa, samples.get(0), samples.get(1), i);
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
		new LSMIGenerateSample(folder).execute();
	}
	
}
