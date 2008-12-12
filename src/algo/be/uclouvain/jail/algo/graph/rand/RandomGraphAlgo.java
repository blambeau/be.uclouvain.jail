package be.uclouvain.jail.algo.graph.rand;

import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.IGraphPredicate;
import be.uclouvain.jail.graph.utils.ITotalOrder;
import be.uclouvain.jail.uinfo.IUserInfo;

/** Randomly generates a graph. */
public class RandomGraphAlgo {

	/** Input information. */
	private IRandomGraphInput input;

	/** Input information. */
	private IRandomGraphResult output;

	/** Graph under construction. */
	private IDirectedGraph graph;
	
	/** Creates an algorithm instance. */
	public RandomGraphAlgo() {
	}

	/** Creates the graph vertices. */
	private void createVertices() {
		IGraphPredicate stop = input.getVertexStopPredicate();
		do {
			graph.createVertex(output.createVertexInfo(graph));
		} while (!stop.evaluate(graph));
	}

	/** Creates the graph edges. */
	private void createEdges() {
		ITotalOrder<Object> vertices = graph.getVerticesTotalOrder();
		int vCount = vertices.size();
		if (vCount == 0) { return; }
		
		IGraphPredicate stop = input.getEdgeStopPredicate();
		do {
			// flit source state
			Object source = output.pickUpSource(graph);
			
			// flip target state
			Object target = output.pickUpTarget(graph, source);
			
			// connect
			IUserInfo edgeInfo = output.createEdgeInfo(graph, source, target);
			if (edgeInfo != null) {
				graph.createEdge(source, target, edgeInfo);
			}
		} while (!stop.evaluate(graph));
	}
	
	/** Launches the generation. */
	public void execute(IRandomGraphInput input, IRandomGraphResult output) {
		// initialization
		this.input = input;
		this.output = output;

		// Let output known that the algo has started
		output.started(input);
		
		// launch main loop, continue until stop or accept
		IGraphPredicate accept = input.getAcceptPredicate();
		IGraphPredicate stop = input.getTryStopPredicate();
		while (true) {
			graph = output.factorGraph();
			createVertices();
			createEdges();
			graph = output.clean(graph);
			if (graph != null && accept.evaluate(graph)) {
				output.success(graph);
				return;
			} else if (stop.evaluate(graph)) {
				output.failed();
			}
		}
	}
	
}
