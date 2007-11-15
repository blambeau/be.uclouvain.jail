package be.uclouvain.jail.dialect.dot;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import att.grappa.Graph;
import att.grappa.GrappaPanel;
import att.grappa.Parser;
import be.uclouvain.jail.Jail;
import be.uclouvain.jail.dialect.IPrintable;
import be.uclouvain.jail.graph.IDirectedGraph;

/**
 * Allows graph to be visualized using dot.
 * 
 * @author blambeau
 */
public class JDotty extends JFrame {

	/** Serial version UID. */
	private static final long serialVersionUID = 1755521514008407690L;

	/** Flag to force silent JDotty in tests. */
	private static boolean silent = false;
	
	/** Tabs. */
	private JTabbedPane tabs;

	/** Creates a JDotty instance. */
	public JDotty() {
		super();
		if (!silent) {
			super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			tabs = new JTabbedPane();
			super.getContentPane().add(tabs);
			super.setSize(800, 600);
		}
	}
	
	/** Forces JDotty to be silent. */
	public static void silent() {
		silent = true;
	}
	
	/** Presents a graph using dot. */
	public void present(IDirectedGraph graph, String labelAttr) throws IOException {
		if (silent) { return; }
		
		final IPrintable p = new DOTDirectedGraphPrintable(graph);
		String dotPath = (String) Jail.getProperty("be.uclouvain.jail.dot.JDotty.dotpath","dot");
		
		// execute dot
		Process dotp = Runtime.getRuntime().exec(dotPath);
		InputStream dotIs = dotp.getInputStream();
		final OutputStream dotOs = dotp.getOutputStream();
		
		// start writing on the pipe
		final Exception[] ex = new Exception[1];
		new Thread(new Runnable(){
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
		
		// start reading on the pipe
		try {
			Parser dotParser = new Parser(dotIs, System.out);
			dotParser.parse();
			Graph grappaGraph = dotParser.getGraph();
			grappaGraph.setEditable(false);
			grappaGraph.repaint();
			dotIs.close();
			
			// create a frame for presentation
			Object name = graph.getUserInfo().getAttribute(labelAttr);
			GrappaPanel panel = new GrappaPanel(grappaGraph);
			tabs.addTab(name == null ? "Noname" : name.toString(), new JScrollPane(panel));
			super.setVisible(true);
		} catch (Exception ex2) {
			throw new IOException("Unable to start jdotty: " + ex2.getMessage());
		}
	}
	
}
