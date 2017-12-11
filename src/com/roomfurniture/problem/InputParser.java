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

    private void parseProblem(String problem) {
        Scanner scanner = new Scanner(problem);

        scanner.useDelimiter("\\D");
        int problemNumber = scanner.nextInt();
        scanner.useDelimiter(defaultDelimiter);
        scanner.skip(":");


        List<Vertex> roomShape = new ArrayList<>();

        Optional<Vertex> lastVertex = parseVertex(scanner);
        while (lastVertex.isPresent()) {
            roomShape.add(lastVertex.get());
            lastVertex = parseVertex(scanner);

            try {
                scanner.skip(";");
                break;
            } catch (NoSuchElementException e) {
            }
        }

        System.out.println(problemNumber + ": " + roomShape);
    }

    private Optional<Vertex> parseVertex(Scanner scanner) {
        Vertex vertex = null;
        try {
            scanner.findInLine("\\( *(-?\\d+(\\.\\d+)?) *, *(-?\\d+(\\.\\d+)?) *\\)");
            MatchResult result = scanner.match();
            double x = Double.parseDouble(result.group(1));
            double y = Double.parseDouble(result.group(3));

            vertex = new Vertex(x, y);
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }

        return Optional.of(vertex);
    }
}
