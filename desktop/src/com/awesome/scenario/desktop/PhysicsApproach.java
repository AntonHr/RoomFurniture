package com.awesome.scenario.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gui.EvaluatorPhysicsRenderer;
import com.gui.SetSpawnRenderer;
import com.roomfurniture.InputParser;
import com.roomfurniture.ga.algorithm.BasicGeneticAlgorithm;
import com.roomfurniture.ga.algorithm.RouletteWheelSelectionStrategy;
import com.roomfurniture.ga.algorithm.SimpleGeneticAlgorithmRunner;
import com.roomfurniture.ga.algorithm.parallel.BasicParallelGeneticAlgorithm;
import com.roomfurniture.ga.algorithm.parallel.ParallelGeneticAlgorithmRunner;
import com.roomfurniture.placing.BasicPlacingProblem;
import com.roomfurniture.placing.PlacingSolution;
import com.roomfurniture.placing.ga.PlacingSolutionBiasedGeneratorStrategy;
import com.roomfurniture.placing.ga.PlacingSolutionCrossoverStrategy;
import com.roomfurniture.placing.ga.PlacingSolutionGeneratorStrategy;
import com.roomfurniture.placing.ga.PlacingSolutionMutationStrategyAdapter;
import com.roomfurniture.placing.physics.PhysicsPlacingSolutionEvaluationStrategy;
import com.roomfurniture.problem.BiasedProblem;
import com.roomfurniture.problem.Problem;
import com.roomfurniture.problem.Vertex;
import com.roomfurniture.solution.Solution;
import com.roomfurniture.solution.storage.SolutionDatabase;

import java.util.*;
import java.util.concurrent.Executors;

public class PhysicsApproach {

    // COMMON PARAMETERS

    // Number of skipped shapes before failure
    private static int allowedSkips = 10;

    // Number of items to place before can timeout
    private static int itemsToPlace = 10;

    // Number of singleThreaditerationCount before earlier conditions are checked
    private static int softMaxIterations = 1000;

    // Whether the optimzer should try and only place high value things during it's optimization
    private static boolean optimizerShouldSortFirst = false;

    // Reducing this parameter increases accuracy, but makes the shapes move slower
    private static float simulatedtimeBetweenUpdates = 0.0001f;

    // Increasing this parameter increases time for fitting
    private static float allowedTimeForFitting = 0.05f;

    // Force applied when spreading items to add
    private static int impulseForce = 10000;

    // Force given to shapes at creation
    private static int spawnForce = 1000000;

    // SINGLE THREAD SETTINGS
    private static int singleThreadPopulationSize = 100;
    private static int singleThreaditerationCount = 10;
    private static int singleThreadOptimizerPopulationSize = 100;
    private static int singleThreadOptimizerIterations = 1000;
    private static int singleThreadOptimizerNoToConsider = 10;

    // MULTI THREAD SETTINGS
    private static int multithreadedPopulationSize = 10;
    private static int multithreadedIterationCount = 100;
    private static int multithreadedOptimizerPopulationSize = 100;
    private static int multithreadedOptimizerIterations = 10;
    private static int multithreadedOptimizernoToConsider = 10;


    public static void runPhysicsSingleThreaded() {
        List<Problem> parse = InputParser.getTestProblems();
        Problem problem = parse.get(0);
        problem = new BiasedProblem(problem, 2);
        double shapeCount = parse.get(0).getFurnitures().size();
//        problem = new Problem(10, problem.getRoom(),Arrays.asList(new Furniture(0,1, problem.getFurnitures().get(0).toShape())));
        BasicPlacingProblem placingProblem = new BasicPlacingProblem(problem,
                Arrays.asList(
                        //                      new Vertex(0, 0)

                        new Vertex(3.0124783515930176, 1.8711000680923462)
//                        new Vertex(6.8586297035217285, 2.661121368408203)
//                        new Vertex(7.128899574279785, 0.977130115032196)


//                        new Vertex(-0.6249994039535522, -2.107789993286133),
//                        new Vertex(11.718749046325684, -13.01494026184082),
//                        new Vertex(25.3125, -29.717182159423828)

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

        BasicGeneticAlgorithm<PlacingSolution> placingSolutionBasicParallelGeneticAlgorithm = new BasicGeneticAlgorithm<>(singleThreadPopulationSize,
                new PhysicsPlacingSolutionEvaluationStrategy(placingProblem, true, softMaxIterations, itemsToPlace / shapeCount, allowedSkips / shapeCount, simulatedtimeBetweenUpdates, allowedTimeForFitting, impulseForce, spawnForce),
//                new PhysicsPlacingSolutionEvaluationStrategy(placingProblem, shouldRender, 10000, 0.25, 0.25),
                new PlacingSolutionCrossoverStrategy(placingProblem),
                new PlacingSolutionMutationStrategyAdapter(0.5, 10),
//                new PlacingSolutionBiasedGeneratorStrategy(placingProblem, 10),
                new PlacingSolutionGeneratorStrategy(placingProblem),
                new RouletteWheelSelectionStrategy<>()
        );


        SimpleGeneticAlgorithmRunner placingSolutionParallelGeneticAlgorithmRunner = new SimpleGeneticAlgorithmRunner<PlacingSolution>(placingSolutionBasicParallelGeneticAlgorithm, (level, message) -> System.out.println(message));
        placingSolutionParallelGeneticAlgorithmRunner.runTestIteration(singleThreaditerationCount);
        Optional<PlacingSolution> bestIndividual = placingSolutionParallelGeneticAlgorithmRunner.findBestIndividual();
        System.out.println(bestIndividual.get());
        System.out.println(bestIndividual.get().getCachedResults());


        HashMap<String, Object> cachedResults = bestIndividual.get().getCachedResults();
        Solution solution = (Solution) cachedResults.get("solution");

        System.out.println("Number of placed items " + solution.findPlacedPositions(placingProblem.getProblem()).size() + "/" + placingProblem.getProblem().getFurnitures().size());

        solution = BasicApproach.optimizeSolution(solution, placingProblem.getProblem(), Executors.newFixedThreadPool(10), singleThreadOptimizerPopulationSize, singleThreadOptimizerIterations, singleThreadOptimizerNoToConsider, optimizerShouldSortFirst);

        System.out.println("Number of placed items " + solution.findPlacedPositions(placingProblem.getProblem()).size() + "/" + placingProblem.getProblem().getFurnitures().size());


        if (!shouldRender)
            DesktopLauncher.application = new LwjglApplication(DesktopLauncher.renderer, config);
        DesktopLauncher.renderer.renderSolution(placingProblem.getProblem(), solution);


        if (placingProblem.getProblem().getNumber() > 0 && placingProblem.getProblem().getNumber() <= 30) {
            SolutionDatabase.createPersonalSolutionDatabase().storeSolutionFor(placingProblem.getProblem().getNumber(), placingProblem.getProblem().score(solution).get(), (double) cachedResults.get("coverage"), solution);
        }

    }

    public static void runMultiThreaded() {
        List<Problem> parse = InputParser.getTestProblems();
        BasicPlacingProblem placingProblem = new BasicPlacingProblem(parse.get(0),
                Arrays.asList(
//                        new Vertex(25, -30),
//                        new Vertex(15, -15),
//                        new Vertex(0, -1)
                        new Vertex(-0.5338521003723145, -1.9143553972244263),
                        new Vertex(12.705730438232422, -13.947439193725586),
                        new Vertex(29.255212783813477, -27.840179443359375),
                        new Vertex(28.507816314697266, -32.981590270996094)
                )
//                SpawnPointStorage.getSpawnPointsForProblem(parse.get(0).getNumber())
        );

        double shapeCount = parse.get(0).getFurnitures().size();
        BasicParallelGeneticAlgorithm<PlacingSolution> placingSolutionBasicParallelGeneticAlgorithm = new BasicParallelGeneticAlgorithm<>(multithreadedPopulationSize,
                new PhysicsPlacingSolutionEvaluationStrategy(placingProblem, false, softMaxIterations, itemsToPlace / shapeCount, allowedSkips / shapeCount, simulatedtimeBetweenUpdates, allowedTimeForFitting, impulseForce, spawnForce),
                new PlacingSolutionCrossoverStrategy(placingProblem),
                new PlacingSolutionMutationStrategyAdapter(0.2, 10),
//                new PlacingSolutionBiasedGeneratorStrategy(placingProblem, itemsToPlace),
                new PlacingSolutionGeneratorStrategy(placingProblem),
                new RouletteWheelSelectionStrategy<>()
        );

        ParallelGeneticAlgorithmRunner<PlacingSolution> placingSolutionParallelGeneticAlgorithmRunner = new ParallelGeneticAlgorithmRunner<>(10, placingSolutionBasicParallelGeneticAlgorithm);
        placingSolutionParallelGeneticAlgorithmRunner.runTestIteration(multithreadedIterationCount);
        Optional<PlacingSolution> bestIndividual = placingSolutionParallelGeneticAlgorithmRunner.findBestIndividual();
        HashMap<String, Object> cachedResults = bestIndividual.get().getCachedResults();

        System.out.println(bestIndividual.get());
        System.out.println(cachedResults);

        Solution solution = (Solution) cachedResults.get("solution");
        System.out.println("Number of placed items " + solution.findPlacedPositions(placingProblem.getProblem()).size() + "/" + placingProblem.getProblem().getFurnitures().size());

        solution = BasicApproach.optimizeSolution(solution, placingProblem.getProblem(), Executors.newFixedThreadPool(10), multithreadedOptimizerPopulationSize, multithreadedOptimizerIterations, multithreadedOptimizernoToConsider, optimizerShouldSortFirst);

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 2000;
        config.height = 1000;

        EvaluatorPhysicsRenderer renderer = new EvaluatorPhysicsRenderer();

        DesktopLauncher.application = new LwjglApplication(renderer, config);
        renderer.renderSolution(placingProblem.getProblem(), solution);

        if (placingProblem.getProblem().getNumber() > 0 && placingProblem.getProblem().getNumber() <= 30) {
            SolutionDatabase.createTeamSolutionDatabase().storeSolutionFor(placingProblem.getProblem().getNumber(), placingProblem.getProblem().score(solution).get(), (double) cachedResults.get("coverage"), solution);
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
