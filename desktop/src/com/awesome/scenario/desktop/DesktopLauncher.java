package com.awesome.scenario.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.math.Interpolation;
import com.google.common.collect.Lists;
import com.gui.DragAndDrop;
import com.gui.EvaluatorPhysicsRenderer;
import com.gui.SimplePhysicsRenderer;
import com.gui.SwingVisualizer;
import com.roomfurniture.InputParser;
import com.roomfurniture.angle.*;
import com.roomfurniture.box2d.DragAndDropPhysicsSimulator;
import com.roomfurniture.box2d.PhysicsSimulatorEvaluator;
import com.roomfurniture.box2d.SimplePhysicsSimulator;
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
        Furniture furniture = collect.get(3);
//k        Furniture furniture = problem.getFurnitures().get(2);
        furnitures.add(furniture);
        Problem testProblem =  new Problem(1, problem.getRoom(), furnitures);
//
//        System.out.println(descriptor);

        List<Angle> angles1 = EdgeAligner.computeShapeAngles(furniture.getVertices());
        List<Angle> angles = EdgeAligner.computeShapeAngles(problem.getRoom().getVerticies());

        Angle angle = angles.get(4);
         for(int i = 0; i < angles1.size(); i++) {
            System.out.println("[" + i + "]:  " +  angles.get(i));
        }


        System.out.println(angle);
        for(int i = 0; i < angles1.size(); i++) {
            System.out.println("[" + i + "]:  " +  angles1.get(i));
        }
        Angle angle1 = angles1.get(0);
        System.out.println(angle1);
        Descriptor descriptor = EdgeAligner.alignTwoAngles(angle, angle1);
        System.out.println(descriptor);

        Solution testSolution =  new Solution(Arrays.asList(
//               descriptor
                new Descriptor(new Vertex(0,0),0)
        ));


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


}
