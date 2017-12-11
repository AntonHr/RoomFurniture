package com.roomfurniture.problem;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        // write your code here

//        GeneticAlgorithm<Bitstring> algorithm = new BitstringGeneticAlgorithmBuilder().withInputSize(8).withEvaluator(individual -> 1.0).build();
//
//        GeneticAlgorithmRunner<Bitstring> runner = new GeneticAlgorithmRunner<>(algorithm, (level, message) -> System.out.println(message));
//        runner.runTestIteration(100);
//        System.out.println(runner.findBestIndividual().get());

        InputParser inputParser = new InputParser();
        System.out.println(inputParser.parse("test.txt"));


    }
}
