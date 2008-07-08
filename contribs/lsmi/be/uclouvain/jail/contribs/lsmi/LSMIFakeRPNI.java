package be.uclouvain.jail.contribs.lsmi;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import be.uclouvain.jail.algo.lsm.LSMAlgo;
import be.uclouvain.jail.algo.lsm.LSMAlgoInput;
import be.uclouvain.jail.algo.lsm.LSMAlgoResult;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.ISample;
import be.uclouvain.jail.fa.IString;
import be.uclouvain.jail.fa.IWalkInfo;
import be.uclouvain.jail.fa.utils.DefaultSample;
import be.uclouvain.jail.vm.toolkits.AutomatonFacade;
import be.uclouvain.jail.vm.toolkits.GraphFacade;
import be.uclouvain.jail.vm.toolkits.InductionFacade;

/**
 * Executes all algorithms.
 * 
 * @author blambeau
 */
public class LSMIFakeRPNI {

	/** Database to use. */
	private LSMIDatabase db;
	
	/** Sample proportions. */
	private double[] sprops = new double[]{0.03, 0.0625, 0.125, 0.25, 0.5, 1.0};
	
	/** Creates a DFA generator based on a folder. */
	public LSMIFakeRPNI(File folder) {
		this.db = new LSMIDatabase(folder);
	}

	/** Checks that a result is valid according to a sample. */
	public void assertValidResult(String algo, ISample<?> sample, IDFA result) {
		try {
			for (IString<?> s: sample) {
				IWalkInfo<?> info = s.walk(result);
				boolean accepted = info.isFullyIncluded() && info.getIncludedPart().isAccepted();
				boolean positive = s.isPositive();
				assert (accepted == positive) : "Valid only when accepted iif positive";
			}
		} catch (AssertionError e) {
			db.addFailure(algo, sample, e);
		}
	}
	
	/** Tests RPNI with LSMI extension. */
	private IDFA lsm(ISample<String> sample, double sprop) {
		sample = allaccept(sample);
		IDFA pta = (IDFA) sample.adapt(IDFA.class);
		
		LSMAlgoInput input = new LSMAlgoInput(pta);
		LSMAlgoResult result = new LSMAlgoResult();

		// execute and log time
		long t1 = System.currentTimeMillis();
		new LSMAlgo().execute(input,result);
		long t2 = System.currentTimeMillis();
		
		IDFA target = result.resultDFA();
		target = AutomatonFacade.uncomplement(target);
		
		assertValidResult("lsm",sample,target);
		
		// set attributes
		db.setAttribute(target,"INDUCT_algo","lsm");
		db.setAttribute(target, "INDUCT_sprop", sprop);
		db.setAttribute(target, "INDUCT_slid", 0);
		db.setAttribute(target, "INDUCT_lprop", 0.0);
		db.setAttribute(target, "INDUCT_llid", 0);
		db.setAttribute(target, "INDUCT_time", t2-t1);
		return target;
	}

	/** Copies a sample. */
	private ISample<String> allaccept(ISample<String> s) {
		IDFA dfa = (IDFA) s.adapt(IDFA.class);
		return new DefaultSample<String>(AutomatonFacade.allaccepting(dfa));
	}
	
	/** Executes the algorithms on a sample. */
	private void execute(IDFA target, ISample<String> sample) throws IOException {
		GraphFacade.identify(target.getGraph(),"id",null);
		
		long total = (sprops.length);
		long count = 1;
		
		// for each proportion
		for (double sprop: sprops) {
				// choose some strings
				ISample<String> chosen = InductionFacade.choose(sample,sprop);

				// execute rpni with lsmi extension
				{
					System.out.println((count++) + "/" + total + ": lSMIed RPNI on " + db.toString(target) 
							         + " with props " + sprop + "/");
					try {
						IDFA result = lsm(chosen,sprop);
						db.addResult(target, sample, result);
					} catch (Exception ex) {
						db.addFailure("lsm",chosen, ex);
					}  catch (AssertionError ex) {
						db.addFailure("lsm",chosen, ex);
					}
				}
		}
	}

	/** Execute all algorithms. */
	private void execute() throws IOException {
		// for each DFA
		Iterator<IDFA> dfas = db.getAllDFAs();
		while (dfas.hasNext()) {
			IDFA dfa = dfas.next();
			
			// for each sample on this dfa
			Iterator<ISample<String>> samples = db.getSamples(dfa);
			while (samples.hasNext()) {
				ISample<String> sample = samples.next();
				execute(dfa,sample);
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
		new LSMIFakeRPNI(folder).execute();
	}
	
}
