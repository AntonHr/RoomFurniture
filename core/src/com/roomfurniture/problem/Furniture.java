package com.roomfurniture.problem;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.List;

public class Furniture {
    private final double scorePerUnitArea;
    private Shape shape;
    private int id;

    public Furniture(int id, double scorePerUnitArea, List<Vertex> vertices) {
        this.id = id;
        this.scorePerUnitArea = scorePerUnitArea;
        this.shape = constructPath(vertices);
    }

    public void updateShape(List<Vertex> vertices) {
        shape = constructPath(vertices);
    }

    private Path2D.Double constructPath(List<Vertex> vertices) {
        Path2D.Double path = new Path2D.Double();
        Vertex vertex = vertices.get(0);
        path.moveTo(vertex.x, vertex.y);
        for (int i = 1; i < vertices.size(); i++) {
            path.lineTo(vertices.get(i).x, vertices.get(i).y);
        }
        path.closePath();
        return path;
    }

    private Furniture(int id, double scorePerUnitArea, Shape polygon) {
        this.id = id;
        this.scorePerUnitArea = scorePerUnitArea;
        this.shape = polygon;
    }

    @Override
    public String toString() {
        return "Furniture{" +
                "scorePerUnitArea=" + scorePerUnitArea + ", vertices=" + getVertices() + '}';
    }

    public double getScorePerUnitArea() {
        return scorePerUnitArea;
    }

    public Shape toShape() {
        return shape;
    }

    public Furniture transform(Descriptor descriptor) {
        Shape shape = AffineTransform.getRotateInstance(descriptor.getRotation()).createTransformedShape(this.shape);
        shape = AffineTransform.getTranslateInstance(descriptor.getPosition().x, descriptor.getPosition().y).createTransformedShape(shape);
        return new Furniture(id, this.scorePerUnitArea, shape);
    }


    public List<Vertex> getVertices() {
        PathIterator pathIterator = shape.getPathIterator(null);
        List<Vertex> vertices = new ArrayList<>();

        while (!pathIterator.isDone()) {
            double[] kilme = new double[2];
            pathIterator.currentSegment(kilme);
            pathIterator.next();
            vertices.add(new Vertex(kilme[0], kilme[1]));
        }

        vertices.remove(vertices.size() - 1);
        return vertices;
    }

    @Override
    public boolean equals(Object obj) {
        return this.id == ((Furniture) obj).id;
    }
}
