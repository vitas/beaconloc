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

package com.samebits.beacon.locator.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.samebits.beacon.locator.R;
import com.samebits.beacon.locator.model.IManagedBeacon;
import com.samebits.beacon.locator.ui.fragment.DetailFragment;
import com.samebits.beacon.locator.util.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;


public class BeaconActivity extends BaseActivity {

    protected int mMode = Constants.LIVE_BEACON_MODE;
    protected IManagedBeacon mBeacon;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, BeaconActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon);
        ButterKnife.bind(this);

        setupToolbar();
        readExtras();

    }

    private void setupToolbar() {

        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void readExtras() {
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            mMode = intent.getIntExtra("MODE", Constants.TRACKED_BEACON_MODE);
            mBeacon = intent.getExtras().getParcelable("BEACON");
            addDetailBeaconFragment(mBeacon);
        }
    }

    private void switchMode(int mode) {
        switch (mode) {
            case Constants.LIVE_BEACON_MODE:
                break;
            case Constants.TRACKED_BEACON_MODE:
                break;
        }
    }

    private void addDetailBeaconFragment(IManagedBeacon beacon) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager != null) {
            if (checkFragmentInstance(R.id.content_frame, DetailFragment.class) == null) {
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.content_frame, DetailFragment.newInstance(beacon), Constants.TAG_FRAGMENT_BEACON_DETAIL)
                        .commit();
            }
        }
    }

}
