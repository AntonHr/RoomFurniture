package com.roomfurniture.ga.algorithm;

import com.roomfurniture.ga.algorithm.interfaces.SelectionStrategy;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class RouletteWheelSelectionStrategy<T> implements SelectionStrategy<T> {
    @Override
    public Result<T> selectWithReplacement(List<Result<T>> population) {
        double fitnessSum = 0.0;
        for(Result<T> member : population) {
            if(member.getFitness().isPresent())
            fitnessSum += member.getFitness().get();
        }

        population.sort(Comparator.comparing(o -> o.getFitness().orElse(0.0)));

        Double crossoverPoint = ThreadLocalRandom.current().nextDouble(0, fitnessSum + 1);

        int index = 0;
        fitnessSum = 0.0;

        while(fitnessSum < crossoverPoint && index < population.size()-1) {
            fitnessSum += population.get(index).getFitness().orElse(0.0);
            index += 1;
        }

        return population.get(index);
    }
}
