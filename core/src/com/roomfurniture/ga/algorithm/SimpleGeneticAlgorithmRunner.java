package com.roomfurniture.ga.algorithm;

import com.roomfurniture.ga.LoggingStrategy;
import com.roomfurniture.ga.algorithm.GeneticAlgorithmRunner;
import com.roomfurniture.ga.algorithm.interfaces.GeneticAlgorithm;

import java.util.Map;
import java.util.Optional;

public class SimpleGeneticAlgorithmRunner<T> implements GeneticAlgorithmRunner<T> {

    private GeneticAlgorithm<T> algorithm;
    private LoggingStrategy loggingStrategy;

    public SimpleGeneticAlgorithmRunner(GeneticAlgorithm<T> algorithm, LoggingStrategy loggingStrategy) {
        this.algorithm = algorithm;
        this.loggingStrategy = loggingStrategy;
    }


    @Override
    public void runTestIteration(int iterations) {
        for(int i = 0; i < iterations; i++){
            // Evaluate the population
            this.algorithm.calculateFitness();

            // log average fitness
            Optional<Double> averageFitness = findAverageFitness();
            String fitnessString = String.valueOf(averageFitness);
            this.loggingStrategy.info("Iteration[" + (i + 1) +"/" + iterations +"]: Average Fitness = " + fitnessString);

            // train the population
            this.algorithm.runTrainIteration();
        }
    }

    @Override
    public Optional<T> findBestIndividual(){
        return algorithm.findBestIndividual();
    }
    @Override
    public Optional<Double> findAverageFitness() {
        return algorithm.findAverageFitness();
    }

}
