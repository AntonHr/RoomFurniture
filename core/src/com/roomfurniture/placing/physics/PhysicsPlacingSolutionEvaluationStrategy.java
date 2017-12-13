package com.roomfurniture.placing.physics;

import com.roomfurniture.ga.algorithm.interfaces.EvaluationStrategy;
import com.roomfurniture.placing.PlacingProblem;
import com.roomfurniture.placing.PlacingSolution;

public class PhysicsPlacingSolutionEvaluationStrategy implements EvaluationStrategy<PlacingSolution> {
    private final PlacingProblem problem;

    public PhysicsPlacingSolutionEvaluationStrategy(PlacingProblem problem) {
        this.problem = problem;
    }

    @Override
    public double evaluate(PlacingSolution individual) {
      // TODO(Kiran): score solution based on outcome - how many you can place

      // TODO(Kiran): add function to convert a placingsolution into a solution

        return 0;
    }
}
