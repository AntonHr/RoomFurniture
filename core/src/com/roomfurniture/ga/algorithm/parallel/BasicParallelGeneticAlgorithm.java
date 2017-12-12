package com.roomfurniture.ga.algorithm.parallel;

import afu.org.checkerframework.checker.oigj.qual.O;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.roomfurniture.ga.algorithm.Result;
import com.roomfurniture.ga.algorithm.interfaces.*;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BasicParallelGeneticAlgorithm<T> implements ParallelGeneticAlgorithm<T> {
    private final int populationSize;
    private final MutationStrategy<T> mutationStrategy;
    private final CrossoverStrategy<T> crossoverStrategy;
    private final SelectionStrategy<T> selectionStrategy;
    private final EvaluationStrategy<T> evaluationStrategy;
    private final GeneratorStrategy<T> generatorStrategy;
    private List<Result<T>> population;
    private Optional<T> bestIndividual = Optional.empty();
    private Optional<Double> bestFitness = Optional.empty();
    private Optional<Double> averageFitness = Optional.empty();

    public BasicParallelGeneticAlgorithm(int populationSize, EvaluationStrategy<T> evaluationStrategy, CrossoverStrategy<T> crossoverStrategy, MutationStrategy<T> mutationStrategy, GeneratorStrategy<T> generatorStrategy, SelectionStrategy<T> selectionStrategy) {
        this.populationSize = populationSize;

        this.mutationStrategy = mutationStrategy;
        this.crossoverStrategy = crossoverStrategy;
        this.selectionStrategy = selectionStrategy;
        this.evaluationStrategy = evaluationStrategy;
        this.generatorStrategy = generatorStrategy;

        this.population = new ArrayList<>();
        for(int i = 0; i < this.populationSize; i++) {
            population.add(new Result<>(generatorStrategy.generate()));
        }

    }

    @Override
    public List<List<Result<T>>> getSubSamples(int count) {
        return Lists.partition(new ArrayList<>(population), count);
    }

    @Override
    public List<Result<T>> runEvolutionStep(List<Result<T>> sample) {
        // TODO(Kiran): Implement stateless GA here
        List<Result<T>> newSample = new ArrayList<>();
        for (Result<T> individual : sample) {
            if (!individual.getFitness().isPresent()) {
                individual.setFitness(evaluationStrategy.evaluate(individual.getValue()));
            }
        }

        for (int i = 0; i < sample.size() / 4; i++) {
            Result<T> parentA = this.selectionStrategy.selectWithReplacement(sample);
            Result<T> parentB = this.selectionStrategy.selectWithReplacement(sample);

            ImmutableList<T> children = this.crossoverStrategy.crossover(parentA.getValue(), parentB.getValue());
            assert (children.size() == 2);
            T childA = children.get(0);
            T childB = children.get(1);

            childA = this.mutationStrategy.mutate(childA);
            childB = this.mutationStrategy.mutate(childB);

            newSample.add(new Result<>(childA));
            newSample.add(new Result<>(childB));
        }

        // keep half of the old population
        for (int i = 0; i < sample.size() / 2; i++) {
            Result<T> parentA = this.selectionStrategy.selectWithReplacement(sample);
            newSample.add(parentA);
        }

        return newSample;
    }

    @Override
    public void recombinePopulation(List<List<Result<T>>> subSamples) {
        double fitnesssum = 0;
        long count = 0;
        List<Result<T>> newPopulation = new ArrayList<>();
        for (List<Result<T>> subSample : subSamples) {
            for (Result<T> sample : subSample) {

                // Only include in fitness sum calculation if has a fitness
                if (sample.getFitness().isPresent()) {
                    double fitness = sample.getFitness().get();

                    if (bestFitness.isPresent()) {
                        if (bestFitness.get() > fitness) {
                            bestFitness = Optional.of(fitness);
                            bestIndividual = Optional.of(sample.getValue());
                        }
                    } else {
                        bestFitness = Optional.of(fitness);
                        bestIndividual = Optional.of(sample.getValue());
                    }

                    fitnesssum += fitness;
                    count++;
                }

                newPopulation.add(sample);
            }
        }
        this.population = newPopulation;
        if (count != 0)
            this.averageFitness = Optional.of(fitnesssum / count);

    }

    @Override
    public Optional<Double> findAverageFitness() {
        return averageFitness;
    }

    @Override
    public Optional<T> findBestIndividual() {
        return bestIndividual;
    }

    @Override
    public Optional<Double> findBestFitness() {
        return bestFitness;
    }
}
