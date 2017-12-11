package com.roomfurniture.problem;

import java.util.List;

public class Room extends Polygon{

    public Room(List<Vertex> verticies) {
        super(verticies);
    }
    @Override
    public String toString() {
        return "Room{" +
                "verticies=" + verticies +
                '}';
    }
}
