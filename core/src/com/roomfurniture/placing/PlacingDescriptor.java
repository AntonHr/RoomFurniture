package com.roomfurniture.placing;

import com.roomfurniture.problem.Furniture;
import com.roomfurniture.problem.Vertex;

public class PlacingDescriptor {
    private final Integer shapeNo;
    private final Integer spawnPoint;
    private Vertex  position;
    private double rotation;

    public Vertex getPosition() {
        return position;
    }

    public double getRotation() {
        return rotation;
    }

    @Override
    public String toString() {
        return "PlacingDescriptor{" +
                "shapeNo=" + shapeNo +
                ", spawnPoint=" + spawnPoint +
                '}';
    }

    public PlacingDescriptor(Integer shapeNo, Integer spawnPoint, Vertex position, Double rotation) {
        this.shapeNo = shapeNo;
        this.spawnPoint = spawnPoint;
        this.position = position;
        this.rotation = rotation;
    }

    public PlacingDescriptor(Integer shapeNo, Integer spawnPoint) {
        this.shapeNo = shapeNo;
        this.spawnPoint = spawnPoint;
        position = new Vertex(0,0);
        rotation = 0;
    }

    public Vertex getVertex(PlacingProblem problem) {
        return problem.getSpawnPoints().get(spawnPoint);
    }

    public Furniture getFurniture(PlacingProblem problem) {
        return problem.getFurnitures().get(shapeNo);
    }

    public void mutate(double thetaAmount, double dx, double dy) {
        rotation += thetaAmount;
        position = new Vertex(position.x + dx, position.y + dy);
    }
}
