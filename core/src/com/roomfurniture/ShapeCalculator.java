package com.roomfurniture;

import com.gui.RoomFurnitureRenderer;
import com.badlogic.gdx.math.GeometryUtils;
import com.badlogic.gdx.math.Polygon;
import com.roomfurniture.problem.Vertex;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
//        return contains(getVertices(shapeA), getVertices(shapeB));
    }

    public static boolean containsPoint(List<Vertex> polygon, Vertex point) {
        int i, j = 0;
        boolean c = false;
//  int i, j, c = 0;
        for (i = 0, j = polygon.size() - 1; i < polygon.size(); j = i++) {
//  for (i = 0, j = nvert-1; i < nvert; j = i++) {
            if ((polygon.get(i).y > point.y) != (polygon.get(j).y > point.y) &&
//    if ( ((verty[i]>testy) != (verty[j]>testy)) &&
                    point.x < (polygon.get(j).x - polygon.get(i).x) * (point.y - polygon.get(i).y) / (polygon.get(j).y - polygon.get(i).y) + polygon.get(i).x) {
//     (testx < (vertx[j]-vertx[i]) * (testy-verty[i]) / (verty[j]-verty[i]) + vertx[i]) )
                c = !c;
//       c = !c;
            }
        }
        return c;
//  return c;
    }

    public static boolean contains(List<Vertex> polygonLarger, List<Vertex> polygonSmaller) {
        for (Vertex vertex : polygonSmaller) {
            if (!containsPoint(polygonLarger, vertex)) return false;
        }
        for (Vertex vertex : polygonLarger) {
            if (containsPoint(polygonSmaller, vertex)) return false;
        }
        return true;
    }

    public static List<Vertex> getVertices(Shape shape) {
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

    public static boolean isConvex(Shape shape) {
        List<Vertex> vertices = getVertices(shape);
        if (vertices.size() < 4)
            return true;
        boolean sign = false;
        int n = vertices.size();
        for (int i = 0; i < n; i++) {
            double dx1 = vertices.get((i + 2) % n).x - vertices.get((i + 1) % n).x;
            double dy1 = vertices.get((i + 2) % n).y - vertices.get((i + 1) % n).y;
            double dx2 = vertices.get(i).x - vertices.get((i + 1) % n).x;
            double dy2 = vertices.get(i).y - vertices.get((i + 1) % n).y;
            double zcrossproduct = dx1 * dy2 - dy1 * dx2;
            if (i == 0)
                sign = zcrossproduct > 0;
            else if (sign != (zcrossproduct > 0))
                return false;
        }
        return true;
    }

//    public static List<Vertex> simplifyShape(List<Vertex> points) {
//        if (points.size() < 3)
//            throw new RuntimeException("Attempted to simplify a line.");
//        else if (points.size() == 3)
//            return new ArrayList<>(points);
//
//        List<Vertex> result = new ArrayList<>();
//        for (int i = -1; i < points.size() - 1; i++) {
//            Vertex a = points.get((i + points.size()) % points.size());
//            Vertex b = points.get((i + 1 + points.size()) % points.size());
//            Vertex c = points.get((i + 2 + points.size()) % points.size());
//
//            if (!Vertex.isCollinear(a, b, c)) {
//                result.add(b);
//            } else {
//                System.out.println("Are colinear: " + a + ", " + b + ", " + c);
//            }
//        }
//
//        if (result.size() != points.size()) {
//            System.out.println(result + " != " + points);
//        }
//        return result;
//    }

    public static List<Vertex> simplifyShape(List<Vertex> points) {
        List<Vertex> original = new ArrayList<>(points);
        original.add(points.get(0));

        ArrayList<Vertex> result = new ArrayList();
        Optional<Vertex> previousDistinctCoordinate = Optional.empty();
        for (int i = 0; i <= original.size() - 2; i++) {
            Vertex currentCoordinate = original.get(i);
            Vertex nextCoordinate = original.get(i + 1);
            if (currentCoordinate.equals(nextCoordinate)) {
                continue;
            }
            if (previousDistinctCoordinate.isPresent()
                    && Vertex.isCollinear(previousDistinctCoordinate.get(), currentCoordinate, nextCoordinate)) {
                continue;
            }
            result.add(currentCoordinate);
            previousDistinctCoordinate = Optional.of(currentCoordinate);
        }
//        result.add(original.get(original.size() - 1));
         if (result.size() != points.size()) {
            System.out.println(result + " != " + points);
        }

        return result;
    }

}
