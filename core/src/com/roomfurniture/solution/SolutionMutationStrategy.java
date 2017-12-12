package com.roomfurniture.solution;

import com.roomfurniture.ga.algorithm.interfaces.MutationStrategy;
import com.roomfurniture.problem.Descriptor;
import com.roomfurniture.problem.Problem;
import com.roomfurniture.problem.Vertex;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SolutionMutationStrategy implements MutationStrategy<Solution> {
    private Problem problem;

    public SolutionMutationStrategy(Problem problem) {
        this.problem = problem;
    }

    @Override
    public Solution mutate(Solution individual) {
        //TODO(Kiran): Implement strategy
        if (ThreadLocalRandom.current().nextDouble() < 0.5) {
            List<Descriptor> descriptors = new ArrayList<>(individual.getDescriptors());
            int mutationPoint = ThreadLocalRandom.current().nextInt(0, descriptors.size());

            Descriptor Originaldescriptor = descriptors.get(mutationPoint);
            Vertex position = Originaldescriptor.getPosition();
            Rectangle2D bounds2D = problem.getRoom().toShape().getBounds2D();
            double minX = bounds2D.getMinX();
            double minY = bounds2D.getMinY();
            double maxX = bounds2D.getMaxX();
            double maxY = bounds2D.getMaxY();

            double dx = ThreadLocalRandom.current().nextDouble() * 2 - 1;
            double dy = ThreadLocalRandom.current().nextDouble() * 2 - 1;

            Vertex newVertex = new Vertex(clamp(minX,maxX,position.x + dx), clamp(minY, maxY,position.y + dy));

            Descriptor newDescriptor = new Descriptor(newVertex, Originaldescriptor.getRotation() + (ThreadLocalRandom.current().nextDouble() * 2 - 1));

            descriptors.set(mutationPoint, newDescriptor);

            return new Solution(descriptors);
        }
        return new Solution(individual.getDescriptors());

    }

    private double clamp(double min, double max, double value) {
        return Math.max(Math.min(value, max), min);
    }
}
