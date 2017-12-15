package com.roomfurniture.angle;

import com.roomfurniture.problem.Vertex;


public class Edge {
    private Vertex beginVertex;
    private Vertex endVertex;

    public Edge(Vertex beginVertex, Vertex endVertex) {
        this.beginVertex = beginVertex;
        this.endVertex = endVertex;
    }
    @Override
    public String toString() {
        return "Edge{" +
                "a=" + beginVertex +
                ", b=" + endVertex +
                '}';
    }


    public double angleTo(Edge secondEdge) {
        Vertex v1 = this.getVector();
        Vertex v2 = secondEdge.getVector();

        double length1 = this.getLength();
        double length2 = secondEdge.getLength();

        double dotProduct = v1.x * v2.x + v1.y * v2.y;
        double angle = dotProduct / length1 * length2;

        return Math.acos(angle);
    }

    public double getLength() {
        return Math.sqrt(Math.pow(beginVertex.x - endVertex.x, 2) + Math.pow(beginVertex.y - endVertex.y, 2));
    }

    public Vertex getVector() {
        return new Vertex(endVertex.x - beginVertex.x, endVertex.y - beginVertex.y);
    }

    public Vertex getBeginVertex() {
        return beginVertex;
    }

    public Vertex getEndVertex() {
        return endVertex;
    }
}
