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

package com.samebits.beacon.locator.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.samebits.beacon.locator.BeaconLocatorApp;


/**
 * Created by vitas on 21/10/15.
 */
public final class PreferencesUtil {

    private static final String PREF_FILE_NAME = "bl_pref_file";
    private static final String PREF_KEY_USER = "key_user";
    private static final String PREF_KEY_PROJECT_NAME = "key_project_name";
    private static final String PREF_KEY_SCAN_BEACON_SORT = "key_scan_beacon_sort";


    private PreferencesUtil() {
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        return BeaconLocatorApp.from(context).getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public static void setUser(Context context, String user) {
        getSharedPreferences(context).edit().putString(PREF_KEY_USER, user).apply();
    }

    public static String getUser(Context context) {
        return getSharedPreferences(context).getString(PREF_KEY_USER, null);
    }

    public static String getProjectName(Context context) {
        return getSharedPreferences(context).getString(PREF_KEY_PROJECT_NAME, Constants.DEFAULT_PROJECT_NAME);
    }

    public static void setProjectName(Context context, String projName) {
        getSharedPreferences(context).edit().putString(PREF_KEY_PROJECT_NAME, projName).apply();
    }

    public static int getScanBeaconSort(Context context) {
        return getSharedPreferences(context).getInt(PREF_KEY_SCAN_BEACON_SORT, Constants.SORT_DISTANCE_NEAREST_FIRST);
    }

    public static void setScanBeaconSort(Context context, int sort) {
        getSharedPreferences(context).edit().putInt(PREF_KEY_SCAN_BEACON_SORT, sort).apply();
    }

    public static void clear(Context context) {
        getSharedPreferences(context).edit().clear().apply();
    }


}