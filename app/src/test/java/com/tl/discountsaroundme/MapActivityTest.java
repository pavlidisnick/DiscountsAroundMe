package com.tl.discountsaroundme;

import org.junit.Test;
import static org.junit.Assert.*;

public class MapActivityTest {
    @Test
    public void measureDistanceIsCorrect1() throws Exception {
        MapActivity mapsActivity = new MapActivity();
        double expectedDistance = 232.166;
        double actualDistance = mapsActivity.measure(41.090051, 23.549554, 41.088241, 23.548624);
        assertEquals(expectedDistance, actualDistance, 17);
    }
}