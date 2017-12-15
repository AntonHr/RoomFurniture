package com.awesome.scenario.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gui.EvaluatorPhysicsRenderer;
import com.gui.RoomFurnitureRenderer;
import com.gui.SwingVisualizer;
import com.roomfurniture.InputParser;
import com.roomfurniture.ShapeCalculator;
import com.roomfurniture.angle.Angle;
import com.roomfurniture.angle.AngleSet;
import com.roomfurniture.angle.EdgeAligner;
import com.roomfurniture.angle.FurnitureAngleSet;
import com.roomfurniture.box2d.PhysicsSimulator;
import com.roomfurniture.problem.Descriptor;
import com.roomfurniture.problem.Furniture;
import com.roomfurniture.problem.Problem;
import com.roomfurniture.problem.Vertex;
import com.roomfurniture.solution.Solution;
import com.roomfurniture.solution.storage.SolutionDatabase;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class AlgorithmApproach {
    public static void runAlgorithmicSolver() {
        Problem problem = InputParser.getTestProblems().get(0);
        List<Furniture> furnitures = new ArrayList<>();
        List<Furniture> collect = problem.getFurnitures().stream().sorted(Comparator.comparingDouble(Furniture::getScore)).collect(Collectors.toList());
        Collections.reverse(collect);
        Problem testProblem = new Problem(problem.getNumber(), problem.getRoom(), collect);
        Furniture furniture = collect.get(4);
        furnitures.add(furniture);

        Solution testSolution = solveProblem(testProblem);


        ExecutorService service = Executors.newFixedThreadPool(10);
        testSolution = BasicApproach.optimizeSolution(testSolution, testProblem, service, 1000, 1000, 10, true);
        testSolution = BasicApproach.optimizeSolution(testSolution, testProblem, service, 1000, 1000, 10, false);
        testSolution = BasicApproach.optimizeSolution(testSolution, testProblem, service, 1000, 1000, 10, true);
        testSolution = BasicApproach.optimizeSolution(testSolution, testProblem, service, 1000, 1000, 10, false);
        testSolution = BasicApproach.optimizeSolution(testSolution, testProblem, service, 1000, 1000, 10, true);
        testSolution = BasicApproach.optimizeSolution(testSolution, testProblem, service, 1000, 1000, 10, false);
        testSolution = BasicApproach.optimizeSolution(testSolution, testProblem, service, 1000, 1000, 10, true);
        testSolution = BasicApproach.optimizeSolution(testSolution, testProblem, service, 1000, 1000, 10, false);

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

//        EvaluatorPhysicsRenderer renderer = new EvaluatorPhysicsRenderer();
        RoomFurnitureRenderer renderer = new RoomFurnitureRenderer(testProblem,testSolution, new PhysicsSimulator(problem, testSolution));

        try {
            SwingVisualizer.visualizeProblem(testProblem, testSolution);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        config.width = 2000;
        config.height = 1000;
//        System.out.println(testSolution.toOutputFormat(testProblem));

        DesktopLauncher.application = new LwjglApplication(renderer, config);

        //renderer.renderSolution(testProblem,testSolution );
             Optional<Double> score = testSolution.score(testProblem);
            System.out.println(testSolution.toOutputFormat(testProblem));


        if (testProblem.getNumber() > 0 && testProblem.getNumber() <= 30) {
//        System.out.println(Solution.fromSerialized(new Scanner(testSolution.toSerialized())).get().toOutputFormat(testProblem) + "\n" + testSolution.toOutputFormat(testProblem));


            SolutionDatabase.createPersonalSolutionDatabase().storeSolutionFor(testProblem.getNumber(), testSolution.score(testProblem).get(),testSolution.findCoverage(testProblem), testSolution);
       }
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
//        angleSets = foldAngleSets(angleSets);
//        angleSets = foldAngleSets(angleSets);
//        angleSets = foldAngleSets(angleSets);
        angleSets = foldAngleSets(angleSets);
        List<Descriptor> descriptors = solveFurniturePositioningFor(problem, 0, angleSets);
        return new Solution(descriptors);

    }

    public final static List<Descriptor> solveFurniturePositioningFor(Problem problem, int index, List<AngleSet> angleSets) {
        if(index >= problem.getFurnitures().size())
            return new ArrayList<>();
        else if(index >= 200) {
            ArrayList<Descriptor> descriptors = new ArrayList<>();
            for(int i = index; i < problem.getFurnitures().size(); i++) {
                descriptors.add(new Descriptor(new Vertex(0,0),0));
            }
            return descriptors;
        } else {
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
