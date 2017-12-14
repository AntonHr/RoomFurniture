package com.roomfurniture.angle;

import com.roomfurniture.problem.Vertex;

/**
 * Created by asus on 14.12.2017 г..
 */
public class Edge {
    private Vertex beginVertex;
    private Vertex endVertex;

    public Edge(Vertex beginVertex, Vertex endVertex) {
        this.beginVertex = beginVertex;
        this.endVertex = endVertex;
    }

    public double angleTo(Edge secondEdge) {
        Vertex v1 = this.getVector();
        Vertex v2 = secondEdge.getVector();

        double l1 = this.getLength();
        double l2 = secondEdge.getLength();

        double dotProduct = v1.x * v2.x + v1.y * v2.y;
        double angle = dotProduct / this.getLength() * secondEdge.getLength();

        return angle;
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
