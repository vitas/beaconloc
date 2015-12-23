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

package com.samebits.beacon.locator.viewModel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.support.annotation.NonNull;
import android.text.format.DateUtils;
import android.view.View;

import com.samebits.beacon.locator.R;
import com.samebits.beacon.locator.model.DetectedBeacon;
import com.samebits.beacon.locator.model.IManagedBeacon;
import com.samebits.beacon.locator.util.BeaconUtil;

/**
 * Created by vitas on 19/10/15.
 */
public class BeaconViewModel extends BaseObservable {

    protected Context context;
    protected IManagedBeacon managedBeacon;

    public BeaconViewModel(Context context, @NonNull IManagedBeacon managedBeacon) {
        this.context = context;
        this.managedBeacon = managedBeacon;
    }

    public String getRssi() {
        return String.format("%d", managedBeacon.getRssi());
    }

    public String getTxPower() {
        return String.format("%d", managedBeacon.getTxPower());
    }

    public String getId() {
        return managedBeacon.getId();
    }

    public String getDistance() {
        return BeaconUtil.getRoundedDistanceString(managedBeacon.getDistance());
    }

    public String getName() {
        return (managedBeacon.getBluetoothName() == null || managedBeacon.getBluetoothName().isEmpty()) ? managedBeacon.getBluetoothAddress() :
                managedBeacon.getBluetoothName();
    }

    public String getSeenSince() {
        return DateUtils.getRelativeTimeSpanString(managedBeacon.getTimeLastSeen(), System.currentTimeMillis(), 0L).toString();
    }

    private boolean isLostBeacon() {
        return ((System.currentTimeMillis() - managedBeacon.getTimeLastSeen()) / 1000L > 5L);
    }

    public String getBeaconType() {
        return managedBeacon.getBeaconType().getString();
    }

    public String getProximity() {
        if (isLostBeacon()) {
            return getSeenSince();
        }
        return context.getString(BeaconUtil.getProximityResourceId(managedBeacon.getDistance()));
    }

    public int getProximityColor() {
        if (isLostBeacon()) {
            return context.getResources().getColor(R.color.hn_orange_dark);
        }
        return context.getResources().getColor(android.R.color.tab_indicator_text);
    }


    public int visibilityMajor() {
        return (managedBeacon.isEddyStoneURL()) ? View.GONE : View.VISIBLE;
    }

    public int visibilityMinor() {
        return (managedBeacon.isEddyStoneURL() || managedBeacon.isEddyStoneUID()) ? View.GONE : View.VISIBLE;
    }

    public String getUuid() {
        return managedBeacon.getUUID();
    }

    public String getMajor() {
        return managedBeacon.getMajor();
    }

    public String getMinor() {
        return managedBeacon.getMinor();
    }

    public String getNameUuid() {

        switch (managedBeacon.getType()) {
            case DetectedBeacon.TYPE_EDDYSTONE_URL:
                return context.getString(R.string.mv_text_url);
            case DetectedBeacon.TYPE_EDDYSTONE_UID:
                return context.getString(R.string.mv_text_namespace);
            default:
                return context.getString(R.string.mv_text_uuid);
        }
    }

    public String getNameMajor() {

        switch (managedBeacon.getType()) {
            case DetectedBeacon.TYPE_EDDYSTONE_UID:
                return context.getString(R.string.mv_text_instance);
            default:
                return context.getString(R.string.mv_text_major);
        }
    }

    public String getNameMinor() {
        return context.getString(R.string.mv_text_minor);
    }


    public View.OnClickListener onClickBeacon() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchBeaconDetailsActivity();
            }
        };
    }

    protected void launchBeaconDetailsActivity() {
        // do abstract?
    }
}
