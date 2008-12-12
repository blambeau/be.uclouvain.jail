package be.uclouvain.jail.gic09;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import net.chefbe.javautils.collections.utils.CrossProduct;
import be.uclouvain.jail.algo.commons.Unable;
import be.uclouvain.jail.algo.fa.rand.RandomDFAInput;
import be.uclouvain.jail.algo.fa.rand.RandomDFAResult;
import be.uclouvain.jail.algo.graph.rand.IRandomGraphInput;
import be.uclouvain.jail.algo.graph.rand.RandomGraphAlgo;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.constraints.AccessibleDFAGraphConstraint;
import be.uclouvain.jail.fa.utils.DFAQueryable;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.utils.ITotalOrder;

/**
 * Generates DFAs.
 * 
 * @author blambeau
 */
public class DFAGenerator {

	/** File writer. */
	private static FileWriter w;
	
	/** Generates a single DFA. */
	public static IDFA generate(int size, int alph, double prop) {
		RandomDFAInput input = new RandomDFAInput();
		input.setVertexCount(size);
		input.setAccepting(0.5);
		input.setAlphabetSize(alph);
		input.setMaxOutDegree(alph*prop);
		input.setDepthControl(false);
		input.setNoDeadLock(false);
		input.setTolerance(-1.0);
		input.setStateMultFactor(1.0);
		input.setMaxTry(100);
		
		RandomDFAResult result = new RandomDFAResult() {
			
			ITotalOrder<Object> vertices = null;
			Object lastTarget = null;
			
			@Override
			public void started(IRandomGraphInput input) {
				super.started(input);
				vertices = null;
				lastTarget = null;
			}

			/** Picks up a source state. */
			public Object pickUpSource(IDirectedGraph graph) {
				if (vertices == null) {
					vertices = graph.getVerticesTotalOrder();
					return vertices.getElementAt(0);
				} else {
					assert (lastTarget != null);
					return lastTarget;
				}
			}

			/** Picks up a target state. */
			public Object pickUpTarget(IDirectedGraph graph, Object source) {
				lastTarget = vertices.getElementAt(randomizer.nextInt(vertices.size()));
				return lastTarget;
			}
			
		};
		result.setConnex(true);
		result.setDeterminize(false);
		result.setMinimize(true);
		
		new RandomGraphAlgo().execute(input,result);
		IDFA dfa = (IDFA) result.adapt(IDFA.class);
		assert new AccessibleDFAGraphConstraint().isRespectedBy(dfa.getGraph());
		return dfa;
	}
	
	public static void output(String[] header, Object...values) throws IOException {
		for (int i=0; i<header.length; i++) {
			if (i != 0) { w.append(","); }
			w.append(header[i] + " = ").append(values[i].toString());
		}
		w.append('\n');
		w.flush();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		Integer[] sizes = {32, 64, 128, 256, 512};
		Integer[] alphs = {2, 10, 25, 50, 100};
		Double[]  props = {1.0};
		Integer[] times = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		Object[][] dimensions = new Object[][]{ sizes, alphs, props, times };

		File f = new File("/home/blambeau/gic09_dfagen.txt");
		if (f.exists()) f.delete();
		w = new FileWriter(f);
		
		List<Object[]> values = CrossProduct.crossProduct(dimensions);
		int ok = 1;
		String[] header = new String[]{
				"id", 
				"e_size", "e_alph", "e_prop", "e_avgout", "e_edgecount", 
				"a_size", "a_alph",           "a_avgout", "a_edgecount", "a_depth"				
		};
		output(header, (Object[]) header);
		for (Object[] tuple: values) {
			int id = (Integer) tuple[3];
			int e_size = (Integer) tuple[0];
			int e_alph = (Integer) tuple[1];
			double e_prop = (Double) tuple[2];
			double e_avgout = e_alph*e_prop;
			double e_edgecount = e_size*e_alph*e_prop;
			
			// generate it
			System.out.print("Generating (" + ok + "/" + values.size() + ") " + e_size + " " + e_alph + " " + e_prop + " " + id + " ... ");
			DFAQueryable dfa = null;
			try {
				IDFA d = generate(e_size, e_alph, e_prop);
				dfa = new DFAQueryable(d);
				//AutomatonFacade.show(d);
			} catch (Unable ex) {
				System.out.println("ko.");
				continue;
			}
			
			int a_size = dfa.getStateCount();
			int a_depth = dfa.getDepth();
			int a_alph = dfa.getActualAlphabetSize();
			double a_avgout = dfa.getAverageOutDegree();
			int a_edgecount = dfa.getEdgeCount();
			output(header, id, 
				e_size, e_alph, e_prop, e_avgout, e_edgecount, 
				a_size, a_alph,         a_avgout, a_edgecount, a_depth);
			System.out.println("ok." + a_depth);
			ok++;
		}
		System.out.println(ok + "/" + values.size());
		
		w.close();
	}

}
