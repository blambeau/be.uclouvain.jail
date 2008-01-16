package be.uclouvain.jail.fa;

/**
 * Extensible sample, which allows adding strings.
 * 
 * @author blambeau
 * @param <L>
 */
public interface IExtensibleSample<L> extends ISample<L> {

	/** Adds a sample string. */
	public void addString(IString<L> string);

}
