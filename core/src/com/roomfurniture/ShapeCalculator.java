package com.roomfurniture;

import com.gui.RoomFurnitureRenderer;
import com.badlogic.gdx.math.GeometryUtils;
import com.badlogic.gdx.math.Polygon;

import java.awt.*;
import java.awt.geom.Area;

public class ShapeCalculator {

    public static double calculateAreaOf(Shape shape) {
        float[] points = RoomFurnitureRenderer.getPoints(shape);
        GeometryUtils.ensureCCW(points);
        Polygon polygon = new Polygon(points);
        return polygon.area();
    }

    public static boolean intersect(Shape shapeA, Shape shapeB) {
        Area polygonA = new Area(shapeA);
        Area polygonB = new Area(shapeB);

        polygonA.intersect(polygonB);
        return !polygonA.isEmpty();
    }

    public static boolean contains(Shape shapeA, Shape shapeB) {
        Area polygonA = new Area(shapeA);
        Area polygonB = new Area(shapeB);
        polygonB.subtract(polygonA);
        return polygonB.isEmpty();
    }
}
