package com.roomfurniture.ga.problems.npartition;

import com.google.common.collect.ImmutableList;
import com.roomfurniture.ga.algorithm.SimpleGeneticAlgorithmRunner;
import com.roomfurniture.ga.algorithm.interfaces.GeneticAlgorithm;
import com.roomfurniture.ga.implementations.list.GaussianConvolutionListMutationStrategy;
import com.roomfurniture.ga.implementations.list.ListGeneratorStrategy;
import com.roomfurniture.ga.implementations.list.ListGeneticAlgorithmBuilder;
import com.roomfurniture.ga.implementations.list.SinglePointListCrossoverStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class NPartitionProblem {
    private final int partitionSize;
    private final List<Integer> items;

    public NPartitionProblem(int partitionSize, List<Integer> items) {
        this.partitionSize = partitionSize;
        this.items = items;
    }

    public Optional<Double> score(Solution solution) {
        ArrayList<Integer> scores = new ArrayList<>();
        for (int i = 0; i < partitionSize; i++) {
            scores.add(0);
        }

        List<Integer> partition = solution.getPartition();
        for (int i = 0; i < items.size(); i++) {
            Integer index = partition.get(i);
            Integer value = items.get(i);

            scores.set(index, scores.get(index) + value);
        }

        int totalScore = 0;

        for (int i = 0; i < partitionSize - 1; i++) {
            for (int j = i + 1; j < partitionSize; j++) {
                totalScore += Math.abs(scores.get(i) - scores.get(j));
            }
        }

        if (totalScore == 0) {
            return Optional.of(1.0);
        } else {
            return Optional.of(1.0 / totalScore);
        }
    }


    public static void main(String[] args) {
        NPartitionProblem instance = new NPartitionProblem(2, ImmutableList.of(4, 5, 6, 7, 8));
        GeneticAlgorithm<List<Integer>> algorithm = new ListGeneticAlgorithmBuilder<Integer>()
                .withMutationStrategy(new GaussianConvolutionListMutationStrategy<Integer>(0.3, 0.0, 1.0, 1.5, Double::intValue))
                .withCrossoverStrategy(new SinglePointListCrossoverStrategy<>())
                .withGeneratorStrategy(new ListGeneratorStrategy<>(5, () -> ThreadLocalRandom.current().nextInt(0, 2)))
                .withEvaluationStrategy(individual -> {
                    Solution solution = new Solution(individual);
                    return instance.score(solution).orElse(0.0);
                })
                .build();

        SimpleGeneticAlgorithmRunner<List<Integer>> runner = new SimpleGeneticAlgorithmRunner<>(algorithm, (level, message) -> System.out.println(message));

        runner.runTestIteration(200);

        System.out.println(runner.findBestIndividual());
        System.out.println(instance.score(new Solution(runner.findBestIndividual().get())));
    }
}
