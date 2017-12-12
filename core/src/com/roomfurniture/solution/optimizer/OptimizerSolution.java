package com.roomfurniture.solution.optimizer;

import com.roomfurniture.problem.Descriptor;
import com.roomfurniture.solution.Solution;

import java.util.List;
import java.util.Optional;

public class OptimizerSolution extends Solution {
    public OptimizerSolution(List<Descriptor> descriptors) {
        super(descriptors);
    }



    public Optional<Double> score(OptimizerProblem problem) {
        return Optional.empty();
    }
}
