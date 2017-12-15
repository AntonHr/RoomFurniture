package com.roomfurniture.placing;

import com.roomfurniture.problem.Furniture;
import com.roomfurniture.problem.Room;
import com.roomfurniture.problem.Vertex;

import java.util.List;

/**
 * Created by Gopiandcode on 15/12/2017.
 */
public interface PlacingProblem {
    Room getRoom();

    List<Furniture> getFurnitures();

    List<Vertex> getSpawnPoints();
}
