package com.damianmichalak.beacontest;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

public class MainApplication extends Application implements BootstrapNotifier {

    private static final String TAG = "MainApplication";
    private RegionBootstrap regionBootstrap;

    public void onCreate() {
        super.onCreate();
        final Region region = new Region("e2c56db5dffb48d2b060d0f5a71096e0", null, null, null);
        regionBootstrap = new RegionBootstrap(this, region);
    }

    @Override
    public void didEnterRegion(Region region) {
        Log.d(TAG, "did enter region.");
        final Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        this.startActivity(intent);
    }

    @Override
    public void didExitRegion(Region region) {

    }

    @Override
    public void didDetermineStateForRegion(int i, Region region) {

    }
}
