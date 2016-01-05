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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.samebits.beacon.locator.R;
import com.samebits.beacon.locator.model.ActionBeacon;
import com.samebits.beacon.locator.ui.adapter.DetailFragmentPagerAdapter;
import com.samebits.beacon.locator.util.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;


public class BeaconActionActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.viewpager)
    ViewPager viewPager;
    @Bind(R.id.sliding_tabs)
    TabLayout slidingTabs;

    private ActionBeacon mActionBeacon;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, BeaconActionActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon);
        ButterKnife.bind(this);

        setupToolbar();
        readExtras();
        setupTabs();

    }

    protected void readExtras() {
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            mActionBeacon = intent.getExtras().getParcelable(Constants.ARG_ACTION_BEACON);
        }
    }

    @Override
    public void finish() {
        //Constants.REQ_UPDATED_ACTION_BEACON
        int resultCode = Activity.RESULT_OK;
        Intent resultIntent = new Intent();
        resultIntent.putExtra(Constants.ARG_ACTION_BEACON, mActionBeacon);
        setResult(resultCode, resultIntent);
        super.finish();
    }

    private void setupTabs() {
        viewPager.setAdapter(new DetailFragmentPagerAdapter(getSupportFragmentManager(),
                mActionBeacon,
                BeaconActionActivity.this));

        slidingTabs.setupWithViewPager(viewPager);
    }

    private void setupToolbar() {

        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
