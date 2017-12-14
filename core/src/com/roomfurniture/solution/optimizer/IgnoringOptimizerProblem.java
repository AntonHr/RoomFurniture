package com.roomfurniture.solution.optimizer;

import com.google.common.collect.Streams;
import com.roomfurniture.ShapeCalculator;
import com.roomfurniture.problem.*;
import com.roomfurniture.solution.Solution;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class IgnoringOptimizerProblem implements OptimizerProblem {

    private final Descriptor outsideRoom;

    private static class FurnitureRepresentation {
        private final Furniture furniture;
        private final Integer index;

        private FurnitureRepresentation(Furniture furniture, Integer index) {
            this.furniture = furniture;
            this.index = index;
        }

        public Furniture getFurniture() {
            return furniture;
        }

        public Integer getIndex() {
            return index;
        }
    }
     private final Room room;
    private final int noToConsider;
    private final List<Descriptor> placedDescriptors;
    private final List<Integer> placedPositions;

    private final List<Furniture> precalculatedPlacedFurniture;
    private List<FurnitureRepresentation> unplacedFurniture;
    private List<FurnitureRepresentation> ignoreFurniture;
    private final int fullSize;

    public IgnoringOptimizerProblem(Problem problem, Solution solution, int noToConsider, boolean shouldSortFirst) {
        room = problem.getRoom();
        this.noToConsider = noToConsider;
        placedPositions = solution.findPlacedPositions(problem);

        List<Furniture> furnitures = problem.getFurnitures();
        List<Furniture> placed = new ArrayList<>();

        ignoreFurniture = new ArrayList<>();
        unplacedFurniture = new ArrayList<>();
        placedDescriptors = new ArrayList<>();

        // store the placed and unplaced furnitures into seperate lists
        int lastIndex = 0;
        for (Integer index : placedPositions) {
            for (int i = lastIndex; i < index; i++) {
                unplacedFurniture.add(new FurnitureRepresentation(furnitures.get(i), i));
            }
            lastIndex = index + 1;

            placed.add(furnitures.get(index));
            placedDescriptors.add(solution.getDescriptors().get(index));

        }
        fullSize = furnitures.size();
        for(int i = lastIndex; i < fullSize; i++) {
            unplacedFurniture.add(new FurnitureRepresentation(furnitures.get(i),i));
        }


        // at this point,
        // placed := list of all placed furnitures
        // placedDescriptors := list of descriptors for placed items
        //
        // unplaced := list of all unplaced furnitures

        if(shouldSortFirst) {
            unplacedFurniture.sort(Comparator.comparingDouble(o -> o.getFurniture().getScore()));
            Collections.reverse(unplacedFurniture);
        }

        if(unplacedFurniture.size() > noToConsider) {
            List<FurnitureRepresentation> temp = new ArrayList<>(unplacedFurniture.subList(0, noToConsider));
            ignoreFurniture = new ArrayList<>(unplacedFurniture.subList(noToConsider, unplacedFurniture.size()));
            unplacedFurniture = temp;
        }
        double maxX = room.getVerticies().stream().max(Comparator.comparingDouble(o -> o.x)).get().x;
        double maxY = room.getVerticies().stream().max(Comparator.comparingDouble(o -> o.x)).get().x;

        outsideRoom = new Descriptor(new Vertex(maxX + 100,  maxY + 100), 0);


        precalculatedPlacedFurniture = Streams.zip(placed.stream(), placedDescriptors.stream(), Furniture::transform).collect(Collectors.toList());


    }

    @Override
    public Solution getOptimizedSolution(Solution solution) {
        assert(solution.getDescriptors().size() == unplacedFurniture.size());
         List<Descriptor> descriptors = new ArrayList<>();
         Optional<Descriptor> result[] = new Optional[fullSize];

         for(int i =0 ;i <placedDescriptors.size(); i++) {
            Integer index = placedPositions.get(i);
            Descriptor desc = placedDescriptors.get(i);
            result[index] = Optional.of(desc);
         }

         for(int i = 0; i < solution.getDescriptors().size(); i++) {
             FurnitureRepresentation repr = unplacedFurniture.get(i);
             result[repr.getIndex()] = Optional.of(solution.getDescriptors().get(i));
         }

         for(int i = 0; i < ignoreFurniture.size(); i++) {
             FurnitureRepresentation repr = unplacedFurniture.get(i);
             result[repr.getIndex()] = Optional.of(outsideRoom.copy());
         }

         return new Solution(Arrays.stream(result).map(Optional::get).collect(Collectors.toList()));

    }

    @Override
    public Optional<Double> score(Solution individual) {
         Shape roomShape = room.toShape();
        List<Descriptor> descriptors = individual.getDescriptors();

        Map<Boolean, List<Furniture>> result = Streams.zip(unplacedFurniture.stream().map(FurnitureRepresentation::getFurniture), descriptors.stream(), Furniture::transform).collect(Collectors.partitioningBy(furniture -> ShapeCalculator.contains(roomShape, furniture.toShape())));

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
        if (areaSum / roomArea <= 0.3) {
            score *= 0.03;
            score *= (1 + areaSum / roomArea);
        }

        return Optional.of(score);

    }

    @Override
    public int getSize() {
        return unplacedFurniture.size();
    }

    @Override
    public Room getRoom() {
        return room;
    }
}
