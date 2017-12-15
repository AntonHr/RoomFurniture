package com.roomfurniture.angle;

import com.roomfurniture.problem.Vertex;

/**
 * Created by asus on 14.12.2017 г..
 */
public class VertexExtensionUtility {
    public static double dotProduct(Vertex a, Vertex b) {
        return a.x * b.x + a.y * b.y;
    }

    public static double getLength(Vertex a) {
        return Math.sqrt(a.x * a.x + a.y * a.y);
    }

    public static double getAngle(Vertex a, Vertex b) {
        return Math.acos(dotProduct(a, b) / getLength(a) * getLength(b));
    }
}
