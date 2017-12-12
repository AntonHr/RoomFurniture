package com.roomfurniture;

import com.roomfurniture.ga.algorithm.SimpleGeneticAlgorithmRunner;
import com.roomfurniture.ga.algorithm.BasicGeneticAlgorithm;
import com.roomfurniture.ga.algorithm.RouletteWheelSelectionStrategy;
import com.roomfurniture.ga.algorithm.interfaces.*;
import com.roomfurniture.ga.algorithm.parallel.BasicParallelGeneticAlgorithm;
import com.roomfurniture.ga.algorithm.parallel.ParallelGeneticAlgorithm;
import com.roomfurniture.ga.algorithm.parallel.ParallelGeneticAlgorithmRunner;
import com.roomfurniture.problem.Descriptor;
import com.roomfurniture.problem.Furniture;
import com.roomfurniture.problem.Problem;
import com.roomfurniture.problem.Vertex;
import com.roomfurniture.solution.*;

import java.io.FileNotFoundException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        // write your code here

        InputParser inputParser = new InputParser();
        List<Problem> problems = inputParser.parse("test.txt");
        Furniture furniture = problems.get(0).getFurnitures().get(0);

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

            SimpleGeneticAlgorithmRunner<Solution> runner = new SimpleGeneticAlgorithmRunner<>(algorithm, (level, message) -> System.out.println(message));
            runner.runTestIteration(10000);
            System.out.println(runner.findBestIndividual().get());

        }

//            ParallelGeneticAlgorithm<Solution> parallelAlgorithm = new BasicParallelGeneticAlgorithm<>(
//                    100000,
//                    solutionEvaluationStrategy,
//                    solutionCrossoverStrategy,
//                    solutionMutationStrategy,
//                    solutionGeneratorStrategy,
//                    new RouletteWheelSelectionStrategy<>());
//            ParallelGeneticAlgorithmRunner<Solution> parallelRunner = new ParallelGeneticAlgorithmRunner<>(10, parallelAlgorithm, (level, message) -> System.out.println(message));
//            parallelRunner.runTestIteration(1000);
//            System.out.println(parallelRunner.findBestIndividual().get());
//            parallelRunner.shutdown();
    }

}
