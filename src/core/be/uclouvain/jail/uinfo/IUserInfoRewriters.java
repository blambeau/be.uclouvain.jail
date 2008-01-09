package be.uclouvain.jail.uinfo;

/**
 * Provides rewriting of user infos.
 * 
 * @author blambeau
 */
public interface IUserInfoRewriters {

	/** Returns the graph rewriter to use. */
	public IUserInfoCreator<IUserInfo> getGraphRewriter();
	
	/** Rewrites a graph info. */
	public IUserInfo rewriteGraphInfo(IUserInfo info);
	
	/** Returns the vertex rewriter to use. */
	public IUserInfoCreator<IUserInfo> getVertexRewriter();
	
	/** Rewrites a vertex info. */
	public IUserInfo rewriteVertexInfo(IUserInfo info);
	
	/** Returns the edge rewriter to use. */
	public IUserInfoCreator<IUserInfo> getEdgeRewriter();
	
	/** Rewrites an edge info. */
	public IUserInfo rewriteEdgeInfo(IUserInfo info);
	
}
