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


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.samebits.beacon.locator.model.ActionBeacon;
import com.samebits.beacon.locator.model.TrackedBeacon;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 * Created by vitas on 20/12/15.
 */
public class DbStoreService extends SQLiteOpenHelper implements StoreService {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "beaconloc.db";

    private static final String SEPARATOR = ",";
    private static final String BEACON_SQL_CREATE_DATA = "CREATE TABLE IF NOT EXISTS "
            + ScanColumns.TABLE_NAME
            + "("
            + ScanColumns.COLUMN_ID
            + " TEXT NOT NULL"
            + SEPARATOR
            + ScanColumns.COLUMN_LAST_SEEN_TIME
            + " INTEGER NOT NULL"
            + SEPARATOR
            + ScanColumns.COLUMN_BLUETOOTH_NAME
            + " TEXT"
            + SEPARATOR
            + ScanColumns.COLUMN_BLUETOOTH_ADDRESS
            + " TEXT NOT NULL"
            + SEPARATOR
            + ScanColumns.COLUMN_UUID
            + " TEXT NOT NULL"
            + SEPARATOR
            + ScanColumns.COLUMN_DISTANCE
            + " REAL"
            + SEPARATOR
            + ScanColumns.COLUMN_RSSI
            + " INTEGER"
            + SEPARATOR
            + ScanColumns.COLUMN_TXPOWER
            + " INTEGER"
            + SEPARATOR
            + ScanColumns.COLUMN_TYPE
            + " INTEGER"
            + SEPARATOR
            + ScanColumns.COLUMN_URL
            + " TEXT"
            + SEPARATOR
            + ScanColumns.COLUMN_MAJOR
            + " TEXT"
            + SEPARATOR
            + ScanColumns.COLUMN_MINOR
            + " TEXT"
            + SEPARATOR
            + " PRIMARY KEY ("
            + ScanColumns.COLUMN_ID + "))";

    private static final String BEACON_ACTION_SQL_CREATE_DATA = "CREATE TABLE IF NOT EXISTS "
            + ActionColumns.TABLE_NAME
            + "("
            + ActionColumns.COLUMN_ID
            + " INTEGER PRIMARY KEY"
            + SEPARATOR
            + ActionColumns.COLUMN_BEACON_ID
            + " TEXT NOT NULL"
            + SEPARATOR
            + ActionColumns.COLUMN_NAME
            + " TEXT NOT NULL"
            + SEPARATOR
            + ActionColumns.COLUMN_EVENT_TYPE
            + " INTEGER NOT NULL"
            + SEPARATOR
            + ActionColumns.COLUMN_ACTION_TYPE
            + " INTEGER NOT NULL"
            + SEPARATOR
            + ActionColumns.COLUMN_ACTION_PARAMS
            + " TEXT"
            + SEPARATOR
            + ActionColumns.COLUMN_IS_ENABLED
            + " INTEGER NOT NULL DEFAULT(1)"
            + SEPARATOR
            + ActionColumns.COLUMN_NOTIF_IS_ENABLED
            + " INTEGER NOT NULL DEFAULT(0)"
            + SEPARATOR
            + ActionColumns.COLUMN_NOTIF_VIBRATE_IS_ENABLED
            + " INTEGER NOT NULL DEFAULT(0)"
            + SEPARATOR
            + ActionColumns.COLUMN_NOTIF_RINGTONE
            + " TEXT"
            + SEPARATOR
            + ActionColumns.COLUMN_NOTIF_MSG
            + " TEXT"
            + ");";

    private static final String BEACON_SQL_DELETE_DATA = "DROP TABLE IF EXISTS "
            + ScanColumns.TABLE_NAME;

    private static final String BEACON_ACTION_SQL_DELETE_DATA = "DROP TABLE IF EXISTS "
            + ActionColumns.TABLE_NAME;

    public DbStoreService(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BEACON_SQL_CREATE_DATA);
        db.execSQL(BEACON_ACTION_SQL_CREATE_DATA);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(BEACON_SQL_DELETE_DATA);
        db.execSQL(BEACON_ACTION_SQL_DELETE_DATA);

        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion,
                            int newVersion) {
    }

    @Override
    public boolean createBeacon(TrackedBeacon beacon) {
        ContentValues values = new ContentValues();

        values.put(ScanColumns.COLUMN_ID, beacon.getId());
        values.put(ScanColumns.COLUMN_LAST_SEEN_TIME, beacon.getTimeLastSeen());
        values.put(ScanColumns.COLUMN_BLUETOOTH_NAME, beacon.getBluetoothName());
        values.put(ScanColumns.COLUMN_BLUETOOTH_ADDRESS, beacon.getBluetoothAddress());
        values.put(ScanColumns.COLUMN_UUID, beacon.getUUID());
        values.put(ScanColumns.COLUMN_DISTANCE, beacon.getDistance());
        values.put(ScanColumns.COLUMN_RSSI, beacon.getRssi());
        values.put(ScanColumns.COLUMN_TXPOWER, beacon.getTxPower());
        values.put(ScanColumns.COLUMN_TYPE, beacon.getBeaconType().ordinal());
        values.put(ScanColumns.COLUMN_URL, beacon.getEddystoneURL());
        values.put(ScanColumns.COLUMN_MAJOR, beacon.getMajor());
        values.put(ScanColumns.COLUMN_MINOR, beacon.getMinor());

        SQLiteDatabase db = getWritableDatabase();

        long res = db.insert(ScanColumns.TABLE_NAME, null, values);
        db.close();

        for (ActionBeacon action : beacon.getActions()) {
            createBeaconAction(action);
        }
        return (res == -1) ? false : true;
    }


    @Override
    public boolean updateBeacon(TrackedBeacon beacon) {
        final ContentValues values = new ContentValues();
        values.put(ScanColumns.COLUMN_LAST_SEEN_TIME, new Date().getTime());
        values.put(ScanColumns.COLUMN_BLUETOOTH_NAME, beacon.getBluetoothName());
        values.put(ScanColumns.COLUMN_BLUETOOTH_ADDRESS, beacon.getBluetoothAddress());
        values.put(ScanColumns.COLUMN_UUID, beacon.getUUID());
        values.put(ScanColumns.COLUMN_DISTANCE, beacon.getDistance());
        values.put(ScanColumns.COLUMN_RSSI, beacon.getRssi());
        values.put(ScanColumns.COLUMN_TXPOWER, beacon.getTxPower());
        values.put(ScanColumns.COLUMN_TYPE, beacon.getBeaconType().ordinal());
        values.put(ScanColumns.COLUMN_URL, beacon.getEddystoneURL());
        values.put(ScanColumns.COLUMN_MAJOR, beacon.getMajor());
        values.put(ScanColumns.COLUMN_MINOR, beacon.getMinor());

        SQLiteDatabase db = getWritableDatabase();
        int numUpdated = db.update(ScanColumns.TABLE_NAME, values, ScanColumns.COLUMN_ID + "=?", new String[]{beacon.getId()});
        db.close();
        return (numUpdated == 0) ? false : true;
    }

    @Override
    public boolean isBeaconExists(String id) {

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT EXISTS (SELECT * FROM " + ScanColumns.TABLE_NAME
                + " WHERE " +ScanColumns.COLUMN_ID +"=? LIMIT 1)" , new String[]{id});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                if (cursor.getInt(0) == 1) {
                    cursor.close();
                    return true;
                } else {
                    cursor.close();
                }
            }
        }
        return false;
    }

    @Override
    public TrackedBeacon getBeacon(String id) {
        TrackedBeacon beacon = new TrackedBeacon();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(ScanColumns.TABLE_NAME,
                new String[]{
                        ScanColumns.COLUMN_ID,
                        ScanColumns.COLUMN_LAST_SEEN_TIME,
                        ScanColumns.COLUMN_BLUETOOTH_NAME,
                        ScanColumns.COLUMN_BLUETOOTH_ADDRESS,
                        ScanColumns.COLUMN_UUID,
                        ScanColumns.COLUMN_DISTANCE,
                        ScanColumns.COLUMN_RSSI,
                        ScanColumns.COLUMN_TXPOWER,
                        ScanColumns.COLUMN_TYPE,
                        ScanColumns.COLUMN_URL,
                        ScanColumns.COLUMN_MAJOR,
                        ScanColumns.COLUMN_MINOR
                },
                ScanColumns.COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {

                beacon.setId(cursor.getString(0));
                beacon.setTimeLastSeen(Long.parseLong(cursor.getString(1)));
                beacon.setBluetoothName(cursor.getString(2));
                beacon.setBluetoothAddress(cursor.getString(3));
                beacon.setUUID(cursor.getString(4));
                beacon.setDistance(Double.parseDouble(cursor.getString(5)));
                beacon.setRssi(Integer.parseInt(cursor.getString(6)));
                beacon.setTxPower(Integer.parseInt(cursor.getString(7)));
                beacon.setType(Integer.parseInt(cursor.getString(8)));
                beacon.setUrl(cursor.getString(9));
                beacon.setMajor(cursor.getString(10));
                beacon.setMinor(cursor.getString(11));

                List<ActionBeacon> actions = getBeaconActions(beacon.getId());
                beacon.addActions(actions);

                cursor.close();
            }
        }
        db.close();
        return beacon;
    }

    @Override
    public boolean updateBeaconDistance(String id, double distance) {
        final ContentValues values = new ContentValues();
        values.put(ScanColumns.COLUMN_DISTANCE, distance);
        values.put(ScanColumns.COLUMN_LAST_SEEN_TIME, new Date().getTime());

        SQLiteDatabase db = getWritableDatabase();
        int numUpdated = db.update(ScanColumns.TABLE_NAME, values, ScanColumns.COLUMN_ID + "=?", new String[]{id});
        db.close();
        return (numUpdated == 0) ? false : true;
    }


    @Override
    public List<TrackedBeacon> getBeacons() {
        List<TrackedBeacon> beacons = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + ScanColumns.TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                TrackedBeacon beacon = new TrackedBeacon();

                beacon.setId(cursor.getString(0));
                beacon.setTimeLastSeen(Long.parseLong(cursor.getString(1)));
                beacon.setBluetoothName(cursor.getString(2));
                beacon.setBluetoothAddress(cursor.getString(3));
                beacon.setUUID(cursor.getString(4));
                beacon.setDistance(Double.parseDouble(cursor.getString(5)));
                beacon.setRssi(cursor.getInt(6));
                beacon.setTxPower(cursor.getInt(7));
                beacon.setType(cursor.getInt(8));
                beacon.setUrl(cursor.getString(9));
                beacon.setMajor(cursor.getString(10));
                beacon.setMinor(cursor.getString(11));

                List<ActionBeacon> actions = getBeaconActions(beacon.getId());
                beacon.addActions(actions);

                beacons.add(beacon);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return beacons;
    }


    @Override
    public boolean createBeaconAction(ActionBeacon beacon) {
        ContentValues values = new ContentValues();

        values.put(ActionColumns.COLUMN_BEACON_ID, beacon.getBeaconId());
        values.put(ActionColumns.COLUMN_NAME, beacon.getName());
        values.put(ActionColumns.COLUMN_EVENT_TYPE, beacon.getEventType().getValue());
        values.put(ActionColumns.COLUMN_ACTION_TYPE, beacon.getActionType().getValue());
        values.put(ActionColumns.COLUMN_ACTION_PARAMS, beacon.getActionParam());
        values.put(ActionColumns.COLUMN_IS_ENABLED, beacon.isEnabled() ? 1 : 0);
        values.put(ActionColumns.COLUMN_NOTIF_IS_ENABLED, beacon.getNotification().isEnabled() ? 1 : 0);
        values.put(ActionColumns.COLUMN_NOTIF_VIBRATE_IS_ENABLED, beacon.getNotification().isVibrate() ? 1 : 0);
        values.put(ActionColumns.COLUMN_NOTIF_RINGTONE, beacon.getNotification().getRingtone());
        values.put(ActionColumns.COLUMN_NOTIF_MSG, beacon.getNotification().getMessage());

        SQLiteDatabase db = getWritableDatabase();

        long res = db.insert(ActionColumns.TABLE_NAME, null, values);
        db.close();
        if (res != -1) {
            beacon.setId((int) res);
        }

        return (res == -1) ? false : true;
    }

    @Override
    public boolean updateBeaconAction(ActionBeacon beacon) {
        final ContentValues values = new ContentValues();
        values.put(ActionColumns.COLUMN_NAME, beacon.getName());
        values.put(ActionColumns.COLUMN_BEACON_ID, beacon.getBeaconId());
        values.put(ActionColumns.COLUMN_EVENT_TYPE, beacon.getEventType().getValue());
        values.put(ActionColumns.COLUMN_ACTION_TYPE, beacon.getActionType().getValue());
        values.put(ActionColumns.COLUMN_ACTION_PARAMS, beacon.getActionParam());
        values.put(ActionColumns.COLUMN_IS_ENABLED, beacon.isEnabled() ? 1 : 0);
        values.put(ActionColumns.COLUMN_NOTIF_IS_ENABLED, beacon.getNotification().isEnabled() ? 1 : 0);
        values.put(ActionColumns.COLUMN_NOTIF_VIBRATE_IS_ENABLED, beacon.getNotification().isVibrate() ? 1 : 0);
        values.put(ActionColumns.COLUMN_NOTIF_RINGTONE, beacon.getNotification().getRingtone());
        values.put(ActionColumns.COLUMN_NOTIF_MSG, beacon.getNotification().getMessage());

        SQLiteDatabase db = getWritableDatabase();
        int numUpdated = db.update(ActionColumns.TABLE_NAME, values, ActionColumns.COLUMN_ID + "=?"
                , new String[]{String.valueOf(beacon.getId())});
        db.close();
        return (numUpdated == 0) ? false : true;
    }

    @Override
    public List<ActionBeacon> getBeaconActions(String beaconId) {
        List<ActionBeacon> actions = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + ActionColumns.TABLE_NAME + " WHERE "
                + ActionColumns.COLUMN_BEACON_ID + "=?", new String[]{String.valueOf(beaconId)});

        if (cursor.moveToFirst()) {
            do {
                ActionBeacon beacon = new ActionBeacon();

                beacon.setId(cursor.getInt(0));
                beacon.setBeaconId(cursor.getString(1));
                beacon.setName(cursor.getString(2));
                beacon.setEventType(ActionBeacon.EventType.fromInt(cursor.getInt(3)));
                beacon.setActionType(ActionBeacon.ActionType.fromInt(cursor.getInt(4)));
                beacon.setActionParam(cursor.getString(5));
                beacon.setIsEnabled(cursor.getInt(6) == 1 ? true : false);
                beacon.getNotification().setIsEnabled(cursor.getInt(7) == 1 ? true : false);
                beacon.getNotification().setIsVibrate(cursor.getInt(8) == 1 ? true : false);
                beacon.getNotification().setRingtone(cursor.getString(9));
                beacon.getNotification().setMessage(cursor.getString(10));

                actions.add(beacon);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return actions;
    }

    @Override
    public boolean deleteBeaconAction(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int numDeleted = db.delete(ActionColumns.TABLE_NAME, ActionColumns.COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return (numDeleted == 0) ? false : true;
    }

    @Override
    public boolean deleteBeaconActions(String beaconId) {
        SQLiteDatabase db = getWritableDatabase();
        int numDeleted = db.delete(ActionColumns.TABLE_NAME, ActionColumns.COLUMN_BEACON_ID + "=?", new String[]{String.valueOf(beaconId)});
        db.close();
        return (numDeleted == 0) ? false : true;
    }

    @Override
    public List<ActionBeacon> getAllEnabledBeaconActions() {
        List<ActionBeacon> actions = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + ActionColumns.TABLE_NAME + " WHERE "
                + ActionColumns.COLUMN_IS_ENABLED + "=?", new String[]{String.valueOf(1)});

        if (cursor.moveToFirst()) {
            do {
                ActionBeacon beacon = new ActionBeacon();

                beacon.setId(cursor.getInt(0));
                beacon.setBeaconId(cursor.getString(1));
                beacon.setName(cursor.getString(2));
                beacon.setEventType(ActionBeacon.EventType.fromInt(cursor.getInt(3)));
                beacon.setActionType(ActionBeacon.ActionType.fromInt(cursor.getInt(4)));
                beacon.setActionParam(cursor.getString(5));
                beacon.setIsEnabled(cursor.getInt(6) == 1 ? true : false);
                beacon.getNotification().setIsEnabled(cursor.getInt(7) == 1 ? true : false);
                beacon.getNotification().setIsVibrate(cursor.getInt(8) == 1 ? true : false);
                beacon.getNotification().setRingtone(cursor.getString(9));
                beacon.getNotification().setMessage(cursor.getString(10));

                actions.add(beacon);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return actions;
    }

    @Override
    public boolean updateBeaconActionEnable(int id, boolean enable) {
        SQLiteDatabase db = getWritableDatabase();

        final ContentValues values = new ContentValues();
        values.put(ActionColumns.COLUMN_IS_ENABLED, enable);
        int numUpdated = db.update(ActionColumns.TABLE_NAME, values, ActionColumns.COLUMN_ID + "=?", new String[]{String.valueOf(id)});

        db.close();
        return (numUpdated == 0) ? false : true;
    }

    @Override
    public List<ActionBeacon> getEnabledBeaconActionsByEvent(int eventType, String beaconId) {
        List<ActionBeacon> actions = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + ActionColumns.TABLE_NAME + " WHERE "
                        + ActionColumns.COLUMN_IS_ENABLED + "=? AND "
                        + ActionColumns.COLUMN_BEACON_ID + "=? ",
                new String[]{String.valueOf(1), String.valueOf(eventType), beaconId});

        if (cursor.moveToFirst()) {
            do {
                ActionBeacon beacon = new ActionBeacon();

                beacon.setId(cursor.getInt(0));
                beacon.setBeaconId(cursor.getString(1));
                beacon.setName(cursor.getString(2));
                beacon.setEventType(ActionBeacon.EventType.fromInt(cursor.getInt(3)));
                beacon.setActionType(ActionBeacon.ActionType.fromInt(cursor.getInt(4)));
                beacon.setActionParam(cursor.getString(5));
                beacon.setIsEnabled(cursor.getInt(6) == 1 ? true : false);
                beacon.getNotification().setIsEnabled(cursor.getInt(7) == 1 ? true : false);
                beacon.getNotification().setIsVibrate(cursor.getInt(8) == 1 ? true : false);
                beacon.getNotification().setRingtone(cursor.getString(9));
                beacon.getNotification().setMessage(cursor.getString(10));

                actions.add(beacon);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return actions;
    }


    @Override
    public boolean deleteBeacon(String id, boolean cascade) {
        SQLiteDatabase db = getWritableDatabase();
        int numDeleted = db.delete(ScanColumns.TABLE_NAME, ScanColumns.COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        if (cascade) {
            deleteBeaconActions(id);
        }
        return (numDeleted == 0) ? false : true;
    }

    protected static abstract class ScanColumns implements BaseColumns {
        public static final String TABLE_NAME = "TrackedBeacon";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_BLUETOOTH_NAME = "bl_name";
        public static final String COLUMN_BLUETOOTH_ADDRESS = "bl_address";
        public static final String COLUMN_LAST_SEEN_TIME = "last_seen_time";
        public static final String COLUMN_UUID = "uuid";
        public static final String COLUMN_RSSI = "rssi";
        public static final String COLUMN_TXPOWER = "tx";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_DISTANCE = "distance";
        public static final String COLUMN_MAJOR = "major";
        public static final String COLUMN_MINOR = "minor";
    }

    protected static abstract class ActionColumns implements BaseColumns {
        public static final String TABLE_NAME = "ActionBeacon";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_BEACON_ID = "beacon_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_EVENT_TYPE = "event_type";
        public static final String COLUMN_ACTION_TYPE = "action_type";
        public static final String COLUMN_ACTION_PARAMS = "action_param";
        public static final String COLUMN_IS_ENABLED = "is_enabled";
        public static final String COLUMN_NOTIF_IS_ENABLED = "notif_enabled";
        public static final String COLUMN_NOTIF_VIBRATE_IS_ENABLED = "notif_vibrate";
        public static final String COLUMN_NOTIF_RINGTONE = "notif_ringtone";
        public static final String COLUMN_NOTIF_MSG = "notif_msg";

    }
}
