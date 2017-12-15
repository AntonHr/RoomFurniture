package com.roomfurniture.solution.storage;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.roomfurniture.solution.Solution;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class SolutionList {

    public static final String SCENARIOWEEK_2_SOLUTIONS = "scenarioweek2solutions";

    private static class SolutionEntry {
        double coverage;
        double score;
        Solution solution;

        public SolutionEntry(double coverage, double score, Solution solution) {
            this.coverage = coverage;
            this.score = score;
            this.solution = solution;
        }

        @Override
        public String toString() {
            return "SolutionEntry{" +
                    "coverage=" + coverage +
                    ", score=" + score +
                    ", solution=" + solution +
                    '}';
        }

        public String toSerialized(){
            return coverage + " " + " " + score + " " + solution.toSerialized();
        }

        public static Optional<SolutionEntry> fromSerialized(Scanner sc) {
           try {
               double coverage = sc.nextDouble();
               double score = sc.nextDouble();
               Optional<Solution> solution = Solution.fromSerialized(sc);
               if(solution.isPresent()) {
                    return Optional.of(new SolutionEntry(coverage, score, solution.get()));
               } else {
                   return Optional.empty();
               }
           } catch (NoSuchElementException ignored) {}
           return Optional.empty();
        }
    }
    private final boolean netEnabled;

    int problem;

    public void storeSolution(double coverage, double score, Solution solution) {
         if(netEnabled)
            updateLocalFile(problem);
        SolutionEntry solutionEntry = new SolutionEntry(coverage, score, solution);
        try {
            FileWriter write = new FileWriter("./solutions/solution_" + problem + ".txt",true);
            write.write("\n");
            write.write(solutionEntry.toSerialized());
            write.flush();
            write.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(netEnabled)
            updateExternalFile(problem);
    }

    public static SolutionList generatePersonalSolutionListFor(int problem) {
        return new SolutionList(false, problem);
    }

    public static SolutionList generateTeamSolutionListFor(int problem) {
        return new SolutionList(true, problem);
    }


    private SolutionList(boolean netEnabled, int problem) {
        this.netEnabled = netEnabled;
        this.problem = problem; }

    public List<SolutionEntry> getInternalSolutions() {
        File file = new File("./solutions/solution_" + problem + ".txt");
        try {
            Scanner scanner = new Scanner(file);
            List<SolutionEntry> result = new ArrayList<>();
            Optional<SolutionEntry> solutionEntry = SolutionEntry.fromSerialized(scanner);
            while(solutionEntry.isPresent()) {
                result.add(solutionEntry.get());
                solutionEntry = SolutionEntry.fromSerialized(scanner);
            }
            return result;
        } catch (FileNotFoundException e) {
            System.out.println("Warning: Could not find a solutions file for problem " + problem);
            return new ArrayList<>();
        }
    }

    public List<Solution> getAllSolutions() {
        if(netEnabled)
            updateLocalFile(problem);
        return getInternalSolutions().stream().map(solutionEntry -> solutionEntry.solution).collect(Collectors.toList());
    }

    public Optional<Solution> getHighestScoringValidSolution() {
        if(netEnabled)
            updateLocalFile(problem);
        return getInternalSolutions().stream().filter(solutionEntry -> solutionEntry.coverage > 0.3).max(Comparator.comparingDouble(o -> o.score)).map(solutionEntry -> solutionEntry.solution);
    }




    private static void updateLocalFile(int problemNo) {
        AWSCredentials anonymousAWSCredentials = new AnonymousAWSCredentials();

        AmazonS3 build = AmazonS3ClientBuilder.standard().withRegion(Regions.EU_WEST_2).build();

        try {
            S3Object solution_1 = build.getObject(SCENARIOWEEK_2_SOLUTIONS, "solution_" + problemNo);
            InputStream objectContent = solution_1.getObjectContent();
            Files.deleteIfExists(Paths.get("./solutions/solution_" + problemNo + ".txt"));

            Files.copy(objectContent, Paths.get( "./solutions/solution_" + problemNo + ".txt"));

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static void updateExternalFile(int problemNo) {
        AWSCredentials anonymousAWSCredentials = new AnonymousAWSCredentials();

        AmazonS3 build = AmazonS3ClientBuilder.standard().withRegion(Regions.EU_WEST_2).build();

        try {
            String key = "solution_" + problemNo;
            PutObjectResult putObjectResult = build.putObject(SCENARIOWEEK_2_SOLUTIONS, key, new File("./solutions/solution_" + problemNo + ".txt"));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void updatePermissions() {
        AWSCredentials anonymousAWSCredentials = new AnonymousAWSCredentials();

        AmazonS3 build = AmazonS3ClientBuilder.standard().withRegion(Regions.EU_WEST_2).build();

        for(int problemNo = 1; problemNo <= 30; problemNo++) {
            try {
                String key = "solution_" + problemNo;
                build.setObjectAcl(SCENARIOWEEK_2_SOLUTIONS, key, CannedAccessControlList.PublicReadWrite);
                System.out.println("Updated permissions of Solution " + problemNo);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    @Override
    public String toString() {
        return "SolutionList{" +
                "problem=" + problem +
                '}';
    }
}
