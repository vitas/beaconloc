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
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.samebits.beacon.locator.BeaconLocatorApp;
import com.samebits.beacon.locator.R;
import com.samebits.beacon.locator.db.DataManager;
import com.samebits.beacon.locator.model.DetectedBeacon;
import com.samebits.beacon.locator.model.TrackedBeacon;
import com.samebits.beacon.locator.util.Constants;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by vitas on 20/12/15.
 */
public class DetailFragment extends Fragment {

    protected DataManager mDataManager;
    protected DetectedBeacon mBeacon;

    public static DetailFragment newInstance(DetectedBeacon beacon) {
        DetailFragment detailFragment = new DetailFragment();
        if (beacon != null) {
            Bundle args = new Bundle();
            args.putParcelable("BEACON", beacon);
            detailFragment.setArguments(args);
        }
        return detailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDataManager = BeaconLocatorApp.from(getActivity()).getComponent().dataManager();

        setRetainInstance(true);

        //FIXME
        getBeacon();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_beacon_detail, container, false);
        ButterKnife.bind(this, fragmentView);

        return fragmentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void getBeacon() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            //mMode = bundle.getInt("MODE", Constants.LIVE_BEACON_MODE );
            mBeacon = bundle.getParcelable("BEACON");
            if (mBeacon != null) {
                TrackedBeacon trackedBeacon = new TrackedBeacon(mBeacon);
                if (mDataManager.storeBeacon(trackedBeacon)) {
                    Log.d(Constants.TAG, String.format("Beacon %s is stored in db", mBeacon.getId()));
                }
            }
        }

        //FIXME remove it after completed
        List<TrackedBeacon> l = mDataManager.getAllBeacons();
        if (l.size() > 0) {
            Log.d(Constants.TAG, "found first beacons: " + l.get(0).getId());
        }
        TrackedBeacon trackedBeacon = mDataManager.getBeacon(mBeacon.getId());
        if(trackedBeacon!= null) {
            Log.d(Constants.TAG, String.format("Beacon %s is loaded from db", trackedBeacon.getId()));

        }

    }
}
