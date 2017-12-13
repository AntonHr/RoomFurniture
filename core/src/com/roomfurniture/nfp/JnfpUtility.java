package com.roomfurniture.nfp;

import com.roomfurniture.nfp.JNFP.MultiPolygon;
import com.roomfurniture.nfp.JNFP.NoFitPolygon;
import com.roomfurniture.ShapeCalculator;
import com.roomfurniture.problem.Vertex;

import java.awt.*;
import java.util.*;

/**
 * Created by asus on 13.12.2017 г..
 */
public class JnfpUtility {
    public static MultiPolygon toMultiPolygon(Shape shape) {
        java.util.List<Vertex> vertices = ShapeCalculator.getVertices(shape);

        java.util.List<Double> xList = new ArrayList<>();
        java.util.List<Double> yList = new ArrayList<>();

        for (Vertex v : vertices) {
            xList.add(v.x);
            yList.add(v.y);
        }

        return new MultiPolygon(xList, yList);
    }

    public static Shape toShape(NoFitPolygon nfp) {
        // TODO implement conversion

        return null;
    }
}
