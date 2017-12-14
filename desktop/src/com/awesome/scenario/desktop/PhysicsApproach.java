package com.awesome.scenario.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gui.EvaluatorPhysicsRenderer;
import com.gui.SetSpawnRenderer;
import com.roomfurniture.InputParser;
import com.roomfurniture.SpawnPointStorage;
import com.roomfurniture.ga.algorithm.BasicGeneticAlgorithm;
import com.roomfurniture.ga.algorithm.RouletteWheelSelectionStrategy;
import com.roomfurniture.ga.algorithm.SimpleGeneticAlgorithmRunner;
import com.roomfurniture.ga.algorithm.parallel.BasicParallelGeneticAlgorithm;
import com.roomfurniture.ga.algorithm.parallel.ParallelGeneticAlgorithmRunner;
import com.roomfurniture.placing.PlacingProblem;
import com.roomfurniture.placing.PlacingSolution;
import com.roomfurniture.placing.ga.PlacingSolutionBiasedGeneratorStrategy;
import com.roomfurniture.placing.ga.PlacingSolutionCrossoverStrategy;
import com.roomfurniture.placing.ga.PlacingSolutionMutationStrategyAdapter;
import com.roomfurniture.placing.physics.PhysicsPlacingSolutionEvaluationStrategy;
import com.roomfurniture.problem.Problem;
import com.roomfurniture.problem.Vertex;
import com.roomfurniture.solution.Solution;
import com.roomfurniture.solution.storage.SolutionDatabase;

import java.util.*;
import java.util.concurrent.Executors;

public class PhysicsApproach {
    public static void runPhysicsSingleThreaded() {
        List<Problem> parse = InputParser.getTestProblems();
        Problem problem = parse.get(0);
//        problem = new Problem(10, problem.getRoom(),Arrays.asList(new Furniture(0,1, problem.getFurnitures().get(0).toShape())));
        PlacingProblem placingProblem = new PlacingProblem(problem,
                Arrays.asList(
                        new Vertex(0, 0)

//                SpawnPointStorage.getSpawnPointsForProblem(problem.getNumber())
//                        new Vertex(15,-15),
//                        new Vertex(0,-1)
                )
        );
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        DesktopLauncher.renderer = new EvaluatorPhysicsRenderer();
        config.width = 2000;
        config.height = 1000;
        boolean shouldRender = true;

        if (shouldRender)
            DesktopLauncher.application = new LwjglApplication(DesktopLauncher.renderer, config);

        int allowedSkips = 10;
        int itemsToPlace = 10;
        int softMaxIterations = 500;
        int iterations = 100;
        float simulatedtimeBetweenUpdates = 0.0001f;
        float allowedTimeForFitting = 0.0005f;
        int populationSize = 100;
        double shapeCount = 500.0;
        BasicGeneticAlgorithm<PlacingSolution> placingSolutionBasicParallelGeneticAlgorithm = new BasicGeneticAlgorithm<>(populationSize,
                new PhysicsPlacingSolutionEvaluationStrategy(placingProblem, true, softMaxIterations, itemsToPlace / shapeCount, allowedSkips / shapeCount, simulatedtimeBetweenUpdates, allowedTimeForFitting),
//                new PhysicsPlacingSolutionEvaluationStrategy(placingProblem, shouldRender, 10000, 0.25, 0.25),
                new PlacingSolutionCrossoverStrategy(placingProblem),
                new PlacingSolutionMutationStrategyAdapter(0.5, 10),
                new PlacingSolutionBiasedGeneratorStrategy(placingProblem, 10),
//                new PlacingSolutionGeneratorStrategy(placingProblem),
                new RouletteWheelSelectionStrategy<>()
        );


        SimpleGeneticAlgorithmRunner placingSolutionParallelGeneticAlgorithmRunner = new SimpleGeneticAlgorithmRunner<PlacingSolution>(placingSolutionBasicParallelGeneticAlgorithm, (level, message) -> System.out.println(message));
        placingSolutionParallelGeneticAlgorithmRunner.runTestIteration(iterations);
        Optional<PlacingSolution> bestIndividual = placingSolutionParallelGeneticAlgorithmRunner.findBestIndividual();
        System.out.println(bestIndividual.get());
        System.out.println(bestIndividual.get().getCachedResults());


        HashMap<String, Object> cachedResults = bestIndividual.get().getCachedResults();
        Solution solution = (Solution) cachedResults.get("solution");

        System.out.println("Number of placed items " + solution.findPlacedPositions(placingProblem.problem).size() + "/" + placingProblem.problem.getFurnitures().size());

        solution = BasicApproach.optimizeSolution(solution, placingProblem.problem, Executors.newFixedThreadPool(10), 100, 1000, 10, true);

        System.out.println("Number of placed items " + solution.findPlacedPositions(placingProblem.problem).size() + "/" + placingProblem.problem.getFurnitures().size());


        if (!shouldRender)
            DesktopLauncher.application = new LwjglApplication(DesktopLauncher.renderer, config);
        DesktopLauncher.renderer.renderSolution(placingProblem.problem, solution);
    }

    public static void runMultiThreaded() {
        List<Problem> parse = InputParser.getTestProblems();
        PlacingProblem placingProblem = new PlacingProblem(parse.get(0),
//                Arrays.asList(
//                        new Vertex(25, -30),
//                        new Vertex(15, -15),
//                        new Vertex(0, -1)
//                )
                SpawnPointStorage.getSpawnPointsForProblem(parse.get(0).getNumber())
        );

        int itemsToPlace = 10;
        int numberOfAllowedSkips = 10;
        BasicParallelGeneticAlgorithm<PlacingSolution> placingSolutionBasicParallelGeneticAlgorithm = new BasicParallelGeneticAlgorithm<>(100,
                new PhysicsPlacingSolutionEvaluationStrategy(placingProblem, false, 200, itemsToPlace / 500.0, numberOfAllowedSkips /500.0, 0.0001f, 0.0005f),
                new PlacingSolutionCrossoverStrategy(placingProblem),
                new PlacingSolutionMutationStrategyAdapter(0.2, 10),
                new PlacingSolutionBiasedGeneratorStrategy(placingProblem, itemsToPlace),
//                new PlacingSolutionGeneratorStrategy(placingProblem),
                new RouletteWheelSelectionStrategy<>()
        );

        ParallelGeneticAlgorithmRunner<PlacingSolution> placingSolutionParallelGeneticAlgorithmRunner = new ParallelGeneticAlgorithmRunner<>(10, placingSolutionBasicParallelGeneticAlgorithm);
        placingSolutionParallelGeneticAlgorithmRunner.runTestIteration(100);
        Optional<PlacingSolution> bestIndividual = placingSolutionParallelGeneticAlgorithmRunner.findBestIndividual();
        HashMap<String, Object> cachedResults = bestIndividual.get().getCachedResults();

        System.out.println(bestIndividual.get());
        System.out.println(cachedResults);

        Solution solution = (Solution) cachedResults.get("solution");
        System.out.println("Number of placed items " + solution.findPlacedPositions(placingProblem.problem).size() + "/" + placingProblem.problem.getFurnitures().size());

        solution = BasicApproach.optimizeSolution(solution, placingProblem.problem, Executors.newFixedThreadPool(10), 100, 1000, 10, true);

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 2000;
        config.height = 1000;

        EvaluatorPhysicsRenderer renderer = new EvaluatorPhysicsRenderer();

        DesktopLauncher.application = new LwjglApplication(renderer, config);
        renderer.renderSolution(placingProblem.problem, solution);

        if (placingProblem.problem.getNumber() > 0 && placingProblem.problem.getNumber() <= 30) {
            SolutionDatabase.createTeamSolutionDatabase().storeSolutionFor(placingProblem.problem.getNumber(), solution.score(placingProblem.problem).get(), (double) cachedResults.get("coverage"), solution);
        }

    }

    public static void runSpawnPointPicker() {
        List<Problem> problems = InputParser.getTestProblems();

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 2000;
        config.height = 1000;


        DesktopLauncher.application = new LwjglApplication(new SetSpawnRenderer(problems.get(0)), config);
    }
}
