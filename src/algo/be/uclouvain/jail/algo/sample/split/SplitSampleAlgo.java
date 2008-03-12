package be.uclouvain.jail.algo.sample.split;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import be.uclouvain.jail.Jail;
import be.uclouvain.jail.algo.utils.AbstractAlgoInput;
import be.uclouvain.jail.fa.ISample;
import be.uclouvain.jail.fa.IString;
import be.uclouvain.jail.fa.utils.DefaultString;

public class SplitSampleAlgo {

	/** Input. */
	public static class Input<T> extends AbstractAlgoInput {
		
		/** Input sample. */
		private ISample<T> sample;
		
		/** Proportion in the first sample. */
		private double proportion = 0.5;

		/** Creates an input instance. */
		public Input(ISample<T> sample) {
			this.sample = sample;
		}

		/** Installs the options. */
		@Override
		protected void installOptions() {
			super.installOptions();
			super.addOption("proportion",false,Double.class,null);
		}

		/** Returns propotion. */
		public double getProportion() {
			return proportion;
		}

		/** Sets propertion. */
		public void setProportion(double proportion) {
			this.proportion = proportion;
		}

		/** Returns input sample. */
		public ISample<T> getSample() {
			return sample;
		}
		
	}
	
	/** Input. */
	public static class Output<T> {
		
		/** First sample. */
		private ISample<T> first;
		
		/** Second sample. */
		private ISample<T> second;
		
		/** Creates an output instance. */
		public Output(ISample<T> first, ISample<T> second) {
			this.first = first;
			this.second = second;
		}
		
		/** Creates an output instance without second set. */
		public Output(ISample<T> first) {
			this.first = first;
		}

		/** Adds a string in the first sample. */
		public void addString(IString<T> s, boolean first) {
			if (first) {
				this.first.addString(s);
			} else if (second != null) {
				this.second.addString(s);
			}
		}
		
		/** Returns first sample. */
		public ISample<T> getFirstSample() {
			return first;
		}
		
		/** Returns second sample. */
		public ISample<T> getSecondSample() {
			return second;
		}
		
	}
	
	/** Randomizer. */
	private Random r = Jail.randomizer();
	
	/** Copies a string. */
	private <T> IString<T> copy(IString<T> s) {
		List<T> letters = new ArrayList<T>();
		for (T t: s) {
			letters.add(t);
		}
		return new DefaultString<T>(s.getAlphabet(),letters,s.isPositive());
	}
	
	/** Executes splitting. */
	public <T> void execute(Input<T> input, Output<T> output) {
		ISample<T> sample = input.getSample();
		double prop = input.getProportion();
		for (IString<T> s: sample) {
			s = copy(s);
			double proba = r.nextDouble();
			if (proba <= prop) {
				output.addString(s, true);
			} else {
				output.addString(s, false);
			}
		}
	}
	
}
