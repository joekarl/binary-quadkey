package com.joekarl.binaryQuadkey;

/**
 * Created by karl on 5/10/15.
 */
public class StringQuadkey {

    public static String fromTileXY(int x, int y, int zoomLevel) {
        char[] quadkeyChars = new char[zoomLevel];
        for (int i = zoomLevel; i > 0; --i) {
            char digit = '0';
            final int mask = 1 << (i - 1);
            if ((x & mask) != 0) {
                digit++;
            }
            if ((y & mask) != 0) {
                digit++;
                digit++;
            }
            quadkeyChars[zoomLevel - i] = digit;
        }
        return new String(quadkeyChars);
    }

    // via http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames#Java
    public static String fromLonLat(double lon, double lat, int zoomLevel) {
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

    public static boolean isParentOf(String parentQk, String childQk) {
        int childZoomLevel = childQk.length();
        int parentZoomLevel = parentQk.length();
        if (childZoomLevel <= parentZoomLevel) {
            // must be at least one zoom level higher
            return false;
        }
        return childQk.startsWith(parentQk);
    }
}
