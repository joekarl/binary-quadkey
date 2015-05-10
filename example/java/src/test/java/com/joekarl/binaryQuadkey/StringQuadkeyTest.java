package com.joekarl.binaryQuadkey;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by karl on 5/10/15.
 */
public class StringQuadkeyTest {
    @Test
    public void createsFromTileXYCorrectly() {
        String expectedQuadkey = "0231121";
        int x = 29, y = 50, zoomLevel = 7;
        String result = StringQuadkey.fromTileXY(x, y, zoomLevel);
        System.out.println(expectedQuadkey);
        System.out.println(result);
        Assert.assertEquals(expectedQuadkey, result);
    }

    @Test
    public void createsFromTileXYCorrectly2() {
        String expectedQuadkey = "1202102332221212";
        int x = 35210, y = 21493, zoomLevel = 16;
        String result = StringQuadkey.fromTileXY(x, y, zoomLevel);
        System.out.println(expectedQuadkey);
        System.out.println(result);
        Assert.assertEquals(expectedQuadkey, result);
    }

    @Test
    public void createsFromLonLatCorrectly() {
        String expectedQuadkey = "02123022310022332";
        double lon = -122.676537, lat = 45.523007;
        int zoomLevel = 17;
        String result = StringQuadkey.fromLonLat(lon, lat, zoomLevel);
        System.out.println(expectedQuadkey);
        System.out.println(result);
        Assert.assertEquals(expectedQuadkey, result);
    }
}
