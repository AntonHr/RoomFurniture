package com.roomfurniture.nfp;

import com.roomfurniture.ShapeCalculator;
import com.roomfurniture.problem.Vertex;

import java.awt.*;

/**
 * Created by asus on 13.12.2017 г..
 */
public class OptimalPlacementCalculator {
    NfpCalculationStrategy nfpCalculator;

    public OptimalPlacementCalculator(NfpCalculationStrategy nfpCalculator) {
        this.nfpCalculator = nfpCalculator;
    }

    public Shape optimize(Shape statShape, Shape orbShape) {
        Shape nfp = nfpCalculator.calculateNfp(statShape, orbShape);

        for (Vertex v : ShapeCalculator.getVertices(nfp)) {

        }

        return null;
    }
}
