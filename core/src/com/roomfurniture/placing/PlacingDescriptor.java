package com.roomfurniture.placing;

import com.roomfurniture.problem.Furniture;
import com.roomfurniture.problem.Vertex;

public class PlacingDescriptor {
    private final Integer shapeNo;
    private final Integer spawnPoint;

    @Override
    public String toString() {
        return "PlacingDescriptor{" +
                "shapeNo=" + shapeNo +
                ", spawnPoint=" + spawnPoint +
                '}';
    }

    public PlacingDescriptor(Integer shapeNo, Integer spawnPoint) {
        this.shapeNo = shapeNo;
        this.spawnPoint = spawnPoint;
    }

    public Vertex getVertex(PlacingProblem problem) {
        return problem.getSpawnPoints().get(spawnPoint);
    }

    public Furniture getFurniture(PlacingProblem problem) {
        if(shapeNo >= problem.getFurnitures().size())
        {
            System.out.println("haha");
        }
        return problem.getFurnitures().get(shapeNo);
    }
}
