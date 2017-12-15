package com.roomfurniture.placing.plain;

import com.roomfurniture.ga.algorithm.interfaces.GeneratorStrategy;
import com.roomfurniture.placing.PlacingDescriptor;
import com.roomfurniture.placing.PlacingProblem;
import com.roomfurniture.placing.PlacingSolution;
import com.roomfurniture.problem.Vertex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class PlainPlacingSolutionGeneratorStrategy implements GeneratorStrategy<PlacingSolution>{
    private final PlacingProblem problem;
    private final int vertexCount;
    private List<Integer> furnitures;


    public PlainPlacingSolutionGeneratorStrategy(PlacingProblem problem) {
        this.problem = problem;
         furnitures = new ArrayList<>();
        for (int i = 0; i < problem.getFurnitures().size(); i++) {
            furnitures.add(i);
        }
        vertexCount = problem.getSpawnPoints().size();
    }

    @Override
    public PlacingSolution generate() {
        List<Integer> furniturePermutation = new ArrayList<>(furnitures);
        List<Integer> vertexStream = new ArrayList<>(furnitures).stream().map(integer -> ThreadLocalRandom.current().nextInt(0, vertexCount)).collect(Collectors.toList());
        List<Vertex> motions = new ArrayList<>();
        for(int i = 0; i < furnitures.size(); i++) {
            motions.add(new Vertex(ThreadLocalRandom.current().nextDouble() * 3, ThreadLocalRandom.current().nextDouble() * 3));
        }

        Collections.shuffle(furniturePermutation);
        List<PlacingDescriptor> descriptors = new ArrayList<>();
        for(int i = 0; i < furnitures.size(); i++) {
           descriptors.add(new PlacingDescriptor(furniturePermutation.get(i), vertexStream.get(i), motions.get(i), ThreadLocalRandom.current().nextDouble() * 3));
        }
        return new PlacingSolution(descriptors);
    }
}
