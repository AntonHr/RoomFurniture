package com.roomfurniture;

import com.roomfurniture.ga.algorithm.RouletteWheelSelectionStrategy;
import com.roomfurniture.ga.algorithm.interfaces.CrossoverStrategy;
import com.roomfurniture.ga.algorithm.interfaces.EvaluationStrategy;
import com.roomfurniture.ga.algorithm.interfaces.GeneratorStrategy;
import com.roomfurniture.ga.algorithm.interfaces.MutationStrategy;
import com.roomfurniture.ga.algorithm.parallel.BasicParallelGeneticAlgorithm;
import com.roomfurniture.ga.algorithm.parallel.ParallelGeneticAlgorithmRunner;
import com.roomfurniture.problem.Descriptor;
import com.roomfurniture.problem.Furniture;
import com.roomfurniture.problem.Problem;
import com.roomfurniture.problem.Vertex;
import com.roomfurniture.solution.*;
import com.gui.SwingVisualizer;
import com.roomfurniture.solution.storage.SolutionDatabase;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        InputParser inputParser = new InputParser();
        List<Problem> parse = inputParser.parse("problemsets.txt");
        try {
            FileWriter fileWriter = new FileWriter("./output.txt", false);
            fileWriter.write(SolutionDatabase.createTeamSolutionDatabase().generateOverallSolutionReportFor(parse));
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                    5000,
                    solutionEvaluationStrategy,
                    solutionCrossoverStrategy,
                    solutionMutationStrategy,
                    solutionGeneratorStrategy,
                    new RouletteWheelSelectionStrategy<>());
            ParallelGeneticAlgorithmRunner<Solution> parallelRunner = new ParallelGeneticAlgorithmRunner<>(10, parallelAlgorithm, (level, message) -> System.out.println(message));
            parallelRunner.runTestIteration(1);
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
//            System.out.println(solution);
//            System.out.println(parallelAlgorithm.findBestFitness());

            Map<Problem, Solution> solutionMap = new HashMap<>();
            solutionMap.put(problem, solution);


            return solutionMap;
        }

//            parallelRunner.shutdown();
        return null;
    }

}
