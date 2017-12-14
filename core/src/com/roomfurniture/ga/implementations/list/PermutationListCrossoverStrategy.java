package com.roomfurniture.ga.implementations.list;

import com.google.common.collect.ImmutableList;
import com.roomfurniture.ga.algorithm.interfaces.CrossoverStrategy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PermutationListCrossoverStrategy<T> implements CrossoverStrategy<List<T>> {
    @Override
    public ImmutableList<List<T>> crossover(List<T> parentA, List<T> parentB) {
        Set<T> seenA = new HashSet<>();
        Set<T> seenB = new HashSet<>();

        List<T> childA = new ArrayList<>();
        List<T> childB = new ArrayList<>();

        for (int i = 0; i < parentA.size(); i++) {
            T elementA = parentA.get(i);
            T elementB = parentB.get(i);

            if (!seenA.contains(elementA)) {
                childA.add(elementA);
                seenA.add(elementA);
            }
            if (!seenA.contains(elementB)) {
                childA.add(elementB);
                seenA.add(elementB);
            }


            if (!seenB.contains(elementB)) {
                childB.add(elementB);
                seenB.add(elementB);
            }
            if (!seenB.contains(elementA)) {
                childB.add(elementA);
                seenB.add(elementA);
            }
        }

        if((childA.size() + childB.size())/2 != parentA.size()) {
            System.out.println("\n\n\n\n\n==================================================================\n" +
                    "BigError Big Error lost some children\n" +
                     "childA(" + childA.size() + "): " + childA +"\n" +
                    "childB(" + childB.size() + "): " + childB + "\n" +
                    "parentA(" + parentA.size() + "): " + parentA + "\n" +
                    "parentB("+ parentB.size() +"): "+  parentB + "\n" +
            "================================================================================\n\n\n\n\n");
        }


        return ImmutableList.of(
                childA,
                childB
        );
    }
}
