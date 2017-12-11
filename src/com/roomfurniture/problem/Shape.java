package com.roomfurniture.problem;

import java.util.List;

public class Shape {
    private final double scorePerUnitArea;
    private final List<Vertex> vertices;

    public Shape(double scorePerUnitArea, List<Vertex> vertices) {
        this.scorePerUnitArea = scorePerUnitArea;
        this.vertices = vertices;
    }

    @Override
    public String toString() {
        return "Shape{" +
                "scorePerUnitArea=" + scorePerUnitArea + ", vertices=" + vertices + '}';
    }
}
