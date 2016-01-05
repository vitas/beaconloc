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
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.Preference;

import com.samebits.beacon.locator.R;
import com.samebits.beacon.locator.model.ActionBeacon;

/**
 * Created by vitas on 20/12/15.
 */
public class BeaconEventPageFragment extends PageBeaconFragment {


    public static BeaconEventPageFragment newInstance(int page) {
        BeaconEventPageFragment detailFragment = new BeaconEventPageFragment();
        return detailFragment;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        this.addPreferencesFromResource(R.xml.preferences_beacon_event);
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {

        String key = preference.getKey();
        if (key.equals("be_event_enter_region")) {
            mActionBeacon.setEventType(ActionBeacon.EventType.EVENT_ENTERS_REGION);
        } else if (key.equals("be_event_leaves_region")) {
            mActionBeacon.setEventType(ActionBeacon.EventType.EVENT_LEAVES_REGION);
        } else {
            mActionBeacon.setEventType(ActionBeacon.EventType.EVENT_NEAR_YOU);
        }

        setData();

        return super.onPreferenceTreeClick(preference);
    }

    @Override
    protected void setData() {
        switch (mActionBeacon.getEventType()) {
            case EVENT_ENTERS_REGION:
                ((CheckBoxPreference) findPreference("be_event_enter_region")).setChecked(true);
                ((CheckBoxPreference) findPreference("be_event_leaves_region")).setChecked(false);
                ((CheckBoxPreference) findPreference("be_event_near_you")).setChecked(false);
                break;
            case EVENT_LEAVES_REGION:
                ((CheckBoxPreference) findPreference("be_event_leaves_region")).setChecked(true);
                ((CheckBoxPreference) findPreference("be_event_enter_region")).setChecked(false);
                ((CheckBoxPreference) findPreference("be_event_near_you")).setChecked(false);
                break;
            default:
                ((CheckBoxPreference) findPreference("be_event_near_you")).setChecked(true);
                ((CheckBoxPreference) findPreference("be_event_leaves_region")).setChecked(false);
                ((CheckBoxPreference) findPreference("be_event_enter_region")).setChecked(false);
        }
    }

}
