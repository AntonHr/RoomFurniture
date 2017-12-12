package com.awesome.scenario;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.google.common.collect.Streams;
import com.roomfurniture.ShapeCalculator;
import com.roomfurniture.problem.Descriptor;
import com.roomfurniture.problem.Furniture;
import com.roomfurniture.problem.Problem;
import com.roomfurniture.problem.Vertex;
import com.roomfurniture.solution.Solution;

import java.awt.*;
import java.awt.geom.PathIterator;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class RoomFurnitureRenderer extends ApplicationAdapter {


    static final int WIDTH = 100;
    static final int HEIGHT = 100;


    private SpriteBatch batch;
    //private Texture img;
    private MyShapeRenderer shapeRenderer;
    private OrthographicCamera cam;


    private float rotationSpeed = 0.5f;

    private final Problem problem;
    private final Solution solution;

    private final List<Furniture> items;
    private List<Furniture> furnitureInRoom;
    private List<Furniture> notIncludedItems;
    private int renderType = 0;


    public RoomFurnitureRenderer(Problem problem, Solution solution) {
        this.problem = problem;
        this.solution = solution;
        items = Streams.zip(problem.getFurnitures().stream(), solution.getDescriptors().stream(), Furniture::transform).collect(Collectors.toList());
    }

    @Override
    public void create() {

        constructObjects();


        batch = new SpriteBatch();
        //img = new Texture("./core/assets/badlogic.jpg");
        shapeRenderer = new MyShapeRenderer();

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        // Constructs a new OrthographicCamera, using the given viewport width and height
        // Height is multiplied by aspect ratio.
        cam = new OrthographicCamera(WIDTH, HEIGHT * (h / w));

        //cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
        cam.zoom = 10f;
        cam.update();
    }

    private void constructObjects() {
        //solution

        Map<Boolean, List<Furniture>> result = items.stream().collect(Collectors.partitioningBy(furniture -> ShapeCalculator.contains(problem.getRoom().toShape(), furniture.toShape())));

        List<Furniture> furnitureInRoom = result.get(true);

        Iterator<Furniture> iterator = furnitureInRoom.iterator();

        while (iterator.hasNext()) {
            Furniture furniture = iterator.next();
            for (Furniture otherFurniture : furnitureInRoom) {
                if (otherFurniture != furniture)
                    if (ShapeCalculator.intersect(furniture.toShape(), otherFurniture.toShape())) {
                        // Keep furniture with highest score
                        if (otherFurniture.getScorePerUnitArea() * ShapeCalculator.calculateAreaOf(otherFurniture.toShape()) >= furniture.getScorePerUnitArea() * ShapeCalculator.calculateAreaOf(furniture.toShape())) {
                            iterator.remove();
                            break;
                        }
                    }
            }
        }

        this.furnitureInRoom = furnitureInRoom;


        //spread

        List<Furniture> notIncludedItems = new ArrayList<>();
        notIncludedItems.addAll(items);


        for (Furniture furniture : furnitureInRoom) {
            int ind = notIncludedItems.indexOf(furniture);
            if (ind != -1)
                notIncludedItems.remove(ind);
        }

        this.notIncludedItems = spread(notIncludedItems);
    }

    private void renderOutItems(double maxValue) {
        //items
        shapeRenderer.begin(MyShapeRenderer.ShapeType.Filled);
        for (Furniture item : notIncludedItems) {
            float[] points = getPoints(item.toShape());

            shapeRenderer.setColor(hueToSaturatedColor((float) (item.getScorePerUnitArea() / maxValue * 360)));
            shapeRenderer.polygon(points);
        }
        shapeRenderer.end();
    }


    private void renderInItems(double maxValue) {
        //solution
        shapeRenderer.begin(MyShapeRenderer.ShapeType.Filled);
        for (Furniture item : furnitureInRoom) {
            float[] points = getPoints(item.toShape());


            shapeRenderer.setColor(hueToSaturatedColor((float) (item.getScorePerUnitArea() / maxValue * 360)));
            shapeRenderer.polygon(points);
        }
        shapeRenderer.end();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        handleInput();
        cam.update();

        shapeRenderer.setProjectionMatrix(cam.combined);


        //room
        shapeRenderer.begin(MyShapeRenderer.ShapeType.Line);
        float[] roomPoints = getPoints(problem.getRoom().toShape());

        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.polygon(roomPoints);
        shapeRenderer.end();


        //colours
        double maxValue = 0;
        for (Furniture item : problem.getFurnitures()) {
            maxValue = Math.max(maxValue, item.getScorePerUnitArea());
        }


        switch (renderType) {
            case 0:
                renderInItems(maxValue);
                renderOutItems(maxValue);
                break;
            case 1:
                renderOutItems(maxValue);
                break;
            case 2:
                renderInItems(maxValue);
                break;
        }


    }

    private List<Furniture> spread(List<Furniture> items) {
        Vector2 dir = new Vector2(1, 0);
        float step = 10;

        List<Furniture> spreadItems = new ArrayList<>();

        for (Furniture item : items) {

            Furniture currentItem = item;

            while (intersectsAnything(currentItem, spreadItems)) {
                currentItem = currentItem.transform(new Descriptor(new Vertex(dir.cpy().scl(step)), 0));
            }
            spreadItems.add(currentItem);
            dir.rotate(14);
        }

        return spreadItems;
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

        if (ShapeCalculator.intersect(item.toShape(), problem.getRoom().toShape())
                || ShapeCalculator.contains(item.toShape(), problem.getRoom().toShape())
                || ShapeCalculator.contains(problem.getRoom().toShape(), item.toShape()))
            return true;

        return false;
    }

    private void handleInput() {

        float zoomInc = 1.05f;
        float translateInc = 10f;
        float rotateInc = 1;

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            cam.zoom *= zoomInc;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            cam.zoom /= zoomInc;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            cam.translate(-translateInc, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            cam.translate(translateInc, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            cam.translate(0, -translateInc, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            cam.translate(0, translateInc, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            cam.rotate(-rotationSpeed, 0, 0, rotateInc);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            cam.rotate(rotationSpeed, 0, 0, rotateInc);
        }


        if (Gdx.input.isKeyPressed(Input.Keys.G)) {
            renderType = (renderType + 1) % 3;
        }

        cam.zoom = Math.max(cam.zoom, 0.1f);
        //cam.zoom = MathUtils.clamp(cam.zoom, 0.1f, 100 / cam.viewportWidth);

//        float effectiveViewportWidth = cam.viewportWidth * cam.zoom;
//        float effectiveViewportHeight = cam.viewportHeight * cam.zoom;
//
//        cam.position.x = MathUtils.clamp(cam.position.x, effectiveViewportWidth / 2f, 100 - effectiveViewportWidth / 2f);
//        cam.position.y = MathUtils.clamp(cam.position.y, effectiveViewportHeight / 2f, 100 - effectiveViewportHeight / 2f);
    }

//    public void resize(int width, int height) {
//        cam.viewportWidth = width;
//        cam.viewportHeight = HEIGHT * (width / height);
//
//        cam.update();
//    }


    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        //img.dispose();
    }


    public static float[] getPoints(Shape path) {
        List<double[]> pointList = new ArrayList<double[]>();
        double[] coords = new double[6];
        int numSubPaths = 0;
        for (PathIterator pi = path.getPathIterator(null);
             !pi.isDone();
             pi.next()) {
            switch (pi.currentSegment(coords)) {
                case PathIterator.SEG_MOVETO:
                    pointList.add(Arrays.copyOf(coords, 2));
                    ++numSubPaths;
                    break;
                case PathIterator.SEG_LINETO:
                    pointList.add(Arrays.copyOf(coords, 2));
                    break;
                case PathIterator.SEG_CLOSE:
                    if (numSubPaths > 1) {
                        throw new IllegalArgumentException("Path contains multiple subpaths");
                    }
                    double[][] points = pointList.toArray(new double[pointList.size()][]);


                    float[] finalReturn = new float[points.length * 2];

                    for (int i = 0; i < pointList.size(); i++) {
                        finalReturn[i * 2] = (float) points[i][0];
                        finalReturn[i * 2 + 1] = (float) points[i][1];
                    }
                    return finalReturn;

                default:
                    throw new IllegalArgumentException("Path contains curves");
            }
        }
        throw new IllegalArgumentException("Unclosed path");
    }


    //note, hue is on 0-360 scale.
    public static Color hueToSaturatedColor(float hue) {
        float r, g, b;
        float Hprime = hue / 60;
        float X = 1 - Math.abs(Hprime % 2 - 1);
        if (Hprime < 1) {
            r = 1;
            g = X;
            b = 0;
        } else if (Hprime < 2) {
            r = X;
            g = 1;
            b = 0;
        } else if (Hprime < 3) {
            r = 0;
            g = 1;
            b = X;
        } else if (Hprime < 4) {
            r = 0;
            g = X;
            b = 1;
        } else if (Hprime < 5) {
            r = X;
            g = 0;
            b = 1;
        } else if (Hprime < 6) {
            r = 1;
            g = 0;
            b = X;
        } else {
            r = 0;
            g = 0;
            b = 0;
        }

        return new Color(r, g, b, 1);
    }
}
