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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.samebits.beacon.locator.BeaconLocatorApp;
import com.samebits.beacon.locator.R;
import com.samebits.beacon.locator.data.DataManager;
import com.samebits.beacon.locator.model.IManagedBeacon;
import com.samebits.beacon.locator.util.Constants;

import butterknife.ButterKnife;

/**
 * Created by vitas on 20/12/15.
 */
public class BeaconDetailFragment extends BaseFragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String ARG_BEACON = "ARG_BEACON";
    protected DataManager mDataManager;
    protected IManagedBeacon mBeacon;
    private int mPage;

    public static BeaconDetailFragment newInstance(IManagedBeacon beacon, int page) {
        BeaconDetailFragment detailFragment = new BeaconDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        if (beacon != null) {
            // detailFragment.setBeacon(beacon);
            args.putParcelable(ARG_BEACON, (Parcelable) beacon);
        }
        detailFragment.setArguments(args);
        return detailFragment;
    }

    public void setBeacon(IManagedBeacon beacon) {
        this.mBeacon = beacon;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDataManager = BeaconLocatorApp.from(getActivity()).getComponent().dataManager();

        readArguments();
        setRetainInstance(true);

    }

    private void readArguments() {
        if (getArguments() != null) {
            mPage = getArguments().getInt(ARG_PAGE);
            mBeacon = getArguments().getParcelable(ARG_BEACON);
        }
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

    private void storeBeacon() {
        if (mDataManager.storeBeacon(mBeacon)) {
            Log.d(Constants.TAG, String.format("Beacon %s is stored in db", mBeacon.getId()));
        }
    }
}
