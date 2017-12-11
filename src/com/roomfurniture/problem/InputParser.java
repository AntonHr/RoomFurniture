package com.roomfurniture.problem;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class InputParser {

    private Pattern defaultDelimiter = new Scanner("").delimiter();

    public void parse(String filename) throws FileNotFoundException {
        File file = new File(filename);
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String problem = scanner.nextLine();
            parseProblem(problem);
        }
    }

    private boolean checkFor(Scanner scanner, String pattern) {
        try {
            scanner.skip(pattern);
            return true;
        } catch (NoSuchElementException ignored) {}
        return false;
    }

    private void parseProblem(String problem) {
        Scanner scanner = new Scanner(problem);


        //room
        scanner.useDelimiter("\\D");
        int problemNumber = scanner.nextInt();
        scanner.useDelimiter(defaultDelimiter);
        scanner.skip(":");

        List<Vertex> roomShape = new ArrayList<>();

        Optional<Vertex> lastVertex = parseVertex(scanner);
        roomShape.add(lastVertex.get());

        while (lastVertex.isPresent()) {
            lastVertex = parseVertex(scanner);
            roomShape.add(lastVertex.get());

            if(checkFor(scanner, " #")) break;
        }

        //shapes


        List<Shape> shapes = new ArrayList<>();

        Optional<Shape> shape = parseShape(scanner);
        while(shape.isPresent()){
            shapes.add(shape.get());
            shape = parseShape(scanner);

            if (checkFor(scanner, ";")) break;
        }

        System.out.println(problemNumber + ": " + roomShape);
        System.out.println(shapes);
    }

    private Optional<Shape> parseShape(Scanner scanner) {
        Pattern initialDelimiter = scanner.delimiter();

        scanner.useDelimiter("\\D");
        int cost = scanner.nextInt();
        scanner.useDelimiter(initialDelimiter);
        scanner.skip(":");

        Vertex vertex = parseVertex(scanner);

        return Optional.empty();

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
