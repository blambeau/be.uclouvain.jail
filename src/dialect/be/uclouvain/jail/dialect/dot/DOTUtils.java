package be.uclouvain.jail.dialect.dot;

import java.io.IOException;
import java.io.OutputStream;

import be.uclouvain.jail.Jail;
import be.uclouvain.jail.dialect.IPrintable;
import be.uclouvain.jail.graph.IDirectedGraph;

/**
 * Provides utilities to use dot on graphs.
 * 
 * @author blambeau
 */
public final class DOTUtils {

	/** Factors a dot process. */
	public static Process getDOTProcess() throws IOException {
		// execute dot
		String dotPath = (String) Jail.getProperty("be.uclouvain.jail.dot.JDotty.dotpath", "dot");
		Process dotp = Runtime.getRuntime().exec(dotPath);
		return dotp;
	}
	
	/** Executes dot on a graph. */
	public static Process executeDOT(IDirectedGraph g) throws IOException {
		final IPrintable p = new DOTDirectedGraphPrintable(g);

		Process dotp = DOTUtils.getDOTProcess();
		final OutputStream dotOs = dotp.getOutputStream();
		
		final Exception[] ex = new Exception[1];
		new Thread(new Runnable() {
			public void run() {
				try {
					p.print(dotOs);
					dotOs.flush();
					dotOs.close();
				} catch (IOException e) {
					ex[0] = e;
				}
			}
		}).start();
		
		return dotp;
	}
	
}
