package com.roomfurniture.placing;


import com.google.common.collect.ImmutableList;
import com.roomfurniture.problem.Furniture;
import com.roomfurniture.problem.Problem;
import com.roomfurniture.problem.Room;
import com.roomfurniture.problem.Vertex;

import java.util.List;

public class BasicPlacingProblem implements PlacingProblem {
    private final Room room;
    private final List<Furniture> furnitures;
    private final List<Vertex> spawnPoints;
    private final Problem problem;

    public BasicPlacingProblem(Problem problem, List<Vertex> spawnPoints) {
        this.problem = problem;
        this.room = problem.getRoom();
        this.furnitures = ImmutableList.copyOf(problem.getFurnitures());
        this.spawnPoints = ImmutableList.copyOf(spawnPoints);
    }


    @Override
    public String toString() {
        return "PlacingProblem{" +
                "room=" + room +
                ", furnitures=" + furnitures +
                ", spawnPoints=" + spawnPoints +
                '}';
    }

    @Override
    public Room getRoom() {
        return room;
    }

    @Override
    public List<Furniture> getFurnitures() {
        return furnitures;
    }

    @Override
    public List<Vertex> getSpawnPoints() {
        return spawnPoints;
    }

    public Problem getProblem() {
        return problem;
    }
}
