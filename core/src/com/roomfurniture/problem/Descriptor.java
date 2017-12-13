package com.roomfurniture.problem;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Pattern;

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

    @Override
    public String toString() {
        return "Descriptor{" +
                "position=" + position +
                ", rotation=" + rotation +
                '}';
    }

    public String toSerialized() {
        return position.x + " " + position.y + " " + rotation + " ";
    }
    public static Optional<Descriptor> fromSerialized(Scanner scanner) {
        try {
            double x = scanner.nextDouble();
            double y = scanner.nextDouble();
            double rotaton = scanner.nextDouble();
            return Optional.of(new Descriptor(new Vertex(x, y), rotaton));
        } catch (NoSuchElementException ignored) {}

        return Optional.empty();
    }
}
