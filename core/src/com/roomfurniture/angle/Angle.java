package com.roomfurniture.angle;

import com.roomfurniture.problem.Vertex;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 14.12.2017 г..
 */
public class Angle {
    private Edge a;
    private Edge b;

    public Angle(Edge a, Edge b) {
        this.a = a;
        this.b = b;
    }

    public Vertex getVertex() {
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
