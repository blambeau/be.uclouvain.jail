package be.uclouvain.jail.tests;

import java.io.IOException;
import java.net.URL;

import junit.framework.TestCase;
import net.chefbe.autogram.ast2.parsing.ParseException;
import be.uclouvain.jail.algo.fa.utils.FAUtils;
import be.uclouvain.jail.dialect.dot.DOTGraphDialect;
import be.uclouvain.jail.dialect.jis.JISGraphDialect;
import be.uclouvain.jail.dialect.seqp.SEQPGraphLoader;
import be.uclouvain.jail.fa.FAStateKind;
import be.uclouvain.jail.fa.IAlphabet;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.INFA;
import be.uclouvain.jail.fa.ISample;
import be.uclouvain.jail.fa.impl.AttributeGraphFAInformer;
import be.uclouvain.jail.fa.impl.GraphDFA;
import be.uclouvain.jail.fa.impl.GraphNFA;
import be.uclouvain.jail.fa.utils.AutoAlphabet;
import be.uclouvain.jail.fa.utils.DefaultSample;
import be.uclouvain.jail.graph.IDirectedGraph;
import be.uclouvain.jail.graph.adjacency.AdjacencyDirectedGraph;
import be.uclouvain.jail.uinfo.IUserInfo;
import be.uclouvain.jail.uinfo.IUserInfoHelper;
import be.uclouvain.jail.uinfo.UserInfoHelper;

/** Provides many utilities for JAIL tests. */ 
public final class JailTestUtils {

	/** Not intended to be instanciated. */
	private JailTestUtils() {}
	
	/*** Helper to use. */
	private static IUserInfoHelper helper = UserInfoHelper.instance();
	
	/** Returns an empty graph. */
	public static final IDirectedGraph EMPTY_GRAPH() {
		IDirectedGraph graph = new AdjacencyDirectedGraph();
		helper.keys(
				"label",
				AttributeGraphFAInformer.STATE_INITIAL_KEY,
				AttributeGraphFAInformer.STATE_KIND_KEY);
		IUserInfo info = helper.install(
				"Q0",
				true,
				FAStateKind.fromBools(true,false)
		);
		graph.createVertex(info);
		return graph;
	}
	
	/** Returns an empty NFA. */
	public static final INFA EMPTY_NFA() {
		return new GraphNFA(EMPTY_GRAPH());
	}
	
	/** Returns an empty graph. */
	public static final IDFA EMPTY_DFA() {
		return new GraphDFA(EMPTY_GRAPH());
	}
	
	/** Returns an empty negative DFA containing one 
	 * negative non accepting state only. */
	public static final IDFA EMPTY_NEG_DFA() {
		IDirectedGraph graph = new AdjacencyDirectedGraph();
		helper.keys(
				"label",
				AttributeGraphFAInformer.STATE_INITIAL_KEY,
				AttributeGraphFAInformer.STATE_KIND_KEY);
		IUserInfo info = helper.install(
				"Q0",
				true,
				FAStateKind.fromBools(false,true)
		);
		graph.createVertex(info);
		return new GraphDFA(graph);
	}
	
	/** One state, one edge DFA. */
	public static final String SINGLE_DFA = "Q0[@kind='ACCEPTING'] = a->Q0.";
	
	/** Returns a SINGLE_DFA instance. */
	public static IDFA SINGLE_DFA() throws Exception {
		return loadSeqPDFA(SINGLE_DFA);
	}

	/** Returns a SINGLE_DFA graph instance. */
	public static IDirectedGraph SINGLE_DFAGraph() throws Exception {
		return loadSeqPGraph(SINGLE_DFA);
	}
	
	/** One state, two edges NFA. */
	public static final String SINGLE_NFA = "Q0[@kind='ACCEPTING'] = a->Q0|a->Q0.";
	
	/** Returns a SINGLE_NFA instance. */
	public static INFA SINGLE_NFA() throws Exception {
		return loadSeqPNFA(SINGLE_NFA);
	}

	/** Returns a SINGLE_NFA graph instance. */
	public static IDirectedGraph SINGLE_NFAGraph() throws Exception {
		return loadSeqPGraph(SINGLE_NFA);
	}
	
	/** NFA of page 56. */
	public static final String HOP_NFA56 = "Q0[@kind='PASSAGE'] = a->Q0|b->Q0|a->Q1,"
	                                     + "Q1[@kind='PASSAGE'] = b->Q2."; 
	
	/** Returns a HOP_NFA56 instance. */
	public static INFA HOP_NFA56() throws Exception {
		return loadSeqPNFA(HOP_NFA56);
	}

	/** Returns a HOP_NFA56 graph instance. */
	public static IDirectedGraph HOP_Graph56() throws Exception {
		return loadSeqPGraph(HOP_NFA56);
	}
	
	/** DFA of page 63 (equiv of HOP_NFA56). */
	public static final String HOP_DFA63 = "Q0[@kind='PASSAGE']  = a->Q01|b->Q0,"
	                                     + "Q01[@kind='PASSAGE'] = a->Q01|b->Q02,"
                                         + "Q02 = a->Q01|b->Q0.";
	
	/** Returns a HOP_DFA63 instance. */
	public static IDFA HOP_DFA63() throws Exception {
		return loadSeqPDFA(HOP_DFA63);
	}
	
	/** Returns a HOP_DFA63 graph instance. */
	public static IDirectedGraph HOP_Graph63() throws Exception {
		return loadSeqPGraph(HOP_DFA63);
	}
	
	/** NFA of page 73. */
	public static final String HOP_NFA73 = "Q0[@kind='PASSAGE'] = epsilon->Q1|plus->Q1|minus->Q1,"
                                         + "Q1[@kind='PASSAGE'] = digit->Q1|dot->Q2|digit->Q4,"
                                         + "Q2[@kind='PASSAGE'] = digit->Q3,"
                                         + "Q3[@kind='PASSAGE'] = digit->Q3|epsilon->Q5,"
                                         + "Q4[@kind='PASSAGE'] = dot->Q3."; 
	
	/** Returns a HOP_NFA73 instance. */
	public static INFA HOP_NFA73() throws Exception {
		return loadSeqPNFA(HOP_NFA73);
	}

	/** Returns a HOP_NFA73 graph instance. */
	public static IDirectedGraph HOP_Graph73() throws Exception {
		return loadSeqPGraph(HOP_NFA73);
	}

	/** DFA of page 78 (equiv HOP_NFA73). */
	public static final String HOP_DFA78 = "Q01 [@kind='PASSAGE']  = plus->Q1|minus->Q1|digit->Q14|dot->Q2,"
                                         + "Q1  [@kind='PASSAGE']  = digit->Q14|dot->Q2,"
                                         + "Q2  [@kind='PASSAGE']  = digit->Q35,"
                                         + "Q14 [@kind='PASSAGE']  = digit->Q14|dot->Q235,"
                                         + "Q235                      = digit->Q35,"
                                         + "Q35                       = digit->Q35.";
	
	/** Returns a HOP_DFA78 instance. */
	public static IDFA HOP_DFA78() throws Exception {
		return loadSeqPDFA(HOP_DFA78);
	}
	
	/** Returns a HOP_DFA78 graph instance. */
	public static IDirectedGraph HOP_Graph78() throws Exception {
		return loadSeqPGraph(HOP_DFA78);
	}
	
	/** DFA of page 155. */
	public static final String HOP_DFA155 = "A [@kind='PASSAGE'] = l0->B|l1->F,"
                                          + "B [@kind='PASSAGE'] = l0->G|l1->C,"
                                          + "C [@kind='ACCEPTING']  = l0->A|l1->C,"
                                          + "D [@kind='PASSAGE'] = l0->C|l1->G,"
                                          + "E [@kind='PASSAGE'] = l0->H|l1->F,"
                                          + "F [@kind='PASSAGE'] = l0->C|l1->G,"
                                          + "G [@kind='PASSAGE'] = l0->G|l1->E,"
                                          + "H [@kind='PASSAGE'] = l0->G|l1->C."; 
	
	/** Returns a HOP_DFA155 instance. */
	public static IDFA HOP_DFA155() throws Exception {
		return loadSeqPDFA(HOP_DFA155);
	}
	
	/** Returns a HOP_DFA155 graph instance. */
	public static IDirectedGraph HOP_Graph155() throws Exception {
		return loadSeqPGraph(HOP_DFA155);
	}
	
	/** DFA of page 162 (min of HOP_FFA155). */
	public static final String HOP_DFA162 = "AE [@kind='PASSAGE'] = l0->BH|l1->DF,"
                                          + "G  [@kind='PASSAGE'] = l0->G|l1->AE,"
                                          + "DF [@kind='PASSAGE'] = l0->C|l1->G,"
                                          + "BH [@kind='PASSAGE'] = l0->G|l1->C,"
                                          + "C  [@kind='ACCEPTING']  = l0->AE|l1->C.";
	

	/** Returns a HOP_DFA162 instance. */
	public static IDFA HOP_DFA162() throws Exception {
		return loadSeqPDFA(HOP_DFA162);
	}
	
	/** Returns a HOP_DFA162 graph instance. */
	public static IDirectedGraph HOP_Graph162() throws Exception {
		return loadSeqPGraph(HOP_DFA162);
	}
	
	/** DFA of page 48 of INRIA. */
	public static final String INRIA_DFA_3_1 = "Q0[@kind='ACCEPTING'] = a->Q0|b->Q1," 
		                                     + "Q1[@kind='PASSAGE']   = a->Q1|b->Q0.";

	/** Returns a INRIA_DFA_3_1 instance. */
	public static IDFA INRIA_DFA_3_1() throws Exception {
		return loadSeqPDFA(INRIA_DFA_3_1);
	}

	/** INRIA Sample 3.2 */
	public static final String INRIA_SAMPLE_3_2 = "+\n"
                                                + "+ a\n"
                                                + "+ b b\n"
                                                + "+ b b a\n"
                                                + "+ b a a b\n"
                                                + "+ b a a a b a\n"
                                                + "- b\n"
                                                + "- a b\n"
                                                + "- a b a\n";
	
	/** Returns Sample 3.2. */
	public static ISample<String> INRIA_SAMPLE_3_2() throws Exception {
		return loadJISSample(INRIA_SAMPLE_3_2);
	}
	
	/** Returns the train DFA. */
	public static IDFA TRAIN_DFA() throws Exception {
		return loadSeqPDFA(resource(JailTestUtils.class,"train_dfa.dot"));
	}
	
	/** Returns a sample for the train. */
	public static ISample<String> TRAIN_SAMPLE() throws Exception {
		// create an all accepting sample
		IAlphabet<String> alphabet = new AutoAlphabet<String>();
		ISample<String> sample = new DefaultSample<String>(alphabet);
		sample.getUserInfoHandler()
		      .getVertexCopier()
		      .addPopulator(FAUtils.getAllAcceptingPopulator());
		
		return loadJISSample(resource(JailTestUtils.class,"train_sample.jis"),sample);
	}
	
	/** Finds a resource. */
	public static URL resource(TestCase who, String where) {
		URL url = who.getClass().getResource(where);
		if (url == null) { throw new IllegalArgumentException("Unable to locate: " + where); }
		return url;
	}
	
	/** Finds a resource. */
	public static URL resource(Class who, String where) {
		URL url = who.getResource(where);
		if (url == null) { throw new IllegalArgumentException("Unable to locate: " + where); }
		return url;
	}

	/** Loads a dot graph. */
	public static IDirectedGraph loadDotGraph(Object from) throws IOException, ParseException {
		return (IDirectedGraph) new DOTGraphDialect().load(from,null);
	}
	
	/** Loads a dot graph. */
	public static IDFA loadDotDFA(Object from) throws IOException, ParseException {
		return new GraphDFA(loadDotGraph(from));
	}
	
	/** Loads a SEQP Graph. */
	public static IDirectedGraph loadSeqPGraph(Object from) throws IOException, ParseException {
		return SEQPGraphLoader.load(from);		
	}
	
	/** Loads a SEQP DFA. */
	public static IDFA loadSeqPDFA(Object from) throws IOException, ParseException {
		return new GraphDFA(SEQPGraphLoader.load(from));		
	}
	
	/** Loads a SEQP NFA. */
	public static INFA loadSeqPNFA(Object from) throws IOException, ParseException {
		return new GraphNFA(SEQPGraphLoader.load(from));		
	}
	
	/** Loads a JIS sample. */
	public static ISample<String> loadJISSample(Object from) throws IOException, ParseException {
		return new JISGraphDialect().parse(from);
	}
	
	/** Loads a JIS sample. */
	public static ISample<String> loadJISSample(Object from, ISample<String> sample) throws IOException, ParseException {
		return new JISGraphDialect().parse(from,sample);
	}
	
	/** Returns an array containing the NFAs as underlying graphs. */
	public static IDirectedGraph[] getAllNFAGraphs() throws Exception {
		return new IDirectedGraph[] {
				SINGLE_NFA().getGraph(), HOP_Graph56(), HOP_Graph73()
		};
	}
	
	/** Returns an array containing all known NFAs. */
	public static INFA[] getAllNFAs() throws Exception {
		return new INFA[] {
				SINGLE_NFA(), HOP_NFA56(), HOP_NFA73()
		};
	}
	
	/** Returns an array containing the DFAs as underlying graphs. */
	public static IDirectedGraph[] getAllDFAGraphs() throws Exception {
		return new IDirectedGraph[] {
				EMPTY_DFA().getGraph(), SINGLE_DFA().getGraph(), 
				HOP_Graph63(), HOP_Graph78(), HOP_Graph155(), HOP_Graph162()
		};
	}
	
	/** Returns an array containing all known DFAs. */
	public static IDFA[] getAllDFAs() throws Exception {
		return new IDFA[] {
				EMPTY_DFA(), SINGLE_DFA(), HOP_DFA63(), HOP_DFA78(), HOP_DFA155(), HOP_DFA162()
		};
	}
	
}
