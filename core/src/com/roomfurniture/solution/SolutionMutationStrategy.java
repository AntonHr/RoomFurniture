package com.roomfurniture.solution;

import com.roomfurniture.ga.algorithm.interfaces.MutationStrategy;
import com.roomfurniture.problem.Descriptor;
import com.roomfurniture.problem.Vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SolutionMutationStrategy implements MutationStrategy<Solution> {
    @Override
    public Solution mutate(Solution individual) {
        //TODO(Kiran): Implement strategy
        if (ThreadLocalRandom.current().nextDouble() < 0.5) {
            List<Descriptor> descriptors = new ArrayList<>(individual.getDescriptors());
            int mutationPoint = ThreadLocalRandom.current().nextInt(0, descriptors.size());

            Descriptor Originaldescriptor = descriptors.get(mutationPoint);
            Vertex newVertex = new Vertex(Originaldescriptor.getPosition().x + (ThreadLocalRandom.current().nextDouble() * 2 - 1), Originaldescriptor.getPosition().y + (ThreadLocalRandom.current().nextDouble() * 2 - 1));

            Descriptor newDescriptor = new Descriptor(newVertex, Originaldescriptor.getRotation() + (ThreadLocalRandom.current().nextDouble() * 2 - 1));

            descriptors.set(mutationPoint, newDescriptor);

            return new Solution(descriptors);
        }
        return new Solution(individual.getDescriptors());

    }
}
