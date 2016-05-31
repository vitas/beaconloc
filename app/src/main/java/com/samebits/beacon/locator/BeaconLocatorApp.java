/*
 *
 *  Copyright (c) 2015 SameBits UG. All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.samebits.beacon.locator;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Parcelable;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.util.Log;

import com.samebits.beacon.locator.data.DataManager;
import com.samebits.beacon.locator.injection.component.ApplicationComponent;
import com.samebits.beacon.locator.injection.component.DaggerApplicationComponent;
import com.samebits.beacon.locator.injection.module.ApplicationModule;
import com.samebits.beacon.locator.model.ActionBeacon;
import com.samebits.beacon.locator.model.ActionRegion;
import com.samebits.beacon.locator.model.DetectedBeacon;
import com.samebits.beacon.locator.model.IManagedBeacon;
import com.samebits.beacon.locator.model.RegionName;
import com.samebits.beacon.locator.model.TrackedBeacon;
import com.samebits.beacon.locator.util.BeaconUtil;
import com.samebits.beacon.locator.util.Constants;
import com.samebits.beacon.locator.util.PreferencesUtil;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


/**
 * Created by vitas on 18/10/15.
 */
public class BeaconLocatorApp extends Application implements BootstrapNotifier, RangeNotifier {

    ApplicationComponent applicationComponent;
    List<Region> mRegions = new ArrayList<>();
    List<TrackedBeacon> mBeacons = new ArrayList<>();
    private BackgroundPowerSaver mBackgroundPowerSaver;
    private BeaconManager mBeaconManager;
    private DataManager mDataManager;
    private RegionBootstrap mRegionBootstrap;

    public static BeaconLocatorApp from(@NonNull Context context) {
        return (BeaconLocatorApp) context.getApplicationContext();
    }

    public ApplicationComponent getComponent() {
        return applicationComponent;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        mBeaconManager = BeaconLocatorApp.from(this).getComponent().beaconManager();
        mDataManager = BeaconLocatorApp.from(this).getComponent().dataManager();

        initBeaconManager();
        enableBackgroundScan(PreferencesUtil.isBackgroundScan(this));

    }

    private void initBeaconManager() {
        mBeaconManager.setBackgroundMode(PreferencesUtil.isBackgroundScan(this));

        if (PreferencesUtil.isEddystoneLayoutUID(this)) {
            mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
        }
        if (PreferencesUtil.isEddystoneLayoutURL(this)) {
            mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_URL_LAYOUT));
        }
        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.ALTBEACON_LAYOUT));

        //konkakt?
        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"));

        mBeaconManager.setBackgroundBetweenScanPeriod(PreferencesUtil.getBackgroundScanInterval(this));

        mBeaconManager.setBackgroundScanPeriod(2000L);          // default is 10000L
        mBeaconManager.setForegroundBetweenScanPeriod(0L);      // default is 0L
        mBeaconManager.setForegroundScanPeriod(1100L);          // Default is 1100L

        mBackgroundPowerSaver = new BackgroundPowerSaver(this);
        mBeaconManager.setRangeNotifier(this);

        try {
            if (mBeaconManager.isAnyConsumerBound()) {
                mBeaconManager.updateScanPeriods();
            }
        } catch (RemoteException e) {
            Log.e(Constants.TAG, "update scan periods error", e);
        }
    }

    public void enableBackgroundScan(boolean enable) {
        if (enable) {
            Log.d(Constants.TAG, "Enable Background Scan");
            enableRegions();
            //loadTrackedBeacons();
        } else {
            Log.d(Constants.TAG, "Disable Background Scan");
            disableRegions();
        }
    }

    private void disableRegions() {
        if (mRegionBootstrap != null) {
            mRegionBootstrap.disable();
        }
    }

    /**
     * consider to use as a cache of beacons
     */
    private void loadTrackedBeacons() {
        mBeacons = mDataManager.getAllBeacons();
    }

    private void enableRegions() {
        mRegions = getAllEnabledRegions();
        if (mRegions.size() > 0) {
            mRegionBootstrap = new RegionBootstrap(this, mRegions);
        } else {
            Log.d(Constants.TAG, "Ignore Background scan, no regions");
        }
    }

    public List<Region> getAllEnabledRegions() {
        List<Region> regions = new ArrayList<>();
        List<ActionBeacon> actions = mDataManager.getAllEnabledBeaconActions();
        for (ActionBeacon action : actions) {
            regions.add(ActionRegion.parseRegion(action));
        }
        return regions;
    }

    @Override
    public void didEnterRegion(Region region) {
        RegionName regName = RegionName.parseString(region.getUniqueId());

        if (regName.isApplicationRegion()) {
            if (regName.getEventType() == ActionBeacon.EventType.EVENT_NEAR_YOU) {
                try {
                    mBeaconManager.startRangingBeaconsInRegion(region);
                } catch (RemoteException e) {
                    Log.e(Constants.TAG, "Error start ranging region: " + regName, e);
                }
            }
            if (regName.getEventType() == ActionBeacon.EventType.EVENT_ENTERS_REGION) {
                Intent intent = new Intent();
                intent.setAction(Constants.NOTIFY_BEACON_ENTERS_REGION);
                intent.putExtra("REGION", (Parcelable)region);
                getApplicationContext().sendOrderedBroadcast(intent, null);
            }
        }
    }

    @Override
    public void didExitRegion(Region region) {

        RegionName regName = RegionName.parseString(region.getUniqueId());

        if (regName.isApplicationRegion()) {
            if (regName.getEventType() == ActionBeacon.EventType.EVENT_NEAR_YOU) {
                try {
                    mBeaconManager.stopRangingBeaconsInRegion(region);
                    // set "far" proximity
                    mDataManager.updateBeaconDistance(regName.getBeaconId(), 99);
                } catch (RemoteException e) {
                    Log.e(Constants.TAG, "Error stop ranging region: " + regName, e);
                }
            }
            if (regName.getEventType() == ActionBeacon.EventType.EVENT_LEAVES_REGION) {
                Intent intent = new Intent();
                intent.setAction(Constants.NOTIFY_BEACON_LEAVES_REGION);
                intent.putExtra("REGION", (Parcelable) region);
                getApplicationContext().sendOrderedBroadcast(intent, null);
            }
        }
    }

    @Override
    public void didDetermineStateForRegion(int i, Region region) {
        Log.d(Constants.TAG, "Region State  " + i + " region " + region);
    }

    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
        if (beacons != null && beacons.size() > 0 && region != null) {
            RegionName regName = RegionName.parseString(region.getUniqueId());
            if (regName.isApplicationRegion()) {
                if (regName.getEventType() == ActionBeacon.EventType.EVENT_NEAR_YOU) {
                    Iterator<Beacon> iterator = beacons.iterator();
                    while (iterator.hasNext()) {
                        Beacon beacon = iterator.next();
                        TrackedBeacon tracked = mDataManager.getBeacon(regName.getBeaconId());
                        mDataManager.updateBeaconDistance(regName.getBeaconId(), beacon.getDistance());
                        if (tracked != null && BeaconUtil.isInProximity(IManagedBeacon.ProximityType.FAR, tracked.getDistance())) {
                            if (BeaconUtil.isInProximity(IManagedBeacon.ProximityType.NEAR, beacon.getDistance())
                                    || BeaconUtil.isInProximity(IManagedBeacon.ProximityType.IMMEDIATE, beacon.getDistance())) {

                                Intent intent = new Intent();
                                intent.setAction(Constants.NOTIFY_BEACON_NEAR_YOU_REGION);
                                intent.putExtra("REGION", (Parcelable)region);
                                getApplicationContext().sendOrderedBroadcast(intent, null);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        enableBackgroundScan(false);
    }


}