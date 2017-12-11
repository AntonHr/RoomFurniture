package com.roomfurniture.problem;

import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class Problem {
   public final Room room;
    private final List<Furniture> furnitures;

    public Problem(Room room, List<Furniture> furnitures) {
        this.room = room;
        this.furnitures = furnitures;
    }

    @Override
    public String toString() {
        return "Problem{" +
                "room=" + room +
                ", furnitures=" + furnitures +
                '}';
    }

    public Optional<Double> score(Solution solution) {
        List<java.awt.Shape> solutions = new ArrayList<>();
        Iterator<Descriptor> iterator = solution.getDescriptors().iterator();
        for (Furniture furniture : furnitures) {
            Descriptor next = iterator.next();
            AffineTransform rotateInstance = AffineTransform.getRotateInstance(next.getRotation());
            AffineTransform translateInstance = AffineTransform.getTranslateInstance(next.getPosition().x, next.getPosition().y);
            java.awt.Shape transformedShape = translateInstance.createTransformedShape(rotateInstance.createTransformedShape(furniture.toPolygon()));
            solutions.add(transformedShape);
        }

        Path2D.Double roomShape = room.toPolygon();
        double sum = 0;
        int index = 0;
        for (java.awt.Shape shape : solutions) {

            if (Polygonizable.contains(roomShape, shape)) {
                sum += furnitures.get(index).getScorePerUnitArea();
            }

            index += 1;
        }
        return Optional.of(sum);

    }

    public List<Furniture> getFurnitures() {
        return furnitures;
    }
}
