package com.roomfurniture.problem;

import com.roomfurniture.ShapeCalculator;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;

public class Furniture {
    private final double scorePerUnitArea;
    private Shape shape;
    private int id;

    public Furniture(int id, double scorePerUnitArea, List<Vertex> vertices) {
        this.id = id;
        this.scorePerUnitArea = scorePerUnitArea;
        this.shape = ShapeCalculator.constructPath(vertices);
    }

    public void updateShape(List<Vertex> vertices) {
        shape = ShapeCalculator.constructPath(vertices);
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
        return ShapeCalculator.getVertices(shape);
    }

    @Override
    public boolean equals(Object obj) {
        return this.id == ((Furniture) obj).id;
    }
}
