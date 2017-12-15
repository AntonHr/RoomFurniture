package com.roomfurniture;

import com.roomfurniture.ga.algorithm.RouletteWheelSelectionStrategy;
import com.roomfurniture.ga.algorithm.interfaces.CrossoverStrategy;
import com.roomfurniture.ga.algorithm.interfaces.EvaluationStrategy;
import com.roomfurniture.ga.algorithm.interfaces.GeneratorStrategy;
import com.roomfurniture.ga.algorithm.interfaces.MutationStrategy;
import com.roomfurniture.ga.algorithm.parallel.BasicParallelGeneticAlgorithm;
import com.roomfurniture.ga.algorithm.parallel.ParallelGeneticAlgorithmRunner;
import com.roomfurniture.problem.*;
import com.roomfurniture.solution.*;
import com.gui.SwingVisualizer;
import com.roomfurniture.solution.storage.SolutionDatabase;
import com.roomfurniture.solution.storage.SolutionList;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;


public class Main {

    public static void main(String[] args) throws FileNotFoundException {
//        List<Vertex> vsFurn = Arrays.asList(new Vertex(2.5, 4), new Vertex(1, 0), new Vertex(4, 0));
//        List<Vertex> vsRoom = Arrays.asList(new Vertex(0, 0), new Vertex(10, 0),
//                                            new Vertex(10, 10), new Vertex(0,10));
//
//        Furniture f = new Furniture(0, 0, vsFurn);
//        Room r = new Room(vsRoom);
//
//        Descriptor dF = new Descriptor(new Vertex(1, 0), 0);
//        Descriptor dK = new Descriptor(new Vertex(-1, 0), 0);
//
//        Shape shh = AffineTransform.getRotateInstance(Math.PI / 4).createTransformedShape(f.toShape());
//        Shape sh = AffineTransform.getRotateInstance(Math.PI / 4).createTransformedShape(shh);
//        Furniture f2 = new Furniture(1, 0, sh);
//
//        Solution s = new Solution(Arrays.asList(dF));
//        Problem p = new Problem(0, r, Arrays.asList(f2));
//
//        JFrame bestFrame = SwingVisualizer.constructVisualizationFrame(p, s);
//        bestFrame.setTitle("Best solution");
//        EventQueue.invokeLater(() -> {
//                bestFrame.setVisible(true);
//        });
        SolutionList.updatePermissions();
        InputParser inputParser = new InputParser();
        List<Problem> parse = inputParser.parse("problemsets.txt");
        try {
            FileWriter fileWriter = new FileWriter("./output.txt", false);
            fileWriter.write(SolutionDatabase.createTeamSolutionDatabase().generateOverallSolutionReportFor(parse));
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
