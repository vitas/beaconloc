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
    String getId();

    int getType();

    String getUUID();

    String getMinor();

    String getMajor();

    double getDistance();

    String getEddystoneURL();

    long getTimeLastSeen();

    String getBluetoothName();

    String getBluetoothAddress();

    int getTxPower();

    int getRssi();

    boolean equalTo(IManagedBeacon target);

    BeaconType getBeaconType();

    boolean isEddyStoneTLM();

    boolean isEddyStoneUID();

    boolean isEddyStoneURL();

    boolean isEddystone();

    enum BeaconType {
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

        public static BeaconType fromInt(int value) {
            for (BeaconType type : BeaconType.values()) {
                if (type.ordinal() == value) {
                    return type;
                }
            }
            return UNSPECIFIED;
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

    enum ProximityType {
        FAR(0),
        NEAR(1),
        IMMEDIATE(2);

        private final int value;

        ProximityType(int value) {
            this.value = value;
        }

        public static ProximityType fromInt(int value) {
            for (ProximityType type : ProximityType.values()) {
                if (type.getValue() == value) {
                    return type;
                }
            }
            return FAR;
        }

        public int getValue() {
            return value;
        }
    }

}
