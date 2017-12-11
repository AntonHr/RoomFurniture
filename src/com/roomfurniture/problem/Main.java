package com.roomfurniture.problem;

import java.io.FileNotFoundException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        // write your code here

//        GeneticAlgorithm<Bitstring> algorithm = new BitstringGeneticAlgorithmBuilder().withInputSize(8).withEvaluator(individual -> 1.0).build();
//
//        GeneticAlgorithmRunner<Bitstring> runner = new GeneticAlgorithmRunner<>(algorithm, (level, message) -> System.out.println(message));
//        runner.runTestIteration(100);
//        System.out.println(runner.findBestIndividual().get());

        InputParser inputParser = new InputParser();
        List<Problem> problems = inputParser.parse("test.txt");

        for(Problem p : problems) {
            System.out.println(p.room.polygon);
        }

        System.out.println(Polygonizable.contains(problems.get(0).room, problems.get(1).room));
        System.out.println(Polygonizable.contains(problems.get(1).room, problems.get(0).room));

//        System.out.println(problems);
//
//        for(Problem p : problems) {
//            CrossoverStrategy<Solution> solutionCrossoverStrategy = (parentA, parentB) -> {
//                //TODO(Kiran): Implement strategy
//                return null;
//            };
//            EvaluationStrategy<Solution> solutionEvaluationStrategy = individual -> {return p.score(individual).orElse(0.0);};
//            MutationStrategy<Solution> solutionMutationStrategy = (MutationStrategy<Solution>) individual -> {
//                //TODO(Kiran): Implement strategy
//                return null;
//            };
//            GeneratorStrategy<Solution> solutionGeneratorStrategy = (GeneratorStrategy<Solution>) () -> {
//                //TODO(Kiran): Implement strategy
//                    return null;
//            };
//
//            GeneticAlgorithm<Solution> algorithm = new BasicGeneticAlgorithm<Solution>(
//                    100,
//                    solutionEvaluationStrategy,
//                    solutionCrossoverStrategy,
//                    solutionMutationStrategy,
//                    solutionGeneratorStrategy,
//                    new RouletteWheelSelectionStrategy<>());
//
//                GeneticAlgorithmRunner<Solution> runner = new GeneticAlgorithmRunner<>(algorithm, (level, message) -> System.out.println(message));
//                runner.runTestIteration(100);
//                System.out.println(runner.findBestIndividual().get());
//
//        }


    }
}
