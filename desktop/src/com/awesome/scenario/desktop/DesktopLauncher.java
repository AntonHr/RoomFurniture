package com.awesome.scenario.desktop;

import com.awesome.scenario.RoomFurnitureMain;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.roomfurniture.InputParser;
import com.roomfurniture.problem.Problem;
import com.roomfurniture.solution.SolutionGeneratorStrategy;

import java.io.FileNotFoundException;
import java.util.List;

public class DesktopLauncher {
    public static void main(String[] arg) throws FileNotFoundException {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.width = 2000;
        config.height = 1000;

        InputParser inputParser = new InputParser();
        List<Problem> problems = inputParser.parse("test2.txt");


        new LwjglApplication(new RoomFurnitureMain(problems.get(0), new SolutionGeneratorStrategy(problems.get(0)).generate()), config);
    }
}
