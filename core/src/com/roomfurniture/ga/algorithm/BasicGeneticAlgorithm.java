package com.roomfurniture.ga.algorithm;

import com.google.common.collect.ImmutableList;
import com.roomfurniture.ga.algorithm.interfaces.*;

import javax.swing.text.html.Option;
import java.util.*;

public class BasicGeneticAlgorithm <T> implements GeneticAlgorithm<T> {
    private int populationSize;
    private final EvaluationStrategy<T> evolutionStrategy;
    private final CrossoverStrategy<T> crossoverStrategy;
    private final MutationStrategy<T> mutationStrategy;
    private GeneratorStrategy<T> generatorStrategy;
    private SelectionStrategy<T> selectionStrategy;
    private List<Result<T>> population;
    private Optional<T> bestIndividual = Optional.empty();
    private Optional<Double> bestFitness = Optional.empty();
    private Optional<Double> averageFitness = Optional.empty();

    public BasicGeneticAlgorithm(int populationSize, EvaluationStrategy<T> evolutionStrategy, CrossoverStrategy<T> crossoverStrategy, MutationStrategy<T> mutationStrategy,
                                 GeneratorStrategy<T> generatorStrategy, SelectionStrategy<T> selectionStrategy){
        this.populationSize = populationSize;
        this.evolutionStrategy = evolutionStrategy;
        this.crossoverStrategy = crossoverStrategy;
        this.mutationStrategy = mutationStrategy;
        this.generatorStrategy = generatorStrategy;
        this.selectionStrategy = selectionStrategy;

        population = new ArrayList<>();


        for(int i = 0; i < this.populationSize; i++) {
            population.add(new Result<>(generatorStrategy.generate()));
        }
    }

    @Override
    public void calculateFitness() {
        Map<T, Double> scores = new LinkedHashMap<>();
        double fitnesssum = 0;
        long count = 0;
        for(Result<T> individual : population) {
            double fitness;
            if(!individual.getFitness().isPresent()) {
                fitness = this.evolutionStrategy.evaluate(individual.getValue());
                individual.setFitness(fitness);
            } else {
                fitness = individual.getFitness().get();
            }
            if (bestFitness.isPresent()) {
                    if (bestFitness.get() < fitness) {
                        bestFitness = Optional.of(fitness);
                        bestIndividual = Optional.of(individual.getValue());
                    }
            } else {
                    bestFitness = Optional.of(fitness);
                    bestIndividual = Optional.of(individual.getValue());
            }


            fitnesssum += fitness;
            count += 1;
        }

        if(count != 0)
            averageFitness = Optional.of(fitnesssum/count);
    }


    @Override
    public void runTrainIteration(){
        // perform crossover and generate new population
        List<Result<T>> newPop = new ArrayList<>();
        for(int i = 0; i < (int)this.populationSize/2; i++){
            Result<T> parentA = this.selectionStrategy.selectWithReplacement(this.population);
            Result<T> parentB = this.selectionStrategy.selectWithReplacement(this.population);

            ImmutableList<T> children = this.crossoverStrategy.crossover(parentA.getValue(),parentB.getValue());
            assert(children.size() == 2);
            T childA = children.get(0);
            T childB = children.get(1);

            childA = this.mutationStrategy.mutate(childA);
            childB = this.mutationStrategy.mutate(childB);

            newPop.add(new Result<>(childA));
            newPop.add(new Result<>(childB));
        }

        this.population = newPop;
    }

    public List<Result<T>> getPopulation() {
        return population;
    }

    public Optional<T> findBestIndividual() {
        return bestIndividual;
    }

    public Optional<Double> findBestFitness() {
        return bestFitness;
    }

    public Optional<Double> findAverageFitness() {
        return averageFitness;
    }
}
