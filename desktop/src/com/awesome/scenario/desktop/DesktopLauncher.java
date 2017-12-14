package com.awesome.scenario.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.gui.EvaluatorPhysicsRenderer;

public class DesktopLauncher {

    public static EvaluatorPhysicsRenderer renderer;
    public static LwjglApplication application;

    public static void main(String[] arg) {
//        optimizeThenRender(solution, problem);
//        PhysicsApproach.runMultiThreaded();
        PhysicsApproach.runPhysicsSingleThreaded();

//        PhysicsApproach.runSpawnPointPicker();
    }


}
