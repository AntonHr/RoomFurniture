package com.roomfurniture.problem;

import java.awt.*;
import java.awt.geom.Path2D;
import java.util.List;

public class Room {

    private Path2D.Double polygon;
    private List<Vertex> verticies;

    public Room(List<Vertex> verticies) {
        this.verticies = verticies;

        polygon = new Path2D.Double();

        Vertex vertex = verticies.get(0);
        polygon.moveTo(vertex.x, vertex.y);
        for(int i = 1; i < verticies.size(); i++) {
            polygon.lineTo(verticies.get(i).x, verticies.get(i).y);
        }
        polygon.closePath();
    }
    @Override
    public String toString() {
        return "Room{" +
                "verticies=" + verticies +
                '}';
    }


    public Shape toShape() {
        return polygon;
    }

    public List<Vertex> getVerticies() {
        return verticies;
    }
}
