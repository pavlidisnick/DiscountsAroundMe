package com.tl.discountsaroundme.controllers;

import android.location.LocationManager;

interface CheckInterface {
    boolean areGPSandNetworkEnabled(LocationManager lm);
}
