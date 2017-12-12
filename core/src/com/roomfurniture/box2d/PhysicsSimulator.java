package com.roomfurniture.box2d;

import com.gui.RoomFurnitureRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.roomfurniture.problem.Furniture;
import com.roomfurniture.problem.Problem;
import com.roomfurniture.solution.Solution;

import java.util.ArrayList;
import java.util.List;

public class PhysicsSimulator {

    public final World world;
    private final List<Body> bodies;

    public PhysicsSimulator(Problem problem, Solution solution) {
        world = new World(new Vector2(0, 0), true);


        List<Furniture> itemsInTheRoom = solution.getItemsInTheRoom(problem);

        bodies = new ArrayList<>();

        itemsInTheRoom.forEach(item -> {
            //body def
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.DynamicBody;

            //shape
            PolygonShape shape = new PolygonShape();
            shape.set(RoomFurnitureRenderer.getPoints(item.toShape()));

            //fixture
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = 1f;
            Body body = world.createBody(bodyDef);
            Fixture fixture = body.createFixture(fixtureDef);

            // Shape is the only disposable of the lot, so get rid of it
            shape.dispose();

            bodies.add(body);
        });

    }

    public void update(float deltaTime) {
        world.step(deltaTime, 6, 2);

    }

    public void letTheFunBegin() {
        bodies.get(0).applyForceToCenter(10, 0,true);
    }
}
