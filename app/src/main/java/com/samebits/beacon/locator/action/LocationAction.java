/*
 *
 *  Copyright (c) 2016 SameBits UG. All rights reserved.
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

package com.samebits.beacon.locator.action;


import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;

import com.samebits.beacon.locator.R;
import com.samebits.beacon.locator.model.NotificationAction;

/**
 * Created by vitas on 03/01/16.
 */
public class LocationAction extends NoneAction {

    static final long MAX_AGE = 10000; // 10 seconds

    public LocationAction(String param, NotificationAction notification) {
        super(param, notification);
    }

    @Override
    public String execute(Context context) {
        Location bestLocation = null;
        final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        for (final String provider : locationManager.getAllProviders()) {

            final Location location = locationManager.getLastKnownLocation(provider);
            final long now = System.currentTimeMillis();
            if (location != null
                    && (bestLocation == null || location.getTime() > bestLocation.getTime())
                    && location.getTime() > now - MAX_AGE) {
                bestLocation = location;
            }
        }

        if (bestLocation != null) {
            final String position = bestLocation.getLatitude() + "," + bestLocation.getLongitude();
            final Uri uri = Uri.parse("geo:" + position + "?z=17&q=" + position);
            final Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
            mapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mapIntent);
        } else {
            return context.getString(R.string.action_location_no_location);
        }
        return super.execute(context);
    }

    @Override
    public boolean isParamRequired() {
        return true;
    }

    @Override
    public String toString() {
        return "LocationAction, param: " + param;
    }
}
