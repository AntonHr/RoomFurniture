package com.roomfurniture.solution;

import com.google.common.collect.ImmutableList;
import com.roomfurniture.ga.algorithm.interfaces.CrossoverStrategy;
import com.roomfurniture.problem.Descriptor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SolutionCrossoverStrategy implements CrossoverStrategy<Solution> {
    @Override
    public ImmutableList<Solution> crossover(Solution parentA, Solution parentB) {
        //TODO(Kiran): Implement strategy
        List<Descriptor> parentADescriptors = parentA.getDescriptors();
        List<Descriptor> parentBDescriptors = parentB.getDescriptors();
        int crossoverPoint = ThreadLocalRandom.current().nextInt(0, parentADescriptors.size());

        List<Descriptor> childA = new ArrayList<>();
        List<Descriptor> childB = new ArrayList<>();

        for (int i = 0; i < parentADescriptors.size(); i++) {
            if (i < crossoverPoint) {
                childA.add(parentADescriptors.get(i));
                childB.add(parentBDescriptors.get(i));
            } else {
                childB.add(parentADescriptors.get(i));
                childA.add(parentBDescriptors.get(i));
            }
        }
        return ImmutableList.of(
                new Solution(childA),
                new Solution(childB)
        );
    }
}
