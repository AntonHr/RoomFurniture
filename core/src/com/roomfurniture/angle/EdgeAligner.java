package com.roomfurniture.angle;

import com.roomfurniture.problem.Descriptor;
import com.roomfurniture.problem.Vertex;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 14.12.2017 г..
 */
public class EdgeAligner {

    private List<Edge> verticesToEdges(List<Vertex> vertices) {
        List<Edge> edges = new ArrayList<>();

        for (int i = 0; i < vertices.size() - 1; i++) {
            edges.add(new Edge(vertices.get(i), vertices.get(i + 1)));
        }

        edges.add(new Edge(vertices.get(vertices.size() - 1), vertices.get(0)));

        return edges;
    }

    public List<Angle> computeShapeAngles(List<Vertex> vertices) {
        List<Edge> edges = verticesToEdges(vertices);
        List<Angle> angles = new ArrayList<>();

        for (int i = 0; i < edges.size() - 1; i++) {
            angles.add(new Angle(edges.get(i), edges.get(i + 1)));
        }

        angles.add(new Angle(edges.get(edges.size() - 1), edges.get(0)));

        return angles;
    }

    public Descriptor alignTwoAngles(Angle stationary, Angle moving) {
        Vertex vStat = stationary.getVertex();
        Vertex vMov = moving.getVertex();

        return null;
    }

    public Descriptor alignAngleSets(List<Angle> stationary, List<Angle> moving) {


        return null;
    }
}
