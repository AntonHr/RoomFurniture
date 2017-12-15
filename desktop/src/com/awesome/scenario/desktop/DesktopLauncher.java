package com.awesome.scenario.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gui.DragAndDrop;
import com.gui.EvaluatorPhysicsRenderer;
import com.gui.SimplePhysicsRenderer;
import com.roomfurniture.InputParser;
import com.roomfurniture.box2d.DragAndDropPhysicsSimulator;
import com.roomfurniture.box2d.PhysicsSimulatorEvaluator;
import com.roomfurniture.box2d.SimplePhysicsSimulator;
import com.roomfurniture.problem.Problem;

public class DesktopLauncher {

    public static EvaluatorPhysicsRenderer renderer;
    public static LwjglApplication application;

    public static void main(String[] arg) {
//        optimizeThenRender(solution, problem);
//        PhysicsApproach.runMultiThreaded();
//        PhysicsApproach.runPhysicsSingleThreaded();

//        PhysicsApproach.runSpawnPointPicker();


        Problem problem = InputParser.getTestProblems().get(0);


        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        DragAndDrop renderer = new DragAndDrop(problem, new DragAndDropPhysicsSimulator(problem.getRoom()));

        config.width = 2000;
        config.height = 1000;

        DesktopLauncher.application = new LwjglApplication(renderer, config);
    }


}
