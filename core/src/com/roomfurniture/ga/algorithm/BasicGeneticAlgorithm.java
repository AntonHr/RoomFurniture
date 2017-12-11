package com.roomfurniture.ga.algorithm;

import com.google.common.collect.ImmutableList;
import com.roomfurniture.ga.algorithm.interfaces.*;

import java.util.*;

public class BasicGeneticAlgorithm <T> implements GeneticAlgorithm<T> {
    private int populationSize;
    private final EvaluationStrategy<T> evolutionStrategy;
    private final CrossoverStrategy<T> crossoverStrategy;
    private final MutationStrategy<T> mutationStrategy;
    private GeneratorStrategy<T> generatorStrategy;
    private SelectionStrategy<T> selectionStrategy;
    private List<T> population;

    public BasicGeneticAlgorithm(int populationSize, EvaluationStrategy<T> evolutionStrategy, CrossoverStrategy<T> crossoverStrategy, MutationStrategy<T> mutationStrategy,
                                 GeneratorStrategy<T> generatorStrategy, SelectionStrategy<T> selectionStrategy){
        this.populationSize = populationSize;
        this.evolutionStrategy = evolutionStrategy;
        this.crossoverStrategy = crossoverStrategy;
        this.mutationStrategy = mutationStrategy;
        this.generatorStrategy = generatorStrategy;
        this.selectionStrategy = selectionStrategy;

        population = new ArrayList<T>();


        for(int i = 0; i < this.populationSize; i++) {
            population.add(generatorStrategy.generate());
        }
    }

    @Override
    public Map<T, Double> calculateFitness() {
        Map<T, Double> scores = new LinkedHashMap<>();
        for(T individual : population) {
            double score = this.evolutionStrategy.evaluate(individual);

            scores.put(individual, score);
        }
        return scores;
    }


    @Override
    public void runTrainIteration(Map<T, Double> scores){
        // perform crossover and generate new population
        List<T> newPop = new ArrayList<>();
        for(int i = 0; i < (int)this.populationSize/2; i++){
            T parentA = this.selectionStrategy.selectWithReplacement(this.population, scores);
            T parentB = this.selectionStrategy.selectWithReplacement(this.population, scores);

            ImmutableList<T> children = this.crossoverStrategy.crossover(parentA,parentB);
            assert(children.size() == 2);
            T childA = children.get(0);
            T childB = children.get(1);

            childA = this.mutationStrategy.mutate(childA);
            childB = this.mutationStrategy.mutate(childB);

            newPop.add(childA);
            newPop.add(childB);
        }

        this.population = newPop;
    }



}
