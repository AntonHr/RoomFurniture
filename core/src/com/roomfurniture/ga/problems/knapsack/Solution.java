package com.roomfurniture.ga.problems.knapsack;

import java.util.ArrayList;

public class Solution {

    private final ArrayList<Integer> solutionItemCounts;


    public Solution(ArrayList<Integer> count) {
        this.solutionItemCounts = count;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Solution{" +
                "solutionItemCounts=" + solutionItemCounts +
                '}');
        for (Integer i : solutionItemCounts) {
            builder.append(i + ",");
        }
        return builder.toString();
    }

    public ArrayList<Integer> getSolutionItemCounts() {
        return new ArrayList<>(solutionItemCounts);
    }

}
