package be.uclouvain.jail.graph.adjacency;

import be.uclouvain.jail.uinfo.IUserInfo;

/**
 * <p>Factory contract to create DirectedGraphImpl states and edges. The directed graph 
 * implementation uses such a factory when states and edges must be created. It provides
 * simple way to use user-defined states and edges (which can be very useful to keep
 * high level efficient methods).</p>  
 * 
 * @author LAMBEAU Bernard
 */
public interface IGraphComponentFactory {

  /** Creates and returns a graph vertex. */
  public IVertex createVertex(IUserInfo info);
  
  /** Creates and returns a graph edge. */
  public IEdge createEdge(IUserInfo info);
  
}
