package com.roomfurniture.solution.optimizer;

import com.roomfurniture.ga.algorithm.interfaces.EvaluationStrategy;
import com.roomfurniture.solution.Solution;

public class OptimizerProblemEvaluationStrategy implements EvaluationStrategy<Solution> {
    private final OptimizerProblem problem;

    public OptimizerProblemEvaluationStrategy(OptimizerProblem problem) {
        this.problem = problem;
    }

    @Override
    public double evaluate(Solution individual) {
        return problem.score(individual).orElse(0.0);
    }
}
