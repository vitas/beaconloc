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


import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.util.Log;

import com.kevalpatel.ringtonepicker.RingtonePickerDialog;
import com.kevalpatel.ringtonepicker.RingtonePickerListener;
import com.samebits.beacon.locator.R;
import com.samebits.beacon.locator.util.Constants;

/**
 * Created by vitas on 20/12/15.
 */
public class BeaconNotificationPageFragment extends PageBeaconFragment {

    private String mNoneRintoneValue = "";

    public static BeaconNotificationPageFragment newInstance(int page) {
        BeaconNotificationPageFragment detailFragment = new BeaconNotificationPageFragment();
        return detailFragment;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        this.addPreferencesFromResource(R.xml.preferences_beacon_notification);
        //default text
        mNoneRintoneValue = getResources().getString(R.string.pref_bn_none_notification_action_ringtone);
    }

    @Override
    protected void setData() {

        SwitchPreferenceCompat switch_enable = (SwitchPreferenceCompat) findPreference("bn_notification_action");
        switch_enable.setChecked(mActionBeacon.getNotification().isEnabled());

        switch_enable.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue instanceof Boolean) {
                    mActionBeacon.getNotification().setIsEnabled(((Boolean) newValue).booleanValue());
                }
                return true;
            }
        });

        setRingtonValue(mActionBeacon.getNotification().getRingtone());

        setMessageText(mActionBeacon.getNotification().getMessage());

        EditTextPreference msg_name = (EditTextPreference) findPreference("bn_notification_action_message");
        msg_name.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue instanceof String) {
                    setMessageText((String) newValue);
                }
                return true;
            }
        });

        SwitchPreferenceCompat vibrate_enable = (SwitchPreferenceCompat) findPreference("bn_notification_action_vibrate");
        vibrate_enable.setChecked(mActionBeacon.getNotification().isVibrate());

        vibrate_enable.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue instanceof Boolean) {
                    mActionBeacon.getNotification().setIsVibrate(((Boolean) newValue).booleanValue());
                }
                return true;
            }
        });
    }

    private void setMessageText(String newValue) {
        EditTextPreference msg_name = (EditTextPreference) findPreference("bn_notification_action_message");
        if (newValue != null && !newValue.isEmpty()) {
            msg_name.setSummary(newValue);
            mActionBeacon.getNotification().setMessage(newValue);
        } else {
            msg_name.setSummary(getString(R.string.pref_bn_default_notification_action_message));
            mActionBeacon.getNotification().setMessage(getString(R.string.pref_bn_default_notification_action_message));
        }
    }

    private String getRingtoneValue() {
        return findPreference("bn_notification_action_ringtone").getSummary().toString();
    }

    private void setRingtonValue(String value) {
        if (value != null && !value.isEmpty()) {
            findPreference("bn_notification_action_ringtone").setSummary(value);
        } else {
            //none
            findPreference("bn_notification_action_ringtone").setSummary(mNoneRintoneValue);
        }
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        if (preference.getKey().equals("bn_notification_action_ringtone")) {

            RingtonePickerDialog.Builder ringtonePickerBuilder = new RingtonePickerDialog
                    .Builder(getActivity(), getActivity().getSupportFragmentManager())

                    //Set title of the dialog.
                    //If set null, no title will be displayed.
                    .setTitle(getString(R.string.dialog_title_select_ringtone))

                    //Allow user to select default ringtone set in phone settings.
                    .displayDefaultRingtone(true)

                    //Allow user to select silent (i.e. No ringtone.).
                    .displaySilentRingtone(true)

                    //set the text to display of the positive (ok) button.
                    //If not set OK will be the default text.
                    .setPositiveButtonText("SET RINGTONE")

                    //set text to display as negative button.
                    //If set null, negative button will not be displayed.
                    .setCancelButtonText("CANCEL")

                    //Set flag true if you want to play the sample of the clicked tone.
                    .setPlaySampleWhileSelection(true)

                    //Set the callback listener.
                    .setListener(new RingtonePickerListener() {
                        @Override
                        public void OnRingtoneSelected(@NonNull String ringtoneName, Uri ringtoneUri) {
                            Log.d(Constants.TAG, String.format("Selected Ringtone Name : %s\nUri : %s", ringtoneName, ringtoneUri));

                            if (ringtoneUri != null) {
                                setRingtonValue(ringtoneUri.toString());
                                mActionBeacon.getNotification().setRingtone(ringtoneUri.toString());

                            } else {
                                // "Silent" was selected
                                setRingtonValue(null);
                                mActionBeacon.getNotification().setRingtone(mNoneRintoneValue);
                            }
                        }
                    });

            ringtonePickerBuilder.addRingtoneType(RingtonePickerDialog.Builder.TYPE_NOTIFICATION);

            //Display the dialog.
            ringtonePickerBuilder.show();

            return true;
        } else {
            return super.onPreferenceTreeClick(preference);
        }
    }

}
