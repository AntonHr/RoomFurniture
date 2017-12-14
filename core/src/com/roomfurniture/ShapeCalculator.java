package com.roomfurniture;

import com.gui.RoomFurnitureRenderer;
import com.badlogic.gdx.math.GeometryUtils;
import com.badlogic.gdx.math.Polygon;
import com.roomfurniture.problem.Vertex;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.List;

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
        for(i = 0, j = polygon.size()-1; i < polygon.size(); j = i++) {
//  for (i = 0, j = nvert-1; i < nvert; j = i++) {
            if((polygon.get(i).y > point.y) != (polygon.get(j).y > point.y) &&
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
        for(Vertex vertex : polygonSmaller){
            if(!containsPoint(polygonLarger, vertex)) return false;
        }
        for(Vertex vertex : polygonLarger) {
            if(containsPoint(polygonSmaller, vertex)) return false;
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

    public static Path2D.Double constructPath(List<Vertex> vertices) {
        Path2D.Double path = new Path2D.Double();
        Vertex vertex = vertices.get(0);
        path.moveTo(vertex.x, vertex.y);
        for (int i = 1; i < vertices.size(); i++) {
            path.lineTo(vertices.get(i).x, vertices.get(i).y);
        }
        path.closePath();
        return path;
    }

    public static Vertex findBottomVertex(Shape shape) {
        List<Vertex> vertices = ShapeCalculator.getVertices(shape);
        Vertex bottomVertex = vertices.get(0);

        for (Vertex v : vertices) {
            if (v.y < bottomVertex.y) {
                bottomVertex = v;
            } else if (v.y == bottomVertex.y) {
                if (v.x < bottomVertex.x) {
                    v = bottomVertex;
                }
            }
        }

        return bottomVertex;
    }
}
