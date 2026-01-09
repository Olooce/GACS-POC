package ke.ac.ku.gacs.model;

/**
 * GACS (ke.ac.ku.gacs.model)
 * Created by: Oloo
 * On: 07/01/2026 12:21
 * Description: Evaluates fitness of chromosomes
 **/

public interface FitnessEvaluator<P extends Problem, C extends Chromosome<P>> {
    /**
     * Calculate fitness score for a chromosome
     * @return fitness value between 0.0 and 1.0
     */
    double evaluate(C chromosome, P problem);
}