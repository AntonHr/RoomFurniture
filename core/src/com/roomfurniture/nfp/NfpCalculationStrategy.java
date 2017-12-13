package com.roomfurniture.nfp;

import java.awt.*;

/**
 * Created by asus on 13.12.2017 г..
 */
public interface CalculationStrategy {
    Shape calculateNfp(Shape stationaryPolygon, Shape orbitingPolygon);
}
