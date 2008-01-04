package be.uclouvain.jail.algo.graph.rand;

import java.util.Random;

import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.IGraphPredicate;
import be.uclouvain.jail.graph.utils.ITotalOrder;
import be.uclouvain.jail.uinfo.IUserInfo;

/** Randomly generates a graph. */
public class RandomGraphAlgo {

	/** Randomizer. */
	private Random r;

	/** Input information. */
	private IRandomGraphInput input;

	/** Input information. */
	private IRandomGraphOutput output;

	/** Graph under construction. */
	private IDirectedGraph graph;
	
	/** Creates an algorithm instance. */
	public RandomGraphAlgo() {
	}

	/** Creates the graph vertices. */
	private void createVertices() {
		IGraphPredicate stop = input.getVertexStopPredicate();
		do {
			graph.createVertex(output.createVertexInfo(r));
		} while (!stop.evaluate(graph));
	}

	/** Creates the graph edges. */
	private void createEdges() {
		ITotalOrder<Object> vertices = graph.getVerticesTotalOrder();
		int vCount = vertices.size();
		if (vCount == 0) { return; }
		
		Object cur = vertices.getElementAt(0);
		IGraphPredicate stop = input.getEdgeStopPredicate();
		do {
			// flip target state
			int sindex = r.nextInt(vCount);
			Object target = vertices.getElementAt(sindex);
			
			// connect
			IUserInfo edgeInfo = output.createEdgeInfo(cur, target, r);
			if (edgeInfo != null) {
				graph.createEdge(cur, target, edgeInfo);
			}
			cur = target;
		} while (!stop.evaluate(graph));
	}
	
	/** Launches the generation. */
	public void execute(IRandomGraphInput input, IRandomGraphOutput output) {
		// initialization
		this.input = input;
		this.output = output;
		this.r = new Random(System.currentTimeMillis());

		// launch main loop, continue until stop or accept
		IGraphPredicate accept = input.getAcceptPredicate();
		IGraphPredicate stop = input.getTryStopPredicate();
		while (true) {
			graph = output.factorGraph();
			createVertices();
			createEdges();
			if (accept.evaluate(graph)) {
				output.success(graph);
				return;
			} else if (stop.evaluate(graph)) {
				output.failed();
			}
		}
	}
	
}
