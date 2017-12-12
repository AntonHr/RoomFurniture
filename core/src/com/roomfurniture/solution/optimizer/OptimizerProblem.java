package com.roomfurniture.solution.optimizer;


import com.roomfurniture.problem.Furniture;

import java.util.List;

public class OptimizerProblem {
    private final List<Furniture> placedItems;
    private final List<Furniture> remainingItems;

    public OptimizerProblem(List<Furniture> placedItems, List<Furniture> remainingItems) {
        this.placedItems = placedItems;
        this.remainingItems = remainingItems;
    }

    public List<Furniture> getPlacedItems() {
        return placedItems;
    }

    public List<Furniture> getRemainingItems() {
        return remainingItems;
    }

    @Override
    public String toString() {
        return "OptimizerProblem{" +
                "placedItems=" + placedItems +
                ", remainingItems=" + remainingItems +
                '}';
    }
}
