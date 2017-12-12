package com.roomfurniture.problem;

import com.badlogic.gdx.math.Vector2;

public class Vertex {
    public double x, y;

    public Vertex(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vertex(Vector2 vector) {
        x = vector.x;
        y = vector.y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ')';
    }
}
