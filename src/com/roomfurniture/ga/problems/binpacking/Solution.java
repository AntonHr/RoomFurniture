package com.roomfurniture.ga.problems.binpacking;

import java.util.List;

/**
 * Created by Gopiandcode on 11/12/2017.
 */
class Solution {
    private final List<Integer> permutation;

    Solution(List<Integer> permutation) {
        this.permutation = permutation;
    }

    public List<Integer> getPermutation() {
        return permutation;
    }
}
