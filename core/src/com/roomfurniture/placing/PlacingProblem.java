package com.roomfurniture.placing;


import com.google.common.collect.ImmutableList;
import com.roomfurniture.problem.Furniture;
import com.roomfurniture.problem.Problem;
import com.roomfurniture.problem.Room;
import com.roomfurniture.problem.Vertex;

import java.util.List;

public class PlacingProblem {
    private final Room room;
    private final List<Furniture> furnitures;
    private final List<Vertex> spawnPoints;

    public PlacingProblem(Problem problem, List<Vertex> spawnPoints) {
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

    public Room getRoom() {
        return room;
    }

    public List<Furniture> getFurnitures() {
        return furnitures;
    }

    public List<Vertex> getSpawnPoints() {
        return spawnPoints;
    }
}
