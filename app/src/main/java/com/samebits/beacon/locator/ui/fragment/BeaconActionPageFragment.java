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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.preference.Preference;
import android.support.v7.preference.SwitchPreferenceCompat;

import com.samebits.beacon.locator.R;
import com.samebits.beacon.locator.model.IManagedBeacon;
import com.samebits.beacon.locator.util.Constants;

/**
 * Created by vitas on 20/12/15.
 */
public class BeaconActionPageFragment extends PageBeaconFragment {

    public static BeaconActionPageFragment newInstance(int page) {
        BeaconActionPageFragment detailFragment = new BeaconActionPageFragment();
        return detailFragment;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        this.addPreferencesFromResource(R.xml.preferences_beacon_action);
    }

    @Override
    protected void setData() {

        SwitchPreferenceCompat switch_manage = (SwitchPreferenceCompat) findPreference("ba_switch_enable");
        //switch_manage.setChecked(mBeacon.isTracked());

        switch_manage.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue instanceof Boolean) {
                    //TODO save action for beacon
                    isDirty = true;
                }
                return true;
            }
        });

    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == Constants.REQ_TASKER_ACTION_NAME_REQUEST) {
            if (data != null) {
                final String id = data.getStringExtra("id");
                final String taskName = data.getDataString();

               // mDataManager.updateBeaconAction(id, taskName);
                //updateAction(BeaconContract.ACTION_TASKER);
            }
        }
    }
}
