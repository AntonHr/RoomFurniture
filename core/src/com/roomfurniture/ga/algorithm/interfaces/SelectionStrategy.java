package com.roomfurniture.ga.algorithm.interfaces;

import com.roomfurniture.ga.algorithm.Result;

import java.util.List;

public interface SelectionStrategy<T> {
    public Result<T> selectWithReplacement(List<Result<T>> population);
}
