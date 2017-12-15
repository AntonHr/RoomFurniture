package com.awesome.scenario.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gui.EvaluatorPhysicsRenderer;
import com.gui.SwingVisualizer;
import com.roomfurniture.InputParser;
import com.roomfurniture.ShapeCalculator;
import com.roomfurniture.angle.*;
import com.roomfurniture.problem.Descriptor;
import com.roomfurniture.problem.Furniture;
import com.roomfurniture.problem.Problem;
import com.roomfurniture.problem.Vertex;
import com.roomfurniture.solution.Solution;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class DesktopLauncher {

    public static EvaluatorPhysicsRenderer renderer;
    public static LwjglApplication application;

    public static void main(String[] arg) {
//        optimizeThenRender(solution, problem);
//        PhysicsApproach.runMultiThreaded();
//        PhysicsApproach.runPhysicsSingleThreaded();

//        PhysicsApproach.runSpawnPointPicker();


        Problem problem = InputParser.getTestProblems().get(0);
//        List<AngleSet> angleSets = AngleSet.generateInitialAngleSet(problem);
//        AngleSet angleSet = angleSets.get(1);
//        List<List<FurnitureAngleSet>> solutions = angleSet.getSolutions();
//        List<FurnitureAngleSet> furnitureAngleSets = solutions.get(0);
//        Angle angle = furnitureAngleSets.get(1).getAngles().get(0);
//        Angle stationary = angleSet.getAngles().get(0);
//        System.out.println("Align Stationary " + stationary +" and " + angle);
//        Descriptor descriptor = EdgeAligner.alignTwoAngles(stationary, angle);
//
//        Furniture furniture = problem.getFurnitures().get(1);

        List<Furniture> furnitures = new ArrayList<>();
        List<Furniture> collect = problem.getFurnitures().stream().sorted(Comparator.comparingDouble(Furniture::getScore)).collect(Collectors.toList());
        Collections.reverse(collect);
        Problem testProblem = new Problem(1, problem.getRoom(), collect);
        Furniture furniture = collect.get(4);
//k        Furniture furniture = problem.getFurnitures().get(2);
        furnitures.add(furniture);
//        Problem testProblem =  new Problem(1, problem.getRoom(), furnitures);
//
//        System.out.println(descriptor);
//        List<AngleSet> angleSets = AngleSet.generateInitialAngleSet(problem);
//
//        List<AngleSet> angleSets1 = foldAngleSets(angleSets);
//
//        // TODO: Change to only list valid angle sets
//        List<AngleSet> matchingAngleSets = geMatchingAngleSets(furniture, angleSets1);
//
//        for(AngleSet set : matchingAngleSets) {
//            System.out.println(set.getAngles() + " are a valid candidate for this furniture" + set.getSolutions().get(furniture));
//        }
//        List<FurnitureAngleSet> furnitureAngleSets = matchingAngleSets.get(0).getSolutions().get(furniture);
//        List<Angle> anglesonRoom = matchingAngleSets.get(1).getAngles();
//        List<Angle> anglesonShape = furnitureAngleSets.get(1).getAngles();
//
//        System.out.println("Angles on room: " + anglesonRoom);
//        System.out.println("Angles on shap: " + anglesonShape);
//        List<Angle> angles1 = EdgeAligner.computeShapeAngles(furniture.getVertices());
//        List<Angle> angles = EdgeAligner.computeShapeAngles(problem.getRoom().getVerticies());
//
//        Angle angle = angles.get(4);
//        for(int i = 0; i < angles1.size(); i++) {
//            System.out.println("[" + i + "]:  " +  angles.get(i));
//        }
//
//        Angle angleRoom = anglesonRoom.get(1);
//        Angle angleShape = anglesonRoom.get(1);
//
//        System.out.println(angle);
//        for(int i = 0; i < angles1.size(); i++) {
//            System.out.println("[" + i + "]:  " +  angles1.get(i));
//        }
//        Angle angle1 = angles1.get(0);
//        System.out.println(angle1);
////        Descriptor descriptor = EdgeAligner.alignTwoAngles(angle, angle1);
//        Descriptor descriptor = EdgeAligner.alignTwoAngles(angleRoom, angleShape);
//        System.out.println(descriptor);
//
//        Solution testSolution =  new Solution(Arrays.asList(
////               descriptor
//                new Descriptor(new Vertex(0,0),0)
//        ));

        Solution testSolution = solveProblem(testProblem);

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
//        DragAndDrop renderer = new DragAndDrop(problem, new DragAndDropPhysicsSimulator(problem.getRoom()));

        EvaluatorPhysicsRenderer renderer = new EvaluatorPhysicsRenderer();

        try {
            SwingVisualizer.visualizeProblem(testProblem, testSolution);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        config.width = 2000;
        config.height = 1000;

        DesktopLauncher.application = new LwjglApplication(renderer, config);
        renderer.renderSolution(testProblem,testSolution );
    }

    public static List<AngleSet> geMatchingAngleSets(Furniture furniture, List<AngleSet> angleSets1) {
        List<AngleSet> matchingAngleSets = new ArrayList<>();
        for(AngleSet set : angleSets1) {
            if(set.getSolutions().get(furniture).size() != 0) {
                matchingAngleSets.add(set);
            }
        }
        return matchingAngleSets;
    }

    public static Solution solveProblem(Problem problem) {
        List<AngleSet> angleSets = AngleSet.generateInitialAngleSet(problem);
        angleSets = foldAngleSets(angleSets);
        angleSets = foldAngleSets(angleSets);
        List<Descriptor> descriptors = solveFurniturePositioningFor(problem, 0, angleSets);
        return new Solution(descriptors);

    }

    public static List<Descriptor> solveFurniturePositioningFor(Problem problem, int index, List<AngleSet> angleSets) {
        if(index >= problem.getFurnitures().size() || index >= 200)
            return new ArrayList<>();
        else {
            Furniture furniture = problem.getFurnitures().get(index);
            List<AngleSet> furnitureAngleSets = geMatchingAngleSets(furniture, angleSets);
            for(AngleSet set : furnitureAngleSets) {
                List<Angle> angles = set.getAngles();
                List<FurnitureAngleSet> furnitureAngles = set.getSolutions().get(furniture);
                for(FurnitureAngleSet solutionSet : furnitureAngles) {
                    List<Angle> solutionAngles = solutionSet.getAngles();
                    for (int i = 0; i < angles.size(); i++) {
                        Descriptor descriptor = EdgeAligner.alignTwoAngles(angles.get(i), solutionAngles.get(i));
                        Furniture possible = furniture.transform(descriptor);
                        if(ShapeCalculator.contains(problem.getRoom().toShape(), possible.toShape())) {
                            System.out.println("Solution found");
                           // TODO Check if recursive branch creates conflicts
                            List<Descriptor> result = solveFurniturePositioningFor(problem, index + 1, angleSets);
                            result.add(0, descriptor);
                            return result;
                        }
                    }
                }
            }

            System.out.println("No solution found");
             List<Descriptor> result = solveFurniturePositioningFor(problem, index + 1, angleSets);
              result.add(0, new Descriptor(new Vertex(0,0),0));
              return result;
        }
    }

    public static List<AngleSet> foldAngleSets(List<AngleSet> angleSets) {
        List<AngleSet> conjoined = new ArrayList<>();
        for(int i = 0; i < angleSets.size()-1; i++) {
                AngleSet.conjoinAngleSets(angleSets.get(i), angleSets.get(i+1)).ifPresent(conjoined::add);
        }
        return conjoined;
    }



}
