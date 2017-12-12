package com.awesome.scenario.desktop;

import com.gui.RoomFurnitureRenderer;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.roomfurniture.InputParser;
import com.roomfurniture.ShapeCalculator;
import com.roomfurniture.ga.algorithm.RouletteWheelSelectionStrategy;
import com.roomfurniture.ga.algorithm.parallel.BasicParallelGeneticAlgorithm;
import com.roomfurniture.ga.algorithm.parallel.ParallelGeneticAlgorithm;
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

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import static com.roomfurniture.Main.doStuff;

public class DesktopLauncher {
    public static void main(String[] arg) throws FileNotFoundException {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.width = 2000;
        config.height = 1000;

        InputParser inputParser = new InputParser();
        List<Problem> parse = inputParser.parse("test.txt");
        Map<Problem, Solution> solutionMap = doStuff(parse);

        for (Map.Entry<Problem, Solution> entry : solutionMap.entrySet()) {
            Solution solution = entry.getValue();
            Problem problem = entry.getKey();


            PhysicsSimulator physicsSimulator = new PhysicsSimulator(problem, solution);

//            LwjglApplication lwjglApplication = new LwjglApplication(new RoomFurnitureRenderer(problem, solution, physicsSimulator), config);

             OptimizerProblem optimizerProblem = new OptimizerProblem(problem, solution);

            BasicParallelGeneticAlgorithm<Solution> solutionBasicParallelGeneticAlgorithm = new BasicParallelGeneticAlgorithm<>(1000, new OptimizerProblemEvaluationStrategy(optimizerProblem),
                    new SolutionCrossoverStrategy(), new SolutionMutationStrategy(problem), new OptimizerProblemGeneratorStrategy(optimizerProblem), new RouletteWheelSelectionStrategy<Solution>());

            ParallelGeneticAlgorithmRunner<Solution> runner = new ParallelGeneticAlgorithmRunner<Solution>(10, solutionBasicParallelGeneticAlgorithm);

            runner.runTestIteration(1000);

            Solution optimizedSolution = optimizerProblem.getOptimizedSolution(runner.findBestIndividual().get());
            new LwjglApplication(new RoomFurnitureRenderer(problem, optimizedSolution, physicsSimulator), config);

            System.out.println("Original score: " + entry.getValue().score(entry.getKey()));
            System.out.println("Original Coverage: " + solution.findCoverage(problem) * 100 + "%");
            System.out.println("Score is " + optimizedSolution.score(problem));
            System.out.println("Coverage: " + optimizedSolution.findCoverage(problem) * 100 + "%");
            break;
        }
//
        Furniture furniture = parse.get(0).getFurnitures().get(0);
        double area = ShapeCalculator.calculateAreaOf(furniture.toShape());
        System.out.println(furniture);


    }
}
/// CODE FOR OPTIMIZER
//            System.out.println("Score is " + value.score(key));
//            System.out.println("real score: " + entry.getValue().score(entry.getKey()));
//            System.out.println("Coverage: " + value.findCoverage(key) * 100 + "%");
//            List<Integer> placedPositions = value.findPlacedPositions(key);
//            System.out.println(placedPositions);
//            OptimizerProblem problem = new OptimizerProblem(key, value);
//
//            BasicParallelGeneticAlgorithm<Solution> solutionBasicParallelGeneticAlgorithm = new BasicParallelGeneticAlgorithm<>(1000, new OptimizerProblemEvaluationStrategy(problem),
//                    new SolutionCrossoverStrategy(), new SolutionMutationStrategy(key), new OptimizerProblemGeneratorStrategy(problem), new RouletteWheelSelectionStrategy<Solution>());
//
//            ParallelGeneticAlgorithmRunner<Solution> runner = new ParallelGeneticAlgorithmRunner<Solution>(10, solutionBasicParallelGeneticAlgorithm);
//
//            runner.runTestIteration(1000);
//
//            Solution optimizedSolution = problem.getOptimizedSolution(runner.findBestIndividual().get());
//            new LwjglApplication(new RoomFurnitureRenderer(key, optimizedSolution), config);
//
//
////            List<Descriptor> descriptors = new ArrayList();
////            for(int i = 0; i < key.getFurnitures().size() - placedPositions.size(); i++) {
////                descriptors.add(new Descriptor(new Vertex(0, 0), 0));
////            }
////            Solution optimizedSolution = new OptimizerProblem(key, value).getOptimizedSolution(new Solution(descriptors));
////            for(int i = 0; i < optimizedSolution.getDescriptors().size(); i++) {
////                int finalI = i;
////                if(placedPositions.stream().anyMatch(integer ->  integer.equals(finalI))) {
////                    System.out.println("[" + i + "]: (" + value.getDescriptors().get(i) + " -> " + optimizedSolution.getDescriptors().get(i) + ")");
////                }else {
////                    System.out.println("[" + i + "]: " + value.getDescriptors().get(i) + " -> " + optimizedSolution.getDescriptors().get(i));
////                }
////            }
////            System.out.println("Solution was \n\n" + value + "\n" + optimizedSolution);
