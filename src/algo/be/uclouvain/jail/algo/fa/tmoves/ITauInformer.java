package be.uclouvain.jail.algo.fa.tmoves;

/**
 * Informs the algorithm about epsilon transitions.
 * 
 * @author LAMBEAU Bernard
 */
public interface ITauInformer<L> {

  /**
   * Returns true if the letter is epsilon, false otherwise.
   * 
   * @param letter the letter to check against epsilon.
   */
  public boolean isEpsilon(L letter);
  
}
