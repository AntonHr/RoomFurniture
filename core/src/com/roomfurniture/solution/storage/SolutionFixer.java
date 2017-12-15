package com.roomfurniture.solution.storage;

import com.roomfurniture.InputParser;
import com.roomfurniture.angle.VertexExtensionUtility;
import com.roomfurniture.problem.Furniture;
import com.roomfurniture.problem.Problem;
import com.roomfurniture.problem.Vertex;
import com.roomfurniture.solution.Solution;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by asus on 15.12.2017 г..
 */
public class SolutionFixer {
    public List<Furniture> fixSolution(Problem problem, Solution solution, double epsilon) {
        List<Furniture> furnitures = solution.getItemsInTheRoom(problem);
        ListIterator<Furniture> furnitureListIterator = furnitures.listIterator();

        while (furnitureListIterator.hasNext()) {
            Furniture furniture = furnitureListIterator.next();

            List<Vertex> furnVertices = new ArrayList<>(furniture.getVertices());
            ListIterator<Vertex> furnVerticesIterator = furnVertices.listIterator();

            while (furnVerticesIterator.hasNext()) {
                Vertex furnVertex = furnVerticesIterator.next();

                for (Vertex roomVertex : problem.getRoom().getVerticies()) {
                    if (Math.abs(roomVertex.x - furnVertex.x) < epsilon &&
                            Math.abs(roomVertex.y - furnVertex.y) < epsilon) {
                        furnVerticesIterator.set(roomVertex);
                    }
                }
            }

            furnitureListIterator.set(new Furniture(furniture.getId(), furniture.getScorePerUnitArea(), furnVertices));
        }

        return furnitures;
    }

    public void fixSolution() throws FileNotFoundException {
        InputParser parser = new InputParser();
        List<Problem> problems = parser.parse("problemsets.txt");

        SolutionDatabase db = SolutionDatabase.createPersonalSolutionDatabase();

        for (int i = 1; i <= 30; i++) {
            Problem problem = problems.get(i);
            List<Solution> solutions = db.getAllSolutionsFor(i);

            for (Solution solution : solutions) {
                List<Furniture> modifiedFurnitures = fixSolution(problem, solution, 0.0000001);
                String output = outputSolution(problem, modifiedFurnitures);
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        int count = 0;
        List<Problem> allProblems = new InputParser().parse("./problemsets.txt");
        SolutionDatabase personalSolutionDatabase = SolutionDatabase.createPersonalSolutionDatabase();
        for(Problem problem : allProblems) {
            List<Solution> allSolutionsFor = personalSolutionDatabase.getAllSolutionsFor(problem.getNumber());
            for(Solution solution : allSolutionsFor) {
                double coverage = solution.findCoverage(problem) * 100;
                Double score = solution.score(problem).orElse(0.0);
                System.out.println(coverage);
                if(coverage > 30) {
                    try {
                        String format = String.format("./recalculated/updated/%d-%.2f_solution_%.2f%%_correct.txt",  problem.getNumber(), score, coverage);
                        FileWriter fileWriter = new FileWriter(format);

                        SolutionFixer fixer = new SolutionFixer();
                        List<Furniture> newFurns = fixer.fixSolution(problem, solution, 0.0000001);
                        String modifiedResult = fixer.outputSolution(problem, newFurns);
                        fileWriter.write(modifiedResult);

                        fileWriter.flush();
                        fileWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public String outputSolution(Problem problem, List<Furniture> itemsInTheRoom) {
        StringBuilder sb = new StringBuilder();
        sb.append(problem.getNumber() + ": ");

        for(int j = 0; j< itemsInTheRoom.size(); j++) {
            Furniture furniture = itemsInTheRoom.get(j);
            List<Vertex> vertices = furniture.getVertices();

            for(int i = 0; i< vertices.size();i++) {
                Vertex vertex = vertices.get(i);
                sb.append("(" + vertex.x + ", " + vertex.y + ")");
                if(i < vertices.size() -1) {
                    sb.append(", ");
                } else if(j<itemsInTheRoom.size() - 1) {
                    sb.append(";");
                }
            }

            if(j < itemsInTheRoom.size()-1) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }
}
