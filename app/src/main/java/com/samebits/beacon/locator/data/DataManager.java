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

package com.samebits.beacon.locator.data;

import android.content.Context;

import com.samebits.beacon.locator.BeaconLocatorApp;
import com.samebits.beacon.locator.injection.component.DaggerDataComponent;
import com.samebits.beacon.locator.injection.module.DataModule;
import com.samebits.beacon.locator.model.ActionBeacon;
import com.samebits.beacon.locator.model.ActionRegion;
import com.samebits.beacon.locator.model.TrackedBeacon;

import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by vitas on 20/12/15.
 */
public class DataManager {

    @Inject
    protected StoreService mStoreService;
    private List<ActionBeacon> mActionBeaconCache = new ArrayList<>();

    public DataManager(Context context) {
        injectDependencies(context);
    }

    protected void injectDependencies(Context context) {
        DaggerDataComponent.builder()
                .applicationComponent(BeaconLocatorApp.from(context).getComponent())
                .dataModule(new DataModule(context))
                .build()
                .inject(this);
    }

    public boolean createBeacon(TrackedBeacon beacon) {
        return mStoreService.createBeacon(beacon);
    }

    public boolean updateBeacon(TrackedBeacon beacon) {
        return mStoreService.updateBeacon(beacon);
    }

    public TrackedBeacon getBeacon(String id) {
        return mStoreService.getBeacon(id);
    }

    public List<TrackedBeacon> getAllBeacons() {
        return mStoreService.getBeacons();
    }

    public boolean createBeaconAction(ActionBeacon beacon) {
        return mStoreService.createBeaconAction(beacon);
    }

    public boolean updateBeaconAction(ActionBeacon beacon) {
        return mStoreService.updateBeaconAction(beacon);
    }

    public boolean deleteActionBeacon(int id) {
        return mStoreService.deleteBeaconAction(id);
    }

    public boolean deleteBeacon(String beaconId) {
        return mStoreService.deleteBeacon(beaconId);
    }

    public List<Region> getAllEnabledRegions() {
        List<Region> regions = new ArrayList<>();
        List<ActionBeacon> actions = mStoreService.getAllEnabledBeaconActions();
        mActionBeaconCache.clear();
        mActionBeaconCache.addAll(actions);
        for(ActionBeacon action: actions){
            regions.add(ActionRegion.parseRegion(action));
        }
        return regions;
    }

    public boolean enableBeaconAction(int id, boolean enable) {
        return mStoreService.updateBeaconActionEnable(id, enable);
    }

    public List<ActionBeacon> getActionBeacons(ActionBeacon.EventType eventType, String actionName) {
        if(!mActionBeaconCache.isEmpty()) {
            List<ActionBeacon> actionBeacons = new ArrayList<>();
            for(ActionBeacon action: mActionBeaconCache) {
                if(action.getName().equalsIgnoreCase(actionName) && action.getEventType() == eventType) {
                    actionBeacons.add(action);
                }
            }
            if (actionBeacons.size()>0) {
                return actionBeacons;
            }
        }
        return mStoreService.getActionBeacons(eventType.getValue(), actionName);
    }

}