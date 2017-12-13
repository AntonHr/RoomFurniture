package com.roomfurniture.nfp;

import java.awt.*;

import com.roomfurniture.nfp.JNFP.MultiPolygon;
import com.roomfurniture.nfp.JNFP.NoFitPolygon;
import com.roomfurniture.nfp.JNFP.Orbiting;


/**
 * Created by asus on 13.12.2017 г..
 */
public class OrbitingNfpStrategy implements NfpCalculationStrategy {
    @Override
    public Shape calculateNfp(Shape stationaryPolygon, Shape orbitingPolygon) {
        MultiPolygon statPoly = JnfpUtility.toMultiPolygon(stationaryPolygon);
        MultiPolygon orbPolygon = JnfpUtility.toMultiPolygon(orbitingPolygon);

        NoFitPolygon nfp = Orbiting.generateNFP(statPoly, orbPolygon);

        return JnfpUtility.toShape(nfp);
    }
}
