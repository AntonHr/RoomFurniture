package com.roomfurniture.box2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.roomfurniture.solution.Solution;

import java.util.ArrayList;
import java.util.List;

public class PhisicsSimulator {

    public void createWorld(Solution solution) {
        World world = new World(new Vector2(0, 0), true);

        List<BodyDef> bodyDefs = new ArrayList<>();


        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;


    }
}
