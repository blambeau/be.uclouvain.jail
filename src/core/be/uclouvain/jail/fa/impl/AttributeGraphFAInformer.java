package be.uclouvain.jail.fa.impl;

import be.uclouvain.jail.uinfo.IUserInfo;

/**
 * Provides an implementation that get FA informations in IUserInfo.
 * 
 * <p>This class internally uses four attribute keys to retrieve (in order)
 * edge letter as well as initial, accepting and error state flags. The 
 * default values for these keys are "letter", "isInitial", "isAccepting"
 * and "isError", respectively.</p>
 * 
 * <p>Please note that this informer throws IllegalStateExceptionS when the
 * attribute values cannot be found inside state and edge IUserInfoS.</p>
 * 
 * @author blambeau
 */
public class AttributeGraphFAInformer implements IGraphFAInformer {

	/** Edge letter key. */
	public static final String EDGE_LETTER_KEY = "letter";
	
	/** Edge letter key. */
	public static final String STATE_INITIAL_KEY = "isInitial";
	
	/** Edge letter key. */
	public static final String STATE_ACCEPTING_KEY = "isAccepting";
	
	/** Edge letter key. */
	public static final String STATE_ERROR_KEY = "isError";
	
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
		this(EDGE_LETTER_KEY,STATE_INITIAL_KEY,STATE_ACCEPTING_KEY,STATE_ERROR_KEY);
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
