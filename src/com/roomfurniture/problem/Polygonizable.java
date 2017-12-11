package com.roomfurniture.problem;


import java.awt.geom.Area;
import java.awt.geom.Path2D;

public interface Polygonizable {
    Path2D.Double toPolygon();

    static boolean intersect(java.awt.Shape s, java.awt.Shape s1) {

        Area polygonA = new Area(s);
        Area polygonB = new Area(s1);

        polygonA.intersect(polygonB);
        return !polygonA.isEmpty();
    }

    static boolean contains(java.awt.Shape s, java.awt.Shape s1) {
        Area polygonA = new Area(s);
        Area polygonB = new Area(s1);
        polygonB.subtract(polygonA);
        return polygonB.isEmpty();
    }
}
