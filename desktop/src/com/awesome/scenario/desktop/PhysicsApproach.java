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
import com.roomfurniture.placing.PlacingProblem;
import com.roomfurniture.placing.PlacingSolution;
import com.roomfurniture.placing.ga.PlacingSolutionBiasedGeneratorStrategy;
import com.roomfurniture.placing.ga.PlacingSolutionCrossoverStrategy;
import com.roomfurniture.placing.ga.PlacingSolutionGeneratorStrategy;
import com.roomfurniture.placing.ga.PlacingSolutionMutationStrategyAdapter;
import com.roomfurniture.placing.physics.PhysicsPlacingSolutionEvaluationStrategy;
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
    private static int softMaxIterations = 50;

    // Whether the optimzer should try and only place high value things during it's optimization
    private static boolean optimizerShouldSortFirst = false;

    // Reducing this parameter increases accuracy, but makes the shapes move slower
    private static float simulatedtimeBetweenUpdates = 0.0001f;

    // Increasing this parameter increases time for fitting
    private static float allowedTimeForFitting = 0.005f;

    // Force applied when spreading items to add
    private static int impulseForce = 10000;

    // Force given to shapes at creation
    private static int spawnForce = 100000;

    // SINGLE THREAD SETTINGS
    private static int singleThreadPopulationSize = 2;
    private static int singleThreaditerationCount = 10;
    private static int singleThreadOptimizerPopulationSize = 100;
    private static int singleThreadOptimizerIterations = 2000;
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
        double shapeCount = parse.get(0).getFurnitures().size();
//        problem = new Problem(10, problem.getRoom(),Arrays.asList(new Furniture(0,1, problem.getFurnitures().get(0).toShape())));
        PlacingProblem placingProblem = new PlacingProblem(problem,
                Arrays.asList(
                        //                      new Vertex(0, 0)

                        new Vertex(-15.431396484375, 5.3742265701293945),
                        new Vertex(-11.057693481445312, 6.946471691131592),
                        new Vertex(-9.999999046325684, 5.517158031463623),
                        new Vertex(-11.429314613342285, 4.516636371612549),
                        new Vertex(-18.261442184448242, 3.373183488845825),
                        new Vertex(-16.632022857666016, 2.772871971130371),
                        new Vertex(-14.859671592712402, 3.4589428901672363),
                        new Vertex(-13.916322708129883, 2.8586313724517822),
                        new Vertex(-15.888777732849121, 0.37162232398986816),
                        new Vertex(-14.002082824707031, 0.8290024399757385),
                        new Vertex(-11.200623512268066, 2.458423137664795),
                        new Vertex(-8.084715843200684, 3.0015628337860107),
                        new Vertex(-11.543659210205078, -4.48805046081543),
                        new Vertex(-12.830042839050293, 0.9719338417053223),
                        new Vertex(-9.742721557617188, -0.5145537257194519),
                        new Vertex(-6.826920032501221, -0.7718306183815002),
                        new Vertex(-2.1387643814086914, -2.4298362731933594),
                        new Vertex(-3.4537363052368164, -1.2577974796295166),
                        new Vertex(-2.853421211242676, 0.7432446479797363),
                        new Vertex(-5.111741542816162, 0.08575952053070068),
                        new Vertex(-5.283257007598877, 2.972975254058838),
                        new Vertex(-6.340950965881348, 1.486487627029419),
                        new Vertex(-7.827441215515137, 0.25727686285972595),
                        new Vertex(-9.17099666595459, 1.6580066680908203),
                        new Vertex(-12.172557830810547, 1.858109951019287),
                        new Vertex(-10.600312232971191, 0.2858628034591675),
                        new Vertex(-12.544179916381836, -0.8575892448425293),
                        new Vertex(-10.257275581359863, -2.515594959259033),
                        new Vertex(-8.827959060668945, -3.6590471267700195),
                        new Vertex(-9.914239883422852, -5.317052841186523),
                        new Vertex(-7.141367435455322, -5.459985256195068),
                        new Vertex(-9.256755828857422, -8.032752990722656),
                        new Vertex(-8.8851318359375, -6.031710624694824),
                        new Vertex(-7.484404563903809, -8.947514533996582),
                        new Vertex(-6.741160869598389, -7.661130428314209),
                        new Vertex(-4.911636829376221, -7.746889114379883),
                        new Vertex(-4.254152297973633, -5.94595193862915)
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
                new PlacingSolutionBiasedGeneratorStrategy(placingProblem, 5),
//                new PlacingSolutionGeneratorStrategy(placingProblem),
                new RouletteWheelSelectionStrategy<>()
        );


        SimpleGeneticAlgorithmRunner placingSolutionParallelGeneticAlgorithmRunner = new SimpleGeneticAlgorithmRunner<PlacingSolution>(placingSolutionBasicParallelGeneticAlgorithm, (level, message) -> System.out.println(message));
        placingSolutionParallelGeneticAlgorithmRunner.runTestIteration(singleThreaditerationCount);
        Optional<PlacingSolution> bestIndividual = placingSolutionParallelGeneticAlgorithmRunner.findBestIndividual();
        System.out.println(bestIndividual.get());
        System.out.println(bestIndividual.get().getCachedResults());


        HashMap<String, Object> cachedResults = bestIndividual.get().getCachedResults();
        Solution solution = (Solution) cachedResults.get("solution");

        System.out.println("Number of placed items " + solution.findPlacedPositions(placingProblem.problem).size() + "/" + placingProblem.problem.getFurnitures().size());

        solution = BasicApproach.optimizeSolution(solution, placingProblem.problem, Executors.newFixedThreadPool(10), singleThreadOptimizerPopulationSize, singleThreadOptimizerIterations, singleThreadOptimizerNoToConsider, optimizerShouldSortFirst);

        System.out.println("Number of placed items " + solution.findPlacedPositions(placingProblem.problem).size() + "/" + placingProblem.problem.getFurnitures().size());


        if (!shouldRender)
            DesktopLauncher.application = new LwjglApplication(DesktopLauncher.renderer, config);
        DesktopLauncher.renderer.renderSolution(placingProblem.problem, solution);


        if (placingProblem.problem.getNumber() > 0 && placingProblem.problem.getNumber() <= 30) {
            SolutionDatabase.createPersonalSolutionDatabase().storeSolutionFor(placingProblem.problem.getNumber(), solution.score(placingProblem.problem).get(), (double) cachedResults.get("coverage"), solution);
        }

    }

    public static void runMultiThreaded() {
        List<Problem> parse = InputParser.getTestProblems();
        PlacingProblem placingProblem = new PlacingProblem(parse.get(0),
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
        System.out.println("Number of placed items " + solution.findPlacedPositions(placingProblem.problem).size() + "/" + placingProblem.problem.getFurnitures().size());

        solution = BasicApproach.optimizeSolution(solution, placingProblem.problem, Executors.newFixedThreadPool(10), multithreadedOptimizerPopulationSize, multithreadedOptimizerIterations, multithreadedOptimizernoToConsider, optimizerShouldSortFirst);

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
