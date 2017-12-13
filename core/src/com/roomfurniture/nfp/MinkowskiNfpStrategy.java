package com.roomfurniture.nfp;

import com.roomfurniture.nfp.JNFP.Minkowski;
import com.roomfurniture.nfp.JNFP.MultiPolygon;
import com.roomfurniture.nfp.JNFP.NoFitPolygon;

import java.awt.*;

/**
 * Created by asus on 13.12.2017 г..
 */
public class MinkowskiNfpStrategy implements NfpCalculationStrategy {
    @Override
    public Shape calculateNfp(Shape stationaryPolygon, Shape orbitingPolygon) {
        MultiPolygon statPolygon = JnfpUtility.toMultiPolygon(stationaryPolygon);
        MultiPolygon orbPolygon = JnfpUtility.toMultiPolygon(orbitingPolygon);

        NoFitPolygon nfp = Minkowski.generateMinkowskiNFP(statPolygon, orbPolygon);

        return JnfpUtility.toShape(nfp);
    }
}
