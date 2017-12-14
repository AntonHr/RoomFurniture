package com.gui;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.roomfurniture.ShapeCalculator;
import com.roomfurniture.box2d.PhysicsSimulatorEvaluator;
import com.roomfurniture.problem.Descriptor;
import com.roomfurniture.problem.Furniture;
import com.roomfurniture.problem.Problem;
import com.roomfurniture.problem.Vertex;
import com.roomfurniture.solution.Solution;

import java.awt.*;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class SetSpawnRenderer extends ApplicationAdapter implements InputProcessor {


    static final int WIDTH = 100;
    static final int HEIGHT = 100;


    private SpriteBatch batch;

    private BitmapFont font;

    private MyShapeRenderer shapeRenderer;
    private OrthographicCamera cam;

    private PhysicsSimulatorEvaluator physicsSimulator;

    private int renderType = 0;

    private List<Furniture> notIncludedItems;

    private Problem problem;


    private List<Vertex> spawnPoints = new ArrayList<>();

    {
        spawnPoints.add(new Vertex(0, 0));
    }

    public SetSpawnRenderer(Problem problem) {
        this.problem = problem;
    }

    @Override
    public void create() {
        Gdx.input.setInputProcessor(this);
        //constructObjects();

        font = new BitmapFont();
        font.setColor(Color.BLACK);
        batch = new SpriteBatch();

        shapeRenderer = new MyShapeRenderer();

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();


        cam = new OrthographicCamera(WIDTH, HEIGHT * (h / w));

        cam.zoom = 1f;
        cam.update();
    }


    @Override
    public void render() {
        if (renderType != 3)
            Gdx.gl.glClearColor(1, 1, 1, 1);
        else
            Gdx.gl.glClearColor(0, 0, 0, 1);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        handleInput();
        cam.update();

        shapeRenderer.setProjectionMatrix(cam.combined);

        //colours
        double maxValue = 0;
        for (Furniture item : problem.getFurnitures()) {
            maxValue = Math.max(maxValue, item.getScorePerUnitArea());
        }


        renderRoom();
        //renderOutItems(maxValue);
        renderSpawnPoints();


        batch.begin();

        int y = 50;
        font.draw(batch, "zoomSpeed: " + zoomInc, 10, y += 20);
        font.draw(batch, "translateSpeed: " + translateInc, 10, y += 40);
        Vector3 screenPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0.0f);
        Vector3 worldPos = cam.unproject(screenPos);
        font.draw(batch, "position: " + (worldPos.x) + ", " + (worldPos.y), 10, y += 20);

        batch.end();
    }

    private void renderSpawnPoints() {
        int i = 0;
        for (Vertex vertex : spawnPoints) {
            renderRepelPoint(vertex.toVector2(), i == spawnPoints.size() - 1);
            i++;
        }
    }

    private void renderOutItems(double maxValue) {
        shapeRenderer.begin(MyShapeRenderer.ShapeType.Filled);
        for (Furniture item : notIncludedItems) {
            float[] points = getPoints(item.toShape());

            shapeRenderer.setColor(valueToColor((float) (item.getScorePerUnitArea() / maxValue)));
            shapeRenderer.polygon(points);
        }
        shapeRenderer.end();
    }


    private void renderRoom() {
        shapeRenderer.begin(MyShapeRenderer.ShapeType.Line);
        float[] roomPoints = getPoints(problem.getRoom().toShape());

        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.polygon(roomPoints);
        shapeRenderer.end();
    }

    private void renderRepelPoint(Vector2 point, boolean active) {


        shapeRenderer.begin(MyShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLUE);

        if (active) {
            shapeRenderer.line(point.x - cam.viewportWidth * cam.zoom, point.y,
                    point.x + cam.viewportWidth * cam.zoom, point.y);

            shapeRenderer.line(point.x, point.y - cam.viewportHeight * cam.zoom,
                    point.x, point.y + cam.viewportHeight * cam.zoom);
        } else {
            shapeRenderer.circle(point.x, point.y, 0.5f, 360);
        }

        shapeRenderer.end();
    }


    private float zoomInc = 1.05f;
    private float zoomUnit = 0.1f;

    private float translateInc = 10f;
    private float translateUnit = 1f;


    private float rotateInc = 0.5f;
    private float rotateUnit = 1f;


    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            cam.zoom += zoomInc;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            cam.zoom -= zoomInc;
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
            cam.rotate(-rotateInc, 0, 0, 1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            cam.rotate(rotateInc, 0, 0, 1);
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


    //0..1
    public static Color valueToColor(float alpha) {
        return new Color(Color.rgba8888(0, 1 - alpha, 0, 1));
    }

    //note, hue is on 0-360 scale.
    public static Color hueToSaturatedColor(float hue) {
        hue *= 360;
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

    @Override
    public boolean keyDown(int keycode) {

        if (Gdx.input.isKeyPressed(Input.Keys.G)) {
            renderType = (renderType + 1) % 4;
        }


        if (Gdx.input.isKeyPressed(Input.Keys.EQUALS)) {
            translateInc += translateUnit;
            zoomInc += zoomUnit;
            rotateInc += rotateUnit;

        }

        if (Gdx.input.isKeyPressed(Input.Keys.MINUS)) {
            translateInc -= translateUnit;
            zoomInc -= zoomUnit;
            rotateInc -= rotateUnit;

        }

        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            spawnPoints.add(new Vertex(0, 0));
        }

        if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            printSpawnPoint();
        }

        return true;
    }

    private void printSpawnPoint() {
        List<Vertex> list = new ArrayList<>(spawnPoints);
        list.remove(list.size() - 1);
        System.out.println("Spawn points: ");
//        System.out.println(list);

        for (Vertex vertex : list) {
            System.out.println("new Vertex(" + +vertex.x + ", " + vertex.y + ")" + (vertex == list.get(list.size() - 1) ? "" : ","));
        }
    }

    Vector2 getMousePosInWorld() {
        Vector3 unprojected = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        return new Vector2(unprojected.x, unprojected.y);
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        moveSpawnPoint(screenX, screenY);

        return true;
    }

    private void moveSpawnPoint(int screenX, int screenY) {
        if (!spawnPoints.isEmpty()) {
            Vertex activePoint = spawnPoints.get(spawnPoints.size() - 1);

            Vector3 unproject = cam.unproject(new Vector3(screenX, screenY, 0));

            activePoint.set(new Vertex(unproject.x, unproject.y));
        }
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        moveSpawnPoint(screenX, screenY);
        return true;
    }


    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }


    private void constructObjects() {
        //spread
        List<Furniture> notIncludedItems = new ArrayList<>();


        Comparator<Furniture> comparator = Comparator.comparing(item -> item.getScorePerUnitArea() * ShapeCalculator.calculateAreaOf(item.toShape()));
        notIncludedItems.sort(comparator);

        notIncludedItems.addAll(problem.getFurnitures());

        this.notIncludedItems = spread(notIncludedItems);
    }

    private List<Furniture> spread(List<Furniture> items) {
        Vector2 dir = new Vector2(1, 0);
        float step = 10;

        List<Furniture> spreadItems = new ArrayList<>();

        for (Furniture item : items) {

            Furniture currentItem = item;

            do {
                currentItem = currentItem.transform(new Descriptor(new Vertex(dir.cpy().scl(step)), 0));
            } while (intersectsAnything(currentItem, spreadItems));
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
}
