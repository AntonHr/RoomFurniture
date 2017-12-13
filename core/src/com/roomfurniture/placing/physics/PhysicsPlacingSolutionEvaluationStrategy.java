package com.roomfurniture.placing.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import com.gui.EvaluatorPhysicsRenderer;
import com.gui.RoomFurnitureRenderer;
import com.roomfurniture.InputParser;
import com.roomfurniture.ShapeCalculator;
import com.roomfurniture.box2d.PhysicsSimulator;
import com.roomfurniture.box2d.PhysicsSimulatorEvaluator;
import com.roomfurniture.ga.algorithm.interfaces.EvaluationStrategy;
import com.roomfurniture.placing.PlacingDescriptor;
import com.roomfurniture.placing.PlacingProblem;
import com.roomfurniture.placing.PlacingSolution;
import com.roomfurniture.problem.Furniture;
import com.roomfurniture.problem.Problem;
import com.roomfurniture.problem.Vertex;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class PhysicsPlacingSolutionEvaluationStrategy implements EvaluationStrategy<PlacingSolution> {
    private final PlacingProblem problem;

    public PhysicsPlacingSolutionEvaluationStrategy(PlacingProblem problem) {
        this.problem = problem;
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
        PhysicsSimulatorEvaluator physicsSimulator = new PhysicsSimulatorEvaluator(problem.getRoom(), itemsToSpawn, spawnPoints);


        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 2000;
        config.height = 1000;

        //EvaluatorPhysicsRenderer renderer = new EvaluatorPhysicsRenderer();
        //LwjglApplication lwjglApplication = new LwjglApplication(renderer, config);

        float dt = 0.0001f; //s
        //float dt = 0.1f; //s
        int ITERATION_COUNT = 418778;
        for (int i = 0; i < ITERATION_COUNT; i++) {
            System.out.println(i + "/" + ITERATION_COUNT);
            physicsSimulator.update(dt);
            //if (i % 10 == 0)
            //renderer.update(physicsSimulator);
        }
        //Gdx.app.exit();

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


        System.out.println("Score after evaluation: " + score);

        double roomArea = ShapeCalculator.calculateAreaOf(problem.getRoom().toShape());


        double finalAreaSum = areaSum;
        double finalScore = score;
        placingSolution.cacheResults(new HashMap<String, Object>() {{
            put("coverage", finalAreaSum / roomArea);//0..1
            put("score", finalScore);
        }});


        // The optimizer often can make up for an initial lack of coverage, but not score
//        if (areaSum / roomArea <= 0.3)
//            score *= 0.03;
//        score *= (1 + areaSum / roomArea);

        return score;
    }
}
