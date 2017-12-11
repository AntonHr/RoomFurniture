package com.roomfurniture.problem;


import java.awt.geom.Area;
import java.awt.geom.Path2D;

public interface Polygonizable {
    Path2D.Double toPolygon();

    static boolean intersect(Polygonizable a, Polygonizable b) {

        Area polygonA = new Area( a.toPolygon());
        Area polygonB = new Area( b.toPolygon());

        polygonA.intersect(polygonB);
        return !polygonA.isEmpty();
    }

    static boolean contains(Polygonizable a, Polygonizable b) {
        Area polygonA = new Area( a.toPolygon());
        Area polygonB = new Area( b.toPolygon());
        polygonB.subtract(polygonA);
        return polygonB.isEmpty();
    }
}
