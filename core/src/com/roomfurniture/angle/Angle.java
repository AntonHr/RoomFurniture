package com.roomfurniture.angle;

import com.roomfurniture.problem.Vertex;

import java.util.*;

/**
 * Created by asus on 14.12.2017 г..
 */
public class Angle {
    private Edge a;
    private Edge b;

    @Override
    public String toString() {
        return "(" +
                Math.toDegrees(getAngleValue()) + " deg," +
                findTopVertex() +
                ')';
    }

    private Vertex topVertex;
    private List<Vertex> bottomVertices;

    public Angle(Edge a, Edge b) {
        this.a = a;
        this.b = b;
        this.topVertex = findTopVertex();
        this.bottomVertices = findBottomVertices();
    }

    public Set<Vertex> getVertices() {
        Set<Vertex> vertices = new HashSet<Vertex>();
        vertices.add(a.getBeginVertex());
        vertices.add(a.getEndVertex());
        vertices.add(b.getBeginVertex());
        vertices.add(b.getEndVertex());

        return vertices;
    }

    private Vertex findTopVertex() {
        List<Vertex> vertices = new ArrayList<>();
        vertices.add(a.getBeginVertex());
        vertices.add(a.getEndVertex());
        vertices.add(b.getBeginVertex());
        vertices.add(b.getEndVertex());

        for (Vertex k: vertices) {
            for (Vertex p: vertices) {
                if (k == p) return k;
            }
        }

        throw new RuntimeException("Non touching edges in Angle.");
    }

    public List<Vertex> findBottomVertices() {
        Set<Vertex> vertices = new HashSet<Vertex>();
        vertices.add(a.getBeginVertex());
        vertices.add(a.getEndVertex());
        vertices.add(b.getBeginVertex());
        vertices.add(b.getEndVertex());

        vertices.remove(getTopVertex());

        return new ArrayList<Vertex>(vertices);
    }

    public Vertex getTopVertex() {
        return topVertex;
    }

    public List<Vertex> getBottomVertices() {
        return bottomVertices;
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
        return new Edge(lastFurnitureAngle.getTopVertex(), currentFurnitureAngle.getTopVertex());
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




