package com.roomfurniture.angle;

import com.roomfurniture.ShapeCalculator;
import com.roomfurniture.problem.Descriptor;
import com.roomfurniture.problem.Vertex;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EdgeAligner {

    private static List<Edge> verticesToEdges(List<Vertex> vertices) {
        List<Edge> edges = new ArrayList<>();

        for (int i = 0; i < vertices.size() - 1; i++) {
            edges.add(new Edge(vertices.get(i), vertices.get(i + 1)));
        }

        edges.add(new Edge(vertices.get(vertices.size() - 1), vertices.get(0)));

        return edges;
    }


    public static List<Angle> computeShapeAngles(List<Vertex> vertices) {
        List<Edge> edges = verticesToEdges(vertices);
        List<Angle> angles = new ArrayList<>();

        for (int i = 0; i < edges.size() - 1; i++) {
            angles.add(new Angle(edges.get(i), edges.get(i + 1)));
        }

        angles.add(new Angle(edges.get(edges.size() - 1), edges.get(0)));

        return angles;
    }

    private Vertex findCentroidBetweenEdges(Edge a, Edge b) {
        Set<Vertex> verticesSet = new HashSet<Vertex>();
        verticesSet.add(a.getBeginVertex());
        verticesSet.add(a.getEndVertex());
        verticesSet.add(b.getBeginVertex());
        verticesSet.add(b.getEndVertex());

        Vertex centroid = new Vertex(0 ,0);
        for (Vertex v : verticesSet) {
            centroid.x += v.x;
            centroid.y += v.y;
        }

        centroid.x /= 3;
        centroid.y /= 3;

        return centroid;
    }

    public Descriptor alignTwoAngles(Angle stationary, Angle moving) {
        double rotationAngle = VertexExtensionUtility.getAngle(stationary.getB().getVector(), moving.getB().getVector());

        double xTrans = stationary.getTopVertex().x - moving.getTopVertex().x;
        double yTrans = stationary.getTopVertex().y - moving.getTopVertex().y;

        return new Descriptor(new Vertex(xTrans, yTrans), rotationAngle);
    }

    public Descriptor alignAngleSets(List<Angle> stationary, List<Angle> moving) {
        return null;
    }
}
