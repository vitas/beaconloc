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

/**
 * Created by vitas on 31/12/15.
 */
public class NotificationAction implements Parcelable {
    public static final Creator<NotificationAction> CREATOR = new Creator<NotificationAction>() {
        @Override
        public NotificationAction createFromParcel(Parcel in) {
            return new NotificationAction(in);
        }

        @Override
        public NotificationAction[] newArray(int size) {
            return new NotificationAction[size];
        }
    };
    private boolean isEnabled;
    private String message = "";
    private String ringtone = "";
    private boolean isVibrate;

    public NotificationAction() {
    }

    protected NotificationAction(Parcel in) {
        isEnabled = in.readByte() != 0;
        message = in.readString();
        ringtone = in.readString();
        isVibrate = in.readByte() != 0;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRingtone() {
        return ringtone;
    }

    public void setRingtone(String ringtone) {
        this.ringtone = ringtone;
    }

    public boolean isVibrate() {
        return isVibrate;
    }

    public void setIsVibrate(boolean isVibrate) {
        this.isVibrate = isVibrate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isEnabled ? 1 : 0));
        dest.writeString(message);
        dest.writeString(ringtone);
        dest.writeByte((byte) (isVibrate ? 1 : 0));
    }
}
