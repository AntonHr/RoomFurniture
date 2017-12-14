package com.roomfurniture;

import com.google.common.collect.Lists;
import com.roomfurniture.ga.algorithm.RouletteWheelSelectionStrategy;
import com.roomfurniture.ga.algorithm.interfaces.CrossoverStrategy;
import com.roomfurniture.ga.algorithm.interfaces.EvaluationStrategy;
import com.roomfurniture.ga.algorithm.interfaces.GeneratorStrategy;
import com.roomfurniture.ga.algorithm.interfaces.MutationStrategy;
import com.roomfurniture.ga.algorithm.parallel.BasicParallelGeneticAlgorithm;
import com.roomfurniture.ga.algorithm.parallel.ParallelGeneticAlgorithmRunner;
import com.roomfurniture.nfp.MinkowskiNfpStrategy;
import com.roomfurniture.nfp.NfpCalculationStrategy;
import com.roomfurniture.nfp.OrbitingNfpStrategy;
import com.roomfurniture.problem.*;
import com.roomfurniture.solution.*;
import com.gui.SwingVisualizer;
import com.roomfurniture.solution.storage.SolutionDatabase;
import com.roomfurniture.solution.storage.SolutionList;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;


public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        List<Vertex> statVertices = new ArrayList<>(
                Arrays.asList(new Vertex(0, 0), new Vertex(1, 0),
                              new Vertex(1, 1), new Vertex(0, 1)));

        List<Double> angles = new EdgeAligner().computeShapeAngles(statVertices);
        System.out.println();

        /*
        List<Vertex> statVertices = new ArrayList<>(
          Arrays.asList(new Vertex(6, 0), new Vertex(8, 0), new Vertex(9, 1.73),
                        new Vertex(8, 3.46), new Vertex(6, 3.46), new Vertex(5, 1.73)));

        List<Vertex> orbVertices = new ArrayList<>(
                Arrays.asList(new Vertex(-12.06, 7.06), new Vertex(-8.93, 7.51), new Vertex(-9.82, 9.3)));

        Shape statShape = ShapeCalculator.constructPath(statVertices);
        Shape orbShape = ShapeCalculator.constructPath(orbVertices);

        NfpCalculationStrategy nfpCalculator = new OrbitingNfpStrategy();

        Shape nfpShape = nfpCalculator.calculateNfp(statShape, orbShape);

        List<Furniture> furnitures = new ArrayList<>();
        furnitures.add(new Furniture(0,500, ShapeCalculator.getVertices(nfpShape)));
        furnitures.add(new Furniture(0,0, statVertices));
        furnitures.add(new Furniture(0,100, orbVertices));

        List<Descriptor> descriptors = new ArrayList<>();
        descriptors.add(new Descriptor(new Vertex(0,0),0));
        descriptors.add(new Descriptor(new Vertex(-10,-10),0));
        descriptors.add(new Descriptor(new Vertex(10,10),0));

        List<Vertex> roomVertices = new ArrayList<>(
                Arrays.asList(new Vertex(0, -10), new Vertex(14, -10),
                              new Vertex(14, 10), new Vertex(0, 10)));

        Problem problem = new Problem(0, new Room(roomVertices), furnitures);
        Solution solution = new Solution(descriptors);

        JFrame bestFrame = SwingVisualizer.constructVisualizationFrame(problem, solution);
        bestFrame.setTitle("Best solution");
        EventQueue.invokeLater(() -> {
            bestFrame.setVisible(true);
        });
        */



        /*SolutionList.updatePermissions();
        InputParser inputParser = new InputParser();
        List<Problem> parse = inputParser.parse("problemsets.txt");
        try {
            FileWriter fileWriter = new FileWriter("./output.txt", false);
            fileWriter.write(SolutionDatabase.createTeamSolutionDatabase().generateOverallSolutionReportFor(parse));
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
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
