package com.awesome.scenario.desktop;

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
    public static void main(String[] arg) {
        InputParser inputParser = new InputParser();
        List<Problem> parse = null;
        try {
            parse = inputParser.parse("test.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//        Map<Problem, Solution> solutionMap = null;
//        try {
//            solutionMap = doStuff(parse);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        for (Map.Entry<Problem, Solution> entry : solutionMap.entrySet()) {
//            Solution solution = entry.getValue();
//            Problem problem = entry.getKey();
//
//
//            PhysicsSimulator physicsSimulator = new PhysicsSimulator(problem, solution);
//
//            System.out.println("Original score: " + entry.getValue().score(entry.getKey()));
//            System.out.println("Original Coverage: " + solution.findCoverage(problem) * 100 + "%");
//            ExecutorService service = Executors.newFixedThreadPool(10);
//            Solution optimizedSolution = optimizeSolution(solution, problem, service);
//
//            Optional<Double> score = Optional.empty();
//            double coverage = 0.0;
//            for (int i = 0; i < 4; i++) {
//                System.out.println("Optimization Pass " + i);
//                optimizedSolution = optimizeSolution(optimizedSolution, problem, service);
//                score = optimizedSolution.score(problem);
//                System.out.println("Score is " + score);
//                coverage = optimizedSolution.findCoverage(problem);
//                System.out.println("Coverage: " + coverage * 100 + "%");
//            }
//            service.shutdown();
//            if (problem.getNumber() > 0 && problem.getNumber() <= 30) {
//                SolutionDatabase.createTeamSolutionDatabase().storeSolutionFor(problem.getNumber(), score.get(), coverage, optimizedSolution);
//            }
//
//            RoomFurnitureRenderer roomFurnitureRenderer = new RoomFurnitureRenderer(problem, solution, physicsSimulator);
//            physicsSimulator.setRenderer(roomFurnitureRenderer);
//            LwjglApplication lwjglApplication = new LwjglApplication(roomFurnitureRenderer, config);
//
//            break;
//        }
////
//        Furniture furniture = parse.get(0).getFurnitures().get(0);
//        double area = ShapeCalculator.calculateAreaOf(furniture.toShape());
//        System.out.println(furniture);


        PlacingProblem placingProblem = new PlacingProblem(parse.get(0),
                Arrays.asList(
                        new Vertex(-10, 10)
                ));

        PlacingSolution placingSolution = new PlacingSolutionGeneratorStrategy(placingProblem).generate();

//    public BasicParallelGeneticAlgorithm(int populationSize, EvaluationStrategy<T> evaluationStrategy, CrossoverStrategy<T> crossoverStrategy, MutationStrategy<T> mutationStrategy, GeneratorStrategy<T> generatorStrategy, SelectionStrategy<T> selectionStrategy) {
        BasicParallelGeneticAlgorithm<PlacingSolution> placingSolutionBasicParallelGeneticAlgorithm = new BasicParallelGeneticAlgorithm<>(100,
                new PhysicsPlacingSolutionEvaluationStrategy(placingProblem),
                new PlacingSolutionCrossoverStrategyAdapter(),
                new PlacingSolutionMutationStrategyAdapter(0.5, 10),
                new PlacingSolutionGeneratorStrategy(placingProblem),
                new RouletteWheelSelectionStrategy<>()
        );

        ParallelGeneticAlgorithmRunner<PlacingSolution> placingSolutionParallelGeneticAlgorithmRunner = new ParallelGeneticAlgorithmRunner<>(10, placingSolutionBasicParallelGeneticAlgorithm);
        placingSolutionParallelGeneticAlgorithmRunner.runTestIteration(10);
        System.out.println(placingSolutionParallelGeneticAlgorithmRunner.findBestIndividual());
//
//        PlacingSolution placingSolution = new PlacingSolution(
//                Arrays.asList(
//                        new PlacingDescriptor(0, 0),
//                        new PlacingDescriptor(1, 1)));
//        );

//        new PhysicsPlacingSolutionEvaluationStrategy(placingProblem).evaluate(placingSolution);
    }

    private static Solution optimizeSolution(Solution solution, Problem problem, ExecutorService service) {
        OptimizerProblem optimizerProblem = new OptimizerProblem(problem, solution);

        BasicParallelGeneticAlgorithm<Solution> solutionBasicParallelGeneticAlgorithm = new BasicParallelGeneticAlgorithm<>(100, new OptimizerProblemEvaluationStrategy(optimizerProblem),
                new SolutionCrossoverStrategy(), new SolutionMutationStrategy(problem), new OptimizerProblemGeneratorStrategy(optimizerProblem), new RouletteWheelSelectionStrategy<Solution>());

        ParallelGeneticAlgorithmRunner<Solution> runner = new ParallelGeneticAlgorithmRunner<Solution>(10, service, solutionBasicParallelGeneticAlgorithm);

        runner.runTestIteration(1000);

        return optimizerProblem.getOptimizedSolution(runner.findBestIndividual().get());
    }


}
