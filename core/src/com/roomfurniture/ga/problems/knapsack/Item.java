package com.roomfurniture.ga.problems.knapsack;

class Item {
    private final int benefit;
    private final int volume;

    Item(int benefit, int volume) {
        this.benefit = benefit;
        this.volume = volume;
    }

    public int getBenefit() {
        return benefit;
    }

    public int getVolume() {
        return volume;
    }
}
