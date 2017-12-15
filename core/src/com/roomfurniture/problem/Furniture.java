package com.roomfurniture.problem;

import com.badlogic.gdx.graphics.Color;
import com.roomfurniture.ShapeCalculator;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.List;

public class Furniture {
    private double scorePerUnitArea;
    private Shape shape;
    private int id;
    private Color color;

    public Furniture(int id, double scorePerUnitArea, List<Vertex> vertices) {
        this.id = id;
        this.scorePerUnitArea = scorePerUnitArea;
        this.shape = constructPath(vertices);
    }

    public Furniture(Furniture item) {
        shape = item.toShape();
        id = item.id;
        scorePerUnitArea = item.scorePerUnitArea;
    }

    public void updateShape(List<Vertex> vertices) {
        shape = constructPath(vertices);
    }

    private Shape constructPath(List<Vertex> vertices) {
        Path2D.Double path = new Path2D.Double();
        Vertex vertex = vertices.get(0);
        path.moveTo(vertex.x, vertex.y);
        for (int i = 1; i < vertices.size(); i++) {
            path.lineTo(vertices.get(i).x, vertices.get(i).y);
        }
        path.closePath();
        return path;
    }

    public Furniture(int id, double scorePerUnitArea, Shape polygon) {
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

    public double getScore() {
        return scorePerUnitArea * ShapeCalculator.calculateAreaOf(shape);
    }

    public List<Vertex> getVertices() {
        return ShapeCalculator.getVertices(shape);
    }

    @Override
    public boolean equals(Object obj) {
        return this.id == ((Furniture) obj).id;
    }

    public int findMeInInitialArray(List<Furniture> initialItemArray) {
        int ind = initialItemArray.indexOf(this);
        if (ind == -1)
            throw new RuntimeException("ooopos");
        return ind;
    }

    public void transformWithMutation(Descriptor descriptor) {
        shape = transform(descriptor).toShape();
    }

    public void set(Furniture current) {
        id = current.id;
        shape = current.toShape();
        scorePerUnitArea = current.scorePerUnitArea;
    }

    public void setColor(Color color) {

        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
