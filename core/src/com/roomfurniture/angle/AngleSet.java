package com.roomfurniture.angle;

import com.roomfurniture.ShapeCalculator;
import com.roomfurniture.problem.Furniture;
import com.roomfurniture.problem.Problem;

import java.util.*;

public class AngleSet {
    private List<Angle> angles;
    private List<List<FurnitureAngleSet>> solutions;
    private List<FurnitureRepresentation> furnitures;

    @Override
    public String toString() {
        return "AngleSet{" +
                "angles=" + angles +
                ", solutions=" + solutions +
                ", furnitures=" + furnitures +
                '}';
    }

    public AngleSet(List<Angle> result, List<List<FurnitureAngleSet>> newSolution, List<FurnitureRepresentation> furnitures) {
        angles = result;
        solutions = newSolution;
        this.furnitures = furnitures;
    }

    public static List<AngleSet> generateInitialAngleSet(Problem problem) {
        List<Angle> angles = EdgeAligner.computeShapeAngles(ShapeCalculator.getVertices(problem.getRoom().toShape()));
        List<AngleSet> set = new ArrayList<>();
        for(Angle angle:angles) {
            List<FurnitureRepresentation> furnitures = new ArrayList<>();
            List<List<FurnitureAngleSet>> solutions = new ArrayList<>();

            for (Furniture furniture : problem.getFurnitures()) {
                FurnitureRepresentation value = FurnitureRepresentation.generateRepresentationOf(furniture);
                furnitures.add(value);
                solutions.add(value.generateInitialAngleSet());
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

        Iterator<List<FurnitureAngleSet>> angleSetAIterator = angleSetA.solutions.iterator();
        Iterator<List<FurnitureAngleSet>> angleSetBIterator = angleSetB.solutions.iterator();
        List<List<FurnitureAngleSet>> newSolution = new ArrayList<>();

        while (angleSetAIterator.hasNext()) {
            List<FurnitureAngleSet> aAngleSet = angleSetAIterator.next();
            List<FurnitureAngleSet> bAngleSet = angleSetBIterator.next();
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

            newSolution.add(newResult);
        }

        if (newSolution.stream().allMatch(List::isEmpty))
            return Optional.empty();
        return Optional.of(new AngleSet(result, newSolution, angleSetA.furnitures));
    }
}
