package com.roomfurniture;

import com.google.common.collect.ImmutableList;
import com.roomfurniture.ga.GeneticAlgorithmRunner;
import com.roomfurniture.ga.algorithm.BasicGeneticAlgorithm;
import com.roomfurniture.ga.algorithm.RouletteWheelSelectionStrategy;
import com.roomfurniture.ga.algorithm.interfaces.*;
import com.roomfurniture.problem.Descriptor;
import com.roomfurniture.problem.Problem;
import com.roomfurniture.problem.Solution;
import com.roomfurniture.problem.Vertex;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        // write your code here

        InputParser inputParser = new InputParser();
        List<Problem> problems = inputParser.parse("test.txt");

        Problem p = problems.get(0);
           CrossoverStrategy<Solution> solutionCrossoverStrategy = (parentA, parentB) -> {
                //TODO(Kiran): Implement strategy
                List<Descriptor> parentADescriptors = parentA.getDescriptors();
                List<Descriptor> parentBDescriptors = parentB.getDescriptors();
                int crossoverPoint = ThreadLocalRandom.current().nextInt(0, parentADescriptors.size());

                List<Descriptor> childA = new ArrayList<>();
                List<Descriptor> childB = new ArrayList<>();

                for(int i = 0; i  < parentADescriptors.size(); i++) {
                        if(i < crossoverPoint) {
                            childA.add(parentADescriptors.get(i));
                            childB.add(parentBDescriptors.get(i));
                        } else {
                            childB.add(parentADescriptors.get(i));
                            childA.add(parentBDescriptors.get(i));
                        }
                }
                return ImmutableList.of(
                        new Solution(childA),
                        new Solution(childB)
                );
            };
            EvaluationStrategy<Solution> solutionEvaluationStrategy = individual -> {return p.score(individual).orElse(0.0);};
            MutationStrategy<Solution> solutionMutationStrategy = (MutationStrategy<Solution>) individual -> {
                //TODO(Kiran): Implement strategy
                if(ThreadLocalRandom.current().nextDouble()  < 0.5) {
                    List<Descriptor> descriptors = new ArrayList<>(individual.getDescriptors());
                    int mutationPoint = ThreadLocalRandom.current().nextInt(0, descriptors.size());

                    Descriptor Originaldescriptor = descriptors.get(mutationPoint);
                    Vertex newVertex = new Vertex(Originaldescriptor.getPosition().x + (ThreadLocalRandom.current().nextDouble() * 2 -1), Originaldescriptor.getPosition().y +  (ThreadLocalRandom.current().nextDouble() *2 -1));

                    Descriptor newDescriptor = new Descriptor(newVertex, Originaldescriptor.getRotation() + (ThreadLocalRandom.current().nextDouble() * 2 -1));

                    descriptors.set(mutationPoint, newDescriptor);

                    return new Solution(descriptors);
                }
                return new Solution(individual.getDescriptors());

            };
            GeneratorStrategy<Solution> solutionGeneratorStrategy = (GeneratorStrategy<Solution>) () -> {
                //TODO(Kiran): Implement strategy
                ArrayList<Descriptor> descriptors = new ArrayList<>();

                for(int i = 0; i < problems.get(0).getFurnitures().size(); i++) {
                    double rotation = ThreadLocalRandom.current().nextDouble() * 2 * Math.PI;
                    Vertex position = new Vertex(ThreadLocalRandom.current().nextDouble() * 10, ThreadLocalRandom.current().nextDouble() * 10);
                    Descriptor descriptor = new Descriptor(position, rotation);
                    descriptors.add(descriptor);
                }
                Solution newSolution = new Solution(descriptors);
                return newSolution;
            };

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
