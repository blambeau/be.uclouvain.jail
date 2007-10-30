package be.uclouvain.jail.algo.fa.kernel;

import be.uclouvain.jail.algo.graph.shortest.dsp.DSP;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.IDirectedGraphPath;
import be.uclouvain.jail.graph.IDirectedGraphWriter;
import be.uclouvain.jail.uinfo.IUserInfo;

/**
 * Extracts the kernel of the language represented by a DFA.
 *  
 * @author blambeau
 */
public class DFAKernelExtractorAlgo {

	/** Executes the algorithm. */
	public void execute(IDFAKernelExtractorInput input, IDFAKernelExtractorResult result) {
		IDFA dfa = input.getDFA();
		IDirectedGraph graph = dfa.getGraph();
		IDirectedGraphWriter writer = result.getNFAWriter();
		
		// root of the future tree
		Object root = dfa.getInitialState();
		IUserInfo rootInfo = graph.getVertexInfo(root);
		
		// Computes Dijkstra Shortest Path
		DSP dsp = new DSP(graph,root);
		
		// for each edge
		for (Object edge: graph.getEdges()) {
			Object source = graph.getEdgeSource(edge);
			Object target = graph.getEdgeTarget(edge);
			
			// compute shortest path to its source
			IDirectedGraphPath path = dsp.shortestPathTo(source);

			// flush path inside the writer
			Object last = path.size()==0 ? writer.createVertex(rootInfo) : path.flush(writer);
			
			// create next state for the edge, and  create it
			Object next = writer.createVertex(graph.getVertexInfo(target));
			writer.createEdge(last, next, graph.getEdgeInfo(edge));
		}
	}

}
