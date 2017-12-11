package com.roomfurniture.problem;

import com.roomfurniture.ga.GeneticAlgorithmRunner;
import com.roomfurniture.ga.bitstring.Bitstring;
import com.roomfurniture.ga.bitstring.BitstringGeneticAlgorithmBuilder;
import com.roomfurniture.ga.algorithm.interfaces.GeneticAlgorithm;

public class Main {

    public static void main(String[] args) {
	// write your code here

//        GeneticAlgorithm<Bitstring> algorithm = new BitstringGeneticAlgorithmBuilder().withInputSize(8).withEvaluator(individual -> 1.0).build();
//
//        GeneticAlgorithmRunner<Bitstring> runner = new GeneticAlgorithmRunner<>(algorithm, (level, message) -> System.out.println(message));
//        runner.runTestIteration(100);
//        System.out.println(runner.findBestIndividual().get());

        InputParser inputParser = new InputParser();
        inputParser.parse("problemsets.txt");
    }
}
