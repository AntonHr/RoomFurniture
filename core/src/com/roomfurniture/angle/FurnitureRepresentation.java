package com.roomfurniture.angle;

import com.roomfurniture.ShapeCalculator;
import com.roomfurniture.problem.Furniture;
import com.roomfurniture.problem.Vertex;

import java.util.List;
import java.util.stream.Collectors;

public class FurnitureRepresentation {
    private Furniture furniture;
    private List<Angle> angles;

    @Override
    public String toString() {
        return "FurnitureRepresentation{" +
                "furniture=" + furniture +
                ", angles=" + angles +
                "}";
    }

    private FurnitureRepresentation(Furniture furniture, List<Angle> angles) {
        this.furniture = furniture;
        this.angles = angles;
    }

    public static FurnitureRepresentation generateRepresentationOf(Furniture furniture) {
        List<Vertex> vertices = ShapeCalculator.getVertices(furniture.toShape());
        List<Angle> angles = new EdgeAligner().computeShapeAngles(vertices);
        return new FurnitureRepresentation(furniture, angles);
    }

    public Angle getAngle(int index) {
        return angles.get(index);
    }
    public Integer findAngleIndex(Angle angle) {
        int i = this.angles.indexOf(angle);
        if(i == -1)
            throw new RuntimeException("Not Present angle queried");
        return i;
    }

    public List<FurnitureAngleSet> generateInitialAngleSet(){
        return  angles.stream().map(angle -> new FurnitureAngleSet(this, angle)).collect(Collectors.toList());
    }
}
