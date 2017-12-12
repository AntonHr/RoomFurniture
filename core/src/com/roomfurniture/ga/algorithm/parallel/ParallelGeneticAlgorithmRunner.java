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
    private final ExecutorService threadPoolExecutor;

    public ParallelGeneticAlgorithmRunner(int threadPoolSize, ParallelGeneticAlgorithm<T> algorithm, LoggingStrategy loggingStrategy) {
        this.threadPoolSize = threadPoolSize;
        this.algorithm = algorithm;
        this.loggingStrategy = loggingStrategy;
        threadPoolExecutor = Executors.newFixedThreadPool(threadPoolSize);
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

    public void shutdown() {
        this.threadPoolExecutor.shutdown();
    }
}
