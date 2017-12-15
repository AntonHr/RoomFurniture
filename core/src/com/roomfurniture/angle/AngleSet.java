package com.roomfurniture.angle;

import com.roomfurniture.ShapeCalculator;
import com.roomfurniture.problem.Furniture;
import com.roomfurniture.problem.Problem;

import java.util.*;
import java.util.stream.Collectors;

public class AngleSet {
    private List<Angle> angles;
    private HashMap<Furniture,List<FurnitureAngleSet>> solutions;
    private List<FurnitureRepresentation> furnitures;

    @Override
    public String toString() {
        return "AngleSet{" +
                "angles=" + angles +
                ", solutions=" + solutions +
                ", furnitures=" + furnitures +
                '}';
    }

    public List<Angle> getAngles() {
        return angles;
    }


    public List<FurnitureRepresentation> getFurnitures() {
        return furnitures;
    }

    private AngleSet(List<Angle> result, HashMap<Furniture,List<FurnitureAngleSet>> newSolution, List<FurnitureRepresentation> furnitures) {
        angles = result;
        solutions = newSolution;
        this.furnitures = furnitures;
    }

    public HashMap<Furniture, List<FurnitureAngleSet>> getSolutions() {
        return solutions;
    }

    public static List<AngleSet> generateInitialAngleSet(Problem problem) {
        List<Angle> angles = EdgeAligner.computeShapeAngles(ShapeCalculator.getVertices(problem.getRoom().toShape()));
        List<AngleSet> set = new ArrayList<>();
        for(Angle angle:angles) {
            List<FurnitureRepresentation> furnitures = new ArrayList<>();
            HashMap<Furniture, List<FurnitureAngleSet>> solutions = new HashMap<>();

            for (Furniture furniture : problem.getFurnitures()) {
                FurnitureRepresentation value = FurnitureRepresentation.generateRepresentationOf(furniture);
                furnitures.add(value);
                solutions.put(furniture, value.generateInitialAngleSet());
            }
            ArrayList<Angle> angleList = new ArrayList<>();
            angleList.add(angle);

            set.add(new AngleSet(angleList, solutions, furnitures));
        }
        return set;
    }

    public static Optional<AngleSet> conjoinAngleSets(AngleSet angleSetA, AngleSet angleSetB) {
        List<Angle> result = new ArrayList<>();

        result.addAll(angleSetA.angles);
        result.addAll(angleSetB.angles);
        result = result.stream().distinct().collect(Collectors.toList());

        Iterator<Furniture> angleSetAIterator = angleSetA.solutions.keySet().iterator();
//        Iterator<Furniture> angleSetBIterator = angleSetB.solutions.keySet().iterator();
        HashMap<Furniture, List<FurnitureAngleSet>> newSolution = new HashMap<>();

        while (angleSetAIterator.hasNext()) {
            Furniture next = angleSetAIterator.next();
            List<FurnitureAngleSet> aAngleSet = angleSetA.solutions.get(next);
            List<FurnitureAngleSet> bAngleSet = angleSetB.solutions.get(next);
            List<FurnitureAngleSet> newResult = new ArrayList<>();

            for (FurnitureAngleSet aAngle : aAngleSet) {
                for (FurnitureAngleSet bAngle : bAngleSet) {
                    Optional<FurnitureAngleSet> furnitureAngleSet = FurnitureAngleSet.conjoinCongruentAngleSets(aAngle, bAngle);
                    if (furnitureAngleSet.isPresent()) {
                        if (Angle.congurentAngleSet(result, furnitureAngleSet.get().getAngles())) {
                            newResult.add(furnitureAngleSet.get());
                        }
                    }
                }
            }

            newSolution.put(next, newResult);
        }

        return Optional.of(new AngleSet(result, newSolution, angleSetA.furnitures));
    }
}
