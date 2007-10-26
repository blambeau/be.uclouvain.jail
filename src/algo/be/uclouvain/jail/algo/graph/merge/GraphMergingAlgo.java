package be.uclouvain.jail.algo.graph.merge;

import java.util.HashSet;
import java.util.Set;

import be.uclouvain.jail.algo.fa.minimize.IBlockStructure;
import be.uclouvain.jail.graph.deco.DirectedGraph;
import be.uclouvain.jail.uinfo.IUserInfo;

/**
 * Simple algorithm that creates a new graph by merging vertices of 
 * another one.
 * 
 * @author blambeau
 */
public class GraphMergingAlgo {

	/** Executes the algorithm. */
	@SuppressWarnings("unchecked")
	public void execute(IGraphMergingInput input, IGraphMergingResult result) {
		DirectedGraph graph = (DirectedGraph) input.getGraph().adapt(DirectedGraph.class);
		
		/* transform each block as a new state */
		IBlockStructure<Object> structure = input.getVertexPartition();
		int size = structure.size();
		
		// array of created states
		Object[] createdStates = new Object[size];
		
		for (int i=0; i<size; i++) {
			Set<Object> block = structure.getBlock(i);
			Set<IUserInfo> infos = graph.getVertexInfos(block);
			createdStates[i] = result.createVertex(infos);
		}
		
		/* create edges */
		for (int i=0; i<size; i++) {
			Set<Object> iBlock = structure.getBlock(i);
			
			// create sets of IUser info by target blocks
			Set[] edges = new Set[size];
			
			// iterate vertices of iBlock
			for (Object vertex: iBlock) {
				// iterate out edges
				for (Object outEdge: graph.getOutgoingEdges(vertex)) {
					
					// find target of current edge and its corresponding block
					Object target = graph.getEdgeTarget(outEdge);
					int tBlock = structure.getBlockOf(target);
					
					// create set if not already done
					if (edges[tBlock]==null) {
						edges[tBlock] = new HashSet<IUserInfo>();
					}
					
					edges[tBlock].add(graph.getEdgeInfo(outEdge));
				}
			}
			
			// created new edges
			for (int j=0; j<size; j++) {
				Set<IUserInfo> edgeInfo = edges[j];
				if (edgeInfo != null) {
					result.createEdge(createdStates[i],createdStates[j],edgeInfo);
				}
			}
		}		
	}
	
}
