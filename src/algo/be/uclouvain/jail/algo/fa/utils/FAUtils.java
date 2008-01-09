package be.uclouvain.jail.algo.fa.utils;

import net.chefbe.autogram.ast2.parsing.ParseException;
import net.chefbe.javautils.robust.exceptions.CoreException;
import be.uclouvain.jail.algo.graph.copy.DirectedGraphCopier;
import be.uclouvain.jail.algo.graph.copy.match.GMatchPopulator;
import be.uclouvain.jail.fa.IFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.adjacency.AdjacencyDirectedGraph;
import be.uclouvain.jail.graph.utils.DirectedGraphWriter;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.IUserInfoPopulator;

/**
 * Provides some utilities for FAs. 
 * 
 * @author blambeau
 */
public class FAUtils {

	/** Returns a populator to show FAs using dot. */
	public static IUserInfoPopulator<IUserInfo> getGraph2DotVertexPopulator() {
		try {
			return GMatchPopulator.parse("{                                                       " +
					                     "    @shape -> when @kind='ACCEPTING' then 'doublecircle'" +
                                         "              when @kind='AVOID' then 'doublecircle'    " +
                                         "              else 'circle',                            " +
                                         "    @style -> 'filled',                                 " +
                                         "    @fillcolor -> when @isInitial=true and              " +  
                                         "                       (@kind='ERROR' or                " +
                                         "                        @kind='AVOID') then 'brown'     " +
                                         "                  when @isInitial=true then 'green'     " +
                                         "                  when @kind='ERROR' then 'orange'      " +
                                         "                  when @kind='AVOID' then 'red'         " +
                                         "                  else 'white',                         " +
                                         "    @label     -> @label                                " +
                                         "}                                                       ",null);
		} catch (ParseException ex) {
			throw new CoreException("Unexpected exception ",ex);
		}
	}

	/** Returns a populator to show FAs using dot. */
	public static IUserInfoPopulator<IUserInfo> getGraph2DotEdgePopulator() {
		try {
			return GMatchPopulator.parse("{                    " +
					                     "    @label -> @letter" +
                                         "}                    ",null);
		} catch (ParseException ex) {
			throw new CoreException("Unexpected exception ",ex);
		}
	}
	
	/** Returns a copy of a fa for dot presentation. */
	public static IDirectedGraph copyForDot(IFA fa) {
		IDirectedGraph graph = new AdjacencyDirectedGraph();
		DirectedGraphWriter writer = new DirectedGraphWriter(graph);
		writer.getEdgeCopier().keepAll();
		writer.getVertexCopier().keepAll();
		writer.getVertexCopier().addPopulator(getGraph2DotVertexPopulator());
		writer.getEdgeCopier().addPopulator(getGraph2DotEdgePopulator());
		DirectedGraphCopier.copy(fa.getGraph(), writer);
		return graph;
	}
	
}
