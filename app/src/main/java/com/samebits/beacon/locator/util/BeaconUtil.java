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

import java.text.DecimalFormat;

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

    public static double getRoundedDistance(double distance) {
        return Math.ceil(distance * 100.0D) / 100.0D;
    }

    public static String getRoundedDistanceString(double distance) {
        double d = Math.ceil(distance * 100.0D) / 100.0D;
        return new DecimalFormat("##0.00").format(d);
    }

    public static TrackedBeacon convertToTracked(DetectedBeacon detectedBeacon) {
        return new TrackedBeacon(detectedBeacon);
    }
}
