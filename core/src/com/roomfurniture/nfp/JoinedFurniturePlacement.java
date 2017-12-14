package com.roomfurniture.nfp;

import com.roomfurniture.problem.Descriptor;
import com.roomfurniture.problem.Furniture;

/**
 * Created by asus on 14.12.2017 г..
 */
public class JoinedFurniturePlacement {
    private Furniture joinedFurniture;
    private Descriptor movedFurnitureDescriptor;

    public JoinedFurniturePlacement(Furniture joinedFurniture, Descriptor movedFurnitureDescriptor) {
        this.joinedFurniture = joinedFurniture;
        this.movedFurnitureDescriptor = movedFurnitureDescriptor;
    }

    public Furniture getJoinedFurniture() {
        return joinedFurniture;
    }

    public Descriptor getMovedFurnitureDescriptor() {
        return movedFurnitureDescriptor;
    }
}
