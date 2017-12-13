package com.roomfurniture.solution;

import com.google.common.collect.Streams;
import com.roomfurniture.ShapeCalculator;
import com.roomfurniture.problem.Descriptor;
import com.roomfurniture.problem.Furniture;
import com.roomfurniture.problem.Problem;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Solution {
    private final List<Descriptor> descriptors;

    public Solution(List<Descriptor> descriptors) {
        this.descriptors = descriptors;
    }

    public List<Descriptor> getDescriptors() {
        return descriptors;
    }

    @Override
    public String toString() {
        return "Solution{" +
                "descriptors=" + descriptors +
                '}';
    }

    public Optional<Double> score(Problem problem) {
        //TODO(Kiran): Add better scoring function

        List<Furniture> furnitures = problem.getFurnitures();
        Shape roomShape = problem.getRoom().toShape();

        Map<Boolean, List<Furniture>> result = Streams.zip(furnitures.stream(), descriptors.stream(), Furniture::transform).collect(Collectors.partitioningBy(furniture -> ShapeCalculator.contains(roomShape, furniture.toShape())));

        List<Furniture> furnitureInRoom = result.get(true);

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


        double roomArea = ShapeCalculator.calculateAreaOf(problem.getRoom().toShape());
        // The optimizer often can make up for an initial lack of coverage, but not score
//        if (areaSum / roomArea <= 0.3)
//            score *= 0.03;
//        score *= (1 + areaSum / roomArea);

        return Optional.of(score);

    }


    public List<Integer> findPlacedPositions(Problem problem) {
        List<Furniture> furnitures = problem.getFurnitures();
        Shape roomShape = problem.getRoom().toShape();
        List<Integer> placedPositions = new ArrayList<>();
        List<Furniture> furnitureInRoom = new ArrayList<>();

        for(int i = 0; i < furnitures.size(); i++) {
            Furniture transform = furnitures.get(i).transform(descriptors.get(i));
            if(ShapeCalculator.contains(roomShape, transform.toShape())) {
                furnitureInRoom.add(transform);
                // collect indexes of valid positions
                placedPositions.add(i);
            }
        }

        Map<Boolean, List<Furniture>> result = Streams.zip(furnitures.stream(), descriptors.stream(), Furniture::transform).collect(Collectors.partitioningBy(furniture -> ShapeCalculator.contains(roomShape, furniture.toShape())));


        Iterator<Furniture> iterator = furnitureInRoom.iterator();
        Iterator<Integer> positioniterator = placedPositions.iterator();

        while (iterator.hasNext()) {
            Furniture furniture = iterator.next();
            positioniterator.next();
            for (Furniture otherFurniture : furnitureInRoom) {
                if (otherFurniture != furniture)
                    if (ShapeCalculator.intersect(furniture.toShape(), otherFurniture.toShape())) {
                        // Keep furniture with highest score
                        if (otherFurniture.getScorePerUnitArea() * ShapeCalculator.calculateAreaOf(otherFurniture.toShape()) >= furniture.getScorePerUnitArea() * ShapeCalculator.calculateAreaOf(furniture.toShape())) {
                            iterator.remove();
                            positioniterator.remove();
                            break;
                        }
                    }
            }
        }


        return placedPositions;
    }

    public double findCoverage(Problem problem) {
        List<Furniture> furnitures = problem.getFurnitures();
        Shape roomShape = problem.getRoom().toShape();

        Map<Boolean, List<Furniture>> result = Streams.zip(furnitures.stream(), descriptors.stream(), Furniture::transform).collect(Collectors.partitioningBy(furniture -> ShapeCalculator.contains(roomShape, furniture.toShape())));

        List<Furniture> furnitureInRoom = result.get(true);

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

        double areaSum = 0.0;

        for (Furniture furniture : furnitureInRoom) {
            areaSum += ShapeCalculator.calculateAreaOf(furniture.toShape());
        }


        double roomArea = ShapeCalculator.calculateAreaOf(problem.getRoom().toShape());

        return areaSum / roomArea;
    }

    //added by Alex
    private List<Furniture> getAllTransformedItems(Problem problem) {
        return Streams.zip(problem.getFurnitures().stream(), this.getDescriptors().stream(), Furniture::transform).collect(Collectors.toList());
    }

    public List<Furniture> getItemsInTheRoom(Problem problem) {
        List<Furniture> allTransformedItems = getAllTransformedItems(problem);
        Map<Boolean, List<Furniture>> result = allTransformedItems.stream().collect(Collectors.partitioningBy(furniture -> ShapeCalculator.contains(problem.getRoom().toShape(), furniture.toShape())));

        List<Furniture> itemsInRoom = result.get(true);

        Iterator<Furniture> iterator = itemsInRoom.iterator();

        while (iterator.hasNext()) {
            Furniture furniture = iterator.next();
            for (Furniture otherFurniture : itemsInRoom) {
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

        return itemsInRoom;
    }

    public static Optional<Solution> fromSerialized(Scanner scanner) {
        List<Descriptor> result = new ArrayList<>();
        Optional<Descriptor> descriptor = Descriptor.fromSerialized(scanner);
        while(descriptor.isPresent()) {
            result.add(descriptor.get());
            descriptor = Descriptor.fromSerialized(scanner);
            try {
                scanner.skip(";");
                break;
            } catch (NoSuchElementException ignored) {}
        }
        if(result.size() != 0)
            return Optional.of(new Solution(result));
        else
            return Optional.empty();
    }

    public String toSerialized() {
        StringBuilder sb = new StringBuilder();
        for(Descriptor d : descriptors) {
            sb.append(d.toSerialized());
        }
        sb.append(";");
        return sb.toString();
    }
}
