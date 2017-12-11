package com.roomfurniture;

import com.roomfurniture.ga.GeneticAlgorithmRunner;
import com.roomfurniture.ga.algorithm.BasicGeneticAlgorithm;
import com.roomfurniture.ga.algorithm.RouletteWheelSelectionStrategy;
import com.roomfurniture.ga.algorithm.interfaces.*;
import com.roomfurniture.problem.Problem;
import com.roomfurniture.solution.*;

import java.io.FileNotFoundException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        // write your code here

        InputParser inputParser = new InputParser();
        List<Problem> problems = inputParser.parse("test.txt");

        for (Problem p : problems) {
            CrossoverStrategy<Solution> solutionCrossoverStrategy = new SolutionCrossoverStrategy();
            EvaluationStrategy<Solution> solutionEvaluationStrategy = new SolutionEvaluationStrategy(p);
            MutationStrategy<Solution> solutionMutationStrategy = new SolutionMutationStrategy();
            GeneratorStrategy<Solution> solutionGeneratorStrategy = new SolutionGeneratorStrategy(p);

            GeneticAlgorithm<Solution> algorithm = new BasicGeneticAlgorithm<Solution>(
                    100,
                    solutionEvaluationStrategy,
                    solutionCrossoverStrategy,
                    solutionMutationStrategy,
                    solutionGeneratorStrategy,
                    new RouletteWheelSelectionStrategy<>());

            GeneticAlgorithmRunner<Solution> runner = new GeneticAlgorithmRunner<>(algorithm, (level, message) -> System.out.println(message));
            runner.runTestIteration(10000);
            System.out.println(runner.findBestIndividual().get());

        }

    }

}
