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

import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vitas on 03/01/16.
 */
public class ActionRegion {

    public static Region parseRegion(ActionBeacon actionBeacon) {
        if (actionBeacon == null) {
            throw new IllegalArgumentException("ActionBeacon object is null");
        }
        String[] idents = actionBeacon.getBeaconId().split(";");
        if (idents == null || idents.length < 3) {
            throw new IllegalArgumentException("ActionBeacon has invalid id");
        }
        List<Identifier> identifiers = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            identifiers.add(Identifier.parse(idents[i]));
        }
        return new Region(RegionName.buildRegionNameId(actionBeacon), identifiers, idents[3]);
    }

}
