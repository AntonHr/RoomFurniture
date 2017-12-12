package com.roomfurniture;

import com.awesome.scenario.RoomFurnitureMain;
import com.badlogic.gdx.math.GeometryUtils;
import com.badlogic.gdx.math.Polygon;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.Arrays;

public class ShapeCalculator {

    public static double calculateAreaOf(Shape shape) {
        float[] points = RoomFurnitureMain.getPoints(shape);
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
