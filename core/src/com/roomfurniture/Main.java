package com.roomfurniture;

import com.roomfurniture.ga.algorithm.RouletteWheelSelectionStrategy;
import com.roomfurniture.ga.algorithm.interfaces.CrossoverStrategy;
import com.roomfurniture.ga.algorithm.interfaces.EvaluationStrategy;
import com.roomfurniture.ga.algorithm.interfaces.GeneratorStrategy;
import com.roomfurniture.ga.algorithm.interfaces.MutationStrategy;
import com.roomfurniture.ga.algorithm.parallel.BasicParallelGeneticAlgorithm;
import com.roomfurniture.ga.algorithm.parallel.ParallelGeneticAlgorithmRunner;
import com.roomfurniture.problem.Descriptor;
import com.roomfurniture.problem.Furniture;
import com.roomfurniture.problem.Problem;
import com.roomfurniture.problem.Vertex;
import com.roomfurniture.solution.*;
import com.gui.SwingVisualizer;
import com.roomfurniture.solution.storage.SolutionDatabase;
import com.roomfurniture.solution.storage.SolutionList;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Main {

    public static void main(String[] args) throws FileNotFoundException {
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
