package be.uclouvain.jail.algo.graph.walk;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import be.uclouvain.jail.Jail;
import be.uclouvain.jail.common.IPredicate;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.IDirectedGraphPath;
import be.uclouvain.jail.graph.utils.DefaultDirectedGraphPath;

/**
 * Randomly walks a graph.
 * 
 * @author blambeau
 */
public class RandomWalkAlgo {

	/** Randomizer. */
	private Random r = Jail.randomizer();
	
	/** Walked graph. */
	private IDirectedGraph graph;
	
	/** Stop walk predicate. */
	private IPredicate<IRandomWalkResult> stop;
	
	/** Path stop predicate to use. */
	private IPredicate<IDirectedGraphPath> pathStop;
	
	/** Executes the algorithm. */
	public void execute(IRandomWalkInput input, IRandomWalkResult result) {
		graph = input.getInputGraph();
		stop = input.getWalkStopPredicate(r);
		pathStop = input.getPathStopPredicate(r);
		
		result.started(input);
		
		// generate paths until stop evaluated to true
		while (!stop.evaluate(result)) {
			IDirectedGraphPath path = walk(input);
			if (path != null) {
				result.addWalkPath(path);
			}
		}
		
		result.ended(input);
	}

	/** Walks the graph. */
	private IDirectedGraphPath walk(IRandomWalkInput input) {
		Object root = input.chooseRootVertex(graph, r);
		if (root == null) { return null; }

		// create path
		DefaultDirectedGraphPath path = new DefaultDirectedGraphPath(graph, root);

		// add subsequent edges until stop
		Object current = root;
		while (!pathStop.evaluate(path)) {
			Collection c = graph.getOutgoingEdges(current);
			
			// handle deadlock state
			if (c.isEmpty()) { return path; }
			
			Object edge = chooseEdge(c);
			
			// add choosen edge to the path, update current
			path.addEdge(edge);
			current = graph.getEdgeTarget(edge);
		}
		
		return path;
	}
	
	/** Chooses an edge from a collection. */
	private Object chooseEdge(Collection c) {
		int size = c.size();
		int index = r.nextInt(size);
		if (c instanceof List) {
			return ((List)c).get(index);
		} else {
			return c.toArray()[index];
		}
	}
	
}
