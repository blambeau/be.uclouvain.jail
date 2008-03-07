package be.uclouvain.jail.algo.fa.inject;

import be.uclouvain.jail.fa.FAStateKind;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.IFA;
import be.uclouvain.jail.fa.IFATrace;
import be.uclouvain.jail.fa.IString;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.IDirectedGraphPath;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.IUserInfoHandler;

/**
 * Injects new strings in DFAs.
 * 
 * @author blambeau
 */
public class DFAInjectionHelper {

	/** Handler to use. */
	private IUserInfoHandler handler;

	/** Injected DFA. */
	private IDFA dfa;

	/** Underlying graph. */
	private IDirectedGraph dfaGraph;
	
	/** Creates an injection utils. */
	public DFAInjectionHelper(IDFA dfa, IUserInfoHandler handler) {
		this.dfa = dfa;
		this.dfaGraph = dfa.getGraph();
		this.handler = handler;
	}

	/** Checks that the trace is correct according to string. */
	private <L> boolean correctTrace(IString<L> s, IFATrace<L> trace) {
		Object last = trace.getLastState();
		FAStateKind lastKind = trace.getFA().getStateKind(last);
		if (s.isPositive() && !FAStateKind.ACCEPTING.equals(lastKind)) {
			//System.out.println("Positive string not ending with an ACCEPTING.");
			return false;
		}
		if (!s.isPositive() && (FAStateKind.ACCEPTING.equals(lastKind)
				            ||  FAStateKind.PASSAGE.equals(lastKind))) {
			//System.out.println("Negative string not ending with an ERROR/AVOID.");
			return false;
		}
		return true;
	}
	
	/** Injects a string. */
	@SuppressWarnings("unchecked")
	public <L> boolean inject(final IString<L> s) {
		IFATrace<L> trace = (IFATrace<L>) s.adapt(IFATrace.class);
		if (trace == null) {
			throw new UnsupportedOperationException("String is expected to be adaptable to a trace");
		}
		assert correctTrace(s, trace) : "Trace is valid according to string.";
		return inject(trace);
	}
	
	/** Injects a word inside a DFA. */
	public <L> boolean inject(final IFATrace<L> trace) {
		final boolean[] newCreated = new boolean[]{false};
		final IFA traceFA = trace.getFA();
		final IDirectedGraphPath tracePath = trace.getGraphPath();
		final IDirectedGraph traceGraph = tracePath.getGraph();
		tracePath.accept(new IDirectedGraphPath.IVisitor() {
			
			/** Current state in DFA. */
			private Object current;
			
			/** On first vertex. */
			private void onFirst(Object edge, Object vertex) {
				current = dfa.getInitialState();
				IUserInfo vertexInfo = traceGraph.getVertexInfo(vertex);
				IUserInfo initInfo = dfaGraph.getVertexInfo(current);
				IUserInfo agg = handler.vertexAggregate(vertexInfo, initInfo);
				dfaGraph.setVertexInfo(current, agg);
			}
			
			/** When a vertex is visited. */
			public void visit(Object edge, Object vertex) {
				// special case of first vertex
				if (edge == null) {
					onFirst(edge,vertex);
					return;
				}
				
				// find next edge if any
				Object letter = traceFA.getEdgeLetter(edge);
				Object nextEdge = dfa.getOutgoingEdge(current, letter);
				
				// not end of merge?
				if (nextEdge != null) {
					// merge edges
					IUserInfo tEdgeInfo = traceGraph.getEdgeInfo(edge);
					IUserInfo dEdgeInfo = dfaGraph.getEdgeInfo(nextEdge);
					IUserInfo aggEdgeInfo = handler.edgeAggregate(tEdgeInfo, dEdgeInfo);
					dfaGraph.setEdgeInfo(nextEdge, aggEdgeInfo);
					
					// update current state
					current = dfaGraph.getEdgeTarget(nextEdge);
					
					// merge states
					IUserInfo tVertexInfo = traceGraph.getVertexInfo(vertex);
					IUserInfo dVertexInfo = dfaGraph.getVertexInfo(current);
					IUserInfo aggVertexInfo = handler.vertexAggregate(tVertexInfo, dVertexInfo);
					dfaGraph.setVertexInfo(current, aggVertexInfo);
				} else {
					// create new state
					IUserInfo tVertexInfo = traceGraph.getVertexInfo(vertex);
					IUserInfo dVertexInfo = handler.vertexAggregate(handler.vertexCopy(tVertexInfo));
					Object next = dfaGraph.createVertex(dVertexInfo);
					
					// create new edge
					IUserInfo tEdgeInfo = traceGraph.getEdgeInfo(edge);
					IUserInfo dEdgeInfo = handler.edgeAggregate(handler.edgeCopy(tEdgeInfo));
					dfaGraph.createEdge(current, next, dEdgeInfo);
					
					// update current to next
					current = next;
					newCreated[0] = true;
				}
			}
			
		});
		
		return newCreated[0];
	}
	
}
