package com.roomfurniture.problem;

public class Vertex {
    public double x, y;

    public Vertex(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
