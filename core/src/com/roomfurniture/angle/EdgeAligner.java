package com.roomfurniture.angle;

import com.roomfurniture.problem.Vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by asus on 14.12.2017 г..
 */
public class EdgeAligner {
    public class Edge {
        private Vertex a;
        private Vertex b;

        public Edge(Vertex a, Vertex b) {
            this.a = a;
            this.b = b;
        }

        public double angleTo(Edge secondEdge) {
            Vertex v1 = this.getVector();
            Vertex v2 = secondEdge.getVector();

            double l1 = this.getLength();
            double l2 = secondEdge.getLength();

            double dotProduct = v1.x * v2.x + v1.y * v2.y;
            double angle = dotProduct / this.getLength() * secondEdge.getLength();

            return angle;
        }

        public double getLength() {
            return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
        }

        public Vertex getVector() {
            return new Vertex(b.x - a.x, b.y - a.y);
        }

        public Vertex getA() {
            return a;
        }

        public Vertex getB() {
            return b;
        }
    }

    private List<Edge> verticesToEdges(List<Vertex> vertices) {
        List<Edge> edges = new ArrayList<>();

        for (int i = 0; i < vertices.size() - 1; i++) {
            edges.add(new Edge(vertices.get(i), vertices.get(i + 1)));
        }

        edges.add(new Edge(vertices.get(vertices.size() - 1), vertices.get(0)));

        return edges;
    }

    public List<Double> computeShapeAngles(List<Vertex> vertices) {
        List<Edge> edges = verticesToEdges(vertices);
        List<Double> angles = new ArrayList<>();

        for (int i = 0; i < edges.size() - 1; i++) {
            double angle = (edges.get(i)).angleTo(edges.get(i + 1));
            angles.add(angle);
        }

        angles.add((edges.get(edges.size() - 1)).angleTo(edges.get(0)));

        return angles;
    }
}
