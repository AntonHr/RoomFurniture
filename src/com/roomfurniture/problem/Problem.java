package com.roomfurniture.problem;

import java.util.List;

public class Problem {
    private final Room room;
    private final List<Shape> shapes;

    public Problem(Room room, List<Shape> shapes) {
        this.room = room;
        this.shapes = shapes;
    }

    @Override
    public String toString() {
        return "Problem{" +
                "room=" + room +
                ", shapes=" + shapes +
                '}';
    }
}
