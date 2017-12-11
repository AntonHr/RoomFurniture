package com.roomfurniture.problem;

import java.util.List;

public class Polygon {
    protected final List<Vertex> verticies;

    public Polygon(List<Vertex> verticies) {
        this.verticies = verticies;
    }

    public static boolean intersect(Polygon polygon1, Polygon polygon2)
    {
        return true;
    }

    public static boolean contains(Polygon polygon1, Polygon polygon2) {
        return true;
    }
}

