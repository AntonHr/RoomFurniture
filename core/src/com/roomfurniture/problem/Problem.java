package com.roomfurniture.problem;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class Problem {
    private int number = 0;
    public final Room room;
    private List<Furniture> furnitures;

    public Problem(int number, Room room, List<Furniture> furnitures) {
        this.number = number;
        this.room = room;
        this.furnitures = ImmutableList.copyOf(furnitures);
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

    public int getNumber() {
        return number;
    }

    public void setFurniture(List<Furniture> furniture) {
        this.furnitures = furniture;
    }
}
