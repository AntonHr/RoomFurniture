package com.roomfurniture.solution;

import com.google.common.collect.Streams;
import com.roomfurniture.ShapeCalculator;
import com.roomfurniture.problem.Descriptor;
import com.roomfurniture.problem.Furniture;
import com.roomfurniture.problem.Problem;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

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

        List<Furniture> furnitures = problem.getFurnitures();
        Shape roomShape = problem.getRoom().toShape();

        Map<Boolean, List<Furniture>> result = Streams.zip(furnitures.stream(), descriptors.stream(), Furniture::transform).collect(Collectors.partitioningBy(furniture -> ShapeCalculator.contains(roomShape, furniture.toShape())));

        List<Furniture> furnitureInRoom = result.get(true);

        Iterator<Furniture> iterator  = furnitureInRoom.iterator();

        while(iterator.hasNext()) {
            Furniture furniture = iterator.next();
            for(Furniture otherFurniture : furnitureInRoom) {
                if(otherFurniture != furniture)
                   if(ShapeCalculator.intersect(furniture.toShape(), otherFurniture.toShape())) {
                        // Keep furniture with highest score
                      if(otherFurniture.getScorePerUnitArea() * ShapeCalculator.calculateAreaOf(otherFurniture.toShape()) >= furniture.getScorePerUnitArea() * ShapeCalculator.calculateAreaOf(furniture.toShape())) {
                           iterator.remove();
                           break;
                       }
               }
            }
        }

        double score = 0;

        for(Furniture furniture : furnitureInRoom) {
                score += furniture.getScorePerUnitArea() * ShapeCalculator.calculateAreaOf(furniture.toShape());
        }


        return Optional.of(score);

    }
}
