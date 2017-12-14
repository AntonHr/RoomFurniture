package com.awesome.scenario.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gui.EvaluatorPhysicsRenderer;
import com.gui.RoomFurnitureRenderer;
import com.roomfurniture.InputParser;
import com.roomfurniture.box2d.PhysicsSimulator;
import com.roomfurniture.ga.algorithm.RouletteWheelSelectionStrategy;
import com.roomfurniture.ga.algorithm.interfaces.CrossoverStrategy;
import com.roomfurniture.ga.algorithm.interfaces.EvaluationStrategy;
import com.roomfurniture.ga.algorithm.interfaces.GeneratorStrategy;
import com.roomfurniture.ga.algorithm.interfaces.MutationStrategy;
import com.roomfurniture.ga.algorithm.parallel.BasicParallelGeneticAlgorithm;
import com.roomfurniture.ga.algorithm.parallel.ParallelGeneticAlgorithmRunner;
import com.roomfurniture.problem.Problem;
import com.roomfurniture.solution.*;
import com.roomfurniture.solution.optimizer.OptimizerProblem;
import com.roomfurniture.solution.optimizer.OptimizerProblemEvaluationStrategy;
import com.roomfurniture.solution.optimizer.OptimizerProblemGeneratorStrategy;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.awesome.scenario.desktop.BasicApproach.optimizeThenRender;

public class DesktopLauncher {

    public static EvaluatorPhysicsRenderer renderer;
    public static LwjglApplication application;

    public static void main(String[] arg) {
//        optimizeThenRender(solution, problem);
        PhysicsApproach.runMultiThreaded();
//        PhysicsApproach.runPhysicsSingleThreaded();
    }


}
