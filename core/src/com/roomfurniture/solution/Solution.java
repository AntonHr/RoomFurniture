package com.roomfurniture.solution;

import com.roomfurniture.ShapeCalculator;
import com.roomfurniture.problem.Descriptor;
import com.roomfurniture.problem.Furniture;
import com.roomfurniture.problem.Problem;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class Solution {
   private final List<Descriptor> descriptors;

   public Solution(List<Descriptor> descriptors) {
      this.descriptors = descriptors;
   }

   public List<Descriptor> getDescriptors() {
      return descriptors;
   }

   @Override
   public String toString() {
      return "Solution{" +
              "descriptors=" + descriptors +
              '}';
   }

    public Optional<Double> score(Problem problem) {
        //TODO(Kiran): Add better scoring function
        List<Shape> solutions = new ArrayList<>();

        List<Furniture> furnitures = problem.getFurnitures();
        Shape roomShape = problem.getRoom().toShape();

        Iterator<Descriptor> iterator = getDescriptors().iterator();
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
}
