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


    public static long fromTileXY(int x, int y, int zoomLevel) {
        long binaryQuadkey = 0L;
        for (int i = zoomLevel; i > 0; --i) {
            int mask = 1 << (i - 1);
            long bitLocation = (64 - ((zoomLevel - i + 1) * 2) + 1);
            if ((x & mask) != 0) {
                binaryQuadkey |= 0b1L << (bitLocation - 1);
            }
            if ((y & mask) != 0) {
                binaryQuadkey |= 0b1L << bitLocation;
            }
        }
        binaryQuadkey |= (long)zoomLevel;
        return binaryQuadkey;
    }

    // via http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames#Java
    public static long fromLonLat(double lon, double lat, int zoomLevel) {
        int xtile = (int) Math.floor( (lon + 180) / 360 * (1 << zoomLevel) ) ;
        int ytile = (int) Math.floor( (1 - Math.log(Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2 * (1 << zoomLevel) ) ;
        if (xtile < 0) {
            xtile = 0;
        }
        if (xtile >= (1 << zoomLevel)) {
            xtile = ((1 << zoomLevel) - 1);
        }
        if (ytile < 0) {
            ytile = 0;
        }
        if (ytile >= (1 << zoomLevel)) {
            ytile = ((1 << zoomLevel) - 1);
        }
        return fromTileXY(xtile, ytile, zoomLevel);
    }

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
