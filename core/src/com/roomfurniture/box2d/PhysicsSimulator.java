package com.roomfurniture.box2d;

import com.gui.RoomFurnitureRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.roomfurniture.ShapeCalculator;
import com.roomfurniture.problem.Furniture;
import com.roomfurniture.problem.Problem;
import com.roomfurniture.problem.Vertex;
import com.roomfurniture.solution.Solution;

import java.util.ArrayList;
import java.util.List;

public class PhysicsSimulator {

    public final World world;
    private final List<Body> bodies;
    private final List<Body> room;
    private boolean active = false;
    private Vector2 repelPoint;

    private List<Furniture> unusedItems;

    public PhysicsSimulator(Problem problem, Solution solution) {
        //world
        world = new World(new Vector2(0, 0), true);

        //items
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

            body.setUserData(item);
            bodies.add(body);
        });

        //room
        room = new ArrayList<>();

        Vertex previousCorner = null;
        for (Vertex currentCorner : problem.getRoom().getVerticies()) {
            if (previousCorner == null) {
                previousCorner = currentCorner;
                continue;
            }
            addRoomWall(previousCorner, currentCorner);
            previousCorner = currentCorner;
        }
        addRoomWall(previousCorner, problem.getRoom().getVerticies().get(0));
    }

    private void addRoomWall(Vertex corner1, Vertex corner2) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        //shape
        EdgeShape shape = new EdgeShape();
        shape.set((float) corner1.x, (float) corner1.y, (float) corner2.x, (float) corner2.y);

        //fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        Body body = world.createBody(bodyDef);
        Fixture fixture = body.createFixture(fixtureDef);

        // Shape is the only disposable of the lot, so get rid of it
        shape.dispose();
        room.add(body);
    }

    public void update(float deltaTime) {
        if (active) {
            for (Body body : bodies) {
                Furniture correspondingItem = (Furniture) body.getUserData();

                float magnitude = (float) (100 * ShapeCalculator.calculateAreaOf(correspondingItem.toShape()));
                Vector2 direction = repelPoint.cpy().sub(body.getLocalCenter()).nor();

                if (direction.isZero())
                    direction = Vector2.X.setToRandomDirection();

                body.applyForceToCenter(direction.scl(-magnitude), true);
            }
            world.step(deltaTime, 6, 2);
        }
    }

    public void letTheFunBegin() {
        active = true;
    }

    public void startSpawning(List<Furniture> unusedItems) {

    }

    public void setRepelPoint(Vector2 repelPoint) {
        this.repelPoint = repelPoint;
    }

    public Vector2 getRepelPoint() {
        return repelPoint;
    }
}
