package com.roomfurniture.ga.algorithm;

import com.roomfurniture.ga.algorithm.interfaces.SelectionStrategy;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class RouletteWheelSelectionStrategy<T> implements SelectionStrategy<T> {
    @Override
    public T selectWithReplacement(List<T> population, Map<T, Double> scores) {
        Double fitnessSum = 0.0;
        for(T member : population) {
            fitnessSum += scores.get(member);
        }

        population.sort(Comparator.comparing(scores::get));

        Double crossoverPoint = ThreadLocalRandom.current().nextDouble(0, fitnessSum + 1);

        int index = 0;
        fitnessSum = 0.0;

        while(fitnessSum < crossoverPoint && index < population.size()-1) {
            fitnessSum += scores.get(population.get(index));
            index += 1;
        }

        return population.get(index);
    }
}
