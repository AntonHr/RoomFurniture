package com.roomfurniture.solution;

import com.roomfurniture.ga.algorithm.interfaces.GeneratorStrategy;
import com.roomfurniture.problem.BasicProblem;
import com.roomfurniture.problem.Descriptor;
import com.roomfurniture.problem.Problem;
import com.roomfurniture.problem.Vertex;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ThreadLocalRandom;

public class SolutionGeneratorStrategy implements GeneratorStrategy<Solution> {
    private final Problem p;
    private final double maxY;
    private final double minY;
    private final double minX;
    private final double maxX;

    public SolutionGeneratorStrategy(Problem p) {
        this.p = p;

        maxX = p.getRoom().getVerticies().stream().max(Comparator.comparingDouble(o -> o.x)).get().x;
        minX = p.getRoom().getVerticies().stream().min(Comparator.comparingDouble(o -> o.x)).get().x;
        minY = p.getRoom().getVerticies().stream().min(Comparator.comparingDouble(o -> o.x)).get().x;
        maxY = p.getRoom().getVerticies().stream().max(Comparator.comparingDouble(o -> o.x)).get().x;
    }

    @Override
    public Solution generate() {
        //TODO(Kiran): Implement strategy
        ArrayList<Descriptor> descriptors = new ArrayList<>();
        Rectangle2D bounds2D = p.getRoom().toShape().getBounds2D();
        double xRange = maxX - minX;
        double yRange = maxY - minY;


        for (int i = 0; i < p.getFurnitures().size(); i++) {
            double rotation = ThreadLocalRandom.current().nextDouble() * 4 * Math.PI;
            Vertex position = new Vertex(ThreadLocalRandom.current().nextDouble() * xRange + minX, (ThreadLocalRandom.current().nextDouble() * yRange + minY) * -1);
            Descriptor descriptor = new Descriptor(position, rotation);
            descriptors.add(descriptor);
        }
        Solution newSolution = new Solution(descriptors);
        return newSolution;
    }
}
