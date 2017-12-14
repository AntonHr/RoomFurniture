package com.roomfurniture.nfp;

import com.roomfurniture.ShapeCalculator;
import com.roomfurniture.convexHull.GrahamScan;
import com.roomfurniture.problem.Room;
import com.roomfurniture.problem.Vertex;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;

/**
 * Created by asus on 13.12.2017 г..
 */
public class OptimalPlacementCalculator {
    private NfpCalculationStrategy nfpCalculator;
    private Room room;

    public OptimalPlacementCalculator(NfpCalculationStrategy nfpCalculator, Room room) {
        this.nfpCalculator = nfpCalculator;
        this.room = room;
    }

    public Shape findBestPlacement(Shape statShape, Shape orbShape) {
        Vertex refVertex = ShapeCalculator.findBottomVertex(orbShape);

        Shape nfp = nfpCalculator.calculateNfp(statShape, orbShape);
        List<Vertex> nfpVertices = ShapeCalculator.getVertices(nfp);

        //Vertex optimumVertex = nfpVertices.get(0);
        Double minArea = -Double.MAX_VALUE;
        Shape minShape = null;

        for (Vertex v : ShapeCalculator.getVertices(nfp)) {
            double xTrans = v.x - refVertex.x;
            double yTrans = v.y - refVertex.y;

            Shape translatedShape =
                    AffineTransform.getTranslateInstance(xTrans, yTrans).createTransformedShape(orbShape);

            Shape joinedShape = GrahamScan.getConvexHull(statShape, translatedShape);
            double joinedShapeArea = ShapeCalculator.calculateAreaOf(joinedShape);

            if (joinedShapeArea < minArea &&
                    ShapeCalculator.contains(room.toShape(), joinedShape)) {
                minArea = joinedShapeArea;
                //optimumVertex = v;
                minShape = joinedShape;
            }
        }

        return minShape;
    }


}
