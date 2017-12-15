package com.roomfurniture.angle;

import com.roomfurniture.problem.Vertex;

import java.util.*;

/**
 * Created by asus on 14.12.2017 г..
 */
public class Angle {
    private Edge a;
    private Edge b;

    private Vertex topVertex;
    private List<Vertex> bottomVertices;

    public Angle(Edge a, Edge b) {
        this.a = a;
        this.b = b;
        this.topVertex = findTopVertex();
        this.bottomVertices = findBottomVertices();
    }

    public Set<Vertex> getVertices() {
        Set<Vertex> vertices = new HashSet<Vertex>();
        vertices.add(a.getBeginVertex());
        vertices.add(a.getEndVertex());
        vertices.add(b.getBeginVertex());
        vertices.add(b.getEndVertex());

        return vertices;
    }

    private Vertex findTopVertex() {
        List<Vertex> vertices = new ArrayList<>();
        vertices.add(a.getBeginVertex());
        vertices.add(a.getEndVertex());
        vertices.add(b.getBeginVertex());
        vertices.add(b.getEndVertex());

        for (Vertex k: vertices) {
            for (Vertex p: vertices) {
                if (k == p) return k;
            }
        }

        throw new RuntimeException("Non touching edges in Angle.");
    }

    public List<Vertex> findBottomVertices() {
        Set<Vertex> vertices = new HashSet<Vertex>();
        vertices.add(a.getBeginVertex());
        vertices.add(a.getEndVertex());
        vertices.add(b.getBeginVertex());
        vertices.add(b.getEndVertex());

        vertices.remove(getTopVertex());

        return new ArrayList<Vertex>(vertices);
    }

    public Vertex getTopVertex() {
        return topVertex;
    }

    public List<Vertex> getBottomVertices() {
        return bottomVertices;
    }

    public Double getAngleValue() {
        return a.angleTo(b);
    }

    public Edge getA() {
        return a;
    }

    public Edge getB() {
        return b;
    }
}
