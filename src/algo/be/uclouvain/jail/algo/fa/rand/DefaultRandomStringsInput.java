package be.uclouvain.jail.algo.fa.rand;

import java.util.Random;

import be.uclouvain.jail.Jail;
import be.uclouvain.jail.algo.utils.AbstractAlgoInput;
import be.uclouvain.jail.common.IPredicate;
import be.uclouvain.jail.fa.IAlphabet;
import be.uclouvain.jail.fa.IString;
import be.uclouvain.jail.fa.utils.IntegerAlphabet;

/**
 * Provides a default implementation of {@link IRandomStringsInput}.
 * 
 * @author blambeau
 */
public class DefaultRandomStringsInput<L> extends AbstractAlgoInput implements IRandomStringsInput<L> {

	/** Randomizer. */
	private Random r = Jail.randomizer();
	
	/** Alphabet to use. */
	private IAlphabet<L> alphabet;

	/** Size of the words to generate. */
	private int stringLength = 10;
	
	/** Probability to stop on the current word. */
	private int stringStopProba = -1;
	
	/** Number of words to generate. */
	private int nbStrings = 100;
	
	/** Number of words to generate. */
	private int stopProba = -1;
	
	/** Creates a input instance. */
	public DefaultRandomStringsInput(IAlphabet<L> alphabet) {
		this.alphabet = alphabet;
	}

	/** Creates an empty random words input. */
	public DefaultRandomStringsInput() {
	}

	/** Installs the options. */
	@Override
	protected void installOptions() {
		super.installOptions();
		super.addOption("alphabet", false, IAlphabet.class, null);
		super.addOption("alphabetSize", false, Integer.class, null);
		super.addOption("nbStrings", false, Integer.class, null);
		super.addOption("stringLength", false, Integer.class, null);
		super.addOption("stopProba", false, Double.class, null);
		super.addOption("stringStopProba", false, Double.class, null);
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

	/** Sets stop probability. */
	public void setStopProba(int stopProba) {
		this.stopProba = stopProba;
	}

	/** Sets length of words to generate. */
	public void setWordLength(int wordLength) {
		this.stringLength = wordLength;
	}

	/** Sets stop word probability. */
	public void setStringStopProba(int stringStopProba) {
		this.stringStopProba = stringStopProba;
	}

	/** Returns stop predicate. */
	public IPredicate<IRandomStringsResult<L>> getStopPredicate() {
		return new IPredicate<IRandomStringsResult<L>>() {
			public boolean evaluate(IRandomStringsResult<L> result) {
				if (stopProba != -1) {
					return r.nextDouble() <= stopProba;
				} else {
					return result.size() >= nbStrings;
				}
			}
		};
	}

	/** Returns word stop predicate. */
	public IPredicate<IString<L>> getStringStopPredicate() {
		return new IPredicate<IString<L>>() {
			public boolean evaluate(IString<L> word) {
				if (stringStopProba != -1) {
					return r.nextDouble() <= stringStopProba;
				} else {
					return word.size() >= stringLength;
				}
			}
		};
	}

}
