package com.roomfurniture.problem;


import com.google.common.collect.Streams;
import com.roomfurniture.ShapeCalculator;
import com.roomfurniture.solution.Solution;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class BiasedProblem implements Problem {
    private class FurnitureRepresentation {
        private final Furniture furniture;
        private Integer index;

        public FurnitureRepresentation(Furniture furniture, int index) {
            this.furniture = furniture;
            this.index = index;
        }

        public FurnitureRepresentation(Furniture furniture) {
            this.furniture = furniture;
        }

        public Furniture getFurniture() {
            return furniture;
        }
        public Integer getIndex() {
            return index;
        }
    }
    private final Problem problem;
    private int biasCount;
    private List<FurnitureRepresentation> representations = new ArrayList<>();
    private List<FurnitureRepresentation> proxyFurnitures;

    public BiasedProblem(Problem problem, int biasCount) {
        this.problem = problem;
        this.biasCount = biasCount;
        List<Furniture> furnitures = problem.getFurnitures();
        for(int i = 0; i < furnitures.size(); i++) {
            representations.add(new FurnitureRepresentation(furnitures.get(i), i));
        }
        ArrayList<FurnitureRepresentation> sortedFurnitures = new ArrayList<>(representations);
        sortedFurnitures.sort(Comparator.comparingDouble(value -> value.furniture.getScore()));
        Collections.reverse(sortedFurnitures);

        if(sortedFurnitures.size() > biasCount) {
            sortedFurnitures =new ArrayList<>(sortedFurnitures.subList(0, biasCount));
        }

        proxyFurnitures = sortedFurnitures;
    }

    @Override
    public Room getRoom() {
        return problem.getRoom();
    }

    @Override
    public List<Furniture> getFurnitures() {
        return proxyFurnitures.stream().map(FurnitureRepresentation::getFurniture).collect(Collectors.toList());
    }

    @Override
    public int getNumber() {
        return problem.getNumber();
    }

    @Override
    public int findMeInInitialArray(Furniture furniture) {
        for(int i = 0; i < proxyFurnitures.size(); i++) {
            if(proxyFurnitures.get(i).getFurniture().equals(furniture))
                return i;
        }

        throw new RuntimeException("Unknown furniture provided");
    }

      public Optional<Double> score(Solution solution) {

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
        return Optional.of(score);
    }


    public Solution getUnbiasedSolution(Solution solution) {
        Descriptor items[] = new Descriptor[representations.size()];
        for(int i = 0; i < representations.size(); i++) {
            items[i] = new Descriptor(new Vertex(0,0),0);
        }

        for(int i = 0; i < solution.getDescriptors().size(); i++) {
            Integer index = proxyFurnitures.get(i).getIndex();
            items[index] = solution.getDescriptors().get(i);
        }
        return new Solution(Arrays.asList(items));
    }

}
