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

/**
 * Created by vitas on 25/10/15.
 */
public final class Constants {

    public static final String TAG = "BeaconLocator";
    public static final String DEFAULT_PROJECT_NAME = "default";
    public static final int SORT_DISTANCE_FAR_FIRST = 1;
    public static final int SORT_DISTANCE_NEAREST_FIRST = 0;
    public static final int SORT_UUID_MAJOR_MINOR = 2;
    public static final String TAG_FRAGMENT_SCAN_LIST = "SCAN_LIST";
    public static final String TAG_FRAGMENT_SCAN_RADAR = "SCAN_RADAR";
    public static final String TAG_FRAGMENT_TRACKED_BEACON_LIST = "TRACKED_BEACON_LIST";

    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String ARG_BEACON = "ARG_BEACON";

    public static final int REQ_UPDATED_TRACKED_BEACON = 10080;
    public static final int REQ_TASKER_ACTION_NAME_REQUEST = 10081;


    private Constants() {
    }


}
