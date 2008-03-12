package be.uclouvain.jail.contribs.lsmi;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import net.chefbe.autogram.ast2.parsing.ParseException;
import net.chefbe.javautils.robust.exceptions.CoreException;
import be.uclouvain.jail.dialect.dot.DOTGraphDialect;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.ISample;
import be.uclouvain.jail.fa.IString;
import be.uclouvain.jail.fa.IWalkInfo;
import be.uclouvain.jail.fa.impl.GraphDFA;
import be.uclouvain.jail.fa.utils.DefaultSample;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.uinfo.IUserInfo;

/**
 * Provides a DFA and sample database.
 * 
 * @author blambeau
 */
public class LSMIDatabase {

	/** Folder where to put files. */
	private File folder;

	/** Creates a database on a folder. */
	public LSMIDatabase(File folder) {
		this.folder = folder;
		if (!folder.exists()) {
			folder.mkdirs();
		}
	}
	
	/** Returns folder. */
	public File getFolder() {
		return folder;
	}
	
	/** Ensures that a file exists. */
	private void ensureFile(File f) throws IOException {
		if (!f.exists()) {
			if (!f.createNewFile()) {
				throw new IOException("Unable to create file " + f);
			}
		}
	}
	
	/** Returns a print writer on a file. */
	private PrintWriter writer(File f) throws IOException {
		ensureFile(f);
		return new PrintWriter(new FileWriter(f));
	}
	
	/** Saves a DFA. */
	private void save(IDFA dfa, String fileName) throws IOException {
		File f = new File(folder,fileName);
		PrintWriter fw = writer(f);
		new DOTGraphDialect().print(dfa.getGraph(), fw, null);
		fw.flush();
		fw.close();
	}
	
	/** Loads a DFA from a file. */
	private IDFA load(File f) throws IOException {
		try {
			IDirectedGraph g = (IDirectedGraph) new DOTGraphDialect().load(f, null);
			return new GraphDFA(g);
		} catch (ParseException ex) {
			throw new CoreException(ex);
		}
	}

	/** Loads a DFA from a file. */
	@SuppressWarnings("unused")
	private IDFA load(String fileName) throws IOException {
		return load(new File(folder,fileName));
	}

	/** Returns an iterator on DFAs of specified size. */
	public Iterator<IDFA> getAllDFAs() {
		return new DFAIterator("dfa_");
	}
	
	/** Returns a DFA attribute. */
	public Object getAttribute(IDFA dfa, String attr) {
		return dfa.getGraph().getGraphInfo().getAttribute(attr);
	}

	/** Sets a DFA attribute. */
	public void setAttribute(IDFA dfa, String attr, Object value) {
		dfa.getGraph().getGraphInfo().setAttribute(attr,value);
	}
	
	/** Returns an iterator on all samples. */
	public Iterator<ISample<String>> getAllSamples() {
		return new SampleIterator("sample_");
	}
	
	/** Returns an iterator on all samples. */
	public Iterator<ISample<String>> getAllTestingSamples() {
		return new SampleIterator("test_");
	}
	
	/** Returns all results. */
	public Iterator<IDFA> getAllResults() {
		return new DFAIterator("result_");
	}
	
	/** Returns sample/test pairs for a given DFA. */
	public Iterator<ISample<String>> getSamples(IDFA dfa) {
		final int size = Integer.parseInt(getAttribute(dfa,"DFA_size").toString());
		final int loid = Integer.parseInt(getAttribute(dfa,"DFA_lid").toString());
		return new SampleIterator(new FilenameFilter() {
			public boolean accept(File arg0, String arg1) {
				return arg1.startsWith("sample_" + size + "_" + loid);
			}
		});
	}


	/** Adds a DFA in the database. */
	public void addDFA(IDFA dfa, int size, int number) throws IOException {
		// mark DFA
		IDirectedGraph g = dfa.getGraph();
		IUserInfo info = g.getUserInfo();
		Object dfa_id = System.currentTimeMillis();

		info.setAttribute("DFA_size", size);
		info.setAttribute("DFA_lid", number);
		info.setAttribute("DFA_id", dfa_id);
		
		// compute file name
		String fileName = "dfa_" + size 
		                + "_" + number
		                + "_" + dfa_id
		                + ".dot";
		save(dfa,fileName);
	}
	
	/** Adds a sample for a DFA. */
	public void addSample(IDFA dfa, ISample<String> sample, ISample<String> test, int number) throws IOException {
		Object dfa_id = getAttribute(dfa,"DFA_id");
		Object sample_id = System.currentTimeMillis();
		
		// save sample
		IDFA samplePTA = (IDFA) sample.adapt(IDFA.class);
		setAttribute(samplePTA,"DFA_id",dfa_id);
		setAttribute(samplePTA,"SAMPLE_id",sample_id);
		setAttribute(samplePTA,"SAMPLE_lid",number);
		setAttribute(samplePTA,"SAMPLE_size",sample.size());
		String fileName = "sample_" + getAttribute(dfa,"DFA_size")
		                + "_" + getAttribute(dfa,"DFA_lid")
		                + "_" + number
		                + "_" + sample_id
		                + ".dot";
		save(samplePTA,fileName);

		// save test sample
		IDFA testPTA = (IDFA) test.adapt(IDFA.class);
		setAttribute(testPTA,"DFA_id",dfa_id);
		setAttribute(testPTA,"TEST_id",sample_id);
		setAttribute(testPTA,"TEST_lid",number);
		setAttribute(samplePTA,"TEST_size",test.size());
		fileName = "test_" + getAttribute(dfa,"DFA_size")
		         + "_" + getAttribute(dfa,"DFA_lid")
		         + "_" + number
		         + "_" + sample_id
		         + ".dot";
		save(testPTA,fileName);
	}

	/** Adds a result in the database. */
	public void addResult(IDFA target, ISample<String> sample, IDFA result) throws IOException {
		Object dfa_id = getAttribute(target,"DFA_id");
		Object sample_id = getAttribute((IDFA)sample.adapt(IDFA.class),"SAMPLE_id");
		Object result_id = System.currentTimeMillis();

		// compute score
		IDFA test = load("test_" + getRequestedSize(target) 
				  + "_" + getAttribute(target,"DFA_lid")
				  + "_" + getAttribute((IDFA)sample.adapt(IDFA.class),"SAMPLE_lid")
				  + "_" + sample_id
				  + ".dot");
		double score = score(result, new DefaultSample<String>(test));
		//System.out.println(score);
		
		// save result
		setAttribute(result,"DFA_id",dfa_id);
		setAttribute(result,"SAMPLE_id",sample_id);
		setAttribute(result,"RESULT_id",result_id);
		setAttribute(result,"RESULT_score",score);
		String fileName = "result_" + getAttribute(target,"DFA_size")
		                + "_" + getAttribute(target,"DFA_lid")
		                + "_" + getAttribute((IDFA)sample.adapt(IDFA.class),"SAMPLE_lid")
		                + "_" + result_id
		                + ".dot";
		save(result,fileName);
	}

	/** Computes the score of a dfa according to a test sample. */
	private double score(IDFA dfa, ISample<String> sample) {
		long size = sample.size();
		long ok = 0;
		for (IString<String> s: sample) {
			IWalkInfo info = s.walk(dfa);
			boolean accepted = info.isFullyIncluded()
			                && info.getIncludedPart().isAccepted();
			boolean positive = s.isPositive();
			if (accepted == positive) {
				ok++;
			}
		}
		return ((double)ok)/((double)size);
	}
	
	/** Returns a string for a DFA. */
	public String toString(IDFA dfa) {
		return "dfa_" + getAttribute(dfa,"DFA_size") + "_" + getAttribute(dfa,"DFA_lid");
	}
	
	/** Returns theoretical size of a DFA. */
	public int getRequestedSize(IDFA dfa) {
		return Integer.parseInt(getAttribute(dfa,"DFA_size").toString());
	}

	/** Reusable iterator on samples. */
	class SampleIterator implements Iterator<ISample<String>> {
		
		/** Files. */
		private File[] files;
		
		/** Next file to treat. */
		private int next;
		
		/** Creates an iterator. */
		public SampleIterator(final String startsWith) {
			this.files = folder.listFiles(new FilenameFilter() {
				public boolean accept(File arg0, String arg1) {
					return arg1.startsWith(startsWith);
				}
			});
		}

		/** Creates an iterator. */
		public SampleIterator(FilenameFilter filter) {
			this.files = folder.listFiles(filter);
		}
		
		/** Returns true if there is a next pair. */
		public boolean hasNext() {
			return next < files.length;
		}

		/** Returns next pair. */
		@SuppressWarnings("unchecked")
		public ISample<String> next() {
			try {
				File fSample = files[next++];
				return new DefaultSample<String>(load(fSample));
			} catch (IOException ex) {
				throw new CoreException(ex);
			} 
		}

		/** Throws Unsupported. */
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
	
	/** Reusable iterator on samples. */
	class DFAIterator implements Iterator<IDFA> {
		
		/** Files. */
		private File[] files;
		
		/** Next file to treat. */
		private int next;
		
		/** Creates an iterator. */
		public DFAIterator(final String startsWith) {
			this.files = folder.listFiles(new FilenameFilter() {
				public boolean accept(File arg0, String arg1) {
					return arg1.startsWith(startsWith);
				}
			});
		}

		/** Creates an iterator. */
		public DFAIterator(FilenameFilter filter) {
			this.files = folder.listFiles(filter);
		}
		
		/** Returns true if there is a next pair. */
		public boolean hasNext() {
			return next < files.length;
		}

		/** Returns next pair. */
		@SuppressWarnings("unchecked")
		public IDFA next() {
			try {
				return load(files[next++]);
			} catch (IOException ex) {
				throw new CoreException(ex);
			} 
		}

		/** Throws Unsupported. */
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
	
}
