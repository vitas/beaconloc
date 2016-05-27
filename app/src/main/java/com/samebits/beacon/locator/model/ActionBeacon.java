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
 * Created by vitas on 28/12/15.
 */
public class ActionBeacon implements Parcelable {
    public static final Creator<ActionBeacon> CREATOR = new Creator<ActionBeacon>() {
        @Override
        public ActionBeacon createFromParcel(Parcel in) {
            return new ActionBeacon(in);
        }

        @Override
        public ActionBeacon[] newArray(int size) {
            return new ActionBeacon[size];
        }
    };
    private int id;
    private String beaconId;
    private String name;
    private EventType eventType = EventType.EVENT_ENTERS_REGION;
    private ActionType actionType = ActionType.ACTION_NONE;
    private String actionParam = "";
    private boolean isEnabled;
    private NotificationAction notification = new NotificationAction();

    public ActionBeacon(String beaconId, String name) {
        this.beaconId = beaconId;
        this.name = name;
    }

    public ActionBeacon() {

    }

    protected ActionBeacon(Parcel in) {
        id = in.readInt();
        beaconId = in.readString();
        name = in.readString();
        eventType = EventType.fromInt(in.readInt());
        actionType = ActionType.fromInt(in.readInt());
        actionParam = in.readString();
        isEnabled = in.readByte() != 0;
        notification = in.readParcelable(NotificationAction.class.getClassLoader());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBeaconId() {
        return beaconId;
    }

    public void setBeaconId(String beaconId) {
        this.beaconId = beaconId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EventType getEventType() {
        return this.eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public String getActionParam() {
        return actionParam;
    }

    public void setActionParam(String actionParam) {
        this.actionParam = actionParam;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(beaconId);
        dest.writeString(name);
        dest.writeInt(eventType.getValue());
        dest.writeInt(actionType.getValue());
        dest.writeString(actionParam);
        dest.writeByte((byte) (isEnabled ? 1 : 0));
        dest.writeParcelable(this.notification, flags);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActionBeacon that = (ActionBeacon) o;

        if (getId() != that.getId()) return false;
        return getBeaconId().equals(that.getBeaconId());

    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getBeaconId().hashCode();
        return result;
    }

    public NotificationAction getNotification() {
        return this.notification;
    }

    public void setNotification(NotificationAction value) {
        this.notification = value;
    }


    public enum ActionType {
        ACTION_NONE(0),
        ACTION_URL(1),
        ACTION_INTENT_ACTION(2),
        ACTION_START_APP(3),
        ACTION_SET_SILENT_ON(4),
        ACTION_SET_SILENT_OFF(5),
        ACTION_GET_LOCATION(6),
        ACTION_TASKER(7),
        ACTION_WEB(8);

        private final int value;

        ActionType(int value) {
            this.value = value;
        }

        public static ActionType fromInt(int value) {
            for (ActionType type : ActionType.values()) {
                if (type.getValue() == value) {
                    return type;
                }
            }
            return ACTION_WEB;
        }

        public int getValue() {
            return value;
        }
    }

    public enum EventType {
        EVENT_ENTERS_REGION(0),
        EVENT_LEAVES_REGION(1),
        EVENT_NEAR_YOU(2);

        private final int value;

        EventType(int value) {
            this.value = value;
        }

        public static EventType fromInt(int value) {
            for (EventType type : EventType.values()) {
                if (type.getValue() == value) {
                    return type;
                }
            }
            return EVENT_ENTERS_REGION;
        }

        public int getValue() {
            return value;
        }
    }
}


