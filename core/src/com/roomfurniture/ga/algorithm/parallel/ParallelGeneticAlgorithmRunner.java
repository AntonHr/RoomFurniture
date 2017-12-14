package com.roomfurniture.ga.algorithm.parallel;

import com.roomfurniture.ga.LoggingStrategy;
import com.roomfurniture.ga.algorithm.GeneticAlgorithmRunner;
import com.roomfurniture.ga.algorithm.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

public class ParallelGeneticAlgorithmRunner<T> implements GeneticAlgorithmRunner<T> {
    private int threadPoolSize;
    private ParallelGeneticAlgorithm<T> algorithm;
    private final LoggingStrategy loggingStrategy;
    private ExecutorService threadPoolExecutor;

    public ParallelGeneticAlgorithmRunner(int threadPoolSize, ParallelGeneticAlgorithm<T> algorithm) {
        this(threadPoolSize, algorithm,  (level, message) -> System.out.println(message));
   }
   public ParallelGeneticAlgorithmRunner(int threadPoolSize, ExecutorService service, ParallelGeneticAlgorithm<T> algorithm) {
        this(threadPoolSize, service, algorithm,  (level, message) -> System.out.println(message));
   }
   public ParallelGeneticAlgorithmRunner(int threadPoolSize, ParallelGeneticAlgorithm<T> algorithm, LoggingStrategy loggingStrategy) {
       this(threadPoolSize,Executors.newFixedThreadPool(threadPoolSize), algorithm, loggingStrategy);
       System.out.println("Made threaadpool");
   }

    public ParallelGeneticAlgorithmRunner(int threadPoolSize, ExecutorService service, ParallelGeneticAlgorithm<T> algorithm, LoggingStrategy loggingStrategy) {
        this.threadPoolSize = threadPoolSize;
        this.algorithm = algorithm;
        this.loggingStrategy = loggingStrategy;
        this.threadPoolExecutor = service;
    }

    public void runTestIteration(int iterations) {
        for (int i = 0; i < iterations; i++) {
            List<List<Result<T>>> subsamples = algorithm.getSubSamples(threadPoolSize);
            List<Future<List<Result<T>>>> jobResults = new ArrayList<>();
            List<List<Result<T>>> output = new ArrayList<>();

            for (List<Result<T>> sample : subsamples) {
                jobResults.add(threadPoolExecutor.submit(() -> algorithm.runEvolutionStep(sample)));
            }

            for (Future<List<Result<T>>> result : jobResults) {
                try {
                    output.add(result.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    this.loggingStrategy.error(e.getMessage());
                }
            }

            algorithm.recombinePopulation(output);

            Optional<Double> averageFitness = findAverageFitness();
            String fitnessString = String.valueOf(averageFitness);
            this.loggingStrategy.info("Iteration[" + (i + 1) +"/" + iterations +"]: Average Fitness = " + fitnessString);
        }
    }

    public Optional<Double> findAverageFitness() {
        return algorithm.findAverageFitness();
    }

    public Optional<T> findBestIndividual() {
        return algorithm.findBestIndividual();
    }

    @Override
    public Optional<Double> findBestFitness() {
        return algorithm.findBestFitness();
    }

    public void shutdown() {
        this.threadPoolExecutor.shutdown();
    }
}
