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
import com.roomfurniture.ga.algorithm.parallel.ParallelGeneticAlgorithmRunner;
import com.roomfurniture.problem.Problem;
import com.roomfurniture.solution.*;
import com.roomfurniture.solution.optimizer.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BasicApproach {
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

        System.out.println("Original score: " + solution.score(problem));
        System.out.println("Original Coverage: " + solution.findCoverage(problem) * 100 + "%");
        ExecutorService service = Executors.newFixedThreadPool(10);
        Solution optimizedSolution = optimizeSolution(solution, problem, service, 1000, 100, 10, true);

        Optional<Double> score = Optional.empty();
        double coverage = 0.0;
        for (int i = 0; i < 4; i++) {
            System.out.println("Optimization Pass " + i);
            optimizedSolution = optimizeSolution(optimizedSolution, problem, service, 1000, 100, 10, true);
            score = optimizedSolution.score(problem);
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
