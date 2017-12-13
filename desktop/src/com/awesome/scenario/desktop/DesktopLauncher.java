package com.awesome.scenario.desktop;

import com.gui.RoomFurnitureRenderer;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.roomfurniture.InputParser;
import com.roomfurniture.ShapeCalculator;
import com.roomfurniture.ga.algorithm.RouletteWheelSelectionStrategy;
import com.roomfurniture.ga.algorithm.parallel.BasicParallelGeneticAlgorithm;
import com.roomfurniture.ga.algorithm.parallel.ParallelGeneticAlgorithmRunner;
import com.roomfurniture.box2d.PhysicsSimulator;
import com.roomfurniture.problem.Furniture;
import com.roomfurniture.problem.Problem;
import com.roomfurniture.solution.Solution;
import com.roomfurniture.solution.SolutionCrossoverStrategy;
import com.roomfurniture.solution.SolutionMutationStrategy;
import com.roomfurniture.solution.optimizer.OptimizerProblem;
import com.roomfurniture.solution.optimizer.OptimizerProblemEvaluationStrategy;
import com.roomfurniture.solution.optimizer.OptimizerProblemGeneratorStrategy;
import com.roomfurniture.solution.storage.SolutionDatabase;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.roomfurniture.Main.doStuff;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.width = 2000;
        config.height = 1000;

        InputParser inputParser = new InputParser();
        List<Problem> parse = null;
        try {
            parse = inputParser.parse("test.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Map<Problem, Solution> solutionMap = null;
        try {
            solutionMap = doStuff(parse);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for (Map.Entry<Problem, Solution> entry : solutionMap.entrySet()) {
            Solution solution = entry.getValue();
            Problem problem = entry.getKey();


            PhysicsSimulator physicsSimulator = new PhysicsSimulator(problem, solution);

//            LwjglApplication lwjglApplication = new LwjglApplication(new RoomFurnitureRenderer(problem, solution, physicsSimulator), config);

            System.out.println("Original score: " + entry.getValue().score(entry.getKey()));
            System.out.println("Original Coverage: " + solution.findCoverage(problem) * 100 + "%");
            ExecutorService service = Executors.newFixedThreadPool(10);
            Solution optimizedSolution = optimizeSolution(solution, problem, service);

            Optional<Double> score = Optional.empty();
            double coverage = 0.0;
            for(int i = 0; i< 4; i++){
                System.out.println("Optimization Pass " + i);
                optimizedSolution = optimizeSolution(optimizedSolution, problem, service);
                score = optimizedSolution.score(problem);
                System.out.println("Score is " + score);
                coverage = optimizedSolution.findCoverage(problem);
                System.out.println("Coverage: " + coverage * 100 + "%");
            }
            service.shutdown();
            if(problem.getNumber() > 0 && problem.getNumber() <= 30) {
                SolutionDatabase.createTeamSolutionDatabase().storeSolutionFor(problem.getNumber(), score.get(),coverage, optimizedSolution);
            }
            new LwjglApplication(new RoomFurnitureRenderer(problem, optimizedSolution, physicsSimulator), config);


            break;
        }
//
        Furniture furniture = parse.get(0).getFurnitures().get(0);
        double area = ShapeCalculator.calculateAreaOf(furniture.toShape());
        System.out.println(furniture);


    }

    private static Solution optimizeSolution(Solution solution, Problem problem, ExecutorService service) {
        OptimizerProblem optimizerProblem = new OptimizerProblem(problem, solution);

        BasicParallelGeneticAlgorithm<Solution> solutionBasicParallelGeneticAlgorithm = new BasicParallelGeneticAlgorithm<>(1000, new OptimizerProblemEvaluationStrategy(optimizerProblem),
                new SolutionCrossoverStrategy(), new SolutionMutationStrategy(problem), new OptimizerProblemGeneratorStrategy(optimizerProblem), new RouletteWheelSelectionStrategy<Solution>());

        ParallelGeneticAlgorithmRunner<Solution> runner = new ParallelGeneticAlgorithmRunner<Solution>(10, service, solutionBasicParallelGeneticAlgorithm);

        runner.runTestIteration(1000);

        return optimizerProblem.getOptimizedSolution(runner.findBestIndividual().get());
    }
}
