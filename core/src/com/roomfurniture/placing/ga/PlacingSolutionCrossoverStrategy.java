package com.roomfurniture.placing.ga;

import com.google.common.collect.ImmutableList;
import com.roomfurniture.ga.algorithm.interfaces.CrossoverStrategy;
import com.roomfurniture.ga.implementations.list.PermutationListCrossoverStrategy;
import com.roomfurniture.placing.PlacingDescriptor;
import com.roomfurniture.placing.PlacingProblem;
import com.roomfurniture.placing.PlacingSolution;
import com.roomfurniture.problem.Furniture;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlacingSolutionCrossoverStrategy implements CrossoverStrategy<PlacingSolution> {
    PermutationListCrossoverStrategy<PlacingDescriptor> delegate = new PermutationListCrossoverStrategy<>();
    private final PlacingProblem problem;

    public PlacingSolutionCrossoverStrategy(PlacingProblem problem) {
        this.problem = problem;
    }

    @Override
    public ImmutableList<PlacingSolution> crossover(PlacingSolution parentA, PlacingSolution parentB) {

         Set<Furniture> seenA = new HashSet<>();
        Set<Furniture> seenB = new HashSet<>();

        List<PlacingDescriptor> childA = new ArrayList<>();
        List<PlacingDescriptor> childB = new ArrayList<>();

        List<PlacingDescriptor> parentBDescriptors = parentB.getDescriptors();
        List<PlacingDescriptor> parentADescriptors = parentA.getDescriptors();

        for (int i = 0; i < parentADescriptors.size(); i++) {
            PlacingDescriptor elementA = parentADescriptors.get(i);
            PlacingDescriptor elementB = parentBDescriptors.get(i);

            Furniture elementAFurniture = elementA.getFurniture(problem);
            Furniture elementBFurniture = elementB.getFurniture(problem);

            if (!seenA.contains(elementAFurniture)) {
                childA.add(elementA);
                seenA.add(elementAFurniture);
            }
            if (!seenA.contains(elementBFurniture)) {
                childA.add(elementB);
                seenA.add(elementBFurniture);
            }


            if (!seenB.contains(elementBFurniture)) {
                childB.add(elementB);
                seenB.add(elementBFurniture);
            }
            if (!seenB.contains(elementAFurniture)) {
                childB.add(elementA);
                seenB.add(elementAFurniture);
            }
        }

        if((childA.size() + childB.size())/2 != parentADescriptors.size()) {
            System.out.println("\n\n\n\n\n==================================================================\n" +
                    "BigError Big Error lost some children\n" +
                     "childA(" + childA.size() + "): " + childA +"\n" +
                    "childB(" + childB.size() + "): " + childB + "\n" +
                    "parentA(" + parentADescriptors.size() + "): " + parentADescriptors + "\n" +
                    "parentB("+ parentBDescriptors.size() +"): "+ parentBDescriptors + "\n" +
            "================================================================================\n\n\n\n\n");
        }


        return ImmutableList.of(
                new PlacingSolution(childA),
                new PlacingSolution(childB)
        );


    }
}
