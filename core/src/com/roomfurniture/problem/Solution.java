package com.roomfurniture.problem;

import java.util.List;

public class Solution {
   private final List<Descriptor> descriptors;

   public Solution(List<Descriptor> descriptors) {
      this.descriptors = descriptors;
   }

   public List<Descriptor> getDescriptors() {
      return descriptors;
   }

   @Override
   public String toString() {
      return "Solution{" +
              "descriptors=" + descriptors +
              '}';
   }
}
