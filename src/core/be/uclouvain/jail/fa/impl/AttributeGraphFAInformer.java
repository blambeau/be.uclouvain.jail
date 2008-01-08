package be.uclouvain.jail.fa.impl;

import be.uclouvain.jail.fa.FAStateKind;
import be.uclouvain.jail.uinfo.IUserInfo;

/**
 * Provides an implementation that get FA informations in IUserInfo.
 * 
 * <p>This class internally uses three attribute keys to retrieve (in order)
 * edge letter as well as initial flag and kind of the state. The 
 * default values for these keys are "letter", "isInitial", "kind", 
 * respectively.</p>
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
	public static final String STATE_KIND_KEY = "kind";
	
	/** Mapping attribute for edge letter. */
	private String edgeLetterAttr;
	
	/** Mapping attribute for isAccepting flag. */
	private String isInitialAttr;

	/** Mapping attribute for kind attribute. */
	private String kindAttr;
	
	/** Creates an informer with default attributes. */
	public AttributeGraphFAInformer() {
		this(EDGE_LETTER_KEY,STATE_INITIAL_KEY,STATE_KIND_KEY);
	}
	
	/** Creates an informer for specific attributes. */
	public AttributeGraphFAInformer(String letter, String isInitial, String kind) {
		this.edgeLetterAttr = letter;
		this.isInitialAttr = isInitial;
		this.kindAttr = kind;
	}

	/** Returns the letter attached to an edge. */
	public Object edgeLetter(IUserInfo s) {
		Object letter = s.getAttribute(edgeLetterAttr);
		if (letter == null) {
			throw new IllegalStateException("UserInfo must contain FA edge letter.");
		}
		return letter;
	}

	/** Checks if a state is marked as initial. */
	public boolean isInitial(IUserInfo s) {
		Object attr = s.getAttribute(isInitialAttr);
		if (attr == null) {
			throw new IllegalStateException("UserInfo must contain FA isInitial flag.");
		}
		return Boolean.TRUE.equals(attr);
	}

	/** Returns state kind. */
	public FAStateKind getStateKind(IUserInfo s) {
		Object attr = s.getAttribute(kindAttr);
		FAStateKind kind = null;
		
		// 1) normal case
		if (attr instanceof FAStateKind) {
			return (FAStateKind) attr;
		}
		
		// 2) conversion from string
		if (attr instanceof String) {
			kind = FAStateKind.valueOf(attr.toString().toUpperCase());
			if (kind == null) {
				throw new IllegalStateException("Bad state kind: " + attr);
			} else {
				s.setAttribute(kindAttr, kind);
			}
			return kind;
		}
		
		// 3) backward compatibility from isError and isAccepting
		if (attr == null) {
			Object isAcceptingO = s.getAttribute("isAccepting");
			Object isErrorO = s.getAttribute("isError");
			if (isAcceptingO != null && isErrorO != null) {
				boolean isAccepting = Boolean.TRUE.equals(isAcceptingO);
				boolean isError = Boolean.TRUE.equals(isErrorO);
				kind = FAStateKind.fromBools(isAccepting, isError);
				s.setAttribute(kindAttr, kind);
				return kind;
			}
		} 
		
		throw new IllegalStateException("UserInfo must contain kind attribute.");
	}

}
