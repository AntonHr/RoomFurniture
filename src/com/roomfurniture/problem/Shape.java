package com.roomfurniture.problem;

import java.util.List;

public class Shape extends Polygon{
    private final double scorePerUnitArea;

    public Shape(double scorePerUnitArea, List<Vertex> vertices) {
        super(vertices);
        this.scorePerUnitArea = scorePerUnitArea;
    }

    @Override
    public String toString() {
        return "Shape{" +
                "scorePerUnitArea=" + scorePerUnitArea + ", vertices=" + verticies + '}';
    }
}
