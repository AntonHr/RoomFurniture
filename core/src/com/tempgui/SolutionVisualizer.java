package com.tempgui;

import com.google.common.collect.Streams;
import com.roomfurniture.InputParser;
import com.roomfurniture.problem.Furniture;
import com.roomfurniture.problem.Problem;
import com.roomfurniture.solution.Solution;
import com.roomfurniture.solution.SolutionGeneratorStrategy;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

public class SolutionVisualizer extends JPanel {
    public static final int HEIGHT = 720;
    public static final int WIDTH = 1280;
    private final Problem problem;
    private final Solution solution;
    private final AffineTransform rotateTransform = new AffineTransform();
    private final AffineTransform scaleTransform = AffineTransform.getScaleInstance(5,5);
    private final List<Furniture> items;

    public SolutionVisualizer(Problem problem, Solution solution) {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.problem = problem;
        this.solution = solution;
        items = Streams.zip(problem.getFurnitures().stream(), solution.getDescriptors().stream(), Furniture::transform).collect(Collectors.toList());
    }

    public void setScale(double value) {
        this.scaleTransform.setToScale(value, value);
        EventQueue.invokeLater(() -> {
            this.repaint();
        });
    }
    public void setRotation(double value) {
        rotateTransform.setToRotation(value);
        EventQueue.invokeLater(() -> {
            this.repaint();
        });

    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.transform(AffineTransform.getTranslateInstance(WIDTH/2, HEIGHT/2));
        g2.transform(rotateTransform);
        g2.transform(scaleTransform);

        g2.draw(problem.getRoom().toShape());

        for(Furniture furniture : items) {
            Color color = g2.getColor();

             double value = furniture.getScorePerUnitArea(); //this is your value between 0 and 1
             double minHue = 120f/255; //corresponds to green
             double maxHue = 0; //corresponds to red
             double hue = value*maxHue + (1-value)*minHue;
             Color c = new Color(Color.HSBtoRGB((float)hue, 1, 0.5f));

                     g2.setColor(c);

            g2.fill(furniture.toShape());
            g2.draw(furniture.toShape());
            g2.setColor(color);
        }

        g2.dispose();

    }

    public static void main(String[] args) throws FileNotFoundException {
        InputParser inputParser = new InputParser();
        List<Problem> problems = inputParser.parse("test.txt");
        JFrame frame = constructVisualizationFrame(problems.get(0), new SolutionGeneratorStrategy(problems.get(0)).generate());

        EventQueue.invokeLater(() -> {
            frame.setVisible(true);
        });

    }

    public static JFrame constructVisualizationFrame(Problem problem, Solution solution) throws FileNotFoundException {
        Furniture furniture = problem.getFurnitures().get(0);

        DefaultBoundedRangeModel scalingModel = new DefaultBoundedRangeModel(5,0,1,10);
        DefaultBoundedRangeModel rotationModel = new DefaultBoundedRangeModel(0, 0, 0, (int) (Math.PI * 1000));

        JFrame frame = new JFrame();
        SolutionVisualizer visualizer = new SolutionVisualizer(problem, solution);
        JSlider scaleSlider = new JSlider(SwingConstants.VERTICAL);
        JSlider rotationSlider= new JSlider(SwingConstants.VERTICAL);


        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(visualizer);
        scalingModel.addChangeListener(e -> {
            int value = scalingModel.getValue();
            visualizer.setScale(value);
        });

        rotationModel.addChangeListener(e -> {
            int value = rotationModel.getValue();

            visualizer.setRotation((double)value/1000.0);
        });
        scaleSlider.setModel(scalingModel);
        rotationSlider.setModel(rotationModel);

        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new GridLayout(1, 2));
        containerPanel.add(scaleSlider);
        containerPanel.add(rotationSlider);
        frame.add(containerPanel, BorderLayout.EAST);
        frame.pack();
        return frame;
    }
}
