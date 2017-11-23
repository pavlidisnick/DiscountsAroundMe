package com.tl.discountsaroundme;

import com.tl.discountsaroundme.firebase_data.StoreManager;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StoreManagerTest {
    @Test
    public void measureDistanceIsCorrect1() throws Exception {
        StoreManager mapFragment = new StoreManager();
        double expectedDistance = 232.166;
        double actualDistance = mapFragment.measure(41.090051, 23.549554, 41.088241, 23.548624);
        assertEquals(expectedDistance, actualDistance, 17);
    }
}