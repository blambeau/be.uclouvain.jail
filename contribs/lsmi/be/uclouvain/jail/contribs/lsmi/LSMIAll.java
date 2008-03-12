package be.uclouvain.jail.contribs.lsmi;

import java.io.IOException;

public class LSMIAll {

	/** Main method. */
	public static void main(String[] args) throws IOException {
		LSMIGenerateDFA.main(args);
		LSMIGenerateSample.main(args);
		LSMIAlgorithms.main(args);
		LSMIFlush.main(args);
	}

}
