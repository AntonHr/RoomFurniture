package com.awesome.scenario.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gui.RoomFurnitureRenderer;
import com.roomfurniture.InputParser;
import com.roomfurniture.box2d.PhysicsSimulator;
import com.roomfurniture.ga.algorithm.RouletteWheelSelectionStrategy;
import com.roomfurniture.ga.algorithm.interfaces.CrossoverStrategy;
import com.roomfurniture.ga.algorithm.interfaces.EvaluationStrategy;
import com.roomfurniture.ga.algorithm.interfaces.GeneratorStrategy;
import com.roomfurniture.ga.algorithm.interfaces.MutationStrategy;
import com.roomfurniture.ga.algorithm.parallel.BasicParallelGeneticAlgorithm;
import com.roomfurniture.ga.algorithm.parallel.ParallelGeneticAlgorithm;
import com.roomfurniture.ga.algorithm.parallel.ParallelGeneticAlgorithmRunner;
import com.roomfurniture.placing.BasicPlacingProblem;
import com.roomfurniture.placing.PlacingSolution;
import com.roomfurniture.placing.ga.PlacingSolutionCrossoverStrategy;
import com.roomfurniture.placing.plain.PlainPlacingSolutionEvaluationStrategy;
import com.roomfurniture.placing.plain.PlainPlacingSolutionGeneratorStrategy;
import com.roomfurniture.placing.plain.PlainPlacingSolutionMutationStrategy;
import com.roomfurniture.problem.BasicProblem;
import com.roomfurniture.problem.Problem;
import com.roomfurniture.problem.Vertex;
import com.roomfurniture.solution.*;
import com.roomfurniture.solution.optimizer.*;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BasicApproach {
     // SINGLE THREAD SETTINGS
    private static int singleThreadPopulationSize = 10;
    private static int singleThreaditerationCount = 10;
    private static int singleThreadOptimizerPopulationSize = 100;
    private static int singleThreadOptimizerIterations = 1000;
    private static int singleThreadOptimizerNoToConsider = 10;

       private static boolean optimizerShouldSortFirst = true;
    private static int divisions = 2;
    private static double thetaRange = Math.PI / 20;

    public static void runPlainSolution() {
        List<Problem> testProblems = InputParser.getTestProblems();


         List<Problem> parse = InputParser.getTestProblems();
        Problem problem = parse.get(0);
        double shapeCount = parse.get(0).getFurnitures().size();
//        problem = new Problem(10, problem.getRoom(),Arrays.asList(new Furniture(0,1, problem.getFurnitures().get(0).toShape())));
        BasicPlacingProblem placingProblem = new BasicPlacingProblem(problem,
                Arrays.asList(
                        //                      new Vertex(0, 0)


//                        new Vertex(-0.6249994039535522, -2.107789993286133),
//                        new Vertex(11.718749046325684, -13.01494026184082),
//                        new Vertex(25.3125, -29.717182159423828)
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
                )


        );

        ParallelGeneticAlgorithm<PlacingSolution> placingSolutionBasicParallelGeneticAlgorithm = new BasicParallelGeneticAlgorithm<>(singleThreadPopulationSize,
               new PlainPlacingSolutionEvaluationStrategy(placingProblem, divisions, thetaRange),
//                new PhysicsPlacingSolutionEvaluationStrategy(placingProblem, shouldRender, 10000, 0.25, 0.25),
                new PlacingSolutionCrossoverStrategy(placingProblem),
//                new PlacingSolutionMutationStrategyAdapter(0.5, 10),
                new PlainPlacingSolutionMutationStrategy(0.3, 10),
//                new PlacingSolutionBiasedGeneratorStrategy(placingProblem, 10),
                new PlainPlacingSolutionGeneratorStrategy(placingProblem),
//                new PlacingSolutionGeneratorStrategy(placingProblem),
                new RouletteWheelSelectionStrategy<>()
        );


        ParallelGeneticAlgorithmRunner<PlacingSolution> placingSolutionParallelGeneticAlgorithmRunner = new ParallelGeneticAlgorithmRunner<PlacingSolution>(10, placingSolutionBasicParallelGeneticAlgorithm);
        placingSolutionParallelGeneticAlgorithmRunner.runTestIteration(singleThreaditerationCount);
        Optional<PlacingSolution> bestIndividual = placingSolutionParallelGeneticAlgorithmRunner.findBestIndividual();
        System.out.println(bestIndividual.get());


        Solution solution = bestIndividual.get().retrieveSolution().get();
        System.out.println("Number of placed items " + solution.findPlacedPositions(placingProblem.getProblem()).size() + "/" + placingProblem.getProblem().getFurnitures().size());

        solution = BasicApproach.optimizeSolution(solution, placingProblem.getProblem(), Executors.newFixedThreadPool(10), singleThreadOptimizerPopulationSize, singleThreadOptimizerIterations, singleThreadOptimizerNoToConsider, optimizerShouldSortFirst);

        System.out.println("Number of placed items " + solution.findPlacedPositions(placingProblem.getProblem()).size() + "/" + placingProblem.getProblem().getFurnitures().size());

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 2000;
        config.height = 1000;


        DesktopLauncher.application = new LwjglApplication(DesktopLauncher.renderer, config);
        DesktopLauncher.renderer.renderSolution(placingProblem.getProblem(), solution);

//
//        if (placingProblem.problem.getNumber() > 0 && placingProblem.problem.getNumber() <= 30) {
//            SolutionDatabase.createPersonalSolutionDatabase().storeSolutionFor(placingProblem.problem.getNumber(), solution.score(placingProblem.problem).get(), (double) cachedResults.get("coverage"), solution);
//        }
//




    }
    public static List<Solution> runGeneticAlgorithm() throws FileNotFoundException {
        List<Problem> problems = InputParser.getTestProblems();
        List<Solution> solutions = new ArrayList<>();

        for (Problem problem : problems) {
            CrossoverStrategy<Solution> solutionCrossoverStrategy = new SolutionCrossoverStrategy();
            EvaluationStrategy<Solution> solutionEvaluationStrategy = new SolutionEvaluationStrategy(problem);
            MutationStrategy<Solution> solutionMutationStrategy = new SolutionMutationStrategy(problem);
            GeneratorStrategy<Solution> solutionGeneratorStrategy = new SolutionGeneratorStrategy(problem);
            BasicParallelGeneticAlgorithm<Solution> parallelAlgorithm = new BasicParallelGeneticAlgorithm<>(
                    1000,
                    solutionEvaluationStrategy,
                    solutionCrossoverStrategy,
                    solutionMutationStrategy,
                    solutionGeneratorStrategy,
                    new RouletteWheelSelectionStrategy<>());
            ParallelGeneticAlgorithmRunner<Solution> parallelRunner = new ParallelGeneticAlgorithmRunner<>(10, parallelAlgorithm, (level, message) -> System.out.println(message));
            parallelRunner.runTestIteration(1);

            Solution solution = parallelRunner.findBestIndividual().get();
            System.out.println(solution);


            solutions.add(solution);


        }

        return solutions;
    }

    public static void optimizeThenRender(Solution solution, Problem problem) {
        PhysicsSimulator physicsSimulator = new PhysicsSimulator(problem, solution);

        System.out.println("Original score: " + problem.score(solution));
        System.out.println("Original Coverage: " + solution.findCoverage(problem) * 100 + "%");
        ExecutorService service = Executors.newFixedThreadPool(10);
        Solution optimizedSolution = optimizeSolution(solution, problem, service, 1000, 100, 10, true);

        Optional<Double> score = Optional.empty();
        double coverage = 0.0;
        for (int i = 0; i < 4; i++) {
            System.out.println("Optimization Pass " + i);
            optimizedSolution = optimizeSolution(optimizedSolution, problem, service, 1000, 100, 10, true);
            score = problem.score(optimizedSolution);
            System.out.println("Score is " + score);
            coverage = optimizedSolution.findCoverage(problem);
            System.out.println("Coverage: " + coverage * 100 + "%");
        }
        service.shutdown();

//            if (problem.getNumber() > 0 && problem.getNumber() <= 30) {
//                SolutionDatabase.createTeamSolutionDatabase().storeSolutionFor(problem.getNumber(), score.get(), coverage, optimizedSolution);
//            }

        RoomFurnitureRenderer roomFurnitureRenderer = new RoomFurnitureRenderer(problem, solution, physicsSimulator);
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 2000;
        config.height = 1000;
        physicsSimulator.setRenderer(roomFurnitureRenderer);
        LwjglApplication lwjglApplication = new LwjglApplication(roomFurnitureRenderer, config);
    }

    public static Solution optimizeSolution(Solution solution, Problem problem, ExecutorService service, int populationSize, int iterations, int noToConsider, boolean shouldSortFirst) {
        OptimizerProblem optimizerProblem = new IgnoringOptimizerProblem(problem, solution, noToConsider, shouldSortFirst);

        BasicParallelGeneticAlgorithm<Solution> solutionBasicParallelGeneticAlgorithm = new BasicParallelGeneticAlgorithm<>(populationSize, new OptimizerProblemEvaluationStrategy(optimizerProblem),
                new SolutionCrossoverStrategy(), new SolutionMutationStrategy(problem), new OptimizerProblemGeneratorStrategy(optimizerProblem), new RouletteWheelSelectionStrategy<Solution>());

        ParallelGeneticAlgorithmRunner<Solution> runner = new ParallelGeneticAlgorithmRunner<Solution>(10, service, solutionBasicParallelGeneticAlgorithm);

        runner.runTestIteration(iterations);

        return optimizerProblem.getOptimizedSolution(runner.findBestIndividual().get());
    }
}
