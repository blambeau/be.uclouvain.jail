/**
 * 
 */
package be.uclouvain.jail.dialect.adl;

import java.io.IOException;
import java.io.PrintWriter;

import net.chefbe.autogram.ast2.parsing.ParseException;
import be.uclouvain.jail.dialect.utils.AbstractGraphDialect;
import be.uclouvain.jail.fa.FAStateKind;
import be.uclouvain.jail.fa.IFA;
import be.uclouvain.jail.fa.ISample;
import be.uclouvain.jail.fa.IString;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.utils.ITotalOrder;
import be.uclouvain.jail.vm.JailVMOptions;

/** Installs the DOT graph dialect. */
public class ADLGraphDialect extends AbstractGraphDialect {

	/** Loads source in dot format. */
	public Object load(Object source, JailVMOptions options) throws IOException, ParseException {
		throw new UnsupportedOperationException("ADL dialect does not implement print.");
	}

	/** Prints source in dot format. */
	public void print(Object source, PrintWriter stream, JailVMOptions options) throws IOException {
	  if (source instanceof ISample) {
		  printSample((ISample)source, stream);
	  } else if (source instanceof IFA) {
		  printAutomaton((IFA)source, stream);
	  }
	}

	/** Prints an automaton. */
	private void printAutomaton(IFA source, PrintWriter stream) {
		IDirectedGraph g = source.getGraph();
		ITotalOrder<Object> states = source.getGraph().getVerticesTotalOrder();
		ITotalOrder<Object> edges = source.getGraph().getEdgesTotalOrder();
		stream.append(states.size() + " " + edges.size() + "\n");
		for (int i=0; i<states.size(); i++) {
			Object state = states.getElementAt(i);
			FAStateKind kind = source.getStateKind(state);
			boolean initial = source.isInitial(state);
			boolean accepting = kind.isFlagAccepting();
			stream.append(i + " " + initial + " " + accepting + "\n");
		}
		for (int i=0; i<edges.size(); i++) {
			Object edge = edges.getElementAt(i);
			Object src = g.getEdgeSource(edge);
			Object trg = g.getEdgeTarget(edge);
			stream.append(states.indexOf(src) + " " + states.indexOf(trg) + " " + source.getEdgeLetter(edge) + "\n");
		}
		stream.flush();
	}

	private void printSample(ISample<Object> source, PrintWriter stream) {
		for (IString<Object> s: source) {
			stream.append(s.isPositive() ? "+ " : "- ");
			for (Object letter: s) {
				stream.append(letter.toString() + " ");
			}
			stream.append("\n");
		}
		stream.flush();
	}

}