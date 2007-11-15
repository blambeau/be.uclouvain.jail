package be.uclouvain.jail.algo.graph.shortest.dsp;

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
		Object sfo = this.createVertex(vinfo("SFO",0));
		Object ord = this.createVertex(vinfo("ORD",1));
		Object lax = this.createVertex(vinfo("LAX",2));
		Object dfw = this.createVertex(vinfo("DFW",3));
		Object bos = this.createVertex(vinfo("BOS",4));
		Object pvd = this.createVertex(vinfo("PVD",5));
		Object jfk = this.createVertex(vinfo("JFK",6));
		Object bwi = this.createVertex(vinfo("BWI",7));
		Object mia = this.createVertex(vinfo("MIA",8));
		
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
	private IUserInfo vinfo(String label, int index) {
		IUserInfo info = new MapUserInfo();
		info.setAttribute("label", label);
		info.setAttribute("index",index);
		info.setAttribute("pair", (index % 2)==0);
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
