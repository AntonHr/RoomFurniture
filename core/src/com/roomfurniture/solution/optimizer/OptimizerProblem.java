package com.roomfurniture.solution.optimizer;

import com.google.common.collect.Streams;
import com.roomfurniture.ShapeCalculator;
import com.roomfurniture.problem.Descriptor;
import com.roomfurniture.problem.Furniture;
import com.roomfurniture.problem.Problem;
import com.roomfurniture.problem.Room;
import com.roomfurniture.solution.Solution;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class OptimizerProblem {
    private final Room room;
    private final List<Descriptor> placedDescriptors;
    private final List<Integer> placedPositions;

    private final List<Furniture> precalculatedPlacedFurniture;
    private final List<Furniture> unplacedFurniture;
    private final int size;
    private final List<Descriptor> ignoredFurniture;
    private final List<Integer> ignoredFurnitureIndexes;
    private final int fullSolutionSize;

    public OptimizerProblem(Problem problem, Solution solution) {
        room = problem.getRoom();
        placedPositions = solution.findPlacedPositions(problem);

        List<Furniture> furnitures = problem.getFurnitures();
        List<Furniture> placed = new ArrayList<>();
        List<Descriptor> unplacedDescriptors = new ArrayList<>();

        size = furnitures.size();
        unplacedFurniture = new ArrayList<>();
        placedDescriptors = new ArrayList<>();

        int lastIndex = 0;
        for(Integer index : placedPositions) {
            for(int i = lastIndex; i < index; i++) {
                unplacedFurniture.add(furnitures.get(i));
                unplacedDescriptors.add(solution.getDescriptors().get(i));
            }
            lastIndex = index + 1;

            placed.add(furnitures.get(index));
            placedDescriptors.add(solution.getDescriptors().get(index));
        }


        for(int i = lastIndex; i < size; i++) {
            unplacedFurniture.add(furnitures.get(i));
            unplacedDescriptors.add(solution.getDescriptors().get(i));
        }

         double roomArea = ShapeCalculator.calculateAreaOf(room.toShape());
        double shapeArea = 0.0;

        for(Furniture furniture : placed) {
            shapeArea += ShapeCalculator.calculateAreaOf(furniture.toShape());
        }

        double remainingSpace = roomArea - shapeArea;
        fullSolutionSize = unplacedFurniture.size();
        ignoredFurniture = new ArrayList<>();
        ignoredFurnitureIndexes = new ArrayList<>();
        int index = 0;
        Iterator<Furniture> unplacediterator = unplacedFurniture.iterator();
        Iterator<Descriptor> unplacedDescriptorIterator = unplacedDescriptors.iterator();
        while (unplacediterator.hasNext())  {
            Furniture furniture = unplacediterator.next();
            Descriptor currentDescriptor = unplacedDescriptorIterator.next();
            if(ShapeCalculator.calculateAreaOf(furniture.toShape()) > remainingSpace) {
                ignoredFurniture.add(currentDescriptor);
                ignoredFurnitureIndexes.add(index);
                unplacediterator.remove();
                unplacedDescriptorIterator.remove();
            }
            index += 1;
        }

       precalculatedPlacedFurniture = Streams.zip(placed.stream(), placedDescriptors.stream(), Furniture::transform).collect(Collectors.toList());




    }


    public Solution getOptimizedSolution(Solution solution) {
        List<Descriptor> descriptors = new ArrayList<>();

        Iterator<Descriptor> problemIterator = placedDescriptors.iterator();
        Iterator<Descriptor> solutionIterator = solution.getDescriptors().iterator();
        Iterator<Descriptor> ignoredIterator = ignoredFurniture.iterator();
        List<Descriptor> fullsolution = new ArrayList<>();

        int lastIndex = 0;
        for(int i = 0; i < ignoredFurnitureIndexes.size(); i++) {
            Integer index = ignoredFurnitureIndexes.get(i);
            for(int j = lastIndex; j < index; j++) {
                fullsolution.add(j, solutionIterator.next());
            }
            lastIndex = index + 1;
            fullsolution.add(ignoredIterator.next());
        }

        for(int j = lastIndex; j < fullSolutionSize; j++) {
                fullsolution.add(j, solutionIterator.next());
        }

        solutionIterator = fullsolution.iterator();

        lastIndex = 0;
        for(int i = 0; i < placedPositions.size(); i++) {
            Integer index = placedPositions.get(i);

            for(int j = lastIndex; j < index; j++) {
                descriptors.add(j, solutionIterator.next());
            }
            lastIndex = index + 1;
            descriptors.add(index, problemIterator.next());
        }
        for(int j = lastIndex; j < size; j++) {
                descriptors.add(j, solutionIterator.next());
        }

        return new Solution(descriptors);
    }

    public Optional<Double> score(Solution individual) {
        Shape roomShape = room.toShape();
        List<Descriptor> descriptors = individual.getDescriptors();

        Map<Boolean, List<Furniture>> result = Streams.zip(unplacedFurniture.stream(), descriptors.stream(), Furniture::transform).collect(Collectors.partitioningBy(furniture -> ShapeCalculator.contains(roomShape, furniture.toShape())));

        List<Furniture> furnitureInRoom = result.get(true).stream().filter(furniture -> precalculatedPlacedFurniture.stream().noneMatch(placedFurniture -> {
            // find furniture in room cost
            return ShapeCalculator.intersect(placedFurniture.toShape(), furniture.toShape());
        })).collect(Collectors.toList());

        Iterator<Furniture> iterator = furnitureInRoom.iterator();
        while (iterator.hasNext()) {
            Furniture furniture = iterator.next();
            for (Furniture otherFurniture : furnitureInRoom) {
                if (otherFurniture != furniture)
                    if (ShapeCalculator.intersect(furniture.toShape(), otherFurniture.toShape())) {
                        // Keep furniture with highest score
                        if (otherFurniture.getScorePerUnitArea() * ShapeCalculator.calculateAreaOf(otherFurniture.toShape()) >= furniture.getScorePerUnitArea() * ShapeCalculator.calculateAreaOf(furniture.toShape())) {
                            iterator.remove();
                            break;
                        }
                    }
            }
        }

        double score = 0;
        double areaSum = 0.0;

        for (Furniture furniture : furnitureInRoom) {
            areaSum += ShapeCalculator.calculateAreaOf(furniture.toShape());
            score += furniture.getScorePerUnitArea() * ShapeCalculator.calculateAreaOf(furniture.toShape());
        }

        for (Furniture furniture : precalculatedPlacedFurniture) {
            areaSum += ShapeCalculator.calculateAreaOf(furniture.toShape());
            score += furniture.getScorePerUnitArea() * ShapeCalculator.calculateAreaOf(furniture.toShape());
        }


        double roomArea = ShapeCalculator.calculateAreaOf(roomShape);
        if (areaSum / roomArea <= 0.3)
            score *= 0.03;
        score *= (1 + areaSum / roomArea);

        return Optional.of(score);

    }

    public int getSize() {
        return size;
    }

    public Room getRoom() {
        return room;
    }
}
