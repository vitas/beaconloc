/*
 *
 *  Copyright (c) 2016 SameBits UG. All rights reserved.
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

package com.samebits.beacon.locator.model;

import com.samebits.beacon.locator.util.Constants;

/**
 * Created by vitas on 03/01/16.
 */
public class RegionName {
    private String prefix;
    private String actionName;
    private ActionBeacon.EventType eventType;

    protected RegionName(String prefix, ActionBeacon.EventType eventType, String actionName) {
        this.prefix = prefix;
        this.actionName = actionName;
        this.eventType = eventType;
    }

    public static RegionName parseString(String value) {
        if (value == null || value.length()==0) {
            throw new IllegalArgumentException("value is null");
        }
        String[] strings =  value.split("::");
        if (strings != null && strings.length > 2) {
            return new RegionName(strings[0], ActionBeacon.EventType.fromInt(Integer.parseInt(strings[1])), strings[2]);
        }
        return new RegionName("unknown", ActionBeacon.EventType.EVENT_ENTERS_REGION, value);
    }

    public boolean isApplicationRegion() {
        return this.prefix.equalsIgnoreCase(Constants.REGION_NAME_PREFIX);
    }

    public String getPrefix() {
        return prefix;
    }

    public String getActionName() {
        return actionName;
    }

    public ActionBeacon.EventType getEventType() {
        return eventType;
    }
}
