package com.roomfurniture.angle;

import com.roomfurniture.problem.Vertex;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 14.12.2017 г..
 */
public class Angle {
    private Edge a;
    private Edge b;

    @Override
    public String toString() {
        return "Angle{" +
                "a=" + a +
                ", b=" + b +
                '}';
    }

    public Angle(Edge a, Edge b) {
        this.a = a;
        this.b = b;
    }

    public Vertex getVertex() {
        List<Vertex> vertices = new ArrayList<>();
        vertices.add(a.getA());
        vertices.add(a.getB());
        vertices.add(b.getA());
        vertices.add(b.getB());

        for (Vertex k: vertices) {
            for (Vertex p: vertices) {
                if (k == p) return k;
            }
        }

        throw new RuntimeException("Non touching edges in Angle.");
    }

    public Double getAngleValue() {
        return a.angleTo(b);
    }

    public Edge getA() {
        return a;
    }

    public Edge getB() {
        return b;
    }

    private static Edge getRelativeDirectionBetween(Angle lastFurnitureAngle, Angle currentFurnitureAngle) {
        return new Edge(lastFurnitureAngle.getVertex(), currentFurnitureAngle.getVertex());
    }

    public static boolean isContainedAngleValue(double roomAngle, double furnitureAngle) {
        if(Math.abs(roomAngle - furnitureAngle) < 0.0000001) {
           return true;
        }
        // if the room angle is > 90 degrees then inward point - to fit, must be greater
        else if(roomAngle > Math.toRadians(90)) {
            if(roomAngle < furnitureAngle)
                return true;
            else
                return false;
            // if the room angle is  < 90 degrees then outward point - to fit, must be smaller
        } else {
            if(roomAngle > furnitureAngle)
                return true;
            else
                return false;
        }
    }

    public static boolean congurentAngleSet(List<Angle> roomAngles, List<Angle> furnitureAngles) {
        assert(roomAngles.size() == furnitureAngles.size());
        if(roomAngles.size() == 1) {
            double furnitureAngle = furnitureAngles.get(0).getAngleValue();
            double roomAngle = roomAngles.get(0).getAngleValue();
            return isContainedAngleValue(roomAngle, furnitureAngle);
        } else {
            Angle lastRoomAngle = roomAngles.get(0) ;
            Angle lastFurnitureAngle = furnitureAngles.get(0) ;

            for(int i = 1; i < roomAngles.size(); i++) {
                Angle currentRoomAngle = roomAngles.get(i);
                Angle currentFurnitureAngle = furnitureAngles.get(i);

                Edge furnitureLocation = getRelativeDirectionBetween(lastFurnitureAngle, currentFurnitureAngle);
                Edge roomLocation = getRelativeDirectionBetween(lastRoomAngle, currentRoomAngle);

                if(furnitureLocation.getLength() > roomLocation.getLength() - 0.0000001) {
                    return false;
                }
            }
            return true;
        }
    }
}




