package com.roomfurniture.solution;

import com.roomfurniture.ga.algorithm.interfaces.GeneratorStrategy;
import com.roomfurniture.problem.Descriptor;
import com.roomfurniture.problem.Problem;
import com.roomfurniture.problem.Vertex;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class SolutionGeneratorStrategy implements GeneratorStrategy<Solution> {
    private final Problem p;

    public SolutionGeneratorStrategy(Problem p) {
        this.p = p;
    }

    @Override
    public Solution generate() {
        //TODO(Kiran): Implement strategy
        ArrayList<Descriptor> descriptors = new ArrayList<>();

        for (int i = 0; i < p.getFurnitures().size(); i++) {
            double rotation = ThreadLocalRandom.current().nextDouble() * 2 * Math.PI;
            Vertex position = new Vertex(ThreadLocalRandom.current().nextDouble() * 10, ThreadLocalRandom.current().nextDouble() * 10);
            Descriptor descriptor = new Descriptor(position, rotation);
            descriptors.add(descriptor);
        }
        Solution newSolution = new Solution(descriptors);
        return newSolution;
    }
}
