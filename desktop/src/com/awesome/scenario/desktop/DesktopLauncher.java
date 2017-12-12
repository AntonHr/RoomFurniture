package com.awesome.scenario.desktop;

import com.gui.RoomFurnitureRenderer;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.roomfurniture.InputParser;
import com.roomfurniture.ShapeCalculator;
import com.roomfurniture.box2d.PhysicsSimulator;
import com.roomfurniture.problem.Furniture;
import com.roomfurniture.problem.Problem;
import com.roomfurniture.solution.Solution;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import static com.roomfurniture.Main.doStuff;

public class DesktopLauncher {
    public static void main(String[] arg) throws FileNotFoundException {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.width = 2000;
        config.height = 1000;

        InputParser inputParser = new InputParser();
        List<Problem> parse = inputParser.parse("test.txt");
        Map<Problem, Solution> solutionMap = doStuff(parse);

        for (Map.Entry<Problem, Solution> entry : solutionMap.entrySet()) {
            Solution solution = entry.getValue();
            Problem problem = entry.getKey();


            PhysicsSimulator physicsSimulator = new PhysicsSimulator(problem, solution);

            LwjglApplication lwjglApplication = new LwjglApplication(new RoomFurnitureRenderer(problem, solution, physicsSimulator), config);


            System.out.println("Score is " + solution.score(problem));
            System.out.println("real score: " + entry.getValue().score(entry.getKey()));
            System.out.println("Coverage: " + solution.findCoverage(problem) * 100 + "%");
            break;
        }
//
        Furniture furniture = parse.get(0).getFurnitures().get(0);
        double area = ShapeCalculator.calculateAreaOf(furniture.toShape());
        System.out.println(furniture);


    }
}
