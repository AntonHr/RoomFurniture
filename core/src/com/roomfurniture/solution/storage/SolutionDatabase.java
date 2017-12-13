package com.roomfurniture.solution.storage;


import com.roomfurniture.problem.Problem;
import com.roomfurniture.solution.Solution;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SolutionDatabase {
    List<SolutionList> solutionLists;
    private static void checkProblemBounds(int problem) {
        if((problem <= 0) || (problem > 30)) {
            throw new RuntimeException("Invalid problem number provided");
        }
    }

    public static SolutionDatabase createPersonalSolutionDatabase() {
        return new SolutionDatabase(false);
    }

    public static SolutionDatabase createTeamSolutionDatabase() {
        return new SolutionDatabase(true);
    }


    private SolutionDatabase(boolean isTeam) {
        solutionLists = new ArrayList<>();
        if(isTeam)
            for(int i = 1; i <= 30; i++) {
                solutionLists.add(SolutionList.generateTeamSolutionListFor(i));
            }
        else
             for(int i = 1; i <= 30; i++) {
                solutionLists.add(SolutionList.generatePersonalSolutionListFor(i));
            }

    }

    public void storeSolutionFor(int problem, double score, double coverage, Solution solution) {
        checkProblemBounds(problem);
        solutionLists.get(problem-1).storeSolution(coverage, score, solution);
    }

    public List<Solution> getAllSolutionsFor(int problem) {
        checkProblemBounds(problem);
        return solutionLists.get(problem-1).getAllSolutions();
    }

    public Optional<Solution> getHighestScoringValidSolutionFor(int problem) {
        checkProblemBounds(problem);
        return solutionLists.get(problem-1).getHighestScoringValidSolution();
    }


    public String generateOverallSolutionReportFor(List<Problem> problems) {
        StringBuilder sb = new StringBuilder();
        for (Problem problem : problems) {
            Optional<Solution> highestScoringValidSolutionForProblem = getHighestScoringValidSolutionFor(problem.getNumber());
            if (highestScoringValidSolutionForProblem.isPresent()) {
                sb.append(highestScoringValidSolutionForProblem.get().toOutputFormat(problem));
                sb.append("\n");
            }
        }

        return sb.toString();
    }

}
