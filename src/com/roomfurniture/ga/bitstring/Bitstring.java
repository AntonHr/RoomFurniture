package com.roomfurniture.ga.bitstring;

import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

public class Bitstring implements Iterable<Boolean>{
    private final char[] string;
    private final char bits;
    private int size;

    public static Bitstring and(Bitstring a, Bitstring b) {
        assert(a.size == b.size);
        Bitstring result = a.copy();

        for(int i = 0; i < a.string.length; i++) {
            result.string[i] = (char) (a.string[i] & b.string[i]);
        }
        return result;
    }


    public static Bitstring or(Bitstring a, Bitstring b) {
        assert(a.size == b.size);
        Bitstring result = a.copy();

        for(int i = 0; i < a.string.length; i++) {
            result.string[i] = (char) (a.string[i] | b.string[i]);
        }
        return result;
    }

    public static Bitstring xor(Bitstring a, Bitstring b) {
        assert(a.size == b.size);
        Bitstring result = a.copy();

        for(int i = 0; i < a.string.length; i++) {
            result.string[i] =(char) (a.string[i] ^ b.string[i]);
        }
        return result;
    }

    public static Bitstring generateRandom(int size) {
        Bitstring result = new Bitstring(size);

        for(int i = 0; i < size; i++) {
            result.put(i, ThreadLocalRandom.current().nextBoolean());
        }
        return result;
    }

    public Bitstring(int size) {
        this.size = size;
        char bits;
        int bytes = size / 8;

        // i.e 7 would have 1 byte, 7 bits, 8 would have 1 byte, eight bits
        bits = (char) (size % 8);
        if (bits != 0) {
            bytes += 1;
        } else {
            bits = 8;
        }
        this.bits = bits;

        string = new char[bytes];
    }

    public Bitstring copy(){
        Bitstring result= new Bitstring(size);

        for(int i = 0; i < size;i++) {
            result.put(i, get(i));
        }

        return result;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < string.length; i++) {
            int bitCount = 8;
            if (i == string.length - 1) {
                bitCount = this.bits;
            }
            char currentByte = string[i];
            for (int j = 0; j < bitCount; j++) {
                char mask = (char) (1 << j);
                if ((currentByte & mask) != 0) {
                    sb.append("1");
                } else {
                    sb.append("0");
                }
            }
            sb.append(",");

        }
        return sb.toString();
    }

    public void set(int bit) {
        int currentByte = bit / 8;
        int byteBit = bit % 8;
        assert(currentByte < this.string.length);
        this.string[currentByte] |= 1 << byteBit;
    }

    public void clear(int bit) {
        int currentByte = bit / 8;
        int byteBit = bit % 8;
        assert(currentByte < this.string.length);
        this.string[currentByte] &= ~(1 << byteBit);
    }

    public void invert(int bit) {
        int currentByte = bit / 8;
        int byteBit = bit % 8;
        assert(currentByte < this.string.length);
        this.string[currentByte] ^= 1 << byteBit;
    }

    public boolean get(int bit) {
        int currentByte = bit / 8;
        int byteBit = bit % 8;
        assert(currentByte < this.string.length);
        return ((this.string[currentByte] >> byteBit) & 1) == 1;
    }

    public void put(int bit, boolean value){
       if(value) {
           set(bit);
       } else {
           clear(bit);
       }
    }

    public int getSize() {
        return size;
    }


    @Override
    public Iterator<Boolean> iterator() {
        return new BitstringIterator();
    }

    private class BitstringIterator implements Iterator<Boolean> {
        int current_bit = 0;
        @Override
        public boolean hasNext() {
            return current_bit < ((string.length - 1) * 8 + bits);
        }

        @Override
        public Boolean next() {
            return get(current_bit++);
        }
    }
}
