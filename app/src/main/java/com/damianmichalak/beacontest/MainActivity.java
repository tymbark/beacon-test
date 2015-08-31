package com.damianmichalak.beacontest;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;

public class MainActivity extends AppCompatActivity implements BeaconConsumer {

    private static final String TAG = "MainActivity";
    private BeaconManager beaconManager;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.web_view);

        beaconManager = BeaconManager.getInstanceForApplication(MainActivity.this);
        beaconManager.setBackgroundScanPeriod(1100l);
        beaconManager.setBackgroundBetweenScanPeriod(1100l);

        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));

        Log.d(TAG, "App ready");
//       UUID="e2 c5 6d b5 df fb 48 d2 b0 60 d0 f5 a7 10 96 e0"
//       MAJOR="00 16"
//       MINOR="00 08"
//       POWER="c5"

        beaconManager.bind(MainActivity.this);

    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.d(TAG, "I just saw an beacon for the first time!");
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadUrl("http://panel.mobil-ad.pl/malpka/");
                    }
                });
            }

            @Override
            public void didExitRegion(Region region) {
                Log.d(TAG, "I no longer see an beacon");
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadUrl("about:blank");
                    }
                });
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                Log.d(TAG, "I have just switched from seeing/not seeing beacons: " + state);
            }
        });

        try {
            beaconManager.startMonitoringBeaconsInRegion(
                    new Region("e2 c5 6d b5 df fb 48 d2 b0 60 d0 f5 a7 10 96 e0", null, null, null));
        } catch (RemoteException e) {
        }

        try {
            beaconManager.startMonitoringBeaconsInRegion(
                    new Region("e2c56db5dffb48d2b060d0f5a71096e0", null, null, null));
        } catch (RemoteException e) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }
}
