package com.roomfurniture.placing.ga;

import com.google.common.collect.Streams;
import com.roomfurniture.ga.algorithm.interfaces.GeneratorStrategy;
import com.roomfurniture.placing.PlacingDescriptor;
import com.roomfurniture.placing.PlacingProblem;
import com.roomfurniture.placing.PlacingSolution;
import com.roomfurniture.problem.Furniture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlacingSolutionBiasedGeneratorStrategy implements GeneratorStrategy<PlacingSolution> {
    private final List<Integer> furnitures;
    private final int vertexCount;
    private final int importantCount;

    public PlacingSolutionBiasedGeneratorStrategy(PlacingProblem problem, int importantCount) {
        this.furnitures = new ArrayList<>();
        List<Furniture> problemFurnitures = problem.getFurnitures();
        assert (importantCount < problemFurnitures.size());
        this.importantCount = importantCount;

        for (int i = 0; i < problemFurnitures.size(); i++) {
            this.furnitures.add(i);
        }

        this.furnitures.sort(Comparator.comparingDouble(o -> problemFurnitures.get(o).getScore()));
        Collections.reverse(this.furnitures);

//        furnitures = ContiguousSet.create(Range.closed(0, problem.getFurnitures().size()), DiscreteDomain.integers());
        vertexCount = problem.getSpawnPoints().size();
    }

    @Override
    public PlacingSolution generate() {
         List<Integer> furniturePermutation = new ArrayList<>(furnitures);
        Stream<Integer> vertexStream = new ArrayList<>(furnitures).stream().map(integer -> ThreadLocalRandom.current().nextInt(0, vertexCount));

        // Shuffle the important ones seperate from the non important ones, so that the overall order is somewhat preserved
        Collections.shuffle(furniturePermutation.subList(importantCount, furniturePermutation.size()));
        Collections.shuffle(furniturePermutation.subList(0, importantCount));

        return new PlacingSolution(Streams.zip(furniturePermutation.stream(), vertexStream, PlacingDescriptor::new).collect(Collectors.toList()));
    }
}
