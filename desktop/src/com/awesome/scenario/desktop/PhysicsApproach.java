package com.awesome.scenario.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gui.EvaluatorPhysicsRenderer;
import com.roomfurniture.InputParser;
import com.roomfurniture.ga.algorithm.BasicGeneticAlgorithm;
import com.roomfurniture.ga.algorithm.RouletteWheelSelectionStrategy;
import com.roomfurniture.ga.algorithm.SimpleGeneticAlgorithmRunner;
import com.roomfurniture.ga.algorithm.parallel.BasicParallelGeneticAlgorithm;
import com.roomfurniture.ga.algorithm.parallel.ParallelGeneticAlgorithmRunner;
import com.roomfurniture.placing.PlacingProblem;
import com.roomfurniture.placing.PlacingSolution;
import com.roomfurniture.placing.ga.PlacingSolutionCrossoverStrategy;
import com.roomfurniture.placing.ga.PlacingSolutionGeneratorStrategy;
import com.roomfurniture.placing.ga.PlacingSolutionMutationStrategyAdapter;
import com.roomfurniture.placing.physics.PhysicsPlacingSolutionEvaluationStrategy;
import com.roomfurniture.problem.Problem;
import com.roomfurniture.problem.Vertex;
import com.roomfurniture.solution.Solution;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class PhysicsApproach {
    public static void runPhysicsSingleThreaded() {
        List<Problem> parse = InputParser.getTestProblems();
        PlacingProblem placingProblem = new PlacingProblem(parse.get(0),
                Arrays.asList(
                        new Vertex(25,-30),
                        new Vertex(15,-15),
                        new Vertex(0,-1)
                ));
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        DesktopLauncher.renderer = new EvaluatorPhysicsRenderer();
        config.width = 2000;
        config.height = 1000;
        boolean shouldRender = true;

        if(shouldRender)
            DesktopLauncher.application = new LwjglApplication(DesktopLauncher.renderer, config);

        BasicGeneticAlgorithm<PlacingSolution> placingSolutionBasicParallelGeneticAlgorithm = new BasicGeneticAlgorithm<>(10,
                new PhysicsPlacingSolutionEvaluationStrategy(placingProblem,shouldRender),
                new PlacingSolutionCrossoverStrategy(placingProblem),
                new PlacingSolutionMutationStrategyAdapter(0.5, 10),
                new PlacingSolutionGeneratorStrategy(placingProblem),
                new RouletteWheelSelectionStrategy<>()
        );


        SimpleGeneticAlgorithmRunner placingSolutionParallelGeneticAlgorithmRunner = new SimpleGeneticAlgorithmRunner<PlacingSolution>(placingSolutionBasicParallelGeneticAlgorithm, (level, message) -> System.out.println(message));
        placingSolutionParallelGeneticAlgorithmRunner.runTestIteration(1);
        Optional<PlacingSolution> bestIndividual = placingSolutionParallelGeneticAlgorithmRunner.findBestIndividual();
        System.out.println(bestIndividual.get());
        System.out.println(bestIndividual.get().getCachedResults());


        HashMap<String, Object> cachedResults = bestIndividual.get().getCachedResults();
        Solution solution = (Solution) cachedResults.get("solution");

        if(!shouldRender)
            DesktopLauncher.application = new LwjglApplication(DesktopLauncher.renderer, config);
        DesktopLauncher.renderer.renderSolution(placingProblem.problem, solution);
    }

    public static void runMultiThreaded() {
        List<Problem> parse = InputParser.getTestProblems();
        PlacingProblem placingProblem = new PlacingProblem(parse.get(0),
                Arrays.asList(
                        new Vertex(25,-30),
                        new Vertex(15,-15),
                        new Vertex(0,-1)
                ));

        BasicParallelGeneticAlgorithm<PlacingSolution> placingSolutionBasicParallelGeneticAlgorithm = new BasicParallelGeneticAlgorithm<>(10,
                new PhysicsPlacingSolutionEvaluationStrategy(placingProblem, false),
                new PlacingSolutionCrossoverStrategy(placingProblem),
                new PlacingSolutionMutationStrategyAdapter(0.5, 10),
                new PlacingSolutionGeneratorStrategy(placingProblem),
                new RouletteWheelSelectionStrategy<>()
        );

        ParallelGeneticAlgorithmRunner<PlacingSolution> placingSolutionParallelGeneticAlgorithmRunner = new ParallelGeneticAlgorithmRunner<>(10, placingSolutionBasicParallelGeneticAlgorithm);
        placingSolutionParallelGeneticAlgorithmRunner.runTestIteration(1);
        Optional<PlacingSolution> bestIndividual = placingSolutionParallelGeneticAlgorithmRunner.findBestIndividual();
        HashMap<String, Object> cachedResults = bestIndividual.get().getCachedResults();

        System.out.println(bestIndividual.get());
        System.out.println(cachedResults);

        Solution solution = (Solution) cachedResults.get("solution");

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 2000;
        config.height = 1000;

        EvaluatorPhysicsRenderer renderer = new EvaluatorPhysicsRenderer();

        DesktopLauncher.application = new LwjglApplication(renderer, config);
        renderer.renderSolution(placingProblem.problem, solution);
    }
}
