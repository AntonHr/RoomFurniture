package com.roomfurniture.angle;

import com.roomfurniture.ga.algorithm.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FurnitureAngleSet {
    private final FurnitureRepresentation furniture;
    private final List<Angle> angles;

    @Override
    public String toString() {
        return "FurnitureAngleSet{" +
                "furniture=" + furniture +
                ", angles=" + angles +
                '}';
    }

    public FurnitureAngleSet(FurnitureRepresentation furniture, List<Angle> angles) {
        this.furniture = furniture;
        this.angles = angles;
    }
    public FurnitureAngleSet(FurnitureRepresentation furniture, Angle angle) {
        this.furniture = furniture;
        this.angles = new ArrayList<>();
        this.angles.add(angle);
    }


    public static Optional<FurnitureAngleSet> conjoinCongruentAngleSets(FurnitureAngleSet angleSetA, FurnitureAngleSet angleSetB) {
        assert(angleSetA.furniture == angleSetB.furniture);

        List<Integer> aIndex = angleSetA.getAngles().stream().map(angleSetA.furniture::findAngleIndex).sorted().collect(Collectors.toList());
        List<Integer> bIndex = angleSetB.getAngles().stream().map(angleSetB.furniture::findAngleIndex).sorted().collect(Collectors.toList());

        Integer smallestA = aIndex.get(0);
        Integer largestA = aIndex.get(aIndex.size() - 1);

        Integer smallestB = bIndex.get(0);
        Integer largestB = bIndex.get(bIndex.size() - 1);


        if(aIndex.size() == 1) {
            if(Math.abs(smallestA - smallestB) == 1) {
                Angle angleA = angleSetA.getAngles().get(0);
                Angle angleB = angleSetB.getAngles().get(0);
                List<Angle> result = new ArrayList<>();
                if(smallestA < smallestB)
                    result.add(angleA);
                else
                    result.add(angleB);
                return Optional.of(new FurnitureAngleSet(angleSetA.furniture, result));
            } else
                return Optional.empty();
        } else {
           if((Math.abs(smallestA - smallestB) == 1) && (Math.abs(largestA - largestB) == 1))  {
               List<Angle> results = new ArrayList<>();

               if(smallestA < smallestB) {
                   results.add(angleSetA.furniture.getAngle(smallestA));
                    for(Integer index : bIndex) {
                        results.add(angleSetA.furniture.getAngle(index));
                    }
                    return Optional.of(new FurnitureAngleSet(angleSetA.furniture, results));
               } else {
                    results.add(angleSetA.furniture.getAngle(smallestB));
                    for(Integer index : aIndex) {
                        results.add(angleSetA.furniture.getAngle(index));
                    }
                    return Optional.of(new FurnitureAngleSet(angleSetA.furniture, results));
               }
            } else
               return Optional.empty();
        }
    }

    public FurnitureRepresentation getFurniture() {
        return furniture;
    }

    public List<Angle> getAngles() {
        return angles;
    }
}


/*
        what do do
        -----------
        1. collect all the angles for the room
        2. for each angle, construct an angleset with all furnitureangleset which match
        3. repeat the following
        4. for each pair of anglesets - find matching anglesets, check if congruent
 */
