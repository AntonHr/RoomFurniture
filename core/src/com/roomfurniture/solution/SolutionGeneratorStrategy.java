package com.roomfurniture.solution;

import com.roomfurniture.ga.algorithm.interfaces.GeneratorStrategy;
import com.roomfurniture.problem.Descriptor;
import com.roomfurniture.problem.Problem;
import com.roomfurniture.problem.Vertex;

import java.awt.geom.Rectangle2D;
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
        Rectangle2D bounds2D = p.room.toShape().getBounds2D();
        double minX = bounds2D.getMinX();
        double maxX = bounds2D.getMaxX();
        double minY = bounds2D.getMinY();
        double maxY = bounds2D.getMaxX();
        double xRange = maxX - minX;
        double yRange = maxY - minY;

        for (int i = 0; i < p.getFurnitures().size(); i++) {
            double rotation = ThreadLocalRandom.current().nextDouble() * 4 * Math.PI;
            Vertex position = new Vertex(ThreadLocalRandom.current().nextDouble() * xRange + minX, ThreadLocalRandom.current().nextDouble() * yRange + minY);
            Descriptor descriptor = new Descriptor(position, rotation);
            descriptors.add(descriptor);
        }
        Solution newSolution = new Solution(descriptors);
        return newSolution;
    }
}
