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
import com.samebits.beacon.locator.model.DetectedBeacon;
import com.samebits.beacon.locator.model.TrackedBeacon;

/**
 * Created by vitas on 09/12/2015.
 */
public final class BeaconUtil {

    public static int getProximityResourceId(double paramDouble) {
        if (paramDouble <= 0.5D) {
            return R.string.proximity_immediate;
        }
        if ((paramDouble > 0.5D) && (paramDouble <= 2.0D)) {
            return R.string.proximity_near;
        }
        return R.string.proximity_far;
    }

    public static boolean equalBeacons(DetectedBeacon detectedBeacon, TrackedBeacon trackedBeacon) {

        if (detectedBeacon != null && trackedBeacon!= null) {
            if (detectedBeacon.getUUID().equals(trackedBeacon.getUuid()) &&
                    trackedBeacon.getMajor().equals(detectedBeacon.getMajor()) &&
                    trackedBeacon.getMinor().equals(detectedBeacon.getMinor())) {
                return true;
            }
        }
        return false;
    }

}
