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
import com.roomfurniture.solution.storage.SolutionDatabase;

import java.io.FileNotFoundException;
import java.util.*;

public class DesktopLauncher {

    public static EvaluatorPhysicsRenderer renderer;
    public static LwjglApplication application;

    public static void main(String[] arg) {
//        optimizeThenRender(solution, problem);
//        PhysicsApproach.runMultiThreaded();
//        PhysicsApproach.runPhysicsSingleThreaded();

//        DragAndDrop renderer = new DragAndDrop(problem, new DragAndDropPhysicsSimulator(problem.getRoom()));
//        PhysicsApproach.runSpawnPointPicker();


        AlgorithmApproach.runAlgorithmicSolver();

//        List<Solution> allSolutionsFor = SolutionDatabase.createPersonalSolutionDatabase().getAllSolutionsFor(1);
//        System.out.println(allSolutionsFor.size());
//        Solution solution = allSolutionsFor.get(1);
//        List<Problem> testProblems = InputParser.getTestProblems();
//        Problem problem = testProblems.get(0);
//
//        String outputFormat = solution.toSerialized();
//        System.out.println(outputFormat);
//
//        System.out.println(Solution.fromSerialized(new Scanner(outputFormat)).get().toOutputFormat(problem) + "\n" + solution.toOutputFormat(problem));
//
//
//         LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
//
//        EvaluatorPhysicsRenderer renderer = new EvaluatorPhysicsRenderer();
//
//        try {
//            SwingVisualizer.visualizeProblem(problem, solution);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        config.width = 2000;
//        config.height = 1000;
//
//        DesktopLauncher.application = new LwjglApplication(renderer, config);
//        renderer.renderSolution(problem,solution);
//



    }


}
