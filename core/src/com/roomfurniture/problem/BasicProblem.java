package com.roomfurniture.problem;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import com.roomfurniture.ShapeCalculator;
import com.roomfurniture.solution.Solution;

import java.awt.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class BasicProblem implements Problem {
    private int number = 0;
   public final Room room;
    private final List<Furniture> furnitures;

    public BasicProblem(int number, Room room, List<Furniture> furnitures) {
        this.number = number;
        this.room = room;
        this.furnitures = ImmutableList.copyOf(furnitures);
    }

    @Override
    public String toString() {
        return "Problem{" +
                "room=" + room +
                ", furnitures=" + furnitures +
                '}';
    }

    @Override
    public Room getRoom() {
        return room;
    }

    @Override
    public List<Furniture> getFurnitures() {
        return furnitures;
    }

    @Override
    public int getNumber() {
        return number;
    }

    @Override
    public int findMeInInitialArray(Furniture furniture) {
        List<Furniture> initialItemArray = getFurnitures();
        int ind = initialItemArray.indexOf(furniture);
        if (ind == -1)
            throw new RuntimeException("ooopos");
        return ind;
    }

     public Optional<Double> score(Solution solution) {
        //TODO(Kiran): Add better scoring function

        List<Furniture> furnitures = getFurnitures();
        Shape roomShape = getRoom().toShape();

        Map<Boolean, List<Furniture>> result = Streams.zip(furnitures.stream(), solution.getDescriptors().stream(), Furniture::transform).collect(Collectors.partitioningBy(furniture -> ShapeCalculator.contains(roomShape, furniture.toShape())));

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


        double roomArea = ShapeCalculator.calculateAreaOf(getRoom().toShape());
        // The optimizer often can make up for an initial lack of coverage, but not score
//        if (areaSum / roomArea <= 0.3)
//            score *= 0.03;
//        score *= (1 + areaSum / roomArea);

        return Optional.of(score);

    }

}
