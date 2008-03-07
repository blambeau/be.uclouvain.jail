package be.uclouvain.jail.algo.induct;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.TestCase;
import be.uclouvain.jail.algo.induct.internal.SLPair;
import be.uclouvain.jail.algo.induct.processor.ForwardLabelProcessor;
import be.uclouvain.jail.algo.induct.processor.ForwardLabelProcessor.Input;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.constraints.PTAGraphConstraint;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.tests.JailTestUtils;
import be.uclouvain.jail.uinfo.IUserInfo;

/**
 * Tests the forward labels preprocessor.
 * 
 * @author blambeau
 */
public class ForwardLabelsTests extends TestCase {

	/** Returns incoming edge of a state. */
	private Object inEdge(IDirectedGraph ptag, Object s) {
		Collection<Object> inEdges = ptag.getIncomingEdges(s);
		
		// initial state?
		if (inEdges.isEmpty()) { return null; }
		
		// only one incoming edge
		assert (inEdges.size() == 1) : "Valid PTA.";
			
		// return single incoming edge
		return inEdges.iterator().next();
	}
	
	/** Asserts that labeling is valid. */
	public void assertValidLabeling(IDFA pta) {
		Set<Integer> labels = new TreeSet<Integer>();
		Map<Object,Integer> labelByClass = new HashMap<Object,Integer>();
		Map<SLPair,Integer> labelByPair = new HashMap<SLPair,Integer>();
		IDirectedGraph ptag = pta.getGraph();

		// compute some data structures
		for (Object state: ptag.getVertices()) {
			IUserInfo info = ptag.getVertexInfo(state);
			
			// retrieve label and clazz
			Object clazz = info.getAttribute("class");
			Integer label = (Integer) info.getAttribute("label");
			
			// label exists on each state
			assertNotNull("Label has been set on each state",label);
			
			// add label as found
			labels.add(label);

			// Same label for same classes
			if (clazz != null && !"".equals(clazz)) {
				Integer clazzLabel = labelByClass.get(clazz);
				if (clazzLabel != null) {
					assertEquals("Same label for same class",label,clazzLabel);
				} else {
					labelByClass.put(clazz, label);
				}
			}
			
			// create source/letter pair
			Object inEdge = inEdge(ptag, state);
			if (inEdge != null) {
				Object source = ptag.getEdgeSource(inEdge);
				Integer sourceLabel = (Integer) ptag.getVertexInfo(source).getAttribute("label");
				Object letter = pta.getEdgeLetter(inEdge);
				SLPair pair = new SLPair(sourceLabel,letter);
				Integer rule = labelByPair.get(pair);
				if (rule != null) {
					assertEquals("(label,letter) functionaly determines target label",rule,label);
				} else {
					labelByPair.put(pair, label);
				}
			}
		}
		
		// consecutive labeling
		for (int i=0; i<labels.size(); i++) {
			assertTrue("Consecutive labels.",labels.contains(i));
		}
	}
	
	/** Tests a forward propagation. */
	public void testForwardPropagation() throws Exception {
		IDFA pta = JailTestUtils.loadDotDFA(JailTestUtils.resource(getClass(), "pta_labels.dot"));
		assertTrue(new PTAGraphConstraint().isRespectedBy(pta.getGraph()));
		//AutomatonFacade.debug(pta);
		
		Input input = new ForwardLabelProcessor.Input(pta,"class","label");
		input.setUnknown("");
		new ForwardLabelProcessor().process(input);
		
		//AutomatonFacade.debug(pta);
		assertValidLabeling(pta);
	}
	
	/** Main method. */
	public static void main(String[] args) throws Exception{
		new ForwardLabelsTests().testForwardPropagation();
	}
	
}
