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
        double dotProduct = dotProduct(a, b);

        double a1 = dotProduct / (getLength(a) * getLength(b));
        if(Math.abs(dotProduct) < 0.0000001)  {
            double crossProduct = a.x * b.y + a.y * b.x;
            if(crossProduct > 0) {
                return -1 *  Math.acos(a1);
            }

        }

        return Math.acos(a1);
    }
}
