
/*
 * Copyright (c) 2010, Bart Kiers
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package com.roomfurniture.convexHull;

import com.roomfurniture.ShapeCalculator;
import com.roomfurniture.problem.Furniture;
import com.roomfurniture.problem.Vertex;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.*;
import java.util.List;

public final class GrahamScan {

   protected static enum Turn {
        CLOCKWISE, COUNTER_CLOCKWISE, COLLINEAR
    }

   protected static boolean areAllCollinear(List<Point2D.Double> points) {

        if (points.size() < 2) {
            return true;
        }

        final Point2D.Double a = points.get(0);
        final Point2D.Double b = points.get(1);

        for (int i = 2; i < points.size(); i++) {

            Point2D.Double c = points.get(i);

            if (getTurn(a, b, c) != Turn.COLLINEAR) {
                return false;
            }
        }

        return true;
    }

   public static Furniture getConvexHull(Furniture first, Furniture second) {
        List<Vertex> firstList = first.getVertices();
        List<Vertex> secondList = second.getVertices();
        List<Point2D.Double> pointList = new LinkedList<>();
        for (Vertex vertex : firstList) {
            pointList.add(new Point2D.Double(vertex.x, vertex.y));
        }
        for (Vertex vertex : secondList) {
            pointList.add((new Point2D.Double(vertex.x, vertex.y)));
        }
        List<Point2D.Double> convexList = getConvexHull(pointList);
        List<Vertex> vertexConvexList = new LinkedList<Vertex>();
        for (Point2D.Double point : convexList) {
            vertexConvexList.add(new Vertex(point.getX(), point.getY()));
        }

        double firstCost = first.getScorePerUnitArea() * ShapeCalculator.calculateAreaOf(first.toShape());
        double secondCost = second.getScorePerUnitArea() * ShapeCalculator.calculateAreaOf(second.toShape());
        double totalArea = ShapeCalculator.calculateAreaOf(first.toShape()) + ShapeCalculator.calculateAreaOf(second.toShape());
        double newCost = (firstCost + secondCost) / totalArea;

        return new Furniture(0, newCost, vertexConvexList);
    }

    public static List<Point2D.Double> getConvexHull(int[] xs, int[] ys) throws IllegalArgumentException {

        if (xs.length != ys.length) {
            throw new IllegalArgumentException("xs and ys don't have the same size");
        }

        List<Point2D.Double> points = new ArrayList<Point2D.Double>();

        for (int i = 0; i < xs.length; i++) {
            points.add(new Point2D.Double(xs[i], ys[i]));
        }

        return getConvexHull(points);
    }

   public static List<Point2D.Double> getConvexHull(List<Point2D.Double> points) throws IllegalArgumentException {

        List<Point2D.Double> sorted = new ArrayList<Point2D.Double>(getSortedPointSet(points));

        if (sorted.size() < 3) {
            throw new IllegalArgumentException("can only create a convex hull of 3 or more unique points");
        }

        if (areAllCollinear(sorted)) {
            throw new IllegalArgumentException("cannot create a convex hull from collinear points");
        }

        Stack<Point2D.Double> stack = new Stack<Point2D.Double>();
        stack.push(sorted.get(0));
        stack.push(sorted.get(1));

        for (int i = 2; i < sorted.size(); i++) {

            Point2D.Double head = sorted.get(i);
            Point2D.Double middle = stack.pop();
            Point2D.Double tail = stack.peek();

            Turn turn = getTurn(tail, middle, head);

            switch (turn) {
                case COUNTER_CLOCKWISE:
                    stack.push(middle);
                    stack.push(head);
                    break;
                case CLOCKWISE:
                    i--;
                    break;
                case COLLINEAR:
                    stack.push(head);
                    break;
            }
        }

        // close the hull
        stack.push(sorted.get(0));

        return new ArrayList<Point2D.Double>(stack);
    }

   protected static Point2D.Double getLowestPoint(List<Point2D.Double> points) {

        Point2D.Double lowest = points.get(0);

        for (int i = 1; i < points.size(); i++) {

            Point2D.Double temp = points.get(i);

            if (temp.y < lowest.y || (temp.y == lowest.y && temp.x < lowest.x)) {
                lowest = temp;
            }
        }

        return lowest;
    }

   protected static Set<Point2D.Double> getSortedPointSet(List<Point2D.Double> points) {

        final Point2D.Double lowest = getLowestPoint(points);

        TreeSet<Point2D.Double> set = new TreeSet<Point2D.Double>(new Comparator<Point2D.Double>() {
            @Override
            public int compare(Point2D.Double a, Point2D.Double b) {

                if (a == b || a.equals(b)) {
                    return 0;
                }

                // use longs to guard against int-underflow
                double thetaA = Math.atan2((long) a.y - lowest.y, (long) a.x - lowest.x);
                double thetaB = Math.atan2((long) b.y - lowest.y, (long) b.x - lowest.x);

                if (thetaA < thetaB) {
                    return -1;
                } else if (thetaA > thetaB) {
                    return 1;
                } else {
                    // collinear with the 'lowest' point, let the point closest to it come first

                    // use longs to guard against int-over/underflow
                    double distanceA = Math.sqrt((((long) lowest.x - a.x) * ((long) lowest.x - a.x)) +
                            (((long) lowest.y - a.y) * ((long) lowest.y - a.y)));
                    double distanceB = Math.sqrt((((long) lowest.x - b.x) * ((long) lowest.x - b.x)) +
                            (((long) lowest.y - b.y) * ((long) lowest.y - b.y)));

                    if (distanceA < distanceB) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            }
        });

        set.addAll(points);

        return set;
    }

   protected static Turn getTurn(Point2D.Double a, Point2D.Double b, Point2D.Double c) {

        // use longs to guard against int-over/underflow
        double crossProduct = ((b.x - a.x) * (c.y - a.y)) -
                ((b.y - a.y) * (c.x - a.x));

        if (crossProduct > 0) {
            return Turn.COUNTER_CLOCKWISE;
        } else if (crossProduct < 0) {
            return Turn.CLOCKWISE;
        } else {
            return Turn.COLLINEAR;
        }
    }
}