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
import android.support.annotation.NonNull;

import com.samebits.beacon.locator.data.DataManager;
import com.samebits.beacon.locator.injection.component.ApplicationComponent;
import com.samebits.beacon.locator.injection.component.DaggerApplicationComponent;
import com.samebits.beacon.locator.injection.component.DataComponent;
import com.samebits.beacon.locator.injection.module.ApplicationModule;
import com.samebits.beacon.locator.model.ActionBeacon;
import com.samebits.beacon.locator.model.DetectedBeacon;
import com.samebits.beacon.locator.util.PreferencesUtil;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
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

    private BackgroundPowerSaver mBackgroundPowerSaver;
    private BeaconManager mBeaconManager;
    private DataManager mDataManager;
    private RegionBootstrap mRegionBootstrap;
    List<Region> mRegions;


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

        if (PreferencesUtil.isBackgroundScan(this)) {

            mBeaconManager = BeaconLocatorApp.from(this).getComponent().beaconManager();
            mBeaconManager.setBackgroundMode(PreferencesUtil.isBackgroundScan(this));
            mDataManager = BeaconLocatorApp.from(this).getComponent().dataManager();
            loadRegions();
        }

    }

    public void loadRegions() {
        mRegions = mDataManager.getAllEnabledRegions();

        if (mRegions.size()>0) {
            mBeaconManager = applicationComponent.beaconManager();
            mRegionBootstrap = new RegionBootstrap(this, mRegions);
            mBackgroundPowerSaver = new BackgroundPowerSaver(this);
        }
    }

    @Override
    public void didEnterRegion(Region region) {

    }

    @Override
    public void didExitRegion(Region region) {

    }

    @Override
    public void didDetermineStateForRegion(int i, Region region) {

    }

    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
        if (beacons != null) {
            if (beacons.size() > 0 && region != null ) {
                Iterator<Beacon> iterator = beacons.iterator();
                while (iterator.hasNext()) {
                    DetectedBeacon dBeacon = new DetectedBeacon(iterator.next());
                    dBeacon.setTimeLastSeen(System.currentTimeMillis());

                    //this.mBeacons.put(dBeacon.getId(), dBeacon);
                }
            }
        }
    }
}