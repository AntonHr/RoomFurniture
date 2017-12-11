package com.roomfurniture.problem;

public class Descriptor {
    private final Vertex position;
    private final double rotation;

    public Descriptor(Vertex position, double rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    public Vertex getPosition() {
        return position;
    }

    public double getRotation() {
        return rotation;
    }
}
