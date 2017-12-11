package com.roomfurniture.problem;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class InputParser {

    public void parse(String filename) throws FileNotFoundException {
        File file = new File(filename);

        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine())
        {
            
        }
    }
}
