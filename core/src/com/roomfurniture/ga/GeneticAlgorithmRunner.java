package com.roomfurniture.ga;

import com.roomfurniture.ga.algorithm.interfaces.GeneticAlgorithm;

import java.util.Map;
import java.util.Optional;

public class GeneticAlgorithmRunner<T> {

    private GeneticAlgorithm<T> algorithm;
    private LoggingStrategy loggingStrategy;

    public GeneticAlgorithmRunner(GeneticAlgorithm<T> algorithm, LoggingStrategy loggingStrategy) {
        this.algorithm = algorithm;
        this.loggingStrategy = loggingStrategy;
    }

    private Optional<Double> findAverageFitness(Map<T, Double> scores) {

        Double total = 0.0;
        Integer count = 0;
        for(Map.Entry<T, Double> entry : scores.entrySet()) {
            total += entry.getValue();
            count += 1;
        }

        if (count != 0){
            return Optional.of(total/count);
        }
        else {
            return Optional.empty();
        }
    }

    private Optional<T> findBestIndividual(Map<T, Double> scores) {
        // find best individual
        Optional<T> bestIndividual = Optional.empty();
        Optional<Double> bestScore = Optional.empty();

        for(Map.Entry<T, Double> entry : scores.entrySet()) {
            double score = entry.getValue();

            if (!bestScore.isPresent() || bestScore.get().compareTo(score) < 0) {
                bestScore = Optional.of(score);
                bestIndividual = Optional.of(entry.getKey());
            }
        }
        System.out.println(bestScore);
        return bestIndividual;
    }


    public void runTestIteration(int iterations) {
        for(int i = 0; i < iterations; i++){
            // Evaluate the population
            Map<T, Double> fitness = this.algorithm.calculateFitness();

            // log average fitness
            Optional<Double> averageFitness = findAverageFitness(fitness);
            String fitnessString = String.valueOf(averageFitness);
            this.loggingStrategy.info("Iteration[" + (i + 1) +"/" + iterations +"]: Average Fitness = " + fitnessString);

            // train the population
            this.algorithm.runTrainIteration(fitness);
        }
    }

    public Optional<T> findBestIndividual(){
        Map<T, Double> fitness = this.algorithm.calculateFitness();
        return this.findBestIndividual(fitness);
    }

}
