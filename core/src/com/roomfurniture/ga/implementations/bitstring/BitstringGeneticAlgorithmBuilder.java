package com.roomfurniture.ga.implementations.bitstring;

import com.roomfurniture.ga.algorithm.BasicGeneticAlgorithm;
import com.roomfurniture.ga.algorithm.RouletteWheelSelectionStrategy;
import com.roomfurniture.ga.algorithm.interfaces.*;

import java.util.Optional;

public class BitstringGeneticAlgorithmBuilder {
    private int populationSize = 100;
    private MutationStrategy<Bitstring> mutationStrategy = new FixedProbabilityBitstringMutationStrategy(0.1);
    private SelectionStrategy<Bitstring> selectionStrategy = new RouletteWheelSelectionStrategy<>();
    private CrossoverStrategy<Bitstring> crossoverStrategy = new SinglePointBitstringCrossoverStrategy();

    // must be provided
    private Optional<GeneratorStrategy<Bitstring>> generatorStrategy = Optional.empty();
    private Optional<EvaluationStrategy<Bitstring>> evaluationStrategy = Optional.empty();

    public BitstringGeneticAlgorithmBuilder(){
    }


    public BitstringGeneticAlgorithmBuilder withMutationStrategy(MutationStrategy<Bitstring> mutationStrategy){
        this.mutationStrategy = mutationStrategy;
        return this;
    }


    public BitstringGeneticAlgorithmBuilder withCrossoverStrategy(CrossoverStrategy<Bitstring> crossoverStrategy){
        this.crossoverStrategy = crossoverStrategy;
        return this;
    }

    public BitstringGeneticAlgorithmBuilder withSelectionStrategy(SelectionStrategy<Bitstring> selectionStrategy) {
        this.selectionStrategy = selectionStrategy;
        return this;
    }

    public BitstringGeneticAlgorithmBuilder withPopulationSize(int populationSize) {
        this.populationSize = populationSize;
        return this;
    }

    public BitstringGeneticAlgorithmBuilder withEvaluator(EvaluationStrategy<Bitstring> evaluationStrategy){
        this.evaluationStrategy = Optional.of(evaluationStrategy);
        return this;
    }

    public BitstringGeneticAlgorithmBuilder withInputSize(int size) {
       this.generatorStrategy = Optional.of(new BitstringGeneratorStrategy(size));
       return this;
    }


    public GeneticAlgorithm<Bitstring> build(){
        if(!this.evaluationStrategy.isPresent()) {
            throw new RuntimeException("No Evaluation Strategy provided.");
        }
        else if(!this.generatorStrategy.isPresent()) {
            throw new RuntimeException("No Generator Strategy provided.");
        }

        return new BasicGeneticAlgorithm<Bitstring>(populationSize, evaluationStrategy.get(), crossoverStrategy, mutationStrategy, generatorStrategy.get(), selectionStrategy);
    }

}
