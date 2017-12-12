package com.roomfurniture;

import com.roomfurniture.ga.algorithm.Result;
import com.roomfurniture.ga.algorithm.RouletteWheelSelectionStrategy;
import com.roomfurniture.ga.algorithm.interfaces.CrossoverStrategy;
import com.roomfurniture.ga.algorithm.interfaces.EvaluationStrategy;
import com.roomfurniture.ga.algorithm.interfaces.GeneratorStrategy;
import com.roomfurniture.ga.algorithm.interfaces.MutationStrategy;
import com.roomfurniture.ga.algorithm.parallel.BasicParallelGeneticAlgorithm;
import com.roomfurniture.ga.algorithm.parallel.ParallelGeneticAlgorithm;
import com.roomfurniture.ga.algorithm.parallel.ParallelGeneticAlgorithmRunner;
import com.roomfurniture.problem.Descriptor;
import com.roomfurniture.problem.Furniture;
import com.roomfurniture.problem.Problem;
import com.roomfurniture.problem.Vertex;
import com.roomfurniture.solution.*;
import com.tempgui.SolutionVisualizer;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        InputParser inputParser = new InputParser();
        List<Problem> parse = inputParser.parse("customTest.txt");
        Map<Problem, Solution> solutionMap = doStuff(parse);

        for (Map.Entry<Problem, Solution> entry : solutionMap.entrySet()) {
            Solution value = entry.getValue();
            Problem key = entry.getKey();
            value.getDescriptors().set(0, new Descriptor(new Vertex(1,1),0));
            System.out.println("Score is " + value.score(key));
            JFrame bestFrame = SolutionVisualizer.constructVisualizationFrame(key, value);
            bestFrame.setTitle("Best solution");
            EventQueue.invokeLater(() -> {
                bestFrame.setVisible(true);
            });
            break;
        }
//
        Furniture furniture = parse.get(0).getFurnitures().get(0);
        double area = ShapeCalculator.calculateAreaOf(furniture.toShape());
        System.out.println("Area : " + area);
        System.out.println(furniture);
    }


    public static Map<Problem, Solution> doStuff(List<Problem> parse) throws FileNotFoundException {
        // write your code here


        List<Problem> problems = parse;
        Furniture furniture = problems.get(0).getFurnitures().get(0);

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
            parallelRunner.runTestIteration(1000);
            Solution solution = parallelRunner.findBestIndividual().get();
            System.out.println(solution);


//            BasicGeneticAlgorithm<Solution> algorithm = new BasicGeneticAlgorithm<Solution>(
//                    100,
//                    solutionEvaluationStrategy,
//                    solutionCrossoverStrategy,
//                    solutionMutationStrategy,
//                    solutionGeneratorStrategy,
//                    new RouletteWheelSelectionStrategy<>());
//
//            SimpleGeneticAlgorithmRunner<Solution> runner = new SimpleGeneticAlgorithmRunner<>(algorithm, (level, message) -> System.out.println(message));
//            runner.runTestIteration(100);
//            Solution x = runner.findBestIndividual().get();
            System.out.println(solution);
            System.out.println(parallelAlgorithm.findBestFitness());

            Map<Problem, Solution> solutionMap = new HashMap<>();
            solutionMap.put(problem, solution);
//
//            int i = 0;
//            for (Result<Solution> result : parallelAlgorithm.getPopulation()) {
//                i++;
//                if (i > 5) break;
//                JFrame bestFrame = SolutionVisualizer.constructVisualizationFrame(problem, result.getValue());
//                bestFrame.setTitle("Solution " + i +  "- fitness: " + result.getFitness());
//                EventQueue.invokeLater(() -> {
//                    bestFrame.setVisible(true);
//                });
//            }
//

            return solutionMap;

//            int i = 0;
//            for(Result<Solution> result: parallelAlgorithm.getPopulation()) {
//                i++;
//                if(i > 5) break;
//                JFrame frame = SolutionVisualizer.constructVisualizationFrame(p, result.getValue());
//                frame.setTitle("Okay Solution no. " + i);
//                EventQueue.invokeLater(() -> {
//                    frame.setVisible(true);
//                });
//            }

        }

//            parallelRunner.shutdown();
        return null;
    }

}
