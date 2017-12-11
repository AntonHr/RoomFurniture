package com.roomfurniture.ga.bitstring;

import com.roomfurniture.ga.algorithm.interfaces.MutationStrategy;

import java.util.concurrent.ThreadLocalRandom;

public class FixedProbabilityBitstringMutationStrategy implements MutationStrategy<Bitstring> {
    private final double mutationProbability;

    public FixedProbabilityBitstringMutationStrategy(double mutationProbability) {
        this.mutationProbability = mutationProbability;
    }

    @Override
    public Bitstring mutate(Bitstring individual) {
        Bitstring result = individual.copy();
        for(int i = 0; i< individual.getSize(); i++) {
            if(ThreadLocalRandom.current().nextDouble(0, 1) < mutationProbability) {
                result.put(i, ThreadLocalRandom.current().nextBoolean());
            } else {
                result.put(i, individual.get(i));
            }
        }
        return result;
    }
}
