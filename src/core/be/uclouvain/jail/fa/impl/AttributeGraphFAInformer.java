package be.uclouvain.jail.fa.impl;

import be.uclouvain.jail.uinfo.IUserInfo;

/**
 * Provides an implementation that get FA informations in IUserInfo.
 *  
 * @author blambeau
 */
public class AttributeGraphFAInformer implements IGraphFAInformer {

	/** Mapping attribute for edge letter. */
	private String edgeLetterAttr;
	
	/** Mapping attribute for isAccepting flag. */
	private String isAcceptingAttr;
	
	/** Mapping attribute for isAccepting flag. */
	private String isErrorAttr;
	
	/** Mapping attribute for isAccepting flag. */
	private String isInitialAttr;

	/** Creates an informer with default attributes. */
	public AttributeGraphFAInformer() {
		this("letter","isInitial","isAccepting","isError");
	}
	
	/** Creates an informer for specific attributes. */
	public AttributeGraphFAInformer(String letter, String isInitial, String isAccepting, String isError) {
		this.edgeLetterAttr = letter;
		this.isInitialAttr = isInitial;
		this.isAcceptingAttr = isAccepting;
		this.isErrorAttr = isError;
	}

	/** Returns the letter attached to an edge. */
	public Object edgeLetter(IUserInfo s) {
		Object letter = s.getAttribute(edgeLetterAttr);
		if (letter == null) {
			throw new IllegalStateException("UserInfo must contain FA edge letter.");
		}
		return letter;
	}

	/** Checks if a state is marked as accepting. */
	public boolean isAccepting(IUserInfo s) {
		Object attr = s.getAttribute(isAcceptingAttr);
		if (attr == null) {
			throw new IllegalStateException("UserInfo must contain FA isAccepting flag.");
		}
		return Boolean.TRUE.equals(attr);
	}

	/** Checks if a state is marked as error. */
	public boolean isError(IUserInfo s) {
		Object attr = s.getAttribute(isErrorAttr);
		if (attr == null) {
			throw new IllegalStateException("UserInfo must contain FA isError flag.");
		}
		return Boolean.TRUE.equals(attr);
	}

	/** Checks if a state is marked as initial. */
	public boolean isInitial(IUserInfo s) {
		Object attr = s.getAttribute(isInitialAttr);
		if (attr == null) {
			throw new IllegalStateException("UserInfo must contain FA isInitial flag.");
		}
		return Boolean.TRUE.equals(attr);
	}

}
