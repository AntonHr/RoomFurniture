package com.roomfurniture;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.Arrays;

public class ShapeCalculator {

    public static double calculateAreaOf(Shape aDouble) {
        PathIterator pathIterator = aDouble.getPathIterator(null);
        int size = 0;
        while(!pathIterator.isDone()) {
            pathIterator.next();
            size += 1;
        }
        Double xs[] = new Double[size-1];
        Double ys[] = new Double[size-1];
        int currentPosition = 0;
        pathIterator = aDouble.getPathIterator(null);

        while(!pathIterator.isDone()) {
            double[] kilme = new double[2];
            pathIterator.currentSegment(kilme);
            pathIterator.next();
            if(currentPosition < size-1) {
                xs[currentPosition] = kilme[0];
                ys[currentPosition] = kilme[1];
            }

            currentPosition++;
        }

        System.out.println(Arrays.toString(xs));
        System.out.println(Arrays.toString(ys));
        double sum = 0.0;
        for (int i = 0; i < size-1; i++) {
            sum = sum + (xs[i] * ys[(i+1)%(size-1)]) - (ys[i] * xs[(i+1)%(size-1)]);
        }
        return Math.abs(0.5 * sum);

    }

    public static boolean intersect(Shape shapeA, Shape shapeB) {
        Area polygonA = new Area(shapeA);
        Area polygonB = new Area(shapeB);

        polygonA.intersect(polygonB);
        return !polygonA.isEmpty();
    }

    public static boolean contains(Shape shapeA, Shape shapeB) {
        Area polygonA = new Area(shapeA);
        Area polygonB = new Area(shapeB);
        polygonB.subtract(polygonA);
        return polygonB.isEmpty();
    }
}
