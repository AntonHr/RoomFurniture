package com.roomfurniture.ga.bitstring;

import com.roomfurniture.ga.algorithm.interfaces.GeneratorStrategy;

public class BitstringGeneratorStrategy implements GeneratorStrategy<Bitstring> {
    private int size;

    public BitstringGeneratorStrategy(int size) {
        this.size = size;
    }

    @Override
    public Bitstring generate() {
        return Bitstring.generateRandom(size);
    }
}
