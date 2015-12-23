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

package com.samebits.beacon.locator.model;


/**
 * Created by vitas on 23/12/15.
 */
public interface IManagedBeacon {
    public String getId();
    public int getType();
    public String getUUID();
    public String getMinor();
    public String getMajor();
    public double getDistance();
    public String getEddystoneURL();
    public long getTimeLastSeen();
    public String getBluetoothName();
    public String getBluetoothAddress();
    public int getTxPower();
    public int getRssi();
    public boolean equalTo(IManagedBeacon target);
    public BeaconType getBeaconType();
    public boolean isEddyStoneTLM();
    public boolean isEddyStoneUID();
    public boolean isEddyStoneURL();
    public boolean isEddystone();
    public boolean isTracked();

    public enum BeaconType {
        UNSPECIFIED("Unspecified"),
        EDDYSTONE("Eddystone"),
        EDDYSTONE_URL("Eddystone-URL"),
        EDDYSTONE_UID("Eddystone-UID"),
        EDDYSTONE_TLM("Eddystone-TLM"),
        IBEACON("iBeacon"),
        ALTBEACON("AltBeacon");

        private String string;

        BeaconType(String string) {
            this.string = string;
        }

        public static BeaconType fromString(String string) {
            if (string != null) {
                for (BeaconType status : BeaconType.values()) {
                    if (string.equalsIgnoreCase(status.string)) {
                        return status;
                    }
                }
            }
            return null;
        }

        public String getString() {
            return string;
        }

    }



}
