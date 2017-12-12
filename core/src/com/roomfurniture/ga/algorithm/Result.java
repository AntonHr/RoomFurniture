package com.roomfurniture.ga.algorithm;

import java.util.Optional;

public class Result<T> {
    private final T value;
    private Optional<Double> fitness;
    public Result(T value, double fitness) {
        this.value = value;
        this.fitness = Optional.of(fitness);
    }

    public Result(T value) {
        this.value = value;
        this.fitness = Optional.empty();
    }

    public Optional<Double> getFitness() {
        return fitness;
    }

    public T getValue() {
        return value;
    }

    public void setFitness(Double fitness) {
        this.fitness = Optional.of(fitness);
    }
}
