package com.roomfurniture.box2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.gui.RoomFurnitureRenderer;
import com.roomfurniture.ShapeCalculator;
import com.roomfurniture.problem.Descriptor;
import com.roomfurniture.problem.Furniture;
import com.roomfurniture.problem.Room;
import com.roomfurniture.problem.Vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class PhysicsSimulatorEvaluator {

    public final World world;
    public final List<Body> bodies;
    private final List<Body> roomWalls;
    private final Room room;
    private boolean active = false;
    private boolean spawning = true;
    private Vector2 repelPoint;
    private static float TRIAL_TIME = 0.1f; //s
    private float timeSinceLast = 0.0f;


    private Body nextBodyToSpawn;


    public Queue<Furniture> itemsToSpawn;
    private Queue<Vertex> spawnPoints;


    public PhysicsSimulatorEvaluator(Room room, Queue<Furniture> itemsToSpawn, Queue<Vertex> spawnPoints) {

        this.itemsToSpawn = itemsToSpawn;
        this.spawnPoints = spawnPoints;

        //world
        world = new World(new Vector2(0, 0), true);
        bodies = new ArrayList<>();

//        //items
//        List<Furniture> itemsInTheRoom = solution.getItemsInTheRoom(problem);
//        bodies = new ArrayList<>();
//        itemsInTheRoom.forEach(item -> {
//            bodies.add(createBody(item));
//        });

        //room
        this.room = room;
        roomWalls = new ArrayList<>();

        Vertex previousCorner = null;
        for (Vertex currentCorner : room.getVerticies()) {
            if (previousCorner == null) {
                previousCorner = currentCorner;
                continue;
            }
            addRoomWall(previousCorner, currentCorner);
            previousCorner = currentCorner;
        }
        addRoomWall(previousCorner, room.getVerticies().get(0));
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
        roomWalls.add(body);
    }


    private Body createBody(Furniture item) {
        //body def
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        //shape
        PolygonShape shape = new PolygonShape();
        //shape.setRadius(0.01f);
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

    //
//
//    int iter = 0;
//
    public void update(float deltaTime) {

        if (spawning) {
            if (!itemsToSpawn.isEmpty() || nextBodyToSpawn != null) {
                timeSinceLast += deltaTime;

                Body b;
                if (nextBodyToSpawn != null) {
                    b = nextBodyToSpawn;
                } else {
                    Furniture item = itemsToSpawn.poll();
                    Vertex spawnPoint = spawnPoints.poll();

                    b = createBody(item);

                    nextBodyToSpawn = b;
                    moveToRepelPoint(b, spawnPoint.toVector2());
                    repelPoint = spawnPoint.toVector2();
                }

                b.setActive(false);

                if (fitsIntoTheRoom(b)) {
                    if (!intersectsAnything(b)) {
                        b.setActive(true);
                        bodies.add(b);
                        nextBodyToSpawn = null;
                        timeSinceLast=0;
                    } else {
                        if (timeSinceLast > TRIAL_TIME) {
                            timeSinceLast = timeSinceLast % TRIAL_TIME;
                            skipCurrentItem(b);
                        }
                    }
                } else {
                    //skip this object
                    //cause it doesn't fit in the room at this position

                    skipCurrentItem(b);
                }


            } else {
                repelPoint = null;
            }
        }

        if (repelPoint != null)
            for (Body body : bodies) {
                Furniture correspondingItem = (Furniture) body.getUserData();

                float magnitude = (float) (100 * ShapeCalculator.calculateAreaOf(correspondingItem.toShape()));
                Vector2 direction = repelPoint.cpy().sub(getBodyCenterPosition(body)).nor();

                if (direction.isZero())
                    direction = Vector2.X.cpy().setToRandomDirection();

                body.applyForceToCenter(direction.scl(-magnitude), true);
            }


        world.step(deltaTime, 6, 2);

//        if (active) {
//
//            while (!toDo.isEmpty())
//                toDo.poll().run();
//
//            if (repelPoint != null)
//                if (spawning) {
//                    if (!unusedItems.isEmpty() || nextBodyToSpawn != null) {
//                        //sinceLastSpawn += deltaTime;
//
//                        // if (sinceLastSpawn >= spawningTime)
//                        Furniture item;
//                        Body b;
//                        if (nextBodyToSpawn != null) {
//                            b = nextBodyToSpawn;
//                            item = (Furniture) b.getUserData();
//                        } else {
//                            item = unusedItems.get(0);
//                            //sinceLastSpawn = sinceLastSpawn % spawningTime;
//                            unusedItems.remove(0);
//
//                            b = createBody(item);
//
//                            nextBodyToSpawn = b;
//                            moveToRepelPoint(b);
//                        }
//                        b.setActive(false);
//
//                        System.out.println("Check:");
//                        if (!intersectsAnything(item.transform(new Descriptor(new Vertex(b.getPosition()), b.getAngle())), renderer.insideItems())) {
//                            System.out.println("success");
//                            b.setActive(true);
//                            renderer.moveInsideRoom(item);
//                            //updateItemInRenderer(b);
//                            bodies.add(b);
//                            nextBodyToSpawn = null;
//                        } else {
//                            System.out.println("fail");
//                        }
//                    }
//                }
//
//            if (repelPoint != null)
//                for (Body body : bodies) {
//                    Furniture correspondingItem = (Furniture) body.getUserData();
//
//                    float magnitude = (float) (100 * ShapeCalculator.calculateAreaOf(correspondingItem.toShape()));
//                    Vector2 direction = repelPoint.cpy().sub(body.getLocalCenter()).nor();
//
//                    if (direction.isZero())
//                        direction = Vector2.X.cpy().setToRandomDirection();
//
//                    body.applyForceToCenter(direction.scl(-magnitude), true);
//                }
//
//
//            world.step(deltaTime, 6, 2);
//
//
//            bodies.forEach(this::updateItemInRenderer);
//
//        }
    }

    public boolean isDone()
    {
        return nextBodyToSpawn==null && itemsToSpawn.isEmpty();
    }

    private void skipCurrentItem(Body b) {
        world.destroyBody(b);
        nextBodyToSpawn = null;
    }

    //TODO: fix this in the other simulator
    private Vector2 getBodyCenterPosition(Body body) {
        return body.getLocalCenter().cpy().add(body.getPosition());
    }

    private void moveToRepelPoint(Body body, Vector2 point) {
        Vector2 bodyCenter = getBodyCenterPosition(body);
        body.setTransform(point.cpy().sub(bodyCenter.cpy().sub(body.getPosition())), body.getAngle());
    }

    public Vector2 getRepelPoint() {
        return repelPoint;
    }

    //    private void updateItemInRenderer(Body body) {
//        Furniture item = (Furniture) body.getUserData();
//
//        PolygonShape shape = (PolygonShape) body.getFixtureList().get(0).getShape();
//
//        int n = shape.getVertexCount();
//        List<Vertex> vertices = new ArrayList<>();
//        for (int i = 0; i < n; i++) {
//            Vector2 vertex = new Vector2();
//            shape.getVertex(i, vertex);
//            vertices.add(new Vertex(vertex));
//        }
//
//        renderer.updateItem(item, vertices, body.getPosition(), body.getAngle());
//    }
//


    private boolean fitsIntoTheRoom(Body body) {
        Furniture item = (Furniture) body.getUserData();
        item = item.transform(new Descriptor(new Vertex(body.getPosition()), body.getAngle()));

        if (!ShapeCalculator.contains(room.toShape(), item.toShape())) {
            return false;
        }
        return true;
    }

    private boolean intersectsAnything(Body body) {
        Furniture item = (Furniture) body.getUserData();
        item = item.transform(new Descriptor(new Vertex(body.getPosition()), body.getAngle()));

        for (Body otherBody : bodies) {
            if (!body.equals(otherBody)) {
                Furniture otherItem = (Furniture) body.getUserData();
                otherItem = otherItem.transform(new Descriptor(new Vertex(otherBody.getPosition()), otherBody.getAngle()));

                if ((ShapeCalculator.intersect(item.toShape(), otherItem.toShape())
                        || ShapeCalculator.contains(item.toShape(), otherItem.toShape())
                        || ShapeCalculator.contains(otherItem.toShape(), item.toShape()))) {
                    return true;
                }
            }
        }


        return false;
    }

    public List<Furniture> getTransformedItems() {
        List<Furniture> transformedItems = new ArrayList<>();

        if (bodies.stream().filter(body -> !body.isActive()).count() > 1) {
            throw new RuntimeException("more than 1 inactive item, something is wrong");
        }

        bodies.stream()
                .filter(Body::isActive)
                .forEach(body -> {
                    Furniture item = (Furniture) body.getUserData();
                    transformedItems.add(item.transform(new Descriptor(new Vertex(body.getPosition()), body.getAngle())));
                });

        return transformedItems;
    }
//
//    public void letTheFunBegin() {
//        active = true;
//    }
//
//    public void startSpawning(List<Furniture> unusedItems) {
//        this.unusedItems = Lists.newCopyOnWriteArrayList(unusedItems);
//        spawning = true;
//    }
//

//
//    public void setRenderer(RoomFurnitureRenderer renderer) {
//        this.renderer = renderer;
//    }
//
//    public RoomFurnitureRenderer getRenderer() {
//        return renderer;
//    }
}
