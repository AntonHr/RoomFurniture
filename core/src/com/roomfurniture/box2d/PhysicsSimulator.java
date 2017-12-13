package com.roomfurniture.box2d;

import com.google.common.collect.Lists;
import com.gui.RoomFurnitureRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.roomfurniture.ShapeCalculator;
import com.roomfurniture.problem.Descriptor;
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
    private boolean spawning = false;
    private Vector2 repelPoint;
    private float spawningTime = 0.2f;//s
    private float sinceLastSpawn = 0;

    private List<Furniture> unusedItems;

    private Problem problem;

    private Body nextBodyToSpawn;
    private RoomFurnitureRenderer renderer;

    public PhysicsSimulator(Problem problem, Solution solution) {
        this.problem = problem;
        //world
        world = new World(new Vector2(0, 0), true);

        //items
        List<Furniture> itemsInTheRoom = solution.getItemsInTheRoom(problem);
        bodies = new ArrayList<>();
        itemsInTheRoom.forEach(item -> {
            bodies.add(createBody(item));
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

    private Body createBody(Furniture item) {
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
        return body;
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


    int iter = 0;

    public void update(float deltaTime) {

        if (iter >= 100)
            return;
        if (active) {
            if (repelPoint != null)
                if (spawning) {
                    if (!unusedItems.isEmpty() || nextBodyToSpawn != null) {
                        //sinceLastSpawn += deltaTime;

                        // if (sinceLastSpawn >= spawningTime)
                        Furniture item;
                        Body b;
                        if (nextBodyToSpawn != null) {
                            b = nextBodyToSpawn;
                            item = (Furniture) b.getUserData();
                        } else {
                            item = unusedItems.get(0);
                            //sinceLastSpawn = sinceLastSpawn % spawningTime;
                            unusedItems.remove(0);
                            b = createBody(item);
                            Vector2 bodyCenter = b.getLocalCenter();
                            b.setTransform(repelPoint.cpy().sub(bodyCenter.sub(b.getPosition())), b.getAngle());
                            nextBodyToSpawn = b;
                        }
                        b.setActive(false);

                        System.out.println("Check:");
                        if (!intersectsAnything(item.transform(new Descriptor(new Vertex(b.getPosition()), b.getAngle())), renderer.insideItems())) {
                            System.out.println("success");
                            b.setActive(true);
                            renderer.moveInsideRoom(item);
                            //updateItemInRenderer(b);
                            bodies.add(b);
                            nextBodyToSpawn = null;
                        } else {
                            System.out.println("fail");
                        }
                    }
                }

            if (repelPoint != null)
                for (Body body : bodies) {
                    Furniture correspondingItem = (Furniture) body.getUserData();

                    float magnitude = (float) (100 * ShapeCalculator.calculateAreaOf(correspondingItem.toShape()));
                    Vector2 direction = repelPoint.cpy().sub(body.getLocalCenter()).nor();

                    if (direction.isZero())
                        direction = Vector2.X.cpy().setToRandomDirection();

                    body.applyForceToCenter(direction.scl(-magnitude), true);
                }


            world.step(deltaTime, 6, 2);


            bodies.forEach(this::updateItemInRenderer);

        }
    }

    private void updateItemInRenderer(Body body) {
        Furniture item = (Furniture) body.getUserData();

        PolygonShape shape = (PolygonShape) body.getFixtureList().get(0).getShape();

        int n = shape.getVertexCount();
        List<Vertex> vertices = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Vector2 vertex = new Vector2();
            shape.getVertex(i, vertex);
            vertices.add(new Vertex(vertex));
        }

        renderer.updateItem(item, vertices, body.getPosition(), body.getAngle());
    }

    private boolean intersectsAnything(Furniture item, List<Furniture> otherItems) {
        for (Furniture otherItem : otherItems) {
            if (!item.equals(otherItem)
                    && (ShapeCalculator.intersect(item.toShape(), otherItem.toShape())
                    || ShapeCalculator.contains(item.toShape(), otherItem.toShape())
                    || ShapeCalculator.contains(otherItem.toShape(), item.toShape()))) {
                return true;
            }
        }

//        if (ShapeCalculator.intersect(item.toShape(), problem.getRoom().toShape())
//                || ShapeCalculator.contains(item.toShape(), problem.getRoom().toShape())
//                || !ShapeCalculator.contains(problem.getRoom().toShape(), item.toShape()) //if not inside the room - invalid
//                )
//
//        {
//            if (ShapeCalculator.intersect(item.toShape(), problem.getRoom().toShape()))
//                System.out.println("1: ");
//
//            if (ShapeCalculator.contains(item.toShape(), problem.getRoom().toShape()))
//                System.out.println("2: ");
//
//            if (!ShapeCalculator.contains(problem.getRoom().toShape(), item.toShape()))
//                System.out.println("3: ");
//            return true;
//        }

        return false;
    }

    public void letTheFunBegin() {
        active = true;
    }

    public void startSpawning(List<Furniture> unusedItems) {
        this.unusedItems = Lists.newCopyOnWriteArrayList(unusedItems);
        spawning = true;
    }

    public void setRepelPoint(Vector2 repelPoint) {
        this.repelPoint = repelPoint;
    }

    public Vector2 getRepelPoint() {
        return repelPoint;
    }

    public void setRenderer(RoomFurnitureRenderer renderer) {
        this.renderer = renderer;
    }

    public RoomFurnitureRenderer getRenderer() {
        return renderer;
    }
}
