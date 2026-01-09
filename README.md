# GACS - Genetic Algorithm Constraint Solver

A generic, reusable Java library for solving constraint satisfaction problems using genetic algorithms.

## Features

- **Generic Design**: Works with any problem domain through interfaces
- **Configurable**: Flexible GA parameters (population size, mutation rate, crossover rate, etc.)
- **Multiple Selection Methods**: Roulette wheel and tournament selection
- **Elitism Support**: Preserve best solutions across generations
- **Production Ready**: Includes serialization, deep cloning, and error handling

## Project Structure

```
GACS/
├── pom.xml
└── src/
    └── main/
        └── java/
            └── ke/ac/ku/gacs/
                ├── core/
                │   └── GeneticAlgorithmSolver.java
                └── model/
                    ├── Problem.java
                    ├── Chromosome.java
                    ├── ChromosomeFactory.java
                    ├── FitnessEvaluator.java
                    ├── GAConfig.java
                    ├── GAResult.java
                    └── SelectionMethod.java
```

## Installation

### Building the Library

```bash
cd GACS
mvn clean install
```

This creates `gacs-1.0.0.jar` in the `target/` directory.

### Using in Your Project
 Add the JAR to your classpath.

## Usage

### 1. Define Your Problem

```java
public class MyProblem implements Problem {
    // Your problem data
    
    @Override
    public boolean isValid() {
        // Validation logic
        return true;
    }
    
    @Override
    public String getDescription() {
        return "My custom problem";
    }
}
```

### 2. Implement Your Chromosome

```java
public class MyChromosome implements Chromosome<MyProblem> {
    private double fitness;
    
    @Override
    public double getFitness() {
        return fitness;
    }
    
    @Override
    public void setFitness(double fitness) {
        this.fitness = fitness;
    }
    
    @Override
    public MyChromosome deepClone() {
        // Deep copy logic
        return new MyChromosome(/* copy data */);
    }
    
    @Override
    public MyChromosome crossover(Chromosome<MyProblem> other) {
        // Crossover logic
        return offspring;
    }
    
    @Override
    public void mutate(GAConfig config) {
        // Mutation logic
    }
}
```

### 3. Create Factory and Evaluator

```java
public class MyChromosomeFactory implements ChromosomeFactory<MyProblem, MyChromosome> {
    @Override
    public MyChromosome create(MyProblem problem) {
        return new MyChromosome(problem);
    }
}

public class MyFitnessEvaluator implements FitnessEvaluator<MyProblem, MyChromosome> {
    @Override
    public double evaluate(MyChromosome chromosome, MyProblem problem) {
        // Calculate fitness (0.0 to 1.0)
        return fitness;
    }
}
```

### 4. Configure and Run

```java
// Configure the genetic algorithm
GAConfig config = GAConfig.builder()
    .populationSize(1000)
    .maxGenerations(100)
    .crossoverRate(0.8)
    .mutationRate(0.1)
    .elitismRate(0.1)
    .targetFitness(1.0)
    .selectionMethod(SelectionMethod.ROULETTE)
    .verbose(true)
    .logInterval(10)
    .build();

// Create problem
MyProblem problem = new MyProblem();

// Create solver
GeneticAlgorithmSolver<MyProblem, MyChromosome> solver = 
    new GeneticAlgorithmSolver<>(
        config,
        new MyChromosomeFactory(),
        new MyFitnessEvaluator()
    );

// Solve
GAResult<MyProblem, MyChromosome> result = solver.solve(problem);

// Get results
if (result.isSuccess()) {
    MyChromosome solution = result.getBestSolution();
    System.out.println("Found solution with fitness: " + result.getFinalFitness());
}
```

## Example: Timetable Scheduling

See the `timetable` package for a complete implementation of school timetable scheduling using GACS.

Key files:
- `TimetableProblem.java` - Problem definition
- `TimetableChromosome.java` - Solution representation
- `TimetableFitnessEvaluator.java` - Fitness calculation
- `TimetableSolver.java` - Main solver

## Configuration Options

| Parameter | Description | Default |
|-----------|-------------|---------|
| populationSize | Number of chromosomes per generation | 1000 |
| maxGenerations | Maximum number of generations | 100 |
| crossoverRate | Probability of crossover (0.0-1.0) | 0.8 |
| mutationRate | Probability of mutation (0.0-1.0) | 0.1 |
| elitismRate | Fraction of top performers to preserve | 0.1 |
| targetFitness | Stop when this fitness is reached | 1.0 |
| selectionMethod | ROULETTE or TOURNAMENT | ROULETTE |
| verbose | Print progress logs | true |
| logInterval | Log every N generations | 10 |

## Integration with Spring Boot

```java
@Service
public class MySchedulerService {
    
    public MyResult solve(MyRequest request) {
        // Configure from request
        GAConfig config = GAConfig.builder()
            .populationSize(request.getPopulationSize())
            .maxGenerations(request.getMaxGenerations())
            .build();
        
        // Create and solve problem
        MyProblem problem = createProblem(request);
        GeneticAlgorithmSolver<MyProblem, MyChromosome> solver = 
            new GeneticAlgorithmSolver<>(config, factory, evaluator);
        
        GAResult<MyProblem, MyChromosome> result = solver.solve(problem);
        
        // Convert to response
        return convertToResponse(result);
    }
}
```

## Thread Safety

The library is not thread-safe by design for performance. Create separate solver instances for concurrent operations:

```java
ExecutorService executor = Executors.newFixedThreadPool(4);
List<Future<GAResult>> futures = new ArrayList<>();

for (Problem problem : problems) {
    futures.add(executor.submit(() -> {
        GeneticAlgorithmSolver solver = new GeneticAlgorithmSolver(...);
        return solver.solve(problem);
    }));
}
```

## Performance Tips

1. **Start with smaller populations** and fewer generations for testing
2. **Tune mutation rate**: Higher for exploration, lower for exploitation
3. **Use elitism** to preserve good solutions
4. **Profile fitness evaluation**: This is usually the bottleneck
5. **Consider caching**: If fitness calculation is expensive
6. **Early stopping**: Set `targetFitness` to stop when good enough