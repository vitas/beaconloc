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

package com.samebits.beacon.locator.data;

import com.samebits.beacon.locator.model.ActionBeacon;
import com.samebits.beacon.locator.model.TrackedBeacon;

import java.util.List;

/**
 * Created by vitas on 20/12/15.
 */
public interface StoreService {

    boolean createBeacon(final TrackedBeacon beacon);

    boolean updateBeacon(final TrackedBeacon beacon);

    boolean deleteBeacon(final String id, boolean cascade);

    TrackedBeacon getBeacon(final String id);

    List<TrackedBeacon> getBeacons();

    boolean updateBeaconDistance(final String id, double distance);

    boolean updateBeaconAction(ActionBeacon beacon);

    boolean createBeaconAction(ActionBeacon beacon);

    List<ActionBeacon> getBeaconActions(final String beaconId);

    boolean deleteBeaconAction(final int id);

    boolean deleteBeaconActions(final String beaconId);

    List<ActionBeacon> getAllEnabledBeaconActions();

    boolean updateBeaconActionEnable(final int id, boolean enable);

    List<ActionBeacon> getEnabledBeaconActionsByEvent(final int eventType, final String beaconId);

    boolean isBeaconExists(String id);
}
