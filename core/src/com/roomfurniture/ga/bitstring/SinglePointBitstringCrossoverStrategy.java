package com.roomfurniture.ga.bitstring;

import com.google.common.collect.ImmutableList;
import com.roomfurniture.ga.algorithm.interfaces.CrossoverStrategy;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class SinglePointBitstringCrossoverStrategy implements CrossoverStrategy<Bitstring> {
    @Override
    public ImmutableList<Bitstring> crossover(Bitstring parentA, Bitstring parentB) {
        assert(parentA.getSize() == parentB.getSize());
        Bitstring childA = new Bitstring(parentA.getSize()),
                  childB = new Bitstring(parentA.getSize());

        Random generator = new Random();
        int crossoverPoint = ThreadLocalRandom.current().nextInt(0, parentA.getSize());

        for(int i = 0; i < parentA.getSize(); i++) {
            if(i < crossoverPoint) {
                childA.put(i, parentA.get(i));
                childB.put(i, parentB.get(i));
            } else {
                childA.put(i, parentB.get(i));
                childB.put(i, parentA.get(i));
            }
        }

        return ImmutableList.of(
                childA,
                childB
        );
    }
}
