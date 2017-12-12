package com.roomfurniture.ga.implementations.list;

import com.roomfurniture.ga.algorithm.interfaces.GeneratorStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ListGeneratorStrategy<T extends Number> implements GeneratorStrategy<List<T>> {
    private final int size;
    private Supplier<T> generator;

    public ListGeneratorStrategy(int size, Supplier<T> generator) {
        this.size = size;
        this.generator = generator;
    }

    @Override
    public List<T> generate() {
        ArrayList<T> result = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            result.add(generator.get());
        }

        return result;
    }
}
