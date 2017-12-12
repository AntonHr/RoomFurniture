package com.roomfurniture.ga.algorithm.parallel;

import com.roomfurniture.ga.algorithm.Result;

import java.util.List;
import java.util.Optional;

public interface ParallelGeneticAlgorithm<T> {
    List<List<Result<T>>> getSubSamples(int threadPoolSize);
    List<Result<T>> runEvolutionStep(List<Result<T>> sample);
    void recombinePopulation(List<List<Result<T>>> subSamples);

    Optional<Double> findAverageFitness();
    Optional<T> findBestIndividual();

    Optional<Double> findBestFitness();
}
