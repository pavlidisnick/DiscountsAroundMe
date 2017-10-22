package com.tl.discountsaroundme;

import org.junit.Test;
import static org.junit.Assert.*;

public class MapsActivityTest {
    @Test
    public void measureDistanceIsCorrect1() throws Exception {
        MapsActivity mapsActivity = new MapsActivity();
        double expectedDistance = 232.166;
        double actualDistance = mapsActivity.measure(41.090051, 23.549554, 41.088241, 23.548624);
        assertEquals(expectedDistance, actualDistance, 17);
    }
}