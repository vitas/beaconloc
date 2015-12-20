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
 * Created by vitas on 20/12/15.
 */
public class TrackedBeacon {
    private String id;
    private String uuid = "";
    private long lastSeenTime;
    private String major = "";
    private String minor = "";
    private double txPower;
    private double rssi;
    private String distance;
    private String bleName;
    private String bleAddress;
    private int type;
    private String url;
    private boolean tracked;

    public TrackedBeacon(DetectedBeacon detectedBeacon) {
        setId(detectedBeacon.getId());
        setLastSeenTime(detectedBeacon.getTimeLastSeen());
        setBleName(detectedBeacon.getBluetoothName());
        setBleAddress(detectedBeacon.getBluetoothAddress());
        setUuid(detectedBeacon.getUUID());
        setRssi(detectedBeacon.getRssi());
        setTxPower(detectedBeacon.getTxPower());
        setType(detectedBeacon.getBeaconTypeCode());
        setUrl(detectedBeacon.getEddystoneURL());
        setDistance(detectedBeacon.getRoundedDistanceString());
        setMajor(detectedBeacon.getMajor());
        setMinor(detectedBeacon.getMinor());
    }

    public TrackedBeacon() {

    }

    public String getUuid() {
        return uuid;
    }

    public String getMajor() {
        return major;
    }

    public String getMinor() {
        return minor;
    }

    public double getTxPower() {
        return txPower;
    }

    public long getLastSeenTime() {
        return lastSeenTime;
    }

    public double getRssi() {
        return rssi;
    }

    public String getDistance() {
        return distance;
    }

    public String getBleName() {
        return bleName;
    }

    public String getBleAddress() {
        return bleAddress;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }

    public void setTxPower(double txPower) {
        this.txPower = txPower;
    }

    public void setLastSeenTime(long lastSeenTime) {
        this.lastSeenTime = lastSeenTime;
    }

    public void setRssi(double rssi) {
        this.rssi = rssi;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setBleName(String bleName) {
        this.bleName = bleName;
    }

    public void setBleAddress(String bleAddress) {
        this.bleAddress = bleAddress;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isTracked() {
        return tracked;
    }

    public void setTracked(boolean tracked) {
        this.tracked = tracked;
    }
}
