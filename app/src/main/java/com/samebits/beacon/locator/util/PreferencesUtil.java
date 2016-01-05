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
import android.preference.PreferenceManager;


/**
 * Created by vitas on 21/10/15.
 */
public final class PreferencesUtil {

    private PreferencesUtil() {
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
        //return BeaconLocatorApp.from(mContext).getSharedPreferences("beaconloc_pref_name", Context.MODE_PRIVATE);
    }

    public static String getDefaultRegionName(Context context) {
        return getSharedPreferences(context).getString("scan_default_region_text", Constants.DEFAULT_PROJECT_NAME);
    }

    public static int getScanBeaconSort(Context context) {
        return Integer.parseInt(getSharedPreferences(context).getString("scan_sorting_order_list", Integer.toString(Constants.SORT_DISTANCE_NEAREST_FIRST)));
    }

    public static void setScanBeaconSort(Context context, int sort) {
        getSharedPreferences(context).edit().putString("scan_sorting_order_list", Integer.toString(sort)).apply();
    }

    public static int getManualScanTimeout(Context context) {
        return Integer.parseInt(getSharedPreferences(context).getString("scan_manual_timeout_list", "30000"));
    }

    public static void clear(Context context) {
        getSharedPreferences(context).edit().clear().apply();
    }

    public static boolean isBackgroundScan(Context context) {
        return getSharedPreferences(context).getBoolean("scan_background_switch", true);
    }

    public static int getBackgroundScanInterval(Context context) {
        return Integer.parseInt(getSharedPreferences(context).getString("scan_background_timeout_list", "60000"));
    }

    public static boolean isEddystoneLayoutUID(Context context) {
        return getSharedPreferences(context).getBoolean("scan_parser_layout_eddystone_uid", true);
    }

    public static boolean isEddystoneLayoutURL(Context context) {
        return getSharedPreferences(context).getBoolean("scan_parser_layout_eddystone_url", true);
    }

    public static int getSilentModeProfile(Context context) {
        return getSharedPreferences(context).getInt("silent_profile_mode", 2);
    }

    public static void setSilentModeProfile(Context context, int mode) {
        getSharedPreferences(context).edit().putInt("silent_profile_mode", mode).apply();
    }

}