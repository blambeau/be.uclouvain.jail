package be.uclouvain.jail.dialect.utils;

import be.uclouvain.jail.dialect.IGraphDialect;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.IUserInfoCreator;
import be.uclouvain.jail.uinfo.IUserInfoRewriters;

/**
 * Provides base implementation for graph dialects.
 * 
 * @author blambeau
 */
public abstract class AbstractGraphDialect implements IGraphDialect {

	/** Helper to use on load. */
	protected IUserInfoRewriters loadRewriters;

	/** Helper to use on save. */
	protected IUserInfoRewriters printRewriters;

	/** Applies a specific rewriter. */
	private IUserInfo applyRewriter(IUserInfoCreator<IUserInfo> rewriter, IUserInfo info) {
		if (rewriter == null) { return info; }
		return rewriter.create(info);
	}
	
	/** Returns the load rewriters. */
	public IUserInfoRewriters getLoadRewriters() {
		return loadRewriters;
	}
	
	/** Sets the rewriters to use to create user infos at graph load. */
	public void setLoadRewriters(IUserInfoRewriters rewriters) {
		this.loadRewriters = rewriters;
	}

	/** Reloads a graph user info by rewriting. */ 
	public IUserInfo reloadGraphInfo(IUserInfo info) {
		if (loadRewriters == null) { return info; }
		return applyRewriter(loadRewriters.getGraphRewriter(),info);
	}
	
	/** Reloads a graph user info by rewriting. */ 
	public IUserInfo reloadVertexInfo(IUserInfo info) {
		if (loadRewriters == null) { return info; }
		return applyRewriter(loadRewriters.getVertexRewriter(),info);
	}
	
	/** Reloads a graph user info by rewriting. */ 
	public IUserInfo reloadEdgeInfo(IUserInfo info) {
		if (loadRewriters == null) { return info; }
		return applyRewriter(loadRewriters.getEdgeRewriter(),info);
	}
	
	/** Returns the load rewriters. */
	public IUserInfoRewriters getPrintRewriters() {
		return printRewriters;
	}
	
	/** Sets the rewriters to apply to user infos before printing. */
	public void setPrintRewriters(IUserInfoRewriters rewriters) {
		this.printRewriters = rewriters;
	}
	
	/** Preprints a graph user info by rewriting. */ 
	public IUserInfo preprintGraphInfo(IUserInfo info) {
		if (printRewriters == null) { return info; }
		return applyRewriter(printRewriters.getGraphRewriter(),info);
	}
	
	/** Preprints a graph user info by rewriting. */ 
	public IUserInfo preprintVertexInfo(IUserInfo info) {
		if (printRewriters == null) { return info; }
		return applyRewriter(printRewriters.getVertexRewriter(),info);
	}
	
	/** Preprints a graph user info by rewriting. */ 
	public IUserInfo preprintEdgeInfo(IUserInfo info) {
		if (printRewriters == null) { return info; }
		return applyRewriter(printRewriters.getEdgeRewriter(),info);
	}

}
