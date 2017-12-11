package com.roomfurniture.solution;

import com.roomfurniture.ga.algorithm.interfaces.EvaluationStrategy;
import com.roomfurniture.problem.Problem;

public class SolutionEvaluationStrategy implements EvaluationStrategy<Solution> {
    private final Problem p;

    public SolutionEvaluationStrategy(Problem p) {
        this.p = p;
    }

    @Override
    public double evaluate(Solution individual) {
        return individual.score(p).orElse(0.0);
    }
}
