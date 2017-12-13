package com.roomfurniture.placing.ga;

import com.roomfurniture.ga.algorithm.interfaces.MutationStrategy;
import com.roomfurniture.ga.implementations.list.PermutationListMutationStrategy;
import com.roomfurniture.placing.PlacingSolution;

public class PlacingSolutionMutationStrategyAdapter implements MutationStrategy<PlacingSolution> {
    public PermutationListMutationStrategy<com.roomfurniture.placing.PlacingDescriptor> delegate;

    public PlacingSolutionMutationStrategyAdapter(double mutationProbability, int maxPermutations) {
        delegate = new PermutationListMutationStrategy<>(mutationProbability, maxPermutations);
    }

    @Override
    public PlacingSolution mutate(PlacingSolution individual) {
        return new PlacingSolution(delegate.mutate(individual.getDescriptors()));
    }
}
