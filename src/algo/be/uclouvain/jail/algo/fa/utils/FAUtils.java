package be.uclouvain.jail.algo.fa.utils;

import net.chefbe.autogram.ast2.parsing.ParseException;
import net.chefbe.javautils.robust.exceptions.CoreException;
import be.uclouvain.jail.algo.graph.copy.DirectedGraphCopier;
import be.uclouvain.jail.algo.graph.copy.match.GMatchPopulator;
import be.uclouvain.jail.fa.FAStateKind;
import be.uclouvain.jail.fa.IFA;
import be.uclouvain.jail.fa.impl.AttributeGraphFAInformer;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.adjacency.AdjacencyDirectedGraph;
import be.uclouvain.jail.graph.utils.DirectedGraphWriter;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.IUserInfoHandler;
import be.uclouvain.jail.uinfo.IUserInfoPopulator;
import be.uclouvain.jail.uinfo.UserInfoHandler;

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
	
	/** Returns a populator to set all states as accepting. */
	public static IUserInfoPopulator<IUserInfo> getAllAcceptingPopulator() {
		return new IUserInfoPopulator<IUserInfo>() {
			public void populate(IUserInfo target, IUserInfo source) {
				String attr = AttributeGraphFAInformer.STATE_KIND_KEY;
				FAStateKind kind = (FAStateKind) source.getAttribute(attr);
				if (FAStateKind.ERROR.equals(kind)) {
					target.setAttribute(attr, FAStateKind.AVOID);
				} else if (FAStateKind.PASSAGE.equals(kind)) {
					target.setAttribute(attr, FAStateKind.ACCEPTING);
				} else {
					target.setAttribute(attr, kind);
				}
			}
		};
	}
	
	/** Returns a copy of a fa for dot presentation. */
	public static IDirectedGraph copyForDot(IFA fa) {
		IDirectedGraph graph = new AdjacencyDirectedGraph();
		IUserInfoHandler handler = new UserInfoHandler();
		handler.getEdgeCopier().keepAll();
		handler.getVertexCopier().keepAll();
		handler.getVertexCopier().addPopulator(getGraph2DotVertexPopulator());
		handler.getEdgeCopier().addPopulator(getGraph2DotEdgePopulator());
		DirectedGraphWriter writer = new DirectedGraphWriter(handler, graph);
		DirectedGraphCopier.copy(fa.getGraph(), writer);
		return graph;
	}
	
}
