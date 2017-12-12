package com.awesome.scenario.desktop;

import com.awesome.scenario.RoomFurnitureRenderer;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.roomfurniture.InputParser;
import com.roomfurniture.ShapeCalculator;
import com.roomfurniture.problem.Descriptor;
import com.roomfurniture.problem.Furniture;
import com.roomfurniture.problem.Problem;
import com.roomfurniture.problem.Vertex;
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
        List<Problem> parse = inputParser.parse("customTest.txt");
        Map<Problem, Solution> solutionMap = doStuff(parse);

        for (Map.Entry<Problem, Solution> entry : solutionMap.entrySet()) {
            Solution value = entry.getValue();
            Problem key = entry.getKey();
            value.getDescriptors().set(0, new Descriptor(new Vertex(1,1),0));
            new LwjglApplication(new RoomFurnitureRenderer(key, value), config);
            System.out.println("Score is " + value.score(key));
            break;
        }
//
        Furniture furniture = parse.get(0).getFurnitures().get(0);
        double area = ShapeCalculator.calculateAreaOf(furniture.toShape());
        System.out.println("Area : " + area);
        System.out.println(furniture);


    }
}
