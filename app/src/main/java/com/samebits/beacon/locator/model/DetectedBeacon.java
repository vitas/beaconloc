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

import android.os.Parcel;
import android.os.Parcelable;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.utils.UrlBeaconUrlCompressor;

import java.text.DecimalFormat;

/**
 * Created by vitas on 09/12/2015.
 */
public class DetectedBeacon extends Beacon {

    public static final int TYPE_EDDYSTONE_TLM = 32;
    public static final int TYPE_EDDYSTONE_UID = 0;
    public static final int TYPE_EDDYSTONE_URL = 16;
    public static final int TYPE_IBEACON_ALTBEACON = 1;
    private long mLastSeen;

    public static final Parcelable.Creator<DetectedBeacon> CREATOR =
            new Parcelable.Creator<DetectedBeacon>() {
                @Override
                public DetectedBeacon createFromParcel(Parcel in) {
                    Beacon b = Beacon.CREATOR.createFromParcel(in);
                    DetectedBeacon dbeacon = new DetectedBeacon(b);
                    dbeacon.mLastSeen = in.readLong();
                    return dbeacon;
                }

                @Override
                public DetectedBeacon[] newArray(int size) {
                    return new DetectedBeacon[size];
                }
            };

    public DetectedBeacon(Beacon paramBeacon) {
        super(paramBeacon);
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        super.writeToParcel(parcel, flags);
        parcel.writeLong(mLastSeen);
    }

    public double getRoundedDistance() {
        return Math.ceil(getDistance() * 100.0D) / 100.0D;
    }

    public String getRoundedDistanceString() {
        double d = Math.ceil(getDistance() * 100.0D) / 100.0D;
        return new DecimalFormat("##0.00").format(d);
    }

    public long getTimeLastSeen() {
        return this.mLastSeen;
    }

    public void setTimeLastSeen(long lastSeen) {
        this.mLastSeen = lastSeen;
    }

    public boolean isEddyStoneTLM() {
        return getBeaconTypeCode() == 32;
    }

    public boolean isEddyStoneUID() {
        return getBeaconTypeCode() == 0;
    }

    public boolean isEddyStoneURL() {
        return getBeaconTypeCode() == 16;
    }

    public boolean isEddystone() {
        return (getBeaconTypeCode() == 0) || (getBeaconTypeCode() == 16) || (getBeaconTypeCode() == 32);
    }

    public Identifier getId2() {
        if (isEddyStoneURL()) {
            return Identifier.parse("");
        }
        return super.getId2();
    }

    public Identifier getId3() {
        if (isEddystone()) {
            return Identifier.parse("");
        }
        return super.getId3();
    }

    public String getId() {
        if ((getBeaconTypeCode() == 0) || (getBeaconTypeCode() == 16) || (getBeaconTypeCode() == 32)) {
            return getId1().toString() + ":" + getId2().toHexString() + "::" + getBluetoothAddress();
        }
        return getId1().toString() + ":" + getId2().toString() + ":" + getId3().toString() + "::" + getBluetoothAddress();
    }

    public String getEddystoneURL() {
        return UrlBeaconUrlCompressor.uncompress(getId1().toByteArray());
    }

    @Override
    public String toString() {
        if (isEddystone()) {
            if (isEddyStoneUID()) {
                return "Namespace: " + getId1().toString() + ", Instance: " + getId2().toString() + "\n" + "RSSI: " + getRssi() + " dBm, TX: " + getTxPower() + " dBm\n" + "Distance: " + getRoundedDistance() + "m";
            }
            if (isEddyStoneURL()) {
                return "URL: " + getEddystoneURL() + "\n" + "RSSI: " + getRssi() + " dBm, TX: " + getTxPower() + " dBm\n" + "Distance: " + getRoundedDistance() + "m";
            }
            return "UUID: " + getId1().toString() + ", Major: " + getId2().toString() + ", Minor: " + getId3().toString() + "\n" + "RSSI: " + getRssi() + " dBm, TX: " + getTxPower() + " dBm\n" + "Distance: " + getRoundedDistance() + "m";
        }
        return "UUID: " + getId1().toString() + ", Major: " + getId2().toString() + ", Minor: " + getId3().toString() + "\n" + "RSSI: " + getRssi() + " dBm, TX: " + getTxPower() + " dBm\n" + "Distance: " + getRoundedDistance() + "m";
    }
}
