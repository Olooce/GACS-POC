package ke.ac.ku.gacs.model;

/**
 * GACS (ke.ac.ku.gacs.model)
 * Created by: Oloo
 * On: 07/01/2026 12:20
 * Description:
 * Description:
 **/

public interface ChromosomeFactory<P extends Problem, C extends Chromosome<P>> {
    /**
     * Create a new random chromosome for the given problem
     */
    C create(P problem);
}
