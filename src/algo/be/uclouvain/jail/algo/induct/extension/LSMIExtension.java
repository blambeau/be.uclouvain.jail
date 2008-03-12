package be.uclouvain.jail.algo.induct.extension;

import be.uclouvain.jail.algo.induct.compatibility.ClassBasedCompatibility;
import be.uclouvain.jail.algo.induct.compatibility.StateKindCompatibility;
import be.uclouvain.jail.algo.induct.internal.InductionAlgo;
import be.uclouvain.jail.algo.induct.listener.InductionLogger;
import be.uclouvain.jail.algo.induct.processor.BackPropagateProcessor;
import be.uclouvain.jail.algo.induct.processor.ForwardLabelProcessor;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.IUserInfoHandler;
import be.uclouvain.jail.vm.toolkits.GraphFacade;

/**
 * Installs the Language-State-Merging-Induction Extension on the induction 
 * algorithm.
 *  
 * @author blambeau
 */
public class LSMIExtension implements IInductionAlgoExtension {
	
	/** State kind compatibility. */
	private StateKindCompatibility skc;
	
	/** Class based compatibility. */
	private ClassBasedCompatibility cbc;
	
	/** Installs on the algorithm. */
	public void install(InductionAlgo algo) {
		// create class based compatibility and initialize it
		skc = new StateKindCompatibility();
		cbc = new ClassBasedCompatibility();
		algo.addCompatibility(skc,cbc);
		
		// add min function on representor
		IUserInfoHandler handler = algo.getInfo().getInput().getUserInfoHandler();
		handler.getVertexAggregator().min(ForwardLabelProcessor.DEFAULT_TARGET_ATTR);
		handler.getVertexAggregator().min("index");
		
		// add incompatibility tracker
		algo.addListener(new InductionLogger(), 
				cbc.getTracker());
	}

	/** Initializes on the new PTA. */
	public void initialize(InductionAlgo algo) {
		GraphFacade.identify(algo.getPTA().getGraph(),"index","index");
		
		// add forward label preprocessor
		new ForwardLabelProcessor().process(algo);
		
		labelize(algo.getPTA().getGraph());

		// initializes the two compatibilities layers
		skc.initialize(algo);
		cbc.initialize(algo);

		// Back propagates incompatibilities
		new BackPropagateProcessor().process(algo);
	}

	/** Labelize the graph. */
	private void labelize(IDirectedGraph g) {
		for (Object v: g.getVertices()) {
			IUserInfo info = g.getVertexInfo(v);
			info.setAttribute("label", 
					"(" + info.getAttribute("index") + " " 
					+ info.getAttribute(ForwardLabelProcessor.DEFAULT_TARGET_ATTR)
					+ ")");
		}
	}
	
}
