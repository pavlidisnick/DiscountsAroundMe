package com.tl.discountsaroundme.Controllers;

import android.location.LocationManager;

interface CheckInterface {
    boolean areGPSandNetworkEnabled(LocationManager lm);
}
