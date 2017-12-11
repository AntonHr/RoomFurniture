package com.roomfurniture.ga.list;

import com.google.common.collect.ImmutableList;
import com.roomfurniture.ga.algorithm.interfaces.CrossoverStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SinglePointListCrossoverStrategy<T> implements CrossoverStrategy<List<T>> {
    @Override
    public ImmutableList<List<T>> crossover(List<T> parentA, List<T> parentB) {
        assert (parentA.size() == parentB.size());
        List<T> childA = new ArrayList<>(),
                childB = new ArrayList<>();

        int crossoverPoint = ThreadLocalRandom.current().nextInt(0, parentA.size());

        for (int i = 0; i < parentA.size(); i++) {
            if (i < crossoverPoint) {
                childA.add(i, parentA.get(i));
                childB.add(i, parentB.get(i));
            } else {
                childA.add(i, parentB.get(i));
                childB.add(i, parentA.get(i));
            }
        }

        return ImmutableList.of(
                childA,
                childB
        );
    }
}
