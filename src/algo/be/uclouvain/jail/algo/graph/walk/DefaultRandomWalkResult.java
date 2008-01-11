package be.uclouvain.jail.algo.graph.walk;

import java.util.ArrayList;
import java.util.List;

import be.uclouvain.jail.algo.graph.copy.match.GMatchPopulator;
import be.uclouvain.jail.algo.utils.AbstractAlgoResult;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.IDirectedGraphPath;
import be.uclouvain.jail.graph.adjacency.AdjacencyDirectedGraph;
import be.uclouvain.jail.graph.utils.DirectedGraphWriter;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.UserInfoCopier;

/**
 * Provides a default implementation of {@link IRandomWalkResult}.
 * 
 * @author blambeau
 */
public class DefaultRandomWalkResult extends AbstractAlgoResult implements IRandomWalkResult {

	/** Created paths. */
	private List<IDirectedGraphPath> paths;
	
	/** Copier to use for vertices. */
	private UserInfoCopier vertexCopier;
	
	/** Copier to use for edges. */
	private UserInfoCopier edgeCopier;

	/** Creates a result instance. */
	public DefaultRandomWalkResult() {
		vertexCopier = new UserInfoCopier();
		vertexCopier.keepAll();
		edgeCopier = new UserInfoCopier();
		edgeCopier.keepAll();
	}
	
	/** Install options. */
	@Override
	protected void installOptions() {
		super.installOptions();
		super.addOption("vertex", "vertexPopulator", false, GMatchPopulator.class, null);
		super.addOption("edge", "edgePopulator", false, GMatchPopulator.class, null);
	}

	/** Returns the vertex copier. */
	public UserInfoCopier getVertexCopier() {
		return vertexCopier;
	}
	
	/** Adds a gmatch vertex populator. */
	public void setVertexPopulator(GMatchPopulator<IUserInfo> populator) {
		vertexCopier.addPopulator(populator);
	}
	
	/** Returns the edge copier. */
	public UserInfoCopier getEdgeCopier() {
		return edgeCopier;
	}
	
	/** Adds a gmatch edge populator. */
	public void setEdgePopulator(GMatchPopulator<IUserInfo> populator) {
		edgeCopier.addPopulator(populator);
	}
	
	/** Fired when algo is started. */ 
	public void started(IRandomWalkInput input) {
		paths = new ArrayList<IDirectedGraphPath>();
	}
	
	/** Fired when algo ends. */
	public void ended(IRandomWalkInput input) {
	}
	
	/** Returns number of generated paths. */
	public int size() {
		return paths.size();
	}

	/** Adds a walk path. */
	public void addWalkPath(IDirectedGraphPath path) {
		paths.add(path);
	}

	/** Flushes a path inside the writer. */
	protected void flushPath(IDirectedGraphPath path, DirectedGraphWriter writer) {
		path.flush(writer);
	}
	
	/** Provides adaptations. */
	public <T> Object adapt(Class<T> c) {
		if (c.isAssignableFrom(getClass())) {
			return this;
		}
		
		// adaptation to a graph by flush
		if (IDirectedGraph.class.equals(c)) {
			IDirectedGraph g = new AdjacencyDirectedGraph();
			DirectedGraphWriter w = new DirectedGraphWriter(g);
			w.setVertexCopier(vertexCopier);
			w.setEdgeCopier(edgeCopier);
			
			for (IDirectedGraphPath path: paths) {
				flushPath(path,w);
			}
			return g; 
		}
		
		return super.adapt(c);
	}

}
