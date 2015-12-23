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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ProgressBar;

import com.samebits.beacon.locator.BeaconLocatorApp;
import com.samebits.beacon.locator.R;
import com.samebits.beacon.locator.data.DataManager;
import com.samebits.beacon.locator.ui.adapter.TrackedBeaconAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by vitas on 9/11/15.
 */
public class TrackedBeaconsFragment extends BaseFragment {

    @Bind(R.id.recycler_beacons)
    RecyclerView mListBeacons;
    @Bind(R.id.progress_indicator)
    ProgressBar mProgressBar;
    @Bind(R.id.empty_view)
    ViewStub mEmpty;
    EmptyView mEmptyView;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    private TrackedBeaconAdapter mBeaconsAdapter;
    private DataManager mDataManager;

    public static TrackedBeaconsFragment newInstance() {
        TrackedBeaconsFragment beaconsFragment = new TrackedBeaconsFragment();
        return beaconsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBeaconsAdapter = new TrackedBeaconAdapter(getActivity());
        mDataManager = BeaconLocatorApp.from(getActivity()).getComponent().dataManager();
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_tracked_beacons, container, false);
        ButterKnife.bind(this, fragmentView);

        setupToolbar();
        setupRecyclerView();
        loadBeacons();

        return fragmentView;
    }

    private void loadBeacons() {
        mBeaconsAdapter.insertBeacons(mDataManager.getAllBeacons());
        emptyListUpdate();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void setupToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setTitle(R.string.title_fragment_tracked_beacons);
        }
    }

    private void setupRecyclerView() {
        View viewFromEmpty = mEmpty.inflate();
        mEmptyView = new EmptyView(viewFromEmpty);
        mEmptyView.text.setText(getString(R.string.text_empty_list_tracked_beacons));

        mListBeacons.setLayoutManager(new LinearLayoutManager(getActivity()));
        mListBeacons.setHasFixedSize(true);
        mProgressBar.setVisibility(View.GONE);
        mListBeacons.setAdapter(mBeaconsAdapter);
    }


    private void emptyListUpdate() {
        if (mBeaconsAdapter.getItemCount() == 0) {
            mEmpty.setVisibility(View.VISIBLE);
            mEmptyView.text.setText(getString(R.string.text_empty_list_tracked_beacons));
        } else {
            mEmpty.setVisibility(View.GONE);
        }
    }


}