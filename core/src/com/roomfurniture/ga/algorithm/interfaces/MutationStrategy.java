package com.roomfurniture.ga.algorithm.interfaces;

public interface MutationStrategy<T> {
    public T mutate(T individual);
}
