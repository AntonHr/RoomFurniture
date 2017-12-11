package com.roomfurniture.ga.list;

import com.roomfurniture.ga.algorithm.interfaces.MutationStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PermutationListMutationStrategy<T> implements MutationStrategy<List<T>> {

    public final double mutationProbability;
    public final int maxPermutations;

    public PermutationListMutationStrategy(double mutationProbability, int maxPermutations) {
        this.mutationProbability = mutationProbability;
        this.maxPermutations = maxPermutations;
    }

    @Override
    public List<T> mutate(List<T> individual) {
        List<T> copy = new ArrayList<>(individual);
        int currentPermutations = 0;

        double probability = ThreadLocalRandom.current().nextDouble();
        while (ThreadLocalRandom.current().nextDouble() < mutationProbability && currentPermutations < maxPermutations) {
            currentPermutations += 1;
            int positionA = ThreadLocalRandom.current().nextInt(0, individual.size());
            int positionB = ThreadLocalRandom.current().nextInt(0, individual.size());

            T temp = copy.get(positionA);
            copy.set(positionA, copy.get(positionB));
            copy.set(positionB, temp);
        }

        return copy;
    }
}
