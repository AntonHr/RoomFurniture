package com.roomfurniture.ga.implementations.list;

import com.roomfurniture.ga.algorithm.interfaces.GeneratorStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PermutationListGeneratorStrategy<T> implements GeneratorStrategy<List<T>> {
    private final List<T> elements;

    public PermutationListGeneratorStrategy(List<T> elements) {
        this.elements = elements;
    }

    @Override
    public List<T> generate() {
        List<T> copy = new ArrayList<>(elements);
        Collections.shuffle(copy);
        return copy;
    }
}
