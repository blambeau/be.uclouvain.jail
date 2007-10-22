package be.uclouvain.jail.algo.graph.dsp;

import be.uclouvain.jail.graph.adjacency.AdjacencyDirectedGraph;
import be.uclouvain.jail.graph.deco.DirectedGraph;
import be.uclouvain.jail.graph.deco.GraphUniqueIndex;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.MapUserInfo;

/**
 * Provides a directed graph with cities.
 * 
 * @author blambeau
 */
public class CitiesDirectedGraph extends DirectedGraph {

	/** Vertices index. */
	private GraphUniqueIndex index;

	/** Creates a graph instance. */
	public CitiesDirectedGraph() {
		super(new AdjacencyDirectedGraph());
		index = new GraphUniqueIndex(GraphUniqueIndex.VERTEX,"label",true).installOn(this);
		createGraph();
	}

	/** Creates the graph. */
	private void createGraph() {
		Object sfo = this.createVertex(vinfo("SFO"));
		Object ord = this.createVertex(vinfo("ORD"));
		Object lax = this.createVertex(vinfo("LAX"));
		Object dfw = this.createVertex(vinfo("DFW"));
		Object bos = this.createVertex(vinfo("BOS"));
		Object pvd = this.createVertex(vinfo("PVD"));
		Object jfk = this.createVertex(vinfo("JFK"));
		Object bwi = this.createVertex(vinfo("BWI"));
		Object mia = this.createVertex(vinfo("MIA"));
		
		this.createEdge(bwi, bwi, einfo(0));

		this.createEdge(sfo, bos, einfo(2704));
		this.createEdge(ord, sfo, einfo(1846));
		this.createEdge(sfo, dfw, einfo(1464));
		this.createEdge(sfo, lax, einfo(337));
		
		this.createEdge(dfw, lax, einfo(1235));
		this.createEdge(lax, mia, einfo(2342));
		
		this.createEdge(dfw, mia, einfo(1121));
		this.createEdge(ord, dfw, einfo(802));
		this.createEdge(dfw, jfk, einfo(1391));
		
		this.createEdge(bwi, mia, einfo(946));
		this.createEdge(mia, jfk, einfo(1090));
		this.createEdge(mia, bos, einfo(1258));
		
		this.createEdge(bwi, ord, einfo(621));
		this.createEdge(bwi, jfk, einfo(184));
		
		this.createEdge(ord, bos, einfo(867));
		this.createEdge(ord, pvd, einfo(849));
		this.createEdge(ord, jfk, einfo(740));
		
		this.createEdge(jfk, pvd, einfo(144));
		this.createEdge(jfk, bos, einfo(187));
	}
	
	/** Creates a vertex info. */
	private IUserInfo vinfo(String label) {
		IUserInfo info = new MapUserInfo();
		info.setAttribute("label", label);
		return info;
	}
	
	/** Creates an edge info. */
	private IUserInfo einfo(Integer dist) {
		IUserInfo info = new MapUserInfo();
		info.setAttribute("distance", dist);
		return info;
	}
	
	/** Returns a vertex mapped to a specific label. */
	public Object getVertex(String label) {
		return index.getVertex(label);
	}

	/** Returns the city name of a vertex. */
	public String getCityName(Object vertex) {
		return (String) getVertexInfo(vertex).getAttribute("label");
	}
	
}
