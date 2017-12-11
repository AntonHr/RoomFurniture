package com.roomfurniture.problem;

import java.util.List;

public class Problem {
   public final Room room;
    private final List<Furniture> furnitures;

    public Problem(Room room, List<Furniture> furnitures) {
        this.room = room;
        this.furnitures = furnitures;
    }

    @Override
    public String toString() {
        return "Problem{" +
                "room=" + room +
                ", furnitures=" + furnitures +
                '}';
    }

    public Room getRoom() {
        return room;
    }

    public List<Furniture> getFurnitures() {
        return furnitures;
    }
}
