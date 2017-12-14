package com.roomfurniture.nfp;

import com.roomfurniture.ShapeCalculator;
import com.roomfurniture.convexHull.GrahamScan;
import com.roomfurniture.problem.Descriptor;
import com.roomfurniture.problem.Furniture;
import com.roomfurniture.problem.Room;
import com.roomfurniture.problem.Vertex;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.Vector;

/**
 * Created by asus on 13.12.2017 г..
 */
public class OptimalPlacementCalculator {
    public class JoinedShapePlacement {
        private Vertex translationVector;
        private Shape joinedShape;

        public JoinedShapePlacement(Vertex translationVector, Shape joinedShape) {
            this.translationVector = translationVector;
            this.joinedShape = joinedShape;
        }

        public Vertex getTranslationVector() {
            return translationVector;
        }

        public Shape getJoinedShape() {
            return joinedShape;
        }
    }

    private NfpCalculationStrategy nfpCalculator;
    private Room room;

    public OptimalPlacementCalculator(NfpCalculationStrategy nfpCalculator, Room room) {
        this.nfpCalculator = nfpCalculator;
        this.room = room;
    }

    public JoinedShapePlacement joinShapes(Shape statShape, Shape orbShape) {
        Vertex refVertex = ShapeCalculator.findBottomVertex(orbShape);

        Shape nfp = nfpCalculator.calculateNfp(statShape, orbShape);
        List<Vertex> nfpVertices = ShapeCalculator.getVertices(nfp);

        Vertex optimumTranslation = null;
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
                minShape = joinedShape;
                optimumTranslation = new Vertex(xTrans, yTrans);
            }
        }

        return new JoinedShapePlacement(optimumTranslation, minShape);
    }

    public JoinedFurniturePlacement joinFurnitures(Furniture statFurniture, Descriptor initialDescriptor,
                                                   Furniture movingFurniture, int rotationStep) {

        Double minArea = -Double.MAX_VALUE;
        JoinedShapePlacement shapeOptimalPlacement = null;
        Double optimalTheta = 0.0;

        for (int i = 0; i <= 360; i += rotationStep) {
            Double theta = (i / 180.0) * Math.PI;

            Shape orbShape = movingFurniture.toShape();
            orbShape = AffineTransform.getRotateInstance(theta).createTransformedShape(orbShape);

            JoinedShapePlacement placement = joinShapes(statFurniture.toShape(), orbShape);

            if (ShapeCalculator.calculateAreaOf(placement.getJoinedShape()) < minArea) {
                shapeOptimalPlacement = placement;
                optimalTheta = theta;
            }
        }

        double firstCost = statFurniture.getScorePerUnitArea() * ShapeCalculator.calculateAreaOf(statFurniture.toShape());
        double secondCost = movingFurniture.getScorePerUnitArea() * ShapeCalculator.calculateAreaOf(movingFurniture.toShape());
        double totalArea = ShapeCalculator.calculateAreaOf(statFurniture.toShape()) + ShapeCalculator.calculateAreaOf(movingFurniture.toShape());
        double newCost = (firstCost + secondCost) / totalArea;

        Furniture joinedFurniture = new Furniture(0, newCost, shapeOptimalPlacement.getJoinedShape());
        Vertex newPosition = new Vertex(initialDescriptor.getPosition().x + shapeOptimalPlacement.getTranslationVector().x,
                initialDescriptor.getPosition().y + shapeOptimalPlacement.getTranslationVector().y);

        Descriptor movedFurnitureDescriptor = new Descriptor(newPosition, optimalTheta);

        return new JoinedFurniturePlacement(joinedFurniture, movedFurnitureDescriptor);
    }
}
