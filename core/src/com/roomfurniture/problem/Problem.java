package com.roomfurniture.problem;

import com.roomfurniture.ShapeCalculator;

import java.awt.*;
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
        //TODO(Kiran): Add better scoring function
        List<Shape> solutions = new ArrayList<>();
        Iterator<Descriptor> iterator = solution.getDescriptors().iterator();
        for (Furniture furniture : furnitures) {
            Descriptor next = iterator.next();
            AffineTransform transform = new AffineTransform();
            transform.rotate(next.getRotation());
            transform.translate(next.getPosition().x, next.getPosition().y);
//            AffineTransform rotateInstance = AffineTransform.getRotateInstance(next.getRotation());
//            AffineTransform translateInstance = AffineTransform.getTranslateInstance();
//            Shape transformedShape = translateInstance.createTransformedShape(rotateInstance.createTransformedShape(furniture.toShape()));
            Shape transformedShape = transform.createTransformedShape(furniture.toShape());
            solutions.add(transformedShape);
        }

        Shape roomShape = room.toShape();
        double sum = 0;
        int index = 0;
        for (Shape shape : solutions) {

            if (ShapeCalculator.contains(roomShape, shape)) {
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
