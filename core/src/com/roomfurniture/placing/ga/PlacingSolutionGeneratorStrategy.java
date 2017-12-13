package com.roomfurniture.placing.ga;

import com.google.common.collect.Streams;
import com.roomfurniture.ga.algorithm.interfaces.GeneratorStrategy;
import com.roomfurniture.placing.PlacingDescriptor;
import com.roomfurniture.placing.PlacingProblem;
import com.roomfurniture.placing.PlacingSolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlacingSolutionGeneratorStrategy implements GeneratorStrategy<PlacingSolution> {
    private final int vertexCount;
    private List<Integer> furnitures;

    public PlacingSolutionGeneratorStrategy(PlacingProblem problem) {

        furnitures = new ArrayList<>();
        for (int i = 0; i < problem.getFurnitures().size(); i++) {
            furnitures.add(i);
        }
//        furnitures = ContiguousSet.create(Range.closed(0, problem.getFurnitures().size()), DiscreteDomain.integers());
        vertexCount = problem.getSpawnPoints().size();
    }

    @Override
    public PlacingSolution generate() {

        List<Integer> furniturePermutation = new ArrayList<>(furnitures);
        Stream<Integer> vertexStream = new ArrayList<>(furnitures).stream().map(integer -> ThreadLocalRandom.current().nextInt(0, vertexCount));

        Collections.shuffle(furniturePermutation);
        return new PlacingSolution(Streams.zip(furniturePermutation.stream(), vertexStream, PlacingDescriptor::new).collect(Collectors.toList()));
    }
}
