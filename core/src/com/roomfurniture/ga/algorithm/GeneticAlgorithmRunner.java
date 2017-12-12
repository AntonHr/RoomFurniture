package com.roomfurniture.ga.algorithm;

import java.util.Optional;

/**
 * Created by Gopiandcode on 12/12/2017.
 */
public interface GeneticAlgorithmRunner<T> {
    void runTestIteration(int iterations);

    Optional<T> findBestIndividual();

    Optional<Double> findAverageFitness();
}
