package com.roomfurniture.problem;

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
        } catch (NoSuchElementException ignored) {}
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

        //shapes


        List<Shape> shapes = new ArrayList<>();

        Optional<Shape> shape = parseShape(scanner);
        while(shape.isPresent()){
            shapes.add(shape.get());
            shape = parseShape(scanner);

            if (checkFor(scanner, ";")) break;
        }

        shape.ifPresent(shapes::add);

//        System.out.println(problemNumber + ": " + roomShape);
//        System.out.println(shapes);

        return Optional.of(new Problem(new Room(roomShape), shapes));
    }

    private List<Vertex> parseVertices(Scanner scanner, String pattern) {
        List<Vertex> roomShape = new ArrayList<>();

        Optional<Vertex> lastVertex = parseVertex(scanner);


        while (lastVertex.isPresent()) {
            roomShape.add(lastVertex.get());
            lastVertex = parseVertex(scanner);

            if(checkFor(scanner, pattern)) break;
        }
        lastVertex.ifPresent(roomShape::add);
        return roomShape;
    }

    private Optional<Shape> parseShape(Scanner scanner) {
        try {
            Pattern initialDelimiter = scanner.delimiter();
            scanner.useDelimiter("\\D");
            int cost = scanner.nextInt();
            scanner.useDelimiter(initialDelimiter);
            scanner.skip(":");

            List<Vertex> vertices = parseVertices(scanner, ";");

            return Optional.of(new Shape(cost, vertices));
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
