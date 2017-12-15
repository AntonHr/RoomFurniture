package com.roomfurniture.placing.physics;

import com.awesome.scenario.desktop.DesktopLauncher;
import com.roomfurniture.ShapeCalculator;
import com.roomfurniture.box2d.PhysicsSimulatorEvaluator;
import com.roomfurniture.ga.algorithm.interfaces.EvaluationStrategy;
import com.roomfurniture.placing.BasicPlacingProblem;
import com.roomfurniture.placing.PlacingDescriptor;
import com.roomfurniture.placing.PlacingSolution;
import com.roomfurniture.problem.Furniture;
import com.roomfurniture.problem.Vertex;

import java.util.*;

public class PhysicsPlacingSolutionEvaluationStrategy implements EvaluationStrategy<PlacingSolution> {
    private final BasicPlacingProblem problem;
    private boolean shouldRender;
    private int softMaxIterations;
    private double successRatio;
    private double failureRatio;
    private float dt;
    private float trial_time;
    private int impulseForce;
    private int spawnForce;

    public PhysicsPlacingSolutionEvaluationStrategy(BasicPlacingProblem problem, boolean shouldRender, int softMaxIterations, double successRatio, double failureRatio, float dt, float trial_time, int impulseForce, int spawnForce) {
        this.problem = problem;
        this.shouldRender = shouldRender;
        this.softMaxIterations = softMaxIterations;
        this.successRatio = successRatio;
        this.failureRatio = failureRatio;
        this.dt = dt;
        this.trial_time = trial_time;
        this.impulseForce = impulseForce;
        this.spawnForce = spawnForce;
    }

    @Override
    public double evaluate(PlacingSolution placingSolution) {
        // TODO(Kiran): score solution based on outcome - how many you can place

        // TODO(Kiran): add function to convert a placingsolution into a solution


        //TODO(Kiran): Add better scoring function


//        List<Furniture> furnitures = problem.getFurnitures();
//        Shape roomShape = problem.getRoom().toShape();
//
//        Map<Boolean, List<Furniture>> result = Streams.zip(furnitures.stream(), descriptors.stream(), Furniture::transform).collect(Collectors.partitioningBy(furniture -> ShapeCalculator.contains(roomShape, furniture.toShape())));
//
//        List<Furniture> furnitureInRoom = result.get(true);

        //getting items and points to spawn
        Queue<Furniture> itemsToSpawn = new LinkedList<>();
        Queue<Vertex> spawnPoints = new LinkedList<>();
        for (PlacingDescriptor descriptor : placingSolution.getDescriptors()) {
            itemsToSpawn.add(descriptor.getFurniture(problem));
            spawnPoints.add(descriptor.getVertex(problem));
        }
        PhysicsSimulatorEvaluator physicsSimulator = new PhysicsSimulatorEvaluator(problem.getRoom(), itemsToSpawn, spawnPoints, softMaxIterations, successRatio, failureRatio, trial_time, impulseForce, spawnForce);


        //s
        //        float dt = 0.1f; //s
//        int ITERATION_COUNT = 4187;
//        for (int i = 0; i < ITERATION_COUNT; i++) {
//            System.out.println(i + "/" + ITERATION_COUNT);
        int iterationCount = 0;
        if(shouldRender)
            DesktopLauncher.renderer.update(physicsSimulator);
        while (!physicsSimulator.isDone()) {
            iterationCount++;
//            if (iterationCount % 100000 == 0) {
//                System.out.println("Iteration Count: " + iterationCount);
//                System.out.println("Items to spawn: " + physicsSimulator.itemsToSpawn.size() + "/ " + (physicsSimulator.bodies.size() - 1));
//            }
            physicsSimulator.update(dt);
        }

        List<Furniture> furnitureInRoom = physicsSimulator.getTransformedItems();

        Iterator<Furniture> iterator = furnitureInRoom.iterator();


        //remove intersecting in the room
        while (iterator.hasNext()) {
            Furniture furniture = iterator.next();
            for (Furniture otherFurniture : furnitureInRoom) {
                if (otherFurniture != furniture)
                    if (ShapeCalculator.intersect(furniture.toShape(), otherFurniture.toShape())) {
                        // Keep furniture with highest score
                        if (otherFurniture.getScorePerUnitArea() * ShapeCalculator.calculateAreaOf(otherFurniture.toShape()) >= furniture.getScorePerUnitArea() * ShapeCalculator.calculateAreaOf(furniture.toShape())) {
                            iterator.remove();
                            break;
                        }
                    }
            }
        }
        double score = 0;
        double areaSum = 0.0;

        for (Furniture furniture : furnitureInRoom) {
            areaSum += ShapeCalculator.calculateAreaOf(furniture.toShape());
            score += furniture.getScorePerUnitArea() * ShapeCalculator.calculateAreaOf(furniture.toShape());
        }


//        System.out.println("Score after evaluation: " + score);

        double roomArea = ShapeCalculator.calculateAreaOf(problem.getRoom().toShape());


        double finalAreaSum = areaSum;
        double finalScore = score;
        placingSolution.cacheResults(new HashMap<String, Object>() {{
            put("coverage", finalAreaSum / roomArea);//0..1
            put("score", finalScore);
            put("solution", physicsSimulator.getSolution(problem.getProblem()));
        }});
        placingSolution.storePhysicsSimulator(physicsSimulator);


        // The optimizer often can make up for an initial lack of coverage, but not score
//        if (areaSum / roomArea <= 0.3)
//            score *= 0.03;
//        score *= (1 + areaSum / roomArea);

        return score;
    }
}
