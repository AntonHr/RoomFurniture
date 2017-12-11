package com.roomfurniture.ga.list;

import com.roomfurniture.ga.algorithm.interfaces.MutationStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public class GaussianConvolutionListMutationStrategy<T extends Number> implements MutationStrategy<List<T>> {
    private final double mutationProbability;
    private final double minimumValue;
    private final double maximumValue;
    private final double variance;
    private Function<Double, T> setter;

    public GaussianConvolutionListMutationStrategy(double mutationProbability, double minimumValue, double maximumValue, double variance, Function<Double, T> setter) {
        this.mutationProbability = mutationProbability;
        this.minimumValue = minimumValue;
        this.maximumValue = maximumValue;
        this.variance = variance;
        this.setter = setter;
    }

    @Override
    public List<T> mutate(List<T> individual) {
        List<T> result = new ArrayList<>(individual);
        for (int i = 0; i < result.size(); i++) {
            if (ThreadLocalRandom.current().nextDouble(0, 1) < this.mutationProbability) {
                double n;
                double value = result.get(i).doubleValue();
                double updatedValue;
                do {
                    n = ThreadLocalRandom.current().nextGaussian() * this.variance;
                    updatedValue = value + n;
                    if (value - maximumValue > this.variance || minimumValue - value > this.variance) {
                        updatedValue = Math.max(Math.min(updatedValue, maximumValue), minimumValue);
                        break;
                    }
                } while (((value + n) > maximumValue) || ((value + n) < minimumValue));

                result.set(i, setter.apply(updatedValue));
            }
        }

        return result;
    }
}
