package com.joekarl.binaryQuadkey;

public class BinaryQuadkey {
    private static final long[] PREFIX_MASKS = {
            0b0000000000000000000000000000000000000000000000000000000000000000L,
            0b1100000000000000000000000000000000000000000000000000000000000000L,
            0b1111000000000000000000000000000000000000000000000000000000000000L,
            0b1111110000000000000000000000000000000000000000000000000000000000L,
            0b1111111100000000000000000000000000000000000000000000000000000000L,
            0b1111111111000000000000000000000000000000000000000000000000000000L,
            0b1111111111110000000000000000000000000000000000000000000000000000L,
            0b1111111111111100000000000000000000000000000000000000000000000000L,
            0b1111111111111111000000000000000000000000000000000000000000000000L,
            0b1111111111111111110000000000000000000000000000000000000000000000L,
            0b1111111111111111111100000000000000000000000000000000000000000000L,
            0b1111111111111111111111000000000000000000000000000000000000000000L,
            0b1111111111111111111111110000000000000000000000000000000000000000L,
            0b1111111111111111111111111100000000000000000000000000000000000000L,
            0b1111111111111111111111111111000000000000000000000000000000000000L,
            0b1111111111111111111111111111110000000000000000000000000000000000L,
            0b1111111111111111111111111111111100000000000000000000000000000000L,
            0b1111111111111111111111111111111111000000000000000000000000000000L,
            0b1111111111111111111111111111111111110000000000000000000000000000L,
            0b1111111111111111111111111111111111111100000000000000000000000000L,
            0b1111111111111111111111111111111111111111000000000000000000000000L,
            0b1111111111111111111111111111111111111111110000000000000000000000L,
            0b1111111111111111111111111111111111111111111100000000000000000000L,
            0b1111111111111111111111111111111111111111111111000000000000000000L
    };

    public static long fromString(String qkString) {
        long zoomLevel = qkString.length();
        long binaryQuadkey = 0L;
        for (int i = 0; i < zoomLevel; ++i) {
            long bitLocation = (64 - ((i + 1) * 2));
            char bitChar = qkString.charAt(i);
            binaryQuadkey |= ((long) Character.getNumericValue(bitChar)) << bitLocation;
        }
        binaryQuadkey |= zoomLevel;
        return binaryQuadkey;
    }

    public static String toString(long binaryQk) {
        int zoomLevel = extractZoomLevel(binaryQk);
        char[] quadkeyChars = new char[zoomLevel];
        for (int i = 0; i < zoomLevel; ++i) {
            long bitLocation = (64 - ((i + 1) * 2));
            int charBits = (int)((binaryQk & (0b11L << bitLocation)) >> bitLocation);
            quadkeyChars[i] = Character.forDigit(charBits, 10);
        }
        return new String(quadkeyChars);
    }

    public static int extractZoomLevel(long binaryQk) {
        return (int) (binaryQk & 0b11111L);
    }

    public static boolean isParentOf(long parentQk, long childQk) {
        int childZoomLevel = extractZoomLevel(childQk);
        int parentZoomLevel = extractZoomLevel(parentQk);
        if (childZoomLevel <= parentZoomLevel) {
            // must be at least one zoom level higher
            return false;
        }
        long mask = PREFIX_MASKS[parentZoomLevel];
        return (parentQk & mask) == (childQk & mask);
    }

}
