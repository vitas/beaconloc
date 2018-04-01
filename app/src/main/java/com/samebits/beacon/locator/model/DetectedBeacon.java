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

import com.samebits.beacon.locator.util.BeaconUtil;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.utils.UrlBeaconUrlCompressor;

/**
 * Created by vitas on 09/12/2015.
 */
public class DetectedBeacon extends Beacon implements IManagedBeacon {

    public static final int TYPE_EDDYSTONE_TLM = 32;
    public static final int TYPE_EDDYSTONE_UID = 0;
    public static final int TYPE_EDDYSTONE_URL = 16;
    public static final int TYPE_IBEACON_ALTBEACON = 1;

    protected long mLastSeen;

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


    @Override
    public long getTimeLastSeen() {
        return this.mLastSeen;
    }

    public void setTimeLastSeen(long lastSeen) {
        this.mLastSeen = lastSeen;
    }

    @Override
    public boolean equalTo(IManagedBeacon target) {
        return getId().equals(target.getId());
    }

    @Override
    public boolean isEddyStoneTLM() {
        return getBeaconTypeCode() == TYPE_EDDYSTONE_TLM;
    }

    @Override
    public boolean isEddyStoneUID() {
        return getBeaconTypeCode() == TYPE_EDDYSTONE_UID;
    }

    @Override
    public boolean isEddyStoneURL() {
        return getBeaconTypeCode() == TYPE_EDDYSTONE_URL;
    }

    @Override
    public boolean isEddystone() {
        return (getBeaconTypeCode() == TYPE_EDDYSTONE_UID)
                || (getBeaconTypeCode() == TYPE_EDDYSTONE_URL) || (getBeaconTypeCode() == TYPE_EDDYSTONE_TLM);
    }

    @Override
    public BeaconType getBeaconType() {
        if (isEddystone()) {
            switch (getBeaconTypeCode()) {
                case TYPE_IBEACON_ALTBEACON:
                    return BeaconType.ALTBEACON;
                case TYPE_EDDYSTONE_TLM:
                    return BeaconType.EDDYSTONE_TLM;
                case TYPE_EDDYSTONE_UID:
                    return BeaconType.EDDYSTONE_UID;
                case TYPE_EDDYSTONE_URL:
                    return BeaconType.EDDYSTONE_URL;
                default:
                    return BeaconType.EDDYSTONE;
            }
        }
        return BeaconType.IBEACON;
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

    @Override
    public String getId() {
        return getUUID() + ";" + getMajor() + ";" + getMinor() + ";FF:FF:FF:FF:FF:FF"; // ";" + getBluetoothAddress();
    }

    @Override
    public int getType() {
        return getBeaconTypeCode();
    }

    @Override
    public String getUUID() {
        return getId1().toString();
    }


    @Override
    public String getMajor() {
        if (isEddystone()) {
            return getId2().toHexString();
        }
        return getId2().toString();
    }

    @Override
    public String getMinor() {
        return getId3().toString();
    }

    @Override
    public String getEddystoneURL() {
        return UrlBeaconUrlCompressor.uncompress(getId1().toByteArray());
    }

    @Override
    public String toString() {
        if (isEddystone()) {
            if (isEddyStoneUID()) {
                return "Namespace: " + getUUID() + ", Instance: " + getMajor() + "\n" + "RSSI: " + getRssi() + " dBm, TX: " + getTxPower() + " dBm\n" + "Distance: " + BeaconUtil.getRoundedDistance(getDistance()) + "m";
            }
            if (isEddyStoneURL()) {
                return "URL: " + getEddystoneURL() + "\n" + "RSSI: " + getRssi() + " dBm, TX: " + getTxPower() + " dBm\n" + "Distance: " + BeaconUtil.getRoundedDistance(getDistance()) + "m";
            }
            return "UUID: " + getUUID() + ", Major: " + getMajor() + ", Minor: " + getMinor() + "\n" + "RSSI: " + getRssi() + " dBm, TX: " + getTxPower() + " dBm\n" + "Distance: " + BeaconUtil.getRoundedDistance(getDistance()) + "m";
        }
        return "UUID: " + getUUID() + ", Major: " + getMajor() + ", Minor: " + getMinor() + "\n" + "RSSI: " + getRssi() + " dBm, TX: " + getTxPower() + " dBm\n" + "Distance: " + BeaconUtil.getRoundedDistance(getDistance()) + "m";
    }
}
