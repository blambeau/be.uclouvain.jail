package be.uclouvain.jail.vm.toolkits;

import java.util.ArrayList;
import java.util.List;

import be.uclouvain.jail.algo.sample.split.SplitSampleAlgo;
import be.uclouvain.jail.fa.IAlphabet;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.ISample;
import be.uclouvain.jail.fa.utils.DefaultSample;

/**
 * Provides a facade on induction algorithms.
 * 
 * @author blambeau
 */
public class InductionFacade {

	/** Copies a sample. */
	public static <T> ISample<T> copy(ISample<T> sample) {
		IDFA dfa = (IDFA) sample.adapt(IDFA.class);
		IDFA copy = AutomatonFacade.copy(dfa,null,null);
		return new DefaultSample<T>(copy);
	}
	
	/** Splits a sample into two parts. */
	public static <T> List<ISample<T>> split(ISample<T> sample, double proportion) {
		IAlphabet<T> alphabet = sample.getAlphabet();
		
		// create input
		SplitSampleAlgo.Input<T> input = new SplitSampleAlgo.Input<T>(sample);
		input.setProportion(proportion);
		
		// create output
		ISample<T> in = new DefaultSample<T>(alphabet);
		ISample<T> out = new DefaultSample<T>(alphabet);
		SplitSampleAlgo.Output<T> output = new SplitSampleAlgo.Output<T>(in,out);
		
		// execute
		new SplitSampleAlgo().execute(input, output);
		
		// prepate result
		List<ISample<T>> samples = new ArrayList<ISample<T>>();
		samples.add(in);
		samples.add(out);
		return samples;
	}
	
	/** Chooses some strings from a sample. */
	public static <T> ISample<T> choose(ISample<T> sample, double proportion) {
		IAlphabet<T> alphabet = sample.getAlphabet();
		
		// create input
		SplitSampleAlgo.Input<T> input = new SplitSampleAlgo.Input<T>(sample);
		input.setProportion(proportion);
		
		// create output
		ISample<T> in = new DefaultSample<T>(alphabet);
		SplitSampleAlgo.Output<T> output = new SplitSampleAlgo.Output<T>(in);
		
		// execute
		new SplitSampleAlgo().execute(input, output);
		
		// prepate result
		return in;
	}
	
}
