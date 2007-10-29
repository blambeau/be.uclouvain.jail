package be.uclouvain.jail.algo.fa.compose;

import junit.framework.TestCase;
import be.uclouvain.jail.dialect.seqp.SEQPGraphLoader;
import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.impl.GraphDFA;

/** Tests the group classes. */
public class GroupClassesTest extends TestCase implements IDFAGroupInformer {

	/** TRAIN dfa. */
	private String TRAIN = "STOPPED=start->STARTED|alarm->open->OPENED," +
	                       "STARTED=stop->STOPPED|alarm->stop->eopen->OPENED," +
	                       "OPENED=close->STOPPED.";
	private IDFA train;
	
	/** RUNNING fluent dfa. */
	private String RUNNING = "NO=start->YES|stop->NO," +
	                         "YES=stop->NO|start->YES.";
	private IDFA running;
	
	/** Creates a test instance. */
	public GroupClassesTest() throws Exception {
		train = new GraphDFA();
		running = new GraphDFA();
		SEQPGraphLoader.load(TRAIN,train.getGraph());
		SEQPGraphLoader.load(RUNNING,running.getGraph());
	}
	
	/** Returns the initial state group. */
	private DFAStateGroup getInitialGroup() {
		Object[] states = new Object[]{
			train.getInitialState(),
			running.getInitialState()
		};
		return new DFAStateGroup(states,this);
	}

	/** Simple delta function. */
	private Object delta(IDFA dfa, Object source, Object letter) {
		Object edge = dfa.getOutgoingEdge(source, letter);
		if (edge == null) {
			if (dfa.getAlphabet().contains(letter)) {
				return null;
			} else {
				return source;
			}
		} else {
			return dfa.getGraph().getEdgeTarget(edge);
		}
	}
	
	/** Computes delta function. */
	private DFAStateGroup compute(DFAStateGroup group, Object letter) {
		Object[] targets = new Object[]{
			delta(train,group.getComponent(0),letter),
			delta(running,group.getComponent(1),letter),
		};
		if (targets[0] == null || targets[1] == null) { 
			return null;
		} else {
			return new DFAStateGroup(targets,this);
		}
	}

	/** Tests DFA state group class. */
	public void testDFAStateGroup() {
		DFAStateGroup group = getInitialGroup();
		for (Object letter: train.getAlphabet()) {
			DFAStateGroup testGroup = compute(group,letter);
			DFAEdgeGroup classEdgesGroup = group.delta(letter);
			DFAStateGroup classGroup = classEdgesGroup == null ? null : classEdgesGroup.getTargetStateGroup(group);
			assertEquals(testGroup,classGroup);
		}
	}
	
	/** Returns the i-th DFA. */
	public IDFA getDFA(int i) {
		return i==0 ? train : running;
	}

}
