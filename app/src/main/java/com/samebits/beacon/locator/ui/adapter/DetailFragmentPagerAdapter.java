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
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.samebits.beacon.locator.R;
import com.samebits.beacon.locator.model.ActionBeacon;
import com.samebits.beacon.locator.ui.fragment.BeaconActionPageFragment;
import com.samebits.beacon.locator.ui.fragment.BeaconDetailPageFragment;
import com.samebits.beacon.locator.ui.fragment.BeaconEventPageFragment;
import com.samebits.beacon.locator.ui.fragment.BeaconNotificationPageFragment;
import com.samebits.beacon.locator.ui.fragment.PageBeaconFragment;
import com.samebits.beacon.locator.util.Constants;

/**
 * Created by vitas on 25/12/15.
 */
public class DetailFragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {
    private int tabTitleResources[] = new int[]{R.string.tab_title_beacon_info, R.string.tab_title_beacon_event, R.string.tab_title_beacon_action,
            R.string.tab_title_beacon_notification};
    private Context mContext;
    private ActionBeacon mActionBeacon;

    public DetailFragmentPagerAdapter(FragmentManager fm, ActionBeacon beacon, Context context) {
        super(fm);
        this.mContext = context;
        this.mActionBeacon = beacon;
    }

    @Override
    public int getCount() {
        return tabTitleResources.length;
    }

    @Override
    public PageBeaconFragment getItem(int position) {
        Bundle args = new Bundle();
        args.putInt(Constants.ARG_PAGE, position + 1);

        PageBeaconFragment frg = BeaconDetailPageFragment.newInstance(position + 1);
        switch (position) {
            case 0:
                frg = BeaconDetailPageFragment.newInstance(position + 1);
                break;
            case 1:
                frg = BeaconEventPageFragment.newInstance(position + 1);
                break;
            case 2:
                frg = BeaconActionPageFragment.newInstance(position + 1);
                break;
            case 3:
                frg = BeaconNotificationPageFragment.newInstance(position + 1);
                break;
        }

        if (mActionBeacon != null) {
            args.putParcelable(Constants.ARG_ACTION_BEACON, mActionBeacon);
            frg.setArguments(args);
        }
        return frg;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return this.mContext.getString(tabTitleResources[position]);
    }
}
