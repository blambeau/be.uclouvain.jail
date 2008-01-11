package be.uclouvain.jail.algo.graph.walk;

import java.util.Random;

import be.uclouvain.jail.algo.utils.AbstractAlgoInput;
import be.uclouvain.jail.common.IPredicate;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.IDirectedGraphPath;
import be.uclouvain.jail.graph.utils.ITotalOrder;

/**
 * Provides a base implementation of {@link IRandomWalkInput}.
 * 
 * @author blambeau
 */
public class DefaultRandomWalkInput extends AbstractAlgoInput implements IRandomWalkInput {

	/** Input graph. */
	protected IDirectedGraph graph;

	/** Choose root at random? */
	protected boolean chooseRoot = true;
	
	/** Number of generated paths. */
	protected int pathCount = 100;

	/** Stopping walk probability. */
	protected double walkStopProba = -1;
	
	/** Size of generated paths. */
	protected int pathLength = 10;

	/** Stopping path probability. */
	protected double pathStopProba = -1;
	
	/** Vertices cache (when random). */
	protected ITotalOrder<Object> vertices;
	
	/** Root vertex cache (when not random). */
	protected Object root;
	
	/** Creates an input instance. */
	public DefaultRandomWalkInput(IDirectedGraph graph) {
		this.graph = graph;
	}

	/** Installs the options. */
	@Override
	protected void installOptions() {
		super.installOptions();
		super.addOption("chooseRoot", false, Boolean.class, null);
		super.addOption("pathCount", false, Integer.class, null);
		super.addOption("walkStopProba", false, Double.class, null);
		super.addOption("pathLength", false, Integer.class, null);
		super.addOption("pathStopProba", false, Double.class, null);
	}

	/** Choose root randomly? */
	public void setChooseRoot(boolean chooseRoot) {
		this.chooseRoot = chooseRoot;
	}

	/** Sets the number of paths to generate. */
	public void setPathCount(int pathCount) {
		this.pathCount = pathCount;
	}

	/** Returns path length. */
	public int getPathLength() {
		return pathLength;
	}

	/** Sets the length of the path to use. */
	public void setPathLength(int pathLength) {
		this.pathLength = pathLength;
	}

	/** Sets the probability to stop a path. */
	public void setPathStopProba(double pathStopProba) {
		this.pathStopProba = pathStopProba;
	}

	/** Sets the probability to stop the walk. */
	public void setWalkStopProba(double walkStopProba) {
		this.walkStopProba = walkStopProba;
	}
	
	/** Returns true if end of path is on length. */
	public boolean isStopOnPathLength() {
		return pathStopProba == -1;
	}

	/** Returns input graph. */
	public IDirectedGraph getInputGraph() {
		return graph;
	}
	
	/** Chooses a random vertex randomly. */
	public Object chooseRootVertex(IDirectedGraph g, Random r) {
		if (chooseRoot) {
			if (vertices == null) { vertices = g.getVerticesTotalOrder(); }
			int size = vertices.size();
			return vertices.getElementAt(r.nextInt(size));
		} 
		
		if (root == null) {
			root = g.getVerticesTotalOrder().getElementAt(0);
		}
		return root;
	}
	
	/** Returns walk stop predicate. */
	public IPredicate<IRandomWalkResult> getWalkStopPredicate(final Random r) {
		return new IPredicate<IRandomWalkResult>() {
			public boolean evaluate(IRandomWalkResult result) {
				if (walkStopProba != -1) {
					return r.nextDouble()<=walkStopProba;
				} else {
					return result.size() == pathCount;
				}
			}
		};
	}

	/** Returns path stop predicate. */
	public IPredicate<IDirectedGraphPath> getPathStopPredicate(final Random r) {
		return new IPredicate<IDirectedGraphPath>() {
			public boolean evaluate(IDirectedGraphPath path) {
				if (pathStopProba != -1) {
					return r.nextDouble()<=pathStopProba;
				} else {
					return path.size()-1 == pathLength;
				}
			}
		};
	}

}
