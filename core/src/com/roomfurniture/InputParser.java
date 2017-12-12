package com.roomfurniture;

import com.roomfurniture.problem.Furniture;
import com.roomfurniture.problem.Problem;
import com.roomfurniture.problem.Room;
import com.roomfurniture.problem.Vertex;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class InputParser {

    private Pattern defaultDelimiter = new Scanner("").delimiter();

    public List<Problem> parse(String filename) throws FileNotFoundException {
        File file = new File(filename);
        Scanner scanner = new Scanner(file);
        List<Problem> problems = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String problem = scanner.nextLine();
            Optional<Problem> parsedProblem = parseProblem(problem);
            parsedProblem.ifPresent(problems::add);
        }
        return problems;
    }

    private boolean checkFor(Scanner scanner, String pattern) {
        try {
            scanner.skip(pattern);
            return true;
        } catch (NoSuchElementException ignored) {
        }
        return false;
    }

    private Optional<Problem> parseProblem(String problem) {
        Scanner scanner = new Scanner(problem);


        //room
        scanner.useDelimiter("\\D");
        int problemNumber = scanner.nextInt();
        scanner.useDelimiter(defaultDelimiter);
        scanner.skip(":");

        List<Vertex> roomShape = parseVertices(scanner, " #");

        //furnitures


        List<Furniture> furnitures = new ArrayList<>();

        int id = 0;
        Optional<Furniture> shape = parseShape(scanner, id++);
        while (shape.isPresent()) {
            furnitures.add(shape.get());
            shape = parseShape(scanner, id++);

            if (checkFor(scanner, ";")) break;
        }

        shape.ifPresent(furnitures::add);

//        System.out.println(problemNumber + ": " + roomShape);
//        System.out.println(furnitures);

        return Optional.of(new Problem(new Room(roomShape), furnitures));
    }

    private List<Vertex> parseVertices(Scanner scanner, String pattern) {
        List<Vertex> roomShape = new ArrayList<>();

        Optional<Vertex> lastVertex = parseVertex(scanner);


        while (lastVertex.isPresent()) {
            roomShape.add(lastVertex.get());
            lastVertex = parseVertex(scanner);

            if (checkFor(scanner, pattern)) break;
        }
        lastVertex.ifPresent(roomShape::add);
        return roomShape;
    }

    private Optional<Furniture> parseShape(Scanner scanner, int id) {
        try {
            Pattern initialDelimiter = scanner.delimiter();
            scanner.useDelimiter("\\D");
            int cost = scanner.nextInt();
            scanner.useDelimiter(initialDelimiter);
            scanner.skip(":");

            List<Vertex> vertices = parseVertices(scanner, ";");

            return Optional.of(new Furniture(id, cost, vertices));
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }


    }

    private Optional<Vertex> parseVertex(Scanner scanner) {
        Vertex vertex;
        try {
            scanner.findInLine("\\( *(-?\\d+(\\.\\d+)?) *, *(-?\\d+(\\.\\d+)?) *\\)");
            MatchResult result = scanner.match();
            double x = Double.parseDouble(result.group(1));
            double y = Double.parseDouble(result.group(3));

            vertex = new Vertex(x, y);
        } catch (NoSuchElementException | IllegalStateException e) {
            return Optional.empty();
        }

        return Optional.of(vertex);
    }
}
