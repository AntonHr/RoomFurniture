package com.roomfurniture.placing.plain;


import com.roomfurniture.ga.algorithm.interfaces.MutationStrategy;
import com.roomfurniture.ga.implementations.list.PermutationListMutationStrategy;
import com.roomfurniture.placing.PlacingDescriptor;
import com.roomfurniture.placing.PlacingSolution;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PlainPlacingSolutionMutationStrategy implements MutationStrategy<PlacingSolution> {
    private double mutationProbability;
    private final int maxPermutations;
    public PermutationListMutationStrategy<com.roomfurniture.placing.PlacingDescriptor> delegate;

    public PlainPlacingSolutionMutationStrategy(double mutationProbability, int maxPermutations) {
        this.mutationProbability = mutationProbability;
        this.maxPermutations = maxPermutations;
        delegate = new PermutationListMutationStrategy<>(mutationProbability, maxPermutations);
    }

    @Override
    public PlacingSolution mutate(PlacingSolution individual) {
        List<PlacingDescriptor> mutate = delegate.mutate(individual.getDescriptors());
        for(int i = 0; i < mutate.size(); i++) {
            if(ThreadLocalRandom.current().nextDouble() <mutationProbability ) {
                mutate.get(i).mutate(ThreadLocalRandom.current().nextDouble(), ThreadLocalRandom.current().nextDouble(), ThreadLocalRandom.current().nextDouble());
            }
        }

        return new PlacingSolution(mutate);
    }
}
