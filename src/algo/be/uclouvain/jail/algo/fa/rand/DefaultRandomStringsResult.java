package be.uclouvain.jail.algo.fa.rand;

import be.uclouvain.jail.algo.utils.AbstractAlgoResult;
import be.uclouvain.jail.fa.FAStateKind;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.IFATrace;
import be.uclouvain.jail.fa.ISample;
import be.uclouvain.jail.fa.IString;
import be.uclouvain.jail.fa.IWalkInfo;
import be.uclouvain.jail.fa.impl.AttributeGraphFAInformer;
import be.uclouvain.jail.fa.utils.DefaultSample;
import be.uclouvain.jail.uinfo.IUserInfoHandler;
import be.uclouvain.jail.uinfo.UserInfoCopier;

/**
 * Provides a default implementation of IRandomWordsResult.
 * 
 * @author blambeau
 */
public class DefaultRandomStringsResult<L> extends AbstractAlgoResult implements IRandomStringsResult<L> {

	/** Sample used to keep words. */
	private ISample<L> sample;

	/** Optional DFA to use to create strings. */ 
	private IDFA labeller;
	
	/** Creates a result with a default sample. */
	public DefaultRandomStringsResult() {
		this(null);
	}
	
	/** Creates a result with a given sample to fill. */
	public DefaultRandomStringsResult(ISample<L> sample) {
		this.sample = sample;
		IUserInfoHandler handler = super.getUserInfoHandler();
		UserInfoCopier vertexCopier = handler.getVertexCopier();
		vertexCopier.keepAll();
		vertexCopier.addConstant(AttributeGraphFAInformer.STATE_INITIAL_KEY, false);
		vertexCopier.addConstant(AttributeGraphFAInformer.STATE_KIND_KEY, FAStateKind.PASSAGE);
		handler.getEdgeCopier().keepAll();
	}

	/** Installs the options. */
	@Override
	protected void installOptions() {
		super.installOptions();
		super.addOption("sample", false, ISample.class, null);
		super.addOption("labeller", false, IDFA.class, null);
		super.addOption("labeler", false, IDFA.class, null);
	}

	/** Sets sample to fill. */
	public void setSample(ISample<L> sample) {
		this.sample = sample;
	}

	/** Sets labeller to use. */
	public void setLabeller(IDFA labeller) {
		this.labeller = labeller;
	}
	
	/** Sets labeller to use. */
	public void setLabeler(IDFA labeller) {
		this.labeller = labeller;
	}
	
	/** Fired when algo is started. */
	public void started(IRandomStringsInput<L> input) {
		if (sample == null) {
			sample = new DefaultSample<L>(input.getAlphabet());
		}
	}
	
	/** Fired when algo is ended. */
	public void ended() {
	}
	
	/** Returns size. */
	public int size() {
		return sample.size();
	}
	
	/** Adds a word. */
	@SuppressWarnings("unchecked")
	public void addString(IString<L> word) {
		IString<L> str = word;
		if (labeller != null) {
			IWalkInfo<L> walk = word.walk(labeller);
			IFATrace<L> normalized = walk.normalize(getUserInfoHandler());
			str = (IString<L>) normalized.adapt(IString.class);
		} 
		sample.addString(str);
	}

	/** Provides adaptations. */
	public <T> Object adapt(Class<T> c) {
		if (c.isAssignableFrom(getClass())) {
			return this;
		} else if (ISample.class.equals(c)) {
			return sample;
		} else {
			return sample.adapt(c);
		}
	}

}
