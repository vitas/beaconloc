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

package com.samebits.beacon.locator.ui.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.samebits.beacon.locator.R;
import com.samebits.beacon.locator.model.IManagedBeacon;
import com.samebits.beacon.locator.ui.fragment.BeaconActionPageFragment;
import com.samebits.beacon.locator.ui.fragment.BeaconDetailPageFragment;
import com.samebits.beacon.locator.ui.fragment.PageBeaconFragment;

/**
 * Created by vitas on 25/12/15.
 */
public class DetailFragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private int tabTitleResources[] = new int[]{R.string.tab_title_beacon_info, R.string.tab_title_beacon_actions};
    private Context mContext;
    private IManagedBeacon mBeacon;

    public DetailFragmentPagerAdapter(FragmentManager fm, IManagedBeacon beacon, Context context) {
        super(fm);
        this.mContext = context;
        this.mBeacon = beacon;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public PageBeaconFragment getItem(int position) {
        switch (position) {
            case 0:
                return BeaconDetailPageFragment.newInstance(mBeacon, position + 1);
            case 1:
                return BeaconActionPageFragment.newInstance(mBeacon, position + 1);
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return this.mContext.getString(tabTitleResources[position]);
    }
}
