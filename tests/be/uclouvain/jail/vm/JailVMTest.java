package be.uclouvain.jail.vm;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URL;

import junit.framework.TestCase;
import be.uclouvain.jail.dialect.dot.JDotty;
import be.uclouvain.jail.examples.JailExamples;

/** Tests JailVM class. */
public class JailVMTest extends TestCase {

	/** Forces JDotty to be silent. */
	static {
		JDotty.silent();
	}
	
	/** Current executed example. */
	private URL current;
	
	/** Tests Jail execution on a jail file. */
	public void testJailExecution(URL what) throws Exception {
		current = what;
		JailVM vm = new JailVM(new IJailVMEnvironment() {

			/** Throws an AssertionError. */
			public void handleError(Throwable t) {
				t.printStackTrace();
				throw new AssertionError("Correct example: " + current);
			}

			/** Returns a trash writer. */
			public PrintWriter getConsoleWriter() {
				return new PrintWriter(new Writer() {
					@Override
					public void close() throws IOException {
					}
					@Override
					public void flush() throws IOException {
					}
					@Override
					public void write(char[] arg0, int arg1, int arg2) throws IOException {
					}
				});
			}
			
			/** Does nothing. */
			public void printConsole(String message, LEVEL level) {
			}
			
		});
		vm.execute(what);
	}
	
	/** Tests execution of all examples. */
	public void testJailExamples() throws Exception {
		for (URL url: JailExamples.getAllExamples()) {
			testJailExecution(url);
		}
	}
	
	/** Tests JailVM class. */
	public void testJailVM() throws Exception {
		testJailExecution(JailVMTest.class.getResource("test.jail"));
	}
	
	/** Tests composition. */
	public void testCompose() throws Exception {
		testJailExecution(JailVMTest.class.getResource("compose.jail"));
	}

	/** Tests TRAIN standard algorithms. */
	public void testTRAIN() throws Exception {
		testJailExecution(JailVMTest.class.getResource("TRAIN.jail"));
	}
	
}
