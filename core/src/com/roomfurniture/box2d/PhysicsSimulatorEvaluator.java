package com.roomfurniture.box2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.gui.RoomFurnitureRenderer;
import com.roomfurniture.ShapeCalculator;
import com.roomfurniture.problem.*;
import com.roomfurniture.solution.Solution;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class PhysicsSimulatorEvaluator {

    public final World world;
    public final List<Body> bodies;
    private final List<Body> roomWalls;
    private final Room room;
    private final int initialTaskSize;
    private boolean active = false;
    private boolean spawning = true;
    private Vector2 repelPoint;
    //    private static float TRIAL_TIME = 0.0005f; //s
    private float TRIAL_TIME; //s
    private float timeSinceLast = 0.0f;


    private Body nextBodyToSpawn;


    public Queue<Furniture> itemsToSpawn;
    private Queue<Vertex> spawnPoints;
    private int itemsAdded = 0;
    private int itemsSkipped = 0;
    private long iterations = 0;
    private int softMaxIterations;
    private double successRatio;
    private double failureRatio;
    private int impulseForce;
    private int spawnForce;


    public PhysicsSimulatorEvaluator(Room room, Queue<Furniture> itemsToSpawn, Queue<Vertex> spawnPoints, int softMaxIterations, double successRatio, double failureRatio, float trial_time, int impulseForce, int spawnForce) {

        initialTaskSize = itemsToSpawn.size();
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
        this.softMaxIterations = softMaxIterations;
        this.successRatio = successRatio;
        this.failureRatio = failureRatio;
        TRIAL_TIME = trial_time;
        this.impulseForce = impulseForce;
        this.spawnForce = spawnForce;
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
        roomWalls.add(body);
    }


    private Body createBody(Furniture item) {
        //body def
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1f;

        if (RoomFurnitureRenderer.getPoints(item.toShape()).length < 8 && ShapeCalculator.isConvex(item.toShape())) {
            //shape
            PolygonShape shape = new PolygonShape();
            //shape.setRadius(0.01f);
            shape.set(RoomFurnitureRenderer.getPoints(item.toShape()));

            fixtureDef.shape = shape;
            Fixture fixture = body.createFixture(fixtureDef);
            //fixture
        } else {
            // TODO: Alex have a look at this
            Box2DSeparator.separate(body, fixtureDef, item.getVertices().stream().map(Vertex::toVector2).collect(Collectors.toList()), 300.0f);
        }
        body.setUserData(item);
        return body;
    }

    //
//
//    int iter = 0;
//
    public void update(float deltaTime) {
        iterations++;


        //apply forces
        if (repelPoint != null)
            for (Body body : bodies) {
                Furniture correspondingItem = (Furniture) body.getUserData();

                float magnitude = (float) (spawnForce * ShapeCalculator.calculateAreaOf(correspondingItem.toShape()));
                Vector2 direction = repelPoint.cpy().sub(getBodyCenterPosition(body)).nor();

                if (direction.isZero())
                    direction = Vector2.X.cpy().setToRandomDirection();

                body.applyForceToCenter(direction.scl(-magnitude), true);
            }

        if (spawning) {
            if (!itemsToSpawn.isEmpty() || nextBodyToSpawn != null) {
                timeSinceLast += deltaTime;

                Body b;
                Furniture item;
                if (nextBodyToSpawn != null) {
                    b = nextBodyToSpawn;
                    item = (Furniture) b.getUserData();
                } else {
                    item = itemsToSpawn.poll();
                    Vertex spawnPoint = spawnPoints.poll();

                    b = createBody(item);

                    nextBodyToSpawn = b;
                    // TODO: Extract variable here?
                    moveToRepelPoint(b, spawnPoint.toVector2());
                    repelPoint = spawnPoint.toVector2();
                }

                b.setActive(false);


                for (int i = 0; i < 360; i++) {


                    boolean fitsIntoTheRoom = fitsIntoTheRoom(b);
                    boolean doesntIntersectAnything = !intersectsAnything(b);
                    if (doesntIntersectAnything && fitsIntoTheRoom) {
                        itemsAdded++;
                        b.setActive(true);
                        bodies.add(b);
                        nextBodyToSpawn = null;
                        timeSinceLast = 0;


                        //set random implulse
                        float magnitude = (float) (impulseForce * ShapeCalculator.calculateAreaOf(item.toShape()));
                        Vector2 direction = new Vector2().setToRandomDirection();
                        b.applyLinearImpulse(direction.scl(-magnitude), getBodyCenterPosition(b), true);

                        break;
                    } else {
                        // TODO: Undo this change
                        // Maintain position
                        if (!fitsIntoTheRoom) {

                            Vector2 localCenter = b.getLocalCenter();
                            Vector2 center1 = new Vector2(b.getWorldPoint(localCenter));

                            b.setTransform(b.getPosition(), (float) (b.getAngle() + i * Math.PI / 180));


                            Vector2 center2 = new Vector2(b.getWorldPoint(localCenter));

                            b.setTransform(b.getPosition().sub(center2.sub(center1)), b.getAngle());
                        }


                        if (timeSinceLast > TRIAL_TIME) {
                            timeSinceLast = timeSinceLast % TRIAL_TIME;
                            skipCurrentItem(b);
                            break;
                        }
                    }
                }


            } else {
                repelPoint = null;
            }
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

    public boolean isDone() {
        boolean addedEnough = itemsAdded > initialTaskSize * successRatio && iterations > softMaxIterations;
        boolean skippedTooMany = itemsSkipped > initialTaskSize * failureRatio && iterations > softMaxIterations;
//        if(skippedTooMany ) {
//            System.out.println("Skipping due to early termination skipped " + itemsSkipped + "/ " + initialTaskSize + ", added " + itemsAdded + ", iterations " + iterations);
//        }

        return (addedEnough || skippedTooMany || nextBodyToSpawn == null && itemsToSpawn.isEmpty());
    }

    private void skipCurrentItem(Body b) {
        itemsSkipped++;
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

    public Solution getSolution(Problem problem) {
//        if (bodies.stream().filter(body -> !body.isActive()).count() > 1) {
//            throw new RuntimeException("more than 1 inactive item, something is wrong");
//        }


        List<Optional<Descriptor>> descriptors = new ArrayList<>();
        for (int i = 0; i < problem.getFurnitures().size(); i++) {
            descriptors.add(Optional.empty());
        }

        bodies.stream()
                .filter(Body::isActive)
                .forEach(body -> {
                    Furniture item = (Furniture) body.getUserData();
                    int ind = item.findMeInInitialArray(problem.getFurnitures());

                    descriptors.set(ind, Optional.of(new Descriptor(new Vertex(body.getPosition()), body.getAngle())));
                });

        return new Solution(descriptors.stream()
                .map(descriptorOpt -> descriptorOpt.orElse(new Descriptor(new Vertex(-1000, -1000), 0)))
                .collect(Collectors.toList()));
    }

    public List<Furniture> getTransformedItems() {
        List<Furniture> transformedItems = new ArrayList<>();

//        if (bodies.stream().filter(body -> !body.isActive()).count() > 1) {
//            throw new RuntimeException("more than 1 inactive item, something is wrong");
//        }

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
