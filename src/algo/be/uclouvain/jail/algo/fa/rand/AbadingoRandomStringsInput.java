package be.uclouvain.jail.algo.fa.rand;

import java.util.Random;

import be.uclouvain.jail.Jail;
import be.uclouvain.jail.algo.utils.AbstractAlgoInput;
import be.uclouvain.jail.common.IPredicate;
import be.uclouvain.jail.fa.IAlphabet;
import be.uclouvain.jail.fa.IString;
import be.uclouvain.jail.fa.utils.IntegerAlphabet;

/**
 * Provides an implementation for Abadingo heuristic to string generation.
 * 
 * @author blambeau
 */
public class AbadingoRandomStringsInput<L> extends AbstractAlgoInput implements IRandomStringsInput<L> {

	/** Randomizer. */
	private Random r = Jail.randomizer();
	
	/** Alphabet to use. */
	private IAlphabet<L> alphabet;

	/** Size of the words to generate. */
	private int wordLength = 10;
	
	/** Number of words to generate. */
	private int nbStrings = 100;
	
	/** Creates a input instance. */
	public AbadingoRandomStringsInput(IAlphabet<L> alphabet) {
		this.alphabet = alphabet;
	}

	/** Creates an empty random words input. */
	public AbadingoRandomStringsInput() {
	}

	/** Installs the options. */
	@Override
	protected void installOptions() {
		super.installOptions();
		super.addOption("alphabet", false, IAlphabet.class, null);
		super.addOption("alphabetSize", false, Integer.class, null);
		super.addOption("nbStrings", false, Integer.class, null);
		super.addOption("wordLength", false, Integer.class, null);
	}

	/** Returns the alphabet to use. */
	public IAlphabet<L> getAlphabet() {
		if (alphabet == null) {
			throw new IllegalStateException("At least :alphabetSize should be set.");
		}
		return alphabet;
	}

	/** Sets the alphabet. */
	public void setAlphabet(IAlphabet<L> alphabet) {
		this.alphabet = alphabet;
	}

	/** Sets the alphabet size. */
	@SuppressWarnings("unchecked")
	public void setAlphabetSize(int size) {
		alphabet = (IAlphabet<L>) new IntegerAlphabet(size);
	}

	/** Sets number of words to generate. */
	public void setNbStrings(int nbStrings) {
		this.nbStrings = nbStrings;
	}

	/** Sets length of words to generate. */
	public void setWordLength(int k) {
		this.wordLength = k;
		
		// number of strings is 2^(k+1)-1
		this.nbStringsOfKLength = Math.round(Math.pow(2,k+1)-1);
		
		// compute the log only once
		this.logNbStringsOfKLength = log2(this.nbStringsOfKLength);
	}

	/** Returns stop predicate. */
	public IPredicate<IRandomStringsResult<L>> getStopPredicate() {
		return new IPredicate<IRandomStringsResult<L>>() {
			public boolean evaluate(IRandomStringsResult<L> result) {
				return result.size() >= nbStrings;
			}
		};
	}

	/** Number of strings of k length. */
	private long nbStringsOfKLength;
	private double logNbStringsOfKLength;
	private long nextLength = -1; 

	/** Returns log(x;2), that is log in base 2. */
	private static double log2(double x) {
		return Math.log(x)/Math.log(2);
	}
	
	/** Returns word stop predicate. */
	public IPredicate<IString<L>> getStringStopPredicate() {
		return new IPredicate<IString<L>>() {
			public boolean evaluate(IString<L> word) {
				if (nextLength == -1) {
					double proba = r.nextDouble()/2;
					assert (proba >= 0.0 && proba <= 0.5) : "Valid proba.";
					nextLength = Math.round(log2(proba) + logNbStringsOfKLength);
					assert (nextLength >= 0 && nextLength <= wordLength) : "Valid length " + nextLength + " according to formula.";
				}
				boolean stop = word.size() >= nextLength;
				if (stop) { nextLength = -1; }
				return stop;
			}
		};
	}
	
}
