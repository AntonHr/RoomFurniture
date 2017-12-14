package com.roomfurniture.placing;

import com.roomfurniture.box2d.PhysicsSimulatorEvaluator;
import com.roomfurniture.problem.Furniture;
import com.roomfurniture.problem.Vertex;
import com.roomfurniture.solution.Solution;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PlacingSolution {
    private final List<PlacingDescriptor> descriptors;
    private HashMap<String, Object> cache;
    private PhysicsSimulatorEvaluator physicsSimulator;


    public PlacingSolution(List<PlacingDescriptor> descriptors) {
        this.descriptors = descriptors;
    }

    @Override
    public String toString() {
        return "PlacingSolution{" +
                "descriptors=" + descriptors +
                '}';
    }


    public List<PlacingDescriptor> getDescriptors() {
        return descriptors;
    }

    public List<Vertex> getVertexes(PlacingProblem problem) {
        return descriptors.stream().map(placingDescriptor -> placingDescriptor.getVertex(problem)).collect(Collectors.toList());
    }

    public List<Furniture> getFurniture(PlacingProblem problem) {
        return descriptors.stream().map(placingDescriptor -> placingDescriptor.getFurniture(problem)).collect(Collectors.toList());
    }

    public void cacheResults(HashMap<String, Object> results) {
        cache = results;
    }

    public HashMap<String, Object> getCachedResults() {
        return cache;
    }

    public void storePhysicsSimulator(PhysicsSimulatorEvaluator physicsSimulator) {
        this.physicsSimulator = physicsSimulator;
    }

    public PhysicsSimulatorEvaluator getPhysicsSimulator() {
        return physicsSimulator;
    }
}
