package com.roomfurniture.ga.algorithm.interfaces;

import com.google.common.collect.ImmutableList;

public interface CrossoverStrategy<T> {
    public ImmutableList<T> crossover(T parentA, T parentB);
}
