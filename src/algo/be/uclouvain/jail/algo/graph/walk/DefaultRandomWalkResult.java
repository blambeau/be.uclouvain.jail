package be.uclouvain.jail.algo.graph.walk;

import java.util.ArrayList;
import java.util.List;

import be.uclouvain.jail.algo.graph.copy.match.GMatchPopulator;
import be.uclouvain.jail.algo.utils.AbstractAlgoResult;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.IDirectedGraphPath;
import be.uclouvain.jail.graph.utils.DirectedGraphWriter;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.IUserInfoHandler;

/**
 * Provides a default implementation of {@link IRandomWalkResult}.
 * 
 * @author blambeau
 */
public class DefaultRandomWalkResult extends AbstractAlgoResult implements IRandomWalkResult {

	/** Created paths. */
	private List<IDirectedGraphPath> paths;
	
	/** Creates a result instance. */
	public DefaultRandomWalkResult() {
		IUserInfoHandler handler = super.getUserInfoHandler();
		handler.getVertexCopier().keepAll();
		handler.getEdgeCopier().keepAll();
	}
	
	/** Install options. */
	@Override
	protected void installOptions() {
		super.installOptions();
		super.addOption("vertex", "vertexPopulator", false, GMatchPopulator.class, null);
		super.addOption("edge", "edgePopulator", false, GMatchPopulator.class, null);
	}

	/** Adds a gmatch vertex populator. */
	public void setVertexPopulator(GMatchPopulator<IUserInfo> populator) {
		super.getUserInfoHandler().getVertexCopier().addPopulator(populator);
	}
	
	/** Adds a gmatch edge populator. */
	public void setEdgePopulator(GMatchPopulator<IUserInfo> populator) {
		super.getUserInfoHandler().getEdgeCopier().addPopulator(populator);
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
			DirectedGraphWriter w = new DirectedGraphWriter(getUserInfoHandler());
			for (IDirectedGraphPath path: paths) {
				flushPath(path,w);
			}
			return w.getGraph(); 
		}
		
		return super.adapt(c);
	}

}
