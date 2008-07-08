package be.uclouvain.jail.algo.utils;

import java.util.Collection;

import net.chefbe.javautils.adapt.AdaptUtils;
import net.chefbe.javautils.adapt.IAdaptable;
import be.uclouvain.jail.algo.commons.IAlgoResult;
import be.uclouvain.jail.fa.IFA;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.adjacency.AdjacencyDirectedGraph;
import be.uclouvain.jail.graph.utils.DirectedGraphWriter;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.IUserInfoHandler;
import be.uclouvain.jail.uinfo.IUserInfoPopulator;
import be.uclouvain.jail.uinfo.UserInfoHandler;
import be.uclouvain.jail.vm.JailVMException;
import be.uclouvain.jail.vm.JailVMOptions;

/**
 * Helps for creation of {@IAlgoResult}.
 * 
 * @author blambeau
 */
public class AbstractAlgoResult implements IAlgoResult, IAdaptable {

	/** Algo helper. */
	private AlgoHelper helper = new AlgoHelper(this);
	
	/** User-info handler. */
	private IUserInfoHandler userInfoHandler;
	
	/** Options are installed? */
	private boolean installed;
	
	/** Returns the helper. */
	protected AlgoHelper getAlgoHelper() {
		return helper;
	}

	/** Returns the user info handler. */
	public IUserInfoHandler getUserInfoHandler() {
		if (userInfoHandler == null) {
			userInfoHandler = new UserInfoHandler();
		}
		return userInfoHandler;
	}

	/** Marks some keep all on copiers. */
	public void keepAll(boolean onGraph, boolean onVertex, boolean onEdge) {
		getUserInfoHandler().keepAll(onGraph,onVertex,onEdge);
	}
	
	/** Adds a graph populator. */
	public void addGraphPopulator(IUserInfoPopulator<IUserInfo> p) {
		getUserInfoHandler().getGraphCopier().addPopulator(p);
	}
	
	/** Adds a vertex populator. */
	public void addVertexPopulator(IUserInfoPopulator<IUserInfo> p) {
		getUserInfoHandler().getVertexCopier().addPopulator(p);
	}
	
	/** Adds an edge populator. */
	public void addEdgePopulator(IUserInfoPopulator<IUserInfo> p) {
		getUserInfoHandler().getEdgeCopier().addPopulator(p);
	}
	
	/** Adds a graph aggregator. */
	public void addGraphAggregator(IUserInfoPopulator<Collection<IUserInfo>> p) {
		getUserInfoHandler().getGraphAggregator().addPopulator(p);
	}
	
	/** Adds a vertex aggregator. */
	public void addVertexAggregator(IUserInfoPopulator<Collection<IUserInfo>> p) {
		getUserInfoHandler().getVertexAggregator().addPopulator(p);
	}
	
	/** Adds an edge aggregator. */
	public void addEdgeAggregator(IUserInfoPopulator<Collection<IUserInfo>> p) {
		getUserInfoHandler().getEdgeAggregator().addPopulator(p);
	}
	
	/** Factors a writer on a graph. */
	public DirectedGraphWriter getWriter(IDirectedGraph g) {
		return new DirectedGraphWriter(userInfoHandler, g);
	}

	/** Returns a writer on a FA. */
	public DirectedGraphWriter getWriter(IFA fa) {
		return new DirectedGraphWriter(userInfoHandler, fa.getGraph());
	}
	
	/** Factors a writer on an new graph. */
	public DirectedGraphWriter getWriter() {
		return new DirectedGraphWriter(userInfoHandler, new AdjacencyDirectedGraph());
	}
	
	/** Installs the options. */
	public void setOptions(JailVMOptions options) throws JailVMException {
		if (!installed) {
			installed = true;
			installOptions();
		}
		helper.setOptions(options);
	}

	/** Install options. */
	protected void installOptions() {
	}

	/** Adds an option. */
	protected void addOption(String property, boolean mandatory, Class type, Object defValue) {
		helper.addOption(property, mandatory, type, defValue);
	}

	/** Adds an option. */
	protected void addOption(String optName, String property, boolean mandatory, Class type, Object defValue) {
		helper.addOption(optName, property, mandatory, type, defValue);
	}

	/** Provides adaptations. */
	public <T> Object adapt(Class<T> c) {
		if (c.isAssignableFrom(getClass())) {
			return this;
		}
		return AdaptUtils.externalAdapt(this,c);
	}

}
