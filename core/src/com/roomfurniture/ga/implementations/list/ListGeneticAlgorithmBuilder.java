package com.roomfurniture.ga.implementations.list;

import com.roomfurniture.ga.algorithm.BasicGeneticAlgorithm;
import com.roomfurniture.ga.algorithm.RouletteWheelSelectionStrategy;
import com.roomfurniture.ga.algorithm.interfaces.*;

import java.util.List;
import java.util.Optional;

public class ListGeneticAlgorithmBuilder<T> {
    private int populationSize = 100;
    private SelectionStrategy<List<T>> selectionStrategy = new RouletteWheelSelectionStrategy<>();
    private CrossoverStrategy<List<T>> crossoverStrategy = new SinglePointListCrossoverStrategy<>();

    // must be provided
    private Optional<MutationStrategy<List<T>>> mutationStrategy = Optional.empty();
    private Optional<GeneratorStrategy<List<T>>> generatorStrategy = Optional.empty();
    private Optional<EvaluationStrategy<List<T>>> evaluationStrategy = Optional.empty();

    public ListGeneticAlgorithmBuilder() {
    }

    public ListGeneticAlgorithmBuilder<T> withPopulationSize(int populationSize) {
        this.populationSize = populationSize;
        return this;
    }

    public ListGeneticAlgorithmBuilder<T> withSelectionStrategy(SelectionStrategy<List<T>> selectionStrategy) {
        this.selectionStrategy = selectionStrategy;
        return this;
    }

    public ListGeneticAlgorithmBuilder<T> withCrossoverStrategy(CrossoverStrategy<List<T>> crossoverStrategy) {
        this.crossoverStrategy = crossoverStrategy;
        return this;
    }

    public ListGeneticAlgorithmBuilder<T> withMutationStrategy(MutationStrategy<List<T>> mutationStrategy) {
        this.mutationStrategy = Optional.of(mutationStrategy);
        return this;
    }

    public ListGeneticAlgorithmBuilder<T> withGeneratorStrategy(GeneratorStrategy<List<T>> generatorStrategy) {
        this.generatorStrategy = Optional.of(generatorStrategy);
        return this;
    }

    public ListGeneticAlgorithmBuilder<T> withEvaluationStrategy(EvaluationStrategy<List<T>> evaluationStrategy) {
        this.evaluationStrategy = Optional.of(evaluationStrategy);
        return this;
    }


    public GeneticAlgorithm<List<T>> build() {
        if (!this.mutationStrategy.isPresent())
            throw new RuntimeException("No Mutation Strategy provided.");
        if (!this.generatorStrategy.isPresent())
            throw new RuntimeException("No Generator Strategy provided.");
        if (!this.evaluationStrategy.isPresent())
            throw new RuntimeException("No Evaluation Strategy provided.");

        return new BasicGeneticAlgorithm<>(this.populationSize, this.evaluationStrategy.get(), this.crossoverStrategy, this.mutationStrategy.get(), this.generatorStrategy.get(), this.selectionStrategy);
    }


}
