package ke.ac.ku.gacs.model;

import java.io.Serializable;

/**
 * GACS (ke.ac.ku.gacs.model)
 * Created by: Oloo
 * On: 07/01/2026 12:18
 * Description: Base interface for chromosome implementations
 **/

public interface Chromosome<P extends Problem> extends Comparable<Chromosome<P>>, Serializable {

    /**
     * Get fitness value
     */
    double getFitness();

    /**
     * Set fitness value
     */
    void setFitness(double fitness);

    /**
     * Create a deep clone
     */
    Chromosome<P> deepClone();

    /**
     * Perform crossover with another chromosome
     */
    Chromosome<P> crossover(Chromosome<P> other);

    /**
     * Perform mutation
     */
    void mutate(GAConfig config);

    @Override
    default int compareTo(Chromosome<P> other) {
        return Double.compare(other.getFitness(), this.getFitness());
    }
}
