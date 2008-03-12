package be.uclouvain.jail.contribs.lsmi;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.TreeSet;

import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.ISample;
import be.uclouvain.jail.uinfo.IUserInfo;

/** 
 * Flushes all results.
 * 
 * @author blambeau
 */
public class LSMIFlush {

	/** Database to use. */
	private LSMIDatabase db;
	
	/** Current heading. */
	private TreeSet<String> heading;
	
	/** Creates a DFA generator based on a folder. */
	public LSMIFlush(File folder) {
		this.db = new LSMIDatabase(folder);
	}
	
	/** Flushes all results. */
	private void flush() throws IOException {
		flushDFAs();
		flushSamples();
		flushTestingSamples();
		flushResults();
	}

	/** Flushes a heading. */
	private void flushHeading(IUserInfo info, Writer w) throws IOException {
		heading = new TreeSet<String>();
		int i=0;
		for (String key: info.getKeys()) {
			if ("rankdir".equals(key)) {
				continue;
			}
			heading.add(key);
		}
		for (String key: heading) {
			if (i++ != 0) {
				w.append(';');
			}
			w.append(key);
		}
		w.append('\n');
	}

	/** Flushes a heading. */
	private void flushInfo(IUserInfo info, Writer w) throws IOException {
		int i=0;
		for (String key: heading) {
			if (i++ != 0) {
				w.append(';');
			}
			Object value = info.getAttribute(key);
			w.append(value == null ? "" : value.toString());
		}
		w.append('\n');
	}
	
	/** Returns info of an object. */
	private IUserInfo getInfo(Object t) {
		if (t instanceof IDFA) {
			return ((IDFA)t).getGraph().getGraphInfo();
		} else if (t instanceof ISample) {
			return getInfo(((ISample)t).adapt(IDFA.class));
		} else {
			throw new IllegalStateException("Unable to extract info of " + t);
		}
	}
	
	/** Flushes. */
	private <T> void flush(Iterator<T> it, Writer writer) throws IOException {
		boolean first = true;
		while (it.hasNext()) {
			T a = it.next();
			IUserInfo info = getInfo(a);
			if (first) {
				flushHeading(info,writer);
			}
			flushInfo(info,writer);
			first = false;
		}		
		writer.flush();
		writer.close();
	}
	
	/** Flushes informations about DFAs. */
	private void flushDFAs() throws IOException {
		File out = new File(db.getFolder(),"dfas.csv");
		FileWriter writer = new FileWriter(out);
		flush(db.getAllDFAs(),writer);
	}

	/** Flushes informations about samples. */
	private void flushSamples() throws IOException {
		File out = new File(db.getFolder(),"samples.csv");
		FileWriter writer = new FileWriter(out);
		flush(db.getAllSamples(),writer);
	}
	
	/** Flushes informations about testing samples. */
	private void flushTestingSamples() throws IOException {
		File out = new File(db.getFolder(),"testing.csv");
		FileWriter writer = new FileWriter(out);
		flush(db.getAllTestingSamples(),writer);
	}
	
	/** Flushes informations about results. */
	private void flushResults() throws IOException {
		File out = new File(db.getFolder(),"results.csv");
		FileWriter writer = new FileWriter(out);
		flush(db.getAllResults(),writer);
	}

	/** Main method. */
	public static void main(String[] args) throws IOException {
		File folder = null;
		if (args.length == 1) {
			folder = new File(args[0]);
		} else {
			folder = new File(".");
		}
		new LSMIFlush(folder).flush();
	}
	
}
