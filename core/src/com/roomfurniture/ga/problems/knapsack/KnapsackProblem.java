package com.roomfurniture.ga.problems.knapsack;

import com.roomfurniture.ga.algorithm.SimpleGeneticAlgorithmRunner;
import com.roomfurniture.ga.algorithm.interfaces.GeneticAlgorithm;
import com.roomfurniture.ga.implementations.list.GaussianConvolutionListMutationStrategy;
import com.roomfurniture.ga.implementations.list.ListGeneratorStrategy;
import com.roomfurniture.ga.implementations.list.ListGeneticAlgorithmBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class KnapsackProblem {
    public final int maximumCapacity;
    public final ArrayList<Item> items;


    public KnapsackProblem(int maximumCapacity, ArrayList<Item> items) {
        this.maximumCapacity = maximumCapacity;
        this.items = items;
    }


    public Optional<Integer> score(Solution solution) {
        ArrayList<Integer> solutionItemCounts = solution.getSolutionItemCounts();
        if (solutionItemCounts.size() != items.size()) throw new RuntimeException("Solution has more entries than items in this problem instance");

        int volume = 0;
        int benefit = 0;

        for (int i = 0; i < items.size(); i++) {
            volume += solutionItemCounts.get(i) * items.get(i).getVolume();
            benefit += solutionItemCounts.get(i) * items.get(i).getBenefit();
            if (volume >= maximumCapacity) return Optional.empty();
        }

        return Optional.of(benefit);
    }


    public static void main(String[] args) {
        // Example instance of problem
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item(4, 6));
        items.add(new Item(3, 7));
        items.add(new Item(5, 8));
        KnapsackProblem problem = new KnapsackProblem(13, items);


        GaussianConvolutionListMutationStrategy<Integer> mutationStrategy = new GaussianConvolutionListMutationStrategy<>(0.5, 0, 10, 2.5, Double::intValue);
        ListGeneratorStrategy<Integer> generatorStrategy = new ListGeneratorStrategy<>(3, () -> Math.max(Math.min(ThreadLocalRandom.current().nextInt(0, 10), 10), 0));

        GeneticAlgorithm<List<Integer>> geneticAlgorithm = new ListGeneticAlgorithmBuilder<Integer>()
                .withMutationStrategy(mutationStrategy)
                .withGeneratorStrategy(generatorStrategy)
                .withEvaluationStrategy(individual -> {
                    Solution solution = new Solution(new ArrayList<>(individual));
                    return problem.score(solution).orElse(0).doubleValue();
                })
                .build();

        SimpleGeneticAlgorithmRunner<List<Integer>> geneticAlgorithmRunner = new SimpleGeneticAlgorithmRunner<>(geneticAlgorithm, (level, message) -> System.out.println(message));

        geneticAlgorithmRunner.runTestIteration(1000);
        System.out.println(geneticAlgorithmRunner.findBestIndividual());
    }
}
