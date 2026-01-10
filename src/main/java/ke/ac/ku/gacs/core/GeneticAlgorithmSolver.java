package ke.ac.ku.gacs.core;
import ke.ac.ku.gacs.model.*;
import java.util.*;

/**
 * GACS (ke.ac.ku.gacs.core)
 * Created by: Oloo
 * On: 07/01/2026 14:34
 * Description:Generic Genetic Algorithm Constraint Solver
 *          Parameterized to work with any problem domain
 **/


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithmSolver<P extends Problem, C extends Chromosome<P>> {

    private final GAConfig config;
    private final ChromosomeFactory<P, C> chromosomeFactory;
    private final FitnessEvaluator<P, C> fitnessEvaluator;

    private List<C> currentGeneration;
    private double currentGenerationFitness;
    private C bestSolution;
    private GAResult<P, C> result;

    public GeneticAlgorithmSolver(
            GAConfig config,
            ChromosomeFactory<P, C> chromosomeFactory,
            FitnessEvaluator<P, C> fitnessEvaluator) {

        this.config = config;
        this.chromosomeFactory = chromosomeFactory;
        this.fitnessEvaluator = fitnessEvaluator;
    }

    /**
     * Execute the genetic algorithm
     */
    public GAResult<P, C> solve(P problem) {
        // Validate problem definition
        if (problem == null) {
            throw new IllegalArgumentException("Problem cannot be null");
        }

        // Validate population size
        if (config.getPopulationSize() <= 0) {
            throw new IllegalArgumentException("Population size must be positive");
        }

        result = new GAResult<>();
        result.setStartTime(System.currentTimeMillis());

        // Initialize population
        initializePopulation(problem);

        // Evolution loop
        int generation = 0;
        while (generation < config.getMaxGenerations()) {

            // Check for optimal solution
            if (bestSolution != null && bestSolution.getFitness() >= config.getTargetFitness()) {
                result.setSuccess(true);
                result.setMessage("Optimal solution found at generation " + generation);
                break;
            }

            // Create next generation
            List<C> nextGeneration = createNextGeneration(problem);

            // Update current generation
            currentGeneration = nextGeneration;
            Collections.sort(currentGeneration);

            // Update best solution
            if (bestSolution == null || currentGeneration.get(0).getFitness() > bestSolution.getFitness()) {
                bestSolution = (C) currentGeneration.get(0).deepClone();
            }

            // Log progress
            if (config.isVerbose() && generation % config.getLogInterval() == 0) {
                logGeneration(generation);
            }

            generation++;
        }

        // Finalize result
        result.setEndTime(System.currentTimeMillis());
        result.setGenerations(generation);
        result.setBestSolution(bestSolution);
        result.setFinalFitness(bestSolution != null ? bestSolution.getFitness() : 0);

        if (!result.isSuccess()) {
            result.setMessage("Max generations reached. Best fitness: " + result.getFinalFitness());
        }

        return result;
    }

    private void initializePopulation(P problem) {
        currentGeneration = new ArrayList<>();
        currentGenerationFitness = 0;

        for (int i = 0; i < config.getPopulationSize(); i++) {
            C chromosome = chromosomeFactory.create(problem);
            double fitness = fitnessEvaluator.evaluate(chromosome, problem);

            // Handle invalid fitness values
            if (Double.isNaN(fitness) || Double.isInfinite(fitness) || fitness < 0) {
                throw new IllegalStateException("Invalid fitness value: " + fitness + " for chromosome at index " + i);
            }

            chromosome.setFitness(fitness);
            currentGeneration.add(chromosome);
            currentGenerationFitness += fitness;
        }

        // Validate population was created
        if (currentGeneration.isEmpty()) {
            throw new IllegalStateException("Failed to initialize population");
        }

        Collections.sort(currentGeneration);
        bestSolution = (C) currentGeneration.get(0).deepClone();

        if (config.isVerbose()) {
            System.out.println("Initial population created. Best fitness: " + bestSolution.getFitness());
        }
    }

    private List<C> createNextGeneration(P problem) {
        List<C> nextGeneration = new ArrayList<>();
        double nextGenerationFitness = 0;

        // Elitism: keep top performers
        int eliteCount = (int) (config.getPopulationSize() * config.getElitismRate());
        for (int i = 0; i < eliteCount; i++) {
            C elite = (C) currentGeneration.get(i).deepClone();
            nextGeneration.add(elite);
            nextGenerationFitness += elite.getFitness();
        }

        // Create offspring through crossover and mutation
        while (nextGeneration.size() < config.getPopulationSize()) {
            C parent1 = selectParent();
            C parent2 = selectParent();

            C offspring;
            if (new Random().nextDouble() < config.getCrossoverRate()) {
                offspring = crossover(parent1, parent2, problem);
            } else {
                offspring = (C) parent1.deepClone();
            }

            if (new Random().nextDouble() < config.getMutationRate()) {
                mutate(offspring, problem);
            }

            double fitness = fitnessEvaluator.evaluate(offspring, problem);

            // Handle invalid fitness values
            if (Double.isNaN(fitness) || Double.isInfinite(fitness) || fitness < 0) {
                throw new IllegalStateException("Invalid fitness value: " + fitness + " during offspring evaluation");
            }

            offspring.setFitness(fitness);

            nextGeneration.add(offspring);
            nextGenerationFitness += fitness;
        }

        currentGenerationFitness = nextGenerationFitness;
        return nextGeneration;
    }

    private C selectParent() {
        if (config.getSelectionMethod() == SelectionMethod.ROULETTE) {
            return selectParentRoulette();
        } else {
            return selectParentTournament();
        }
    }

    private C selectParentRoulette() {
        if (currentGenerationFitness <= 0) {
            int index = new Random().nextInt(Math.max(1, config.getPopulationSize() / 10));
            return (C) currentGeneration.get(index).deepClone();
        }

        double selectionPool = currentGenerationFitness / 10;
        double random = new Random().nextDouble() * selectionPool;
        double sum = 0;
        int i = 0;

        while (sum <= random && i < currentGeneration.size()) {
            sum += currentGeneration.get(i).getFitness();
            i++;
        }

        int index = Math.max(0, Math.min(i - 1, currentGeneration.size() - 1));
        return (C) currentGeneration.get(index).deepClone();
    }

    private C selectParentTournament() {
        // Handle edge case: ensure tournament size is at least 1
        int tournamentSize = Math.max(1, Math.min(5, config.getPopulationSize() / 10));
        C best = null;

        for (int i = 0; i < tournamentSize; i++) {
            int index = new Random().nextInt(currentGeneration.size());
            C candidate = currentGeneration.get(index);
            if (best == null || candidate.getFitness() > best.getFitness()) {
                best = candidate;
            }
        }

        return (C) best.deepClone();
    }

    private C crossover(C parent1, C parent2, P problem) {
        // Delegate to chromosome's crossover implementation
        return (C) parent1.crossover(parent2);
    }

    private void mutate(C chromosome, P problem) {
        chromosome.mutate(config);
    }

    private void logGeneration(int generation) {
        System.out.printf("Generation %d: Best=%.4f, Avg=%.4f%n",
                generation,
                currentGeneration.get(0).getFitness(),
                currentGenerationFitness / config.getPopulationSize());
    }

    public C getBestSolution() {
        return bestSolution;
    }

    public GAResult<P, C> getResult() {
        return result;
    }
}