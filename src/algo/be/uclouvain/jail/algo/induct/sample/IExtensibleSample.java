package be.uclouvain.jail.algo.induct.sample;

/**
 * Extensible sample, which allows adding strings.
 * 
 * @author blambeau
 * @param <L>
 */
public interface IExtensibleSample<L> extends ISample<L> {

	/** Adds a sample string. */
	public void addSampleString(ISampleString<L> string);

}
