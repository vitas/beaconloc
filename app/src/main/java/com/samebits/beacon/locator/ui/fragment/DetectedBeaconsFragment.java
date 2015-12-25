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
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ProgressBar;

import com.samebits.beacon.locator.R;
import com.samebits.beacon.locator.ui.adapter.DetectedBeaconAdapter;
import com.samebits.beacon.locator.util.Constants;
import com.samebits.beacon.locator.util.PreferencesUtil;

import org.altbeacon.beacon.Beacon;

import java.util.Collection;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by vitas on 9/11/15.
 */
public class DetectedBeaconsFragment extends ScanFragment {

    //40 sec timeout for scanning
    static final int SCAN_TIMEOUT = 40000;
    protected CountDownTimer mTimer;
    @Bind(R.id.recycler_detected_beacons)
    RecyclerView mListBeacons;
    @Bind(R.id.progress_indicator)
    ProgressBar mProgressBar;
    @Bind(R.id.empty_scan_view)
    ViewStub mEmpty;
    EmptyView mEmptyView;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    private DetectedBeaconAdapter mBeaconsAdapter;


    public static DetectedBeaconsFragment newInstance() {
        DetectedBeaconsFragment beaconsFragment = new DetectedBeaconsFragment();
        return beaconsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBeaconsAdapter = new DetectedBeaconAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_scan_beacons, container, false);
        ButterKnife.bind(this, fragmentView);

        setupToolbar();
        setupRecyclerView();
        setupTimer();

        return fragmentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mTimer.cancel();
        ButterKnife.unbind(this);
    }

    private void setupToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(R.string.title_fragment_detected_beacons);
        }
    }

    private void setupRecyclerView() {
        View viewFromEmpty = mEmpty.inflate();
        mEmptyView = new EmptyView(viewFromEmpty);
        mEmptyView.text.setText(getString(R.string.text_please_start_scan));

        mListBeacons.setLayoutManager(new LinearLayoutManager(getActivity()));
        mListBeacons.setHasFixedSize(true);
        mProgressBar.setVisibility(View.GONE);
        // mListBeacons.addItemDecoration(new DividerItemDecoration(getActivity()));
        mListBeacons.setAdapter(mBeaconsAdapter);
    }

    private void setupTimer() {
        mTimer = new CountDownTimer(SCAN_TIMEOUT, PreferencesUtil.getManualScanTimeout(getApplicationContext())) {
            public void onFinish() {
                stopScanTimeout();
            }

            public void onTick(long tick) {
            }
        };
    }

    private void stopScanTimeout() {
        stopScan();
        mEmptyView.text.setText(getString(R.string.text_scan_not_found));
    }

    private void emptyListSetup() {
        if (mBeaconsAdapter.getItemCount() == 0) {
            mEmpty.setVisibility(View.VISIBLE);
            mEmptyView.text.setText(getString(R.string.text_please_start_scan));
        } else {
            mEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    public void startScan() {
        mProgressBar.setVisibility(View.VISIBLE);
        mEmpty.setVisibility(View.GONE);
        mBeaconsAdapter.removeAll();
        mTimer.start();
        super.startScan();
    }

    @Override
    public void stopScan() {
        mProgressBar.setVisibility(View.GONE);
        super.stopScan();
        emptyListSetup();
    }

    @Override
    public void onCanScan() {
        emptyListSetup();
    }

    @Override
    public void updateBeaconList(final Collection<Beacon> beacons, final org.altbeacon.beacon.Region region) {
        if (getActivity()!=null) {
            //update list, even nothing, we want update last seen time on detected beacons
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    mBeaconsAdapter.notifyDataSetChanged();
                    Log.d(Constants.TAG, "called on region " + region.toString());
                }
            });
        }
    }

    @Override
    public void updateBeaconList(final Collection<Beacon> beacons) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    mBeaconsAdapter.insertBeacons(beacons);
                    mBeaconsAdapter.sort(PreferencesUtil.getScanBeaconSort(getApplicationContext()));
                    mTimer.cancel();
                }
            });
        }
    }

}