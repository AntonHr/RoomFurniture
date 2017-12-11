package com.roomfurniture.problem;

import java.util.List;

public class Room {
    private final List<Vertex> verticies;

    public Room(List<Vertex> verticies) {
        this.verticies = verticies;
    }

    @Override
    public String toString() {
        return "Room{" +
                "verticies=" + verticies +
                '}';
    }
}
