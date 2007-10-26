package be.uclouvain.jail.algo.fa.tmoves;

import java.util.Set;

import be.uclouvain.jail.fa.IDFA;
import be.uclouvain.jail.fa.INFA;
import be.uclouvain.jail.uinfo.IUserInfo;

/**
 * Abstract output of the tau-remover algorithm.
 * 
 * <p>Pseudo code of the algorithm as seen by this abstraction is described in 
 * {@link TauRemoverAlgo} javadoc documentation.</p>
 * 
 * @author LAMBEAU Bernard
 */
public interface ITauRemoverResult {

  /** "Algorithm started" event. */
  public void started(IDFA dfa);

  /** "Algorithm ended" event. */
  public void ended();
  
  /**
   * Creates a result target state mapped to a source state.
   * 
   * @param sourceState a source NFA state.
   * @return an identifier for created target state (when id support is enabled). 
   */
  public Object createTargetState(IUserInfo sourceState);

  /**
   * Creates transitions in the result.
   */
  public void createTargetTransitions(Set<Object> sources, Set<Object> targets, Set<IUserInfo> edges);

  /** Returns resulting NFA. */
  public INFA getResultingNFA();
  
}
