package ke.ac.ku.gacs.model;

import java.io.Serializable;

/**
 * GACS (ke.ac.ku.gacs.model)
 * Created by: Oloo
 * On: 07/01/2026 12:32
 * Description: Configuration for the genetic algorithm
 **/

public class GAConfig implements Serializable {
    private int populationSize = 1000;
    private int maxGenerations = 100;
    private double crossoverRate = 0.8;
    private double mutationRate = 0.1;
    private double elitismRate = 0.1;
    private double targetFitness = 1.0;
    private SelectionMethod selectionMethod = SelectionMethod.ROULETTE;
    private boolean verbose = true;
    private int logInterval = 10;

    public GAConfig() {}

    public static GAConfigBuilder builder() {
        return new GAConfigBuilder();
    }

    // Getters and setters
    public int getPopulationSize() { return populationSize; }
    public void setPopulationSize(int populationSize) { this.populationSize = populationSize; }

    public int getMaxGenerations() { return maxGenerations; }
    public void setMaxGenerations(int maxGenerations) { this.maxGenerations = maxGenerations; }

    public double getCrossoverRate() { return crossoverRate; }
    public void setCrossoverRate(double crossoverRate) { this.crossoverRate = crossoverRate; }

    public double getMutationRate() { return mutationRate; }
    public void setMutationRate(double mutationRate) { this.mutationRate = mutationRate; }

    public double getElitismRate() { return elitismRate; }
    public void setElitismRate(double elitismRate) { this.elitismRate = elitismRate; }

    public double getTargetFitness() { return targetFitness; }
    public void setTargetFitness(double targetFitness) { this.targetFitness = targetFitness; }

    public SelectionMethod getSelectionMethod() { return selectionMethod; }
    public void setSelectionMethod(SelectionMethod selectionMethod) { this.selectionMethod = selectionMethod; }

    public boolean isVerbose() { return verbose; }
    public void setVerbose(boolean verbose) { this.verbose = verbose; }

    public int getLogInterval() { return logInterval; }
    public void setLogInterval(int logInterval) { this.logInterval = logInterval; }

    public static class GAConfigBuilder {
        private final GAConfig config = new GAConfig();

        public GAConfigBuilder populationSize(int size) {
            config.populationSize = size;
            return this;
        }

        public GAConfigBuilder maxGenerations(int generations) {
            config.maxGenerations = generations;
            return this;
        }

        public GAConfigBuilder crossoverRate(double rate) {
            config.crossoverRate = rate;
            return this;
        }

        public GAConfigBuilder mutationRate(double rate) {
            config.mutationRate = rate;
            return this;
        }

        public GAConfigBuilder elitismRate(double rate) {
            config.elitismRate = rate;
            return this;
        }

        public GAConfigBuilder targetFitness(double fitness) {
            config.targetFitness = fitness;
            return this;
        }

        public GAConfigBuilder selectionMethod(SelectionMethod method) {
            config.selectionMethod = method;
            return this;
        }

        public GAConfigBuilder verbose(boolean verbose) {
            config.verbose = verbose;
            return this;
        }

        public GAConfigBuilder logInterval(int interval) {
            config.logInterval = interval;
            return this;
        }

        public GAConfig build() {
            return config;
        }
    }
}
