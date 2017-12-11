package com.roomfurniture.problem;

import java.awt.*;
import java.awt.geom.Path2D;
import java.util.List;

public class Furniture {
    private final double scorePerUnitArea;
    private List<Vertex> vertices;
    private Path2D.Double polygon;

    public Furniture(double scorePerUnitArea, List<Vertex> vertices) {
        this.scorePerUnitArea = scorePerUnitArea;
        this.vertices = vertices;

        polygon = new Path2D.Double();
        Vertex vertex = vertices.get(0);
        polygon.moveTo(vertex.x, vertex.y);
        for(int i = 1; i < vertices.size(); i++) {
            polygon.lineTo(vertices.get(i).x, vertices.get(i).y);
        }
        polygon.closePath();
    }

    @Override
    public String toString() {
        return "Furniture{" +
                "scorePerUnitArea=" + scorePerUnitArea + ", vertices=" + this.vertices + '}';
    }

    public double getScorePerUnitArea() {
        return scorePerUnitArea;
    }

    public Shape toShape() {
        return polygon;
    }
}
