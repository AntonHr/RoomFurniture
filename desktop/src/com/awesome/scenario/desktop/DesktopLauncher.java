package com.awesome.scenario.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.gui.EvaluatorPhysicsRenderer;
import com.roomfurniture.ShapeCalculator;
import com.roomfurniture.angle.*;
import com.roomfurniture.problem.Descriptor;
import com.roomfurniture.problem.Furniture;
import com.roomfurniture.problem.Problem;
import com.roomfurniture.problem.Vertex;
import com.roomfurniture.solution.Solution;

import java.util.*;

public class DesktopLauncher {

    public static EvaluatorPhysicsRenderer renderer;
    public static LwjglApplication application;

    public static void main(String[] arg) {
//        optimizeThenRender(solution, problem);
//        PhysicsApproach.runMultiThreaded();
//        PhysicsApproach.runPhysicsSingleThreaded();

//        DragAndDrop renderer = new DragAndDrop(problem, new DragAndDropPhysicsSimulator(problem.getRoom()));
        PhysicsApproach.runSpawnPointPicker();


//        AlgorithmApproach.runAlgorithmicSolver();
    }


}
