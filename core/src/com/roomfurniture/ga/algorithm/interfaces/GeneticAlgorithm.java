package com.roomfurniture.ga.algorithm.interfaces;

import java.util.Map;
import java.util.Optional;


public interface GeneticAlgorithm<T> {
    void calculateFitness();

    void runTrainIteration();

    Optional<T> findBestIndividual();

    Optional<Double> findAverageFitness();
}
