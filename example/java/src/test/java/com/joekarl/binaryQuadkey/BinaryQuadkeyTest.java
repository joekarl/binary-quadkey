package com.joekarl.binaryQuadkey;

import org.junit.Assert;
import org.junit.Test;

public class BinaryQuadkeyTest {

    @Test
    public void createsFromTileXYCorrectly() {
        long expectedBinaryQuadkey = 0b0010110101100100000000000000000000000000000000000000000000000111L;
        int x = 29, y = 50, zoomLevel = 7;
        long result = BinaryQuadkey.fromTileXY(x, y, zoomLevel);
        System.out.println(Long.toBinaryString(expectedBinaryQuadkey));
        System.out.println(Long.toBinaryString(result));
        Assert.assertEquals(expectedBinaryQuadkey, result);
    }

    @Test
    public void createsFromTileXYCorrectly2() {
        long expectedBinaryQuadkey = BinaryQuadkey.fromString("1202102332221212");
        int x = 35210, y = 21493, zoomLevel = 16;
        long result = BinaryQuadkey.fromTileXY(x, y, zoomLevel);
        System.out.println(Long.toBinaryString(expectedBinaryQuadkey));
        System.out.println(Long.toBinaryString(result));
        Assert.assertEquals(expectedBinaryQuadkey, result);
    }

    @Test
    public void createsFromLonLatCorrectly() {
        long expectedBinaryQuadkey = BinaryQuadkey.fromString("02123022310022332");
        double lon = -122.676537, lat = 45.523007;
        int zoomLevel = 17;
        long result = BinaryQuadkey.fromLonLat(lon, lat, zoomLevel);
        System.out.println(Long.toBinaryString(expectedBinaryQuadkey));
        System.out.println(Long.toBinaryString(result));
        Assert.assertEquals(expectedBinaryQuadkey, result);
    }

    @Test
    public void parsesQuadkeyString() {
        long expectedBinaryQuadkey = 0b0010010011100001110010011011001000000000000000000000000000010000L;
        String quadkey = "0210320130212302";
        long result = BinaryQuadkey.fromString(quadkey);
        System.out.println(Long.toBinaryString(expectedBinaryQuadkey));
        System.out.println(Long.toBinaryString(result));
        Assert.assertEquals(expectedBinaryQuadkey, result);
    }

    @Test
    public void parsesQuadkeyString2() {
        long expectedBinaryQuadkey = 0b0010000000000000000000000000000000000000000000000000000000000010L;
        String quadkey = "02";
        long result = BinaryQuadkey.fromString(quadkey);
        System.out.println(Long.toBinaryString(expectedBinaryQuadkey));
        System.out.println(Long.toBinaryString(result));
        Assert.assertEquals(expectedBinaryQuadkey, result);
    }

    @Test
    public void parsesQuadkeyString3() {
        long expectedBinaryQuadkey = 0b0000000000000000000000000000000000000000000000000000000000000001L;
        String quadkey = "0";
        long result = BinaryQuadkey.fromString(quadkey);
        System.out.println(Long.toBinaryString(expectedBinaryQuadkey));
        System.out.println(Long.toBinaryString(result));
        Assert.assertEquals(expectedBinaryQuadkey, result);
    }

    @Test
    public void quadkeyToBinaryQuadkey() {
        String expectedQuadkeyString = "0210320130212302";
        long binaryQuadkey = 0b0010010011100001110010011011001000000000000000000000000000010000L;
        String result = BinaryQuadkey.toString(binaryQuadkey);
        System.out.println(expectedQuadkeyString);
        System.out.println(result);
        Assert.assertEquals(expectedQuadkeyString, result);
    }

    @Test
    public void extractsZoomLevel() {
        int expectedZoomLevel = 16;
        long binaryQuadkey = 0b0010010011100001110010011011001000000000000000000000000000010000L;
        int result = BinaryQuadkey.extractZoomLevel(binaryQuadkey);
        Assert.assertEquals(expectedZoomLevel, result);
    }

    @Test
    public void prefexChecking_identifiesParent() {
        String qkA = "0210320130212302";
        String qkB = "02103201";
        long binaryQkA = BinaryQuadkey.fromString(qkA);
        long binaryQkB = BinaryQuadkey.fromString(qkB);
        Assert.assertTrue(BinaryQuadkey.isParentOf(binaryQkB, binaryQkA));
    }

    @Test
    public void prefexChecking_identifiesNonParent() {
        String qkA = "0210320130212302";
        String qkB = "02103202";
        long binaryQkA = BinaryQuadkey.fromString(qkA);
        long binaryQkB = BinaryQuadkey.fromString(qkB);
        Assert.assertFalse(BinaryQuadkey.isParentOf(binaryQkB, binaryQkA));
    }

    @Test
    public void prefixChecking_childBiggerThanParent() {
        String qkA = "0210320130212302";
        String qkB = "02103201";
        long binaryQkA = BinaryQuadkey.fromString(qkA);
        long binaryQkB = BinaryQuadkey.fromString(qkB);
        Assert.assertFalse(BinaryQuadkey.isParentOf(binaryQkA, binaryQkB));
    }

    @Test
    public void prefixChecking_childSameSizeAsParent() {
        String qkA = "0210320130212302";
        String qkB = "0210320130212302";
        long binaryQkA = BinaryQuadkey.fromString(qkA);
        long binaryQkB = BinaryQuadkey.fromString(qkB);
        Assert.assertFalse(BinaryQuadkey.isParentOf(binaryQkA, binaryQkB));
    }
}