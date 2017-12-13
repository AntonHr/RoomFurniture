package com.roomfurniture.placing.ga;

import com.google.common.collect.ImmutableList;
import com.roomfurniture.ga.algorithm.interfaces.CrossoverStrategy;
import com.roomfurniture.ga.implementations.list.PermutationListCrossoverStrategy;
import com.roomfurniture.placing.PlacingDescriptor;
import com.roomfurniture.placing.PlacingSolution;

import java.util.ArrayList;
import java.util.List;

public class PlacingSolutionCrossoverStrategyAdapter implements CrossoverStrategy<PlacingSolution> {
    PermutationListCrossoverStrategy<PlacingDescriptor> delegate = new PermutationListCrossoverStrategy<>();

    @Override
    public ImmutableList<PlacingSolution> crossover(PlacingSolution parentA, PlacingSolution parentB) {
        ImmutableList<List<PlacingDescriptor>> crossover = delegate.crossover(parentA.getDescriptors(), parentB.getDescriptors());
        return ImmutableList.of(
                new PlacingSolution(new ArrayList<>(crossover.get(0))),
                new PlacingSolution(new ArrayList<>(crossover.get(1)))
        );
    }
}
