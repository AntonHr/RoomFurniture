package com.roomfurniture.solution.optimizer;

import com.roomfurniture.problem.Room;
import com.roomfurniture.solution.Solution;

import java.util.Optional;

/**
 * Created by Gopiandcode on 14/12/2017.
 */
public interface OptimizerProblem {
    Solution getOptimizedSolution(Solution solution);

    Optional<Double> score(Solution individual);

    int getSize();

    Room getRoom();
}
