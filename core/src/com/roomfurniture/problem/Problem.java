package com.roomfurniture.problem;

import com.google.common.collect.Streams;
import com.roomfurniture.ShapeCalculator;
import com.roomfurniture.solution.Solution;

import java.awt.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public interface Problem {
    Room getRoom();

    List<Furniture> getFurnitures();

    int getNumber();

    int findMeInInitialArray(Furniture furniture);

    Optional<Double> score(Solution solution);
}
