package com.awesome.scenario.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gui.EvaluatorPhysicsRenderer;
import com.gui.SimplePhysicsRenderer;
import com.roomfurniture.box2d.PhysicsSimulatorEvaluator;
import com.roomfurniture.box2d.SimplePhysicsSimulator;

public class DesktopLauncher {

    public static EvaluatorPhysicsRenderer renderer;
    public static LwjglApplication application;

    public static void main(String[] arg) {
//        optimizeThenRender(solution, problem);
//        PhysicsApproach.runMultiThreaded();
        PhysicsApproach.runPhysicsSingleThreaded();

//        PhysicsApproach.runSpawnPointPicker();

//
//        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
//        SimplePhysicsRenderer renderer = new SimplePhysicsRenderer();
//
//
//        renderer.update(new SimplePhysicsSimulator());
//
//        config.width = 2000;
//        config.height = 1000;
//
//        DesktopLauncher.application = new LwjglApplication(renderer, config);
    }


}
