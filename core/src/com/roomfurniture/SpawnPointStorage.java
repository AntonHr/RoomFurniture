package com.roomfurniture;

import com.google.common.collect.ImmutableList;
import com.roomfurniture.problem.Vertex;

import java.util.List;

public class SpawnPointStorage {

    private static ImmutableList<List<Vertex>> spawnPoints = ImmutableList.of(
            // Problem 1
            ImmutableList.of(
                    new Vertex(5, 5)
            ),
            // Problem 2
            ImmutableList.of(
                    new Vertex(2.85, 2.75),
                    new Vertex(6.90, 3.40),
                    new Vertex(7.5, 1.55)
            ),
            // Problem 3
            ImmutableList.of(
                    new Vertex(2.5, 2.5),
                    new Vertex(4, 5.9),
                    new Vertex(0.8, 5.9)
            ),
            // Problem 4
            ImmutableList.of(
                    new Vertex(0.1, 1.1)
            ),
            // Problem 5
            ImmutableList.of(
                    new Vertex(0.4, 1.0)
            ),
            // Problem 6
            ImmutableList.of(
                    new Vertex(-0.9, -0.6)
            ),
            // Problem 7
            ImmutableList.of(
                    new Vertex(-0.6, 0.9)
            ),
            // Problem 8
            ImmutableList.of(
                    new Vertex(-0.175, -1)
            ),
            // Problem 9
            ImmutableList.of(
                    new Vertex(-6.3, -5.3),
                    new Vertex(-3.6, -3.9),
                    new Vertex(-7.3, 0.9),
                    new Vertex(-4.0, 0.5),
                    new Vertex(-2.8, 1.0),
                    new Vertex(-1.1, -0.77),
                    new Vertex(-0.71, 1.1)
            ),
            // Problem 10
            ImmutableList.of(
                    // Untested
                    new Vertex(2.5, -2.05),
                    new Vertex(0.78, -1.1),
                    new Vertex(-0.3, -1.8),
                    new Vertex(-2.2, 0.6),
                    new Vertex(0.27, 2.4),
                    new Vertex(-2.16, 0.63),
                    new Vertex(-5.8, -0.1),
                    new Vertex(-5.4, -1.9),
                    new Vertex(-4.5, -4.3),
                    new Vertex(-3.34, -2.99),
                    new Vertex(-3.98, 3.12),
                    new Vertex(-4.36, 5.26),
                    new Vertex(-7.4, 4.5),
                    new Vertex(-6.5, 8.5),
                    new Vertex(-3.26, 8.55),
                    new Vertex(-0.31, 8.0),
                    new Vertex(0.835, 6.43),
                    new Vertex(-1.35, 6.277)
            ),
            // Problem 11
            ImmutableList.of(
                    // Untested
                    new Vertex(-4.885, -7.762),
                    new Vertex(-7.0, -8.19),
                    new Vertex(-9.3, -8.1),
                    new Vertex(-9.1, -5.9),
                    new Vertex(-11.5, -4.44),
                    new Vertex(-8.8, -3.5),
                    new Vertex(-9.83, -0.3),
                    new Vertex(-12.8, 1.32),
                    new Vertex(-14.3, 1.34),
                    new Vertex(-14.91, 3.892),
                    new Vertex(-14.04, 3.35),
                    new Vertex(-16.0, 0.6),
                    new Vertex(-15.5, 6),
                    new Vertex(-18.4, 3.77),
                    new Vertex(-11.07, 7.51),
                    new Vertex(-11.5, 5.17),
                    new Vertex(-11.3, 2.7),
                    new Vertex(-8.0, 3.35),
                    new Vertex(-7.24, -0.08),
                    new Vertex(-5.2, 3.3),
                    new Vertex(-2.635, 1.03),
                    new Vertex(-2.185, -2.30),
                    new Vertex(-3.58, -1.102),
                    new Vertex(-4.9, 0.42)
            ),
            // Problem 12
            ImmutableList.of(
                    // Untested
                    new Vertex(0.37, 2.97),
                    new Vertex(-0.96, 4.1)
            ),
            // Problem 13
            ImmutableList.of(
                    // Untested
                    new Vertex(3.26, 8.56),
                    new Vertex(8.21, 12.96),
                    new Vertex(0.23, 8.29),
                    new Vertex(0.62, 5.26),
                    new Vertex(3.78, 3.15),
                    new Vertex(5.16, 1.55),
                    new Vertex(3.46, -1.75),
                    new Vertex(2.63, 1.16),
                    new Vertex(1.31, -0.48),
                    new Vertex(-0.61, 0.702),
                    new Vertex(-2.23, -2.37),
                    new Vertex(0.872, -5.12)
            ),
            // Problem 14
            ImmutableList.of(
                    new Vertex(1.8, 4.8),
                    new Vertex(4.3, 2.5),
                    new Vertex(1.6, 2.32),
                    new Vertex(4.16, -0.46),
                    new Vertex(1.26, -0.6),
                    new Vertex(-0.46, 0.98),
                    new Vertex(-3.22, -1.54),
                    new Vertex(-5.64, -1.48),
                    new Vertex(-3.58, 0.7),
                    new Vertex(-3.84, 2.56),
                    new Vertex(-1.84, -3.64),
                    new Vertex(0.78, -6.10),
                    new Vertex(1.02, -3.66)
            ),
            // Problem 15
            ImmutableList.of(
                    // Untested
                    new Vertex(-1.3, 18.1),
                    new Vertex(-6.4, 11.4),
                    new Vertex(0.83, 13.8),
                    new Vertex(-2.38, 11.31),
                    new Vertex(-5.64, 9.14),
                    new Vertex(-3.085, 3.19),
                    new Vertex(-0.91, 6.28),
                    new Vertex(0.59, 5.44),
                    new Vertex(2.65, 6.94),
                    new Vertex(-0.180, 1.16),
                    new Vertex(1.63, 1.58),
                    new Vertex(1.5699, -1.355),
                    new Vertex(1.5, 0.6),
                    new Vertex(3.63, 2.74),
                    new Vertex(5.175, 0.745),
                    new Vertex(5.52, 4.9),
                    new Vertex(7.59, 5.29),
                    new Vertex(8.32, 3.82),
                    new Vertex(11.16, 4.175),
                    new Vertex(10.53, 7.15),
                    new Vertex(8.71, 6.975),
                    new Vertex(7.31, 7.11),
                    new Vertex(6.26, 8.58),
                    new Vertex(4.78, 8.375)
            ),
            // Problem 16
            ImmutableList.of(
                    // Untested
                    new Vertex(2.9, -3.9),
                    new Vertex(0.25, -8.2),
                    new Vertex(3.2, -8.5),
                    new Vertex(2.3, -10),
                    new Vertex(-1.05, -6.15),
                    new Vertex(-2.05, -0.35),
                    new Vertex(1.85, 1.65),
                    new Vertex(-0.85, 2.80),
                    new Vertex(0.30, 5.5),
                    new Vertex(-4.3, 2.25),
                    new Vertex(-3.65, 4.1),
                    new Vertex(-2.8, 5.65),
                    new Vertex(-0.30, 8.6),
                    new Vertex(-3.3, 9.3),
                    new Vertex(-6.05, 8.45),
                    new Vertex(-6.4, 6.9),
                    new Vertex(-2.3, 14.0),
                    new Vertex(-3.9, 13.2),
                    new Vertex(-5.55, 11.0),
                    new Vertex(-9.5, 12.15),
                    new Vertex(-7.8, 11.8),
                    new Vertex(-6.85, 15.0),
                    new Vertex(-4.2, 12.0),
                    new Vertex(-0.75, 10.85)
            ),
            // Problem 17
            ImmutableList.of(
                    new Vertex(12.05, -12.35),
                    new Vertex(3.15, 1.45),
                    new Vertex(1.5, -5.0),
                    new Vertex(-3.45, 3.9),
                    new Vertex(-0.85, -0.9),
                    new Vertex(29, -27),
                    new Vertex(23.5, -32.8),
                    new Vertex(28.4, -33.0)
            ),
            // Problem 18
            ImmutableList.of(
                    new Vertex(4.26, 4.42),
                    new Vertex(-0.88, 2.24),
                    new Vertex(0.42, 5.16),
                    new Vertex(-2.88, -2.56),
                    new Vertex(-4.8, 0.6),
                    new Vertex(-5.26, 2.12),
                    new Vertex(3.94, -2.14),
                    new Vertex(4.42, -3.78),
                    new Vertex(3.64, -5.6)
            ),
            // Problem 19
            ImmutableList.of(
                    new Vertex(1.74, -10.65),
                    new Vertex(-2.65, -9.86),
                    new Vertex(-3.48, -12.8),
                    new Vertex(-3.26, -9.8),
                    new Vertex(-4.35, -17.0),
                    new Vertex(-6.30, -21.17),
                    new Vertex(-4.06, -20.445),
                    new Vertex(-8.04, -16.313),
                    new Vertex(-0.362, -4.567),
                    new Vertex(-1.885, -1.5225),
                    new Vertex(2.32, -1.16),
                    new Vertex(-1.3775, 2.61),
                    new Vertex(-5.8, 0.725),
                    new Vertex(-5.292, 3.263),
                    new Vertex(-22.475, 10.5125),
                    new Vertex(-9.57, 8.1925),
                    new Vertex(-10.367, -5.07),
                    new Vertex(-10.44, 1.015),
                    new Vertex(-14.28, 3.842),
                    new Vertex(-12.47, 11.67),
                    new Vertex(-2.90, 7.03),
                    new Vertex(-0.58, 7.9325),
                    new Vertex(-2.392, 9.28),
                    new Vertex(-1.377, 13.702501),
                    new Vertex(3.480, 2.2475),
                    new Vertex(4.35, 9.0625),
                    new Vertex(7.322, 4.422499),
                    new Vertex(10.585, 2.392),
                    new Vertex(9.7875, 0.943),
                    new Vertex(11.5275, -0.6525),
                    new Vertex(9.28, 16.53),
                    new Vertex(10, 14.42),
                    new Vertex(-4.72, 30.0),
                    new Vertex(-15.7325, 19.937),
                    new Vertex(-27.985, 17.69),
                    new Vertex(-27.115, -14.065)
            ),
            // Problem 20
            ImmutableList.of(
                    new Vertex(-18.525,42.825),
                    new Vertex(10.625,118.8),
                    new Vertex(3.03,35.31560),
                    new Vertex(-3.66,35.549),
                    new Vertex(0.615,31.98),
                    new Vertex(4.657496,28.265),
                    new Vertex(-2.4725,28.572),
                    new Vertex(-5.635,27.01),
                    new Vertex(0.402498,23.8),
                    new Vertex(3.45,21.2125),
                    new Vertex(-4.7725,22.075),
                    new Vertex(-6.44,13.105),
                    new Vertex(-9.2575,32.435),
                    new Vertex(-12.1325,16.6125),
                    new Vertex(-36.9275,23.2624),
                    new Vertex(9.6025,16.7275),
                    new Vertex(11.155,25.64),
                    new Vertex(15.9275,15.29),
                    new Vertex(8.51,10.7),
                    new Vertex(43.35,47.4),
                    new Vertex(-13.175,100.52),
                    new Vertex(5.405,9.195),
                    new Vertex(-0.805,12.3575),
                    new Vertex(-2.4725,14.83),
                    new Vertex(2.817,6.837),
                    new Vertex(1.955,1.777),
                    new Vertex(-0.7475,4.595),
                    new Vertex(-0.9775,2.2375),
                    new Vertex(-12.4775,-0.925),
                    new Vertex(-9.659,-9.55),
                    new Vertex(-9.659, -9.55),
                    new Vertex(-9.775,3.215),
                    new Vertex(-6.957,4.48),
                    new Vertex(-8.16,6.665),
                    new Vertex(-17.25,-12.3),
                    new Vertex(-33.23,-12.1375),
                    new Vertex(-31.452,7.6425),
                    new Vertex(-39.8475,23.455),
                    new Vertex(-22.712,5.8025),
                    new Vertex(-23.172,8.2175),
                    new Vertex(-25.7025,11.265),
                    new Vertex(-20.815, 12.415),
                    new Vertex(-19.435, 14.31),
                    new Vertex(-4.4, -29.38)
            )

    );

    public static List<Vertex> getSpawnPointsForProblem(int problemNo) {
        return spawnPoints.get(problemNo - 1);
    }

    public static List<List<Vertex>> getSpawnPointsForAllProblems() {
        return spawnPoints;
    }
}
