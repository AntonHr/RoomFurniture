package com.roomfurniture.placing.plain;

import com.roomfurniture.ShapeCalculator;
import com.roomfurniture.ga.algorithm.interfaces.EvaluationStrategy;
import com.roomfurniture.placing.BasicPlacingProblem;
import com.roomfurniture.placing.PlacingDescriptor;
import com.roomfurniture.placing.PlacingSolution;
import com.roomfurniture.problem.Descriptor;
import com.roomfurniture.problem.Furniture;
import com.roomfurniture.problem.Vertex;
import com.roomfurniture.solution.Solution;

import java.util.*;

public class PlainPlacingSolutionEvaluationStrategy implements EvaluationStrategy<PlacingSolution> {
    private final BasicPlacingProblem problem;
//    private final Map<Furniture, List<Furniture>> cached;
    private final List<Furniture> furnitures;
    private final int divisions;
    private double thetaRange;

    public PlainPlacingSolutionEvaluationStrategy(BasicPlacingProblem problem, int divisions, double thetaRange) {
        this.problem = problem;
//        this.cached = new HashMap<>();
        this.furnitures = problem.getFurnitures();
        this.divisions = divisions;

//        for (Furniture furniture : furnitures) {
//            List<Furniture> precalculatedTransforms = new ArrayList<>();
//            for (int i = 0; i < this.divisions; i++) {
//                precalculatedTransforms.add(furniture.transform(new Descriptor(new Vertex(0, 0), i * Math.PI * 2 / divisions)));
//            }
//            cached.put(furniture, precalculatedTransforms);
//        }
        this.thetaRange = thetaRange;
    }

    @Override
    public double evaluate(PlacingSolution individual) {
        List<PlacingDescriptor> descriptors = individual.getDescriptors();
        List<Furniture> placedFurniture = new ArrayList<>();
        List<Descriptor> placedDescriptors = new ArrayList<>();
        double bestSeen = 0.0;
        for (PlacingDescriptor descriptor : descriptors) {
            Vertex vertex = descriptor.getVertex(problem);
            Furniture originalfurniture = descriptor.getFurniture(problem);

            Optional<Furniture> optimalFurniture = Optional.empty();
            Optional<Descriptor> optimalDescriptor = Optional.empty();

//            double bestIntersectingArea = 0.0;
//            int i = 0;
//            while(bestIntersectingArea == 0.0 && i < divisions) {
//                Furniture cachedFurniture = cached.get(originalfurniture).get(i);
//                bestIntersectingArea = ShapeCalculator.calculateIntersectingAreaOf(problem.getRoom().toShape(), cachedFurniture.toShape());
//                System.out.println("As I rotate "  + Math.toDegrees(i * Math.PI * 2 / divisions )+ "degrees I get " + bestIntersectingArea);
//            }
//            if(bestIntersectingArea != 0.0) {
//                double lastSeenIntersectingArea = bestIntersectingArea;
//                Furniture lastSeenGoodFurniture = cached.get(originalfurniture).get(i);
//                i++;
//                Furniture lastRetrievedFurniture = cached.get(originalfurniture).get(i);
//                double currentIntersectingArea = ShapeCalculator.calculateIntersectingAreaOf(problem.getRoom().toShape(), lastRetrievedFurniture.toShape());
//
//
//                while(currentIntersectingArea > lastSeenIntersectingArea && i < divisions) {
//                    lastSeenGoodFurniture = lastRetrievedFurniture;
//                    lastSeenIntersectingArea = currentIntersectingArea;
//                    i++;
//                    lastRetrievedFurniture = cached.get(originalfurniture).get(i);
//                    currentIntersectingArea = ShapeCalculator.calculateIntersectingAreaOf(problem.getRoom().toShape(), lastRetrievedFurniture.toShape());
//                }
//            }
//

            double bestSeenInLoop = 0.0;
            double rotation = descriptor.getRotation();
            for (int i = 0; i < divisions * 2; i++) {
                Furniture cachedFurniture = originalfurniture.transform(new Descriptor(descriptor.getPosition(), rotation + (i - divisions) * thetaRange));
                double v = ShapeCalculator.calculateIntersectingAreaOf(problem.getRoom().toShape(), cachedFurniture.toShape());
                if(v > bestSeenInLoop)
                    bestSeenInLoop  = v;
//                System.out.println("As I rotate "  + Math.toDegrees(i * Math.PI * 2 / divisions )+ "degrees I get " + v);
                if (ShapeCalculator.contains(problem.getRoom().getVerticies(), cachedFurniture.getVertices())) {
                    optimalFurniture = Optional.of(originalfurniture);
                    optimalDescriptor = Optional.of(new Descriptor(vertex.copy(), i * Math.PI * 2 / divisions));
                }
            }
            bestSeen += bestSeenInLoop;
            optimalFurniture.ifPresent(placedFurniture::add);
            optimalDescriptor.ifPresent(placedDescriptors::add);
        }

        Descriptor[] descriptorArray = new Descriptor[problem.getFurnitures().size()];
        for(int i = 0; i < problem.getFurnitures().size(); i++) {
            descriptorArray[i] = new Descriptor(new Vertex(0,0), 0.0);
        }
        for (int i = 0; i < placedFurniture.size(); i++) {
            descriptorArray[problem.getProblem().findMeInInitialArray(placedFurniture.get(i))] = placedDescriptors.get(i);
        }

        Solution solution = new Solution(Arrays.asList(descriptorArray));

        individual.storeSolution(solution);
//        SwingVisualizer.visualizeProblem(problem.problem, solution);
        return problem.getProblem().score(solution).orElse(0.0) + bestSeen;
    }
}
