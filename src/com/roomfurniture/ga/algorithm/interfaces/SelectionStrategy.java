package com.roomfurniture.ga.algorithm.interfaces;

import java.util.List;
import java.util.Map;

public interface SelectionStrategy<T> {
    public T selectWithReplacement(List<T> population, Map<T, Double> scores);
}
