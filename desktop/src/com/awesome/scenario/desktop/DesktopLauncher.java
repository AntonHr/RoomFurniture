package com.awesome.scenario.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.gui.ProblemRenderer;
import com.roomfurniture.InputParser;
import com.roomfurniture.ga.algorithm.RouletteWheelSelectionStrategy;
import com.roomfurniture.ga.algorithm.parallel.BasicParallelGeneticAlgorithm;
import com.roomfurniture.ga.algorithm.parallel.ParallelGeneticAlgorithmRunner;
import com.roomfurniture.placing.PlacingDescriptor;
import com.roomfurniture.placing.PlacingProblem;
import com.roomfurniture.placing.PlacingSolution;
import com.roomfurniture.placing.ga.PlacingSolutionCrossoverStrategyAdapter;
import com.roomfurniture.placing.ga.PlacingSolutionGeneratorStrategy;
import com.roomfurniture.placing.ga.PlacingSolutionMutationStrategyAdapter;
import com.roomfurniture.placing.physics.PhysicsPlacingSolutionEvaluationStrategy;
import com.roomfurniture.problem.Problem;
import com.roomfurniture.problem.Vertex;
import com.roomfurniture.solution.Solution;
import com.roomfurniture.solution.SolutionCrossoverStrategy;
import com.roomfurniture.solution.SolutionMutationStrategy;
import com.roomfurniture.solution.optimizer.OptimizerProblem;
import com.roomfurniture.solution.optimizer.OptimizerProblemEvaluationStrategy;
import com.roomfurniture.solution.optimizer.OptimizerProblemGeneratorStrategy;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class DesktopLauncher {
    public static void main(String[] arg) throws FileNotFoundException {
        InputParser parser = new InputParser();
        Problem problem = parser.parse("test.txt").get(0);
        ProblemRenderer renderer = new ProblemRenderer(problem);

        new LwjglApplication(renderer);
    }


}
