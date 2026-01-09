package ke.ac.ku.gacs.model;

import java.io.Serializable;

/**
 * GACS (ke.ac.ku.gacs.model)
 * Created by: Oloo
 * On: 07/01/2026 13:57
 * Description:  Result of genetic algorithm execution
 **/

public class GAResult<P extends Problem, C extends Chromosome<P>> implements Serializable {
    private boolean success;
    private String message;
    private int generations;
    private double finalFitness;
    private C bestSolution;
    private long startTime;
    private long endTime;

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public int getGenerations() { return generations; }
    public void setGenerations(int generations) { this.generations = generations; }

    public double getFinalFitness() { return finalFitness; }
    public void setFinalFitness(double finalFitness) { this.finalFitness = finalFitness; }

    public C getBestSolution() { return bestSolution; }
    public void setBestSolution(C bestSolution) { this.bestSolution = bestSolution; }

    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }

    public long getEndTime() { return endTime; }
    public void setEndTime(long endTime) { this.endTime = endTime; }

    public long getExecutionTimeMs() {
        return endTime - startTime;
    }

    @Override
    public String toString() {
        return String.format("GAResult{success=%s, generations=%d, fitness=%.4f, time=%dms}",
                success, generations, finalFitness, getExecutionTimeMs());
    }
}
