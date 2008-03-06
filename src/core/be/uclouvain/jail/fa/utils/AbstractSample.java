package be.uclouvain.jail.fa.utils;

import be.uclouvain.jail.fa.IAlphabet;
import be.uclouvain.jail.fa.ISample;
import be.uclouvain.jail.fa.functions.FAStateKindFunction;
import be.uclouvain.jail.fa.impl.AttributeGraphFAInformer;
import be.uclouvain.jail.uinfo.IUserInfoHandler;
import be.uclouvain.jail.uinfo.UserInfoAggregator;
import be.uclouvain.jail.uinfo.UserInfoHandler;

/**
 * Provides a base for implementing samples.
 * 
 * @author blambeau
 */
public abstract class AbstractSample<L> implements ISample<L> {

	/** Alphabet to use. */
	protected IAlphabet<L> alphabet;
	
	/** UserInfoS handler. */
	protected IUserInfoHandler userInfoHandler;
	
	/** Creates a sample instance. */
	public AbstractSample(IAlphabet<L> alphabet) {
		this.alphabet = alphabet;
		this.userInfoHandler = new UserInfoHandler();
		
		UserInfoAggregator stateAggregator = userInfoHandler.getVertexAggregator();
		stateAggregator.boolOr(AttributeGraphFAInformer.STATE_INITIAL_KEY);
		stateAggregator.stateKind(AttributeGraphFAInformer.STATE_KIND_KEY,FAStateKindFunction.OR,FAStateKindFunction.OR,true);

		UserInfoAggregator edgeAggregator = userInfoHandler.getEdgeAggregator();
		edgeAggregator.first(AttributeGraphFAInformer.EDGE_LETTER_KEY);
	}

	/** Returns used handler. */
	public IUserInfoHandler getUserInfoHandler() {
		return userInfoHandler;
	}

	/** Returns the alphabet. */
	public IAlphabet<L> getAlphabet() {
		return alphabet;
	}

}
