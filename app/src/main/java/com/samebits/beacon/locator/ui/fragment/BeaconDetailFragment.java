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

package com.samebits.beacon.locator.ui.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.samebits.beacon.locator.BeaconLocatorApp;
import com.samebits.beacon.locator.R;
import com.samebits.beacon.locator.data.DataManager;
import com.samebits.beacon.locator.model.IManagedBeacon;
import com.samebits.beacon.locator.util.BeaconUtil;
import com.samebits.beacon.locator.util.Constants;

/**
 * Created by vitas on 20/12/15.
 */
public class BeaconDetailFragment extends PreferenceFragmentCompat {

    protected DataManager mDataManager;
    protected IManagedBeacon mBeacon;

    public static BeaconDetailFragment newInstance(IManagedBeacon beacon, int page) {
        BeaconDetailFragment detailFragment = new BeaconDetailFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.ARG_PAGE, page);
        if (beacon != null) {
            args.putParcelable(Constants.ARG_BEACON, (Parcelable) beacon);
        }
        detailFragment.setArguments(args);
        return detailFragment;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        this.addPreferencesFromResource(R.xml.preferences_beacon_info);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = super.onCreateView(inflater, container, savedInstanceState);

        readArguments();
        setData();

        return fragmentView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataManager = BeaconLocatorApp.from(getActivity()).getComponent().dataManager();
    }

    protected void setData() {

        findPreference("bd_uuid_info").setSummary(mBeacon.getUUID());
        findPreference("bd_txpower_info").setSummary(String.format("%d dB", mBeacon.getTxPower()));
        findPreference("bd_rssi_info").setSummary(String.format("%d dB", mBeacon.getRssi()));
        findPreference("bd_bluetooth_name_info").setSummary((mBeacon.getBluetoothName() == null || mBeacon.getBluetoothName().equals("")) ? mBeacon.getBluetoothAddress() : mBeacon.getBluetoothName());
        findPreference("bd_distance_info").setSummary(BeaconUtil.getRoundedDistanceString(mBeacon.getDistance()) + " m");
        findPreference("bd_major_info").setSummary(mBeacon.getMajor());
        findPreference("bd_minor_info").setSummary(mBeacon.getMinor());

    }

    protected void readArguments() {
        if (getArguments() != null) {
            mBeacon = getArguments().getParcelable(Constants.ARG_BEACON);
        }
    }

    protected void storeBeacon() {
        if (mDataManager.storeBeacon(mBeacon)) {
            Log.d(Constants.TAG, String.format("Beacon %s is stored in db", mBeacon.getId()));
        }
    }


}
