package be.uclouvain.jail.algo.fa.rand;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import be.uclouvain.jail.Jail;
import be.uclouvain.jail.common.IPredicate;
import be.uclouvain.jail.fa.IAlphabet;
import be.uclouvain.jail.fa.IString;
import be.uclouvain.jail.fa.utils.DefaultString;
import be.uclouvain.jail.graph.utils.ITotalOrder;

/**
 * Generates strings randomly.
 * 
 * @author blambeau
 */
public class RandomStringsAlgo {

	/** Randomizer. */
	private Random r = Jail.randomizer();
	
	/** Executes the algorithm. */
	public <L> void execute(IRandomStringsInput<L> input, IRandomStringsResult<L> result) {
		result.started(input);
		
		// get letters and alphabet size
		IAlphabet<L> alphabet = input.getAlphabet();
		ITotalOrder<L> letters = alphabet.getLetters();
		int size = letters.size();

		// get the two predicates
		IPredicate<IRandomStringsResult<L>> stop = input.getStopPredicate();
		IPredicate<IString<L>> wordStop = input.getStringStopPredicate();
		
		// generate words until stop
		while (!stop.evaluate(result)) {
			List<L> wordLetters = new ArrayList<L>();
			DefaultString<L> word = new DefaultString<L>(alphabet, wordLetters, true);
			
			// generate word until end
			while (!wordStop.evaluate(word)) {
				L letter = letters.getElementAt(r.nextInt(size));
				wordLetters.add(letter);
			}
			
			// push word in result
			result.addString(word);
		}
		
		result.ended();
	}
	
}
