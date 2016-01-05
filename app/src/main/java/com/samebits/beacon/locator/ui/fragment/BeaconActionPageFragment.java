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
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.widget.Toast;

import com.samebits.beacon.locator.BeaconLocatorApp;
import com.samebits.beacon.locator.R;
import com.samebits.beacon.locator.action.ActionExecutor;
import com.samebits.beacon.locator.action.IAction;
import com.samebits.beacon.locator.model.ActionBeacon;
import com.samebits.beacon.locator.util.Constants;

/**
 * Created by vitas on 20/12/15.
 */
public class BeaconActionPageFragment extends PageBeaconFragment {

    private ActionExecutor mActionExecutor;

    public static BeaconActionPageFragment newInstance(int page) {
        BeaconActionPageFragment detailFragment = new BeaconActionPageFragment();
        return detailFragment;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        this.addPreferencesFromResource(R.xml.preferences_beacon_action);
        mActionExecutor = BeaconLocatorApp.from(getActivity()).getComponent().actionExecutor();
    }

    @Override
    protected void setData() {

        setActionParam1(mActionBeacon.getActionParam());

        EditTextPreference param_name = (EditTextPreference) findPreference("ba_action_param1");
        param_name.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue instanceof String) {
                    setActionParam1((String) newValue);
                }
                return true;
            }
        });

        setActionType(Integer.toString(mActionBeacon.getActionType().getValue()));

        final ListPreference action_list = (ListPreference) findPreference("ba_action_list");
        action_list.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue instanceof String) {
                    setActionType((String) newValue);
                }
                return true;
            }
        });

        Preference testButton = getPreferenceManager().findPreference("ba_action_test");
        testButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference arg0) {
                IAction action = mActionExecutor.actionBuilder(mActionBeacon.getActionType(),
                        mActionBeacon.getActionParam(), mActionBeacon.getNotification());
                String resMessage = mActionExecutor.storeAndExecute(action);
                if (resMessage != null ) {
                    Toast.makeText(getActivity(), resMessage, Toast.LENGTH_LONG).show();
                }                return true;
            }
        });

    }

    private ActionBeacon.ActionType getActionType() {
        final ListPreference action_list = (ListPreference) findPreference("ba_action_list");
        return ActionBeacon.ActionType.fromInt(Integer.parseInt(action_list.getValue()));
    }

    private void setActionType(String newValue) {
        final ListPreference action_list = (ListPreference) findPreference("ba_action_list");
        if (newValue != null && !newValue.isEmpty()) {
            int index = action_list.findIndexOfValue(newValue);
            action_list.setSummary(index >= 0
                    ? action_list.getEntries()[index]
                    : null);
            mActionBeacon.setActionType(ActionBeacon.ActionType.fromInt(index >= 0 ? index : 0));
        } else {
            action_list.setSummary(action_list.getEntries()[0]);
            mActionBeacon.setActionType(ActionBeacon.ActionType.fromInt(0));
        }
    }

    private String getActionParam1() {
        EditTextPreference param_name = (EditTextPreference) findPreference("ba_action_param1");
        return param_name.getSummary().toString();
    }

    private void setActionParam1(String newValue) {
        EditTextPreference param_name = (EditTextPreference) findPreference("ba_action_param1");
        if (newValue != null && !newValue.isEmpty()) {
            param_name.setSummary(newValue);
            mActionBeacon.setActionParam(newValue);
        } else {
            param_name.setSummary("");
            mActionBeacon.setActionParam("");
        }
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
