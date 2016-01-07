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

package com.samebits.beacon.locator.util;

import com.samebits.beacon.locator.R;
import com.samebits.beacon.locator.model.ActionBeacon;
import com.samebits.beacon.locator.model.IManagedBeacon;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vitas on 09/12/2015.
 */
public final class BeaconUtil {

    public static IManagedBeacon.ProximityType getProximity(double paramDouble) {
        if (paramDouble <= 0.5D) {
            return IManagedBeacon.ProximityType.IMMEDIATE;
        }
        if ((paramDouble > 0.5D) && (paramDouble <= 3.0D)) {
            return IManagedBeacon.ProximityType.NEAR;
        }
        return IManagedBeacon.ProximityType.FAR;
    }

    public static int getProximityResourceId(IManagedBeacon.ProximityType proximityType) {
        if (proximityType == IManagedBeacon.ProximityType.IMMEDIATE) {
            return R.string.proximity_immediate;
        }
        if (proximityType == IManagedBeacon.ProximityType.NEAR) {
            return R.string.proximity_near;
        }
        return R.string.proximity_far;
    }

    public static int getEventTypeResourceId(ActionBeacon.EventType eventType) {
        switch (eventType) {
            case EVENT_LEAVES_REGION:
                return R.string.mv_event_type_leaves_region;
            case EVENT_ENTERS_REGION:
                return R.string.mv_event_type_enters_region;
            case EVENT_NEAR_YOU:
                return R.string.mv_event_type_near_you;
        }
        return R.string.action_alarm_text_title;
    }

    public static boolean isInProximity(IManagedBeacon.ProximityType proximityType, double paramDouble) {
        return (getProximity(paramDouble) == proximityType) ? true : false;
    }

    public static double getRoundedDistance(double distance) {
        return Math.ceil(distance * 100.0D) / 100.0D;
    }

    public static String getRoundedDistanceString(double distance) {
        return new DecimalFormat("##0.00").format(getRoundedDistance(distance));
    }


    public static Map<String, IManagedBeacon> sortBecons(Map<String, IManagedBeacon> beacons, final int sortMode) {
        Object localObject = new ArrayList(beacons.entrySet());
        Collections.sort((List) localObject, new Comparator() {
            @Override
            public int compare(Object lhs, Object rhs) {
                return compare((Map.Entry<String, IManagedBeacon>) (lhs), (Map.Entry<String, IManagedBeacon>) (rhs));
            }

            private int compare(Map.Entry<String, IManagedBeacon> obj1, Map.Entry<String, IManagedBeacon> obj2) {
                if (sortMode == Constants.SORT_UUID_MAJOR_MINOR) {
                    int i = obj1.getValue().getUUID().compareTo(obj2.getValue().getUUID());
                    if (i != 0) {
                        return i;
                    }
                    i = obj1.getValue().getMajor().compareTo(obj2.getValue().getMajor());
                    if (i != 0) {
                        return i;
                    }
                    if ((!obj1.getValue().isEddystone()) && (!obj2.getValue().isEddystone())) {
                        return obj1.getValue().getMinor().compareTo(obj2.getValue().getMinor());
                    }
                    return 0;
                }
                double d1 = obj1.getValue().getDistance();
                double d2 = obj2.getValue().getDistance();
                if (d1 == d2) {
                    return 0;
                }
                if (sortMode == Constants.SORT_DISTANCE_NEAREST_FIRST) {
                    if (d1 < d2) {
                        return -1;
                    }
                    return 1;
                }
                if (sortMode == Constants.SORT_DISTANCE_FAR_FIRST) {
                    if (d1 < d2) {
                        return 1;
                    }
                    return -1;
                }
                return -1;
            }
        });
        LinkedHashMap localLinkedHashMap = new LinkedHashMap();
        localObject = ((List) localObject).iterator();
        while (((Iterator) localObject).hasNext()) {
            Map.Entry localEntry = (Map.Entry) ((Iterator) localObject).next();
            localLinkedHashMap.put(localEntry.getKey(), localEntry.getValue());
        }
        return localLinkedHashMap;
    }
}
