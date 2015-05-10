package com.joekarl.binaryQuadkey;

import java.util.*;

public class Main {

    static final long SEED = 8972347823904L;
    static final long DIFF_SEED = 8972347823902354L;
    static final int KEY_LENGTH = 15;
    static final int NUM_KEYS = 10000000;

    public Main() {
        long binaryStart, binaryEnd, stringStart, stringEnd;

        // just warm up the jvm
        for (int i = 0; i < 10000; ++i) {
            genBinaryQuadkeys(100, 15, SEED);
            genStringQuadkeys(100, 15, SEED);
        }

        String parentQk = "01030212";
        long parentBinaryQk = BinaryQuadkey.fromString(parentQk);
        String equalityQk = "012301230123012";
        long equalityBinaryQk = BinaryQuadkey.fromString(equalityQk);

        boolean equality;
        boolean parent;

        long[] binaryQks = genBinaryQuadkeys(NUM_KEYS, KEY_LENGTH, SEED);
        binaryStart = System.currentTimeMillis();
        equality = false;
        parent = false;
        for (long qk : binaryQks) {
            parent = BinaryQuadkey.isParentOf(parentBinaryQk, qk);
        }
        for (long qk : binaryQks) {
            equality = qk == equalityBinaryQk;
        }

        binaryEnd = System.currentTimeMillis();

        System.out.println("printing equality/parent so it won't be optimized out " + equality + parent);

        String[] stringQks = genStringQuadkeys(NUM_KEYS, KEY_LENGTH, SEED);
        stringStart = System.currentTimeMillis();
        equality = false;
        parent = false;
        for (String qk : stringQks) {
            parent = StringQuadkey.isParentOf(parentQk, qk);
        }
        for (String qk : stringQks) {
            equality = qk.equals(equalityQk);
        }
        stringEnd = System.currentTimeMillis();

        System.out.println("printing equality/parent so it won't be optimized out " + equality + parent);

        System.out.println("Binary time: " + (binaryEnd - binaryStart));
        System.out.println("String time: " + (stringEnd - stringStart));
    }

    long[] genBinaryQuadkeys(int n, int zoomLevel, long seed) {
        Random r = new Random(seed);
        long[] quadkeys = new long[n];
        for (int i = 0; i < n; ++i) {
            double lon = r.nextDouble();
            double lat = r.nextDouble();
            quadkeys[i] = BinaryQuadkey.fromLonLat(lon, lat, zoomLevel);
        }
        return quadkeys;
    }

    String[] genStringQuadkeys(int n, int zoomLevel, long seed) {
        Random r = new Random(seed);
        String[] quadkeys = new String[n];
        for (int i = 0; i < n; ++i) {
            double lon = r.nextDouble();
            double lat = r.nextDouble();
            quadkeys[i] = StringQuadkey.fromLonLat(lon, lat, zoomLevel);
        }
        return quadkeys;
    }

    public static void main(String ... args) {
        new Main();
    }

}
