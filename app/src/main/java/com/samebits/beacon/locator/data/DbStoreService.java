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

import com.samebits.beacon.locator.model.IManagedBeacon;
import com.samebits.beacon.locator.model.TrackedBeacon;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by vitas on 20/12/15.
 */
public class DbStoreService extends SQLiteOpenHelper implements StoreService {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "beaconloc.db";
    public static final String DATABASE_PATH = "com.samebits.beacon.locator";

    private static final String SEPARATOR = ",";
    private static final String SQL_CREATE_DATA = "CREATE TABLE IF NOT EXISTS "
            + ScanColumns.TABLE_NAME
            + "("
            + ScanColumns.COLUMN_NAME_ID
            + " TEXT NOT NULL"
            + SEPARATOR
            + ScanColumns.COLUMN_NAME_LAST_SEEN_TIME
            + " INTEGER NOT NULL"
            + SEPARATOR
            + ScanColumns.COLUMN_NAME_BLUETOOTH_NAME
            + " TEXT"
            + SEPARATOR
            + ScanColumns.COLUMN_NAME_BLUETOOTH_ADDRESS
            + " TEXT NOT NULL"
            + SEPARATOR
            + ScanColumns.COLUMN_NAME_UUID
            + " TEXT NOT NULL"
            + SEPARATOR
            + ScanColumns.COLUMN_NAME_DISTANCE
            + " REAL"
            + SEPARATOR
            + ScanColumns.COLUMN_NAME_RSSI
            + " INTEGER"
            + SEPARATOR
            + ScanColumns.COLUMN_NAME_TXPOWER
            + " INTEGER"
            + SEPARATOR
            + ScanColumns.COLUMN_NAME_TYPE
            + " INTEGER"
            + SEPARATOR
            + ScanColumns.COLUMN_NAME_URL
            + " TEXT"
            + SEPARATOR
            + ScanColumns.COLUMN_NAME_MAJOR
            + " TEXT"
            + SEPARATOR
            + ScanColumns.COLUMN_NAME_MINOR
            + " TEXT"
            + SEPARATOR
            + ScanColumns.COLUMN_NAME_IS_TRACKED
            + " BOOLEAN"
            + SEPARATOR
            + " PRIMARY KEY ("
            + ScanColumns.COLUMN_NAME_LAST_SEEN_TIME + SEPARATOR
            + ScanColumns.COLUMN_NAME_ID + "))";

    private static final String SQL_DELETE_DATA = "DROP TABLE IF EXISTS "
            + ScanColumns.TABLE_NAME;

    public DbStoreService(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public SQLiteDatabase getDb() {
        return getWritableDatabase();
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_DATA);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_DATA);

        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion,
                            int newVersion) {
    }

    @Override
    public boolean createBeacon(IManagedBeacon beacon) {
        ContentValues values = new ContentValues();

        values.put(ScanColumns.COLUMN_NAME_ID, beacon.getId());
        values.put(ScanColumns.COLUMN_NAME_LAST_SEEN_TIME, beacon.getTimeLastSeen());
        values.put(ScanColumns.COLUMN_NAME_BLUETOOTH_NAME, beacon.getBluetoothName());
        values.put(ScanColumns.COLUMN_NAME_BLUETOOTH_ADDRESS, beacon.getBluetoothAddress());
        values.put(ScanColumns.COLUMN_NAME_UUID, beacon.getUUID());
        values.put(ScanColumns.COLUMN_NAME_DISTANCE, beacon.getDistance());
        values.put(ScanColumns.COLUMN_NAME_RSSI, beacon.getRssi());
        values.put(ScanColumns.COLUMN_NAME_TXPOWER, beacon.getTxPower());
        values.put(ScanColumns.COLUMN_NAME_TYPE, beacon.getType());
        values.put(ScanColumns.COLUMN_NAME_URL, beacon.getEddystoneURL());
        values.put(ScanColumns.COLUMN_NAME_MAJOR, beacon.getMajor());
        values.put(ScanColumns.COLUMN_NAME_MINOR, beacon.getMinor());
        values.put(ScanColumns.COLUMN_NAME_IS_TRACKED, beacon.isTracked());

        SQLiteDatabase db = getDb();

        long res = db.insert(ScanColumns.TABLE_NAME, null, values);
        db.close();
        return (res == -1) ? false : true;

    }

    @Override
    public boolean updateBeacon(IManagedBeacon beacon) {
        deleteBeacon(beacon.getId());
        return createBeacon(beacon);
    }

    @Override
    public IManagedBeacon getBeacon(String id) {
        TrackedBeacon beacon = new TrackedBeacon();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(ScanColumns.TABLE_NAME,
                new String[]{
                        ScanColumns.COLUMN_NAME_ID,
                        ScanColumns.COLUMN_NAME_LAST_SEEN_TIME,
                        ScanColumns.COLUMN_NAME_BLUETOOTH_NAME,
                        ScanColumns.COLUMN_NAME_BLUETOOTH_ADDRESS,
                        ScanColumns.COLUMN_NAME_UUID,
                        ScanColumns.COLUMN_NAME_DISTANCE,
                        ScanColumns.COLUMN_NAME_RSSI,
                        ScanColumns.COLUMN_NAME_TXPOWER,
                        ScanColumns.COLUMN_NAME_TYPE,
                        ScanColumns.COLUMN_NAME_URL,
                        ScanColumns.COLUMN_NAME_MAJOR,
                        ScanColumns.COLUMN_NAME_MINOR,
                        ScanColumns.COLUMN_NAME_IS_TRACKED
                },
                ScanColumns.COLUMN_NAME_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);


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
                beacon.setTracked(Boolean.parseBoolean(cursor.getString(12)));

                cursor.close();
            }
        }
        db.close();
        return beacon;
    }

    @Override
    public List<IManagedBeacon> getBeacons() {
        List<IManagedBeacon> beacons = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();
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
                beacon.setRssi(Integer.parseInt(cursor.getString(6)));
                beacon.setTxPower(Integer.parseInt(cursor.getString(7)));
                beacon.setType(Integer.parseInt(cursor.getString(8)));
                beacon.setUrl(cursor.getString(9));
                beacon.setMajor(cursor.getString(10));
                beacon.setMinor(cursor.getString(11));
                beacon.setTracked(Boolean.parseBoolean(cursor.getString(12)));

                beacons.add(beacon);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return beacons;
    }

    @Override
    public boolean deleteBeacon(String id) {
        SQLiteDatabase db = getDb();
        int numDeleted = db.delete(ScanColumns.TABLE_NAME, ScanColumns.COLUMN_NAME_UUID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return (numDeleted == 0) ? false : true;
    }

    protected static abstract class ScanColumns implements BaseColumns {
        public static final String TABLE_NAME = "TrackedBeacon";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_BLUETOOTH_NAME = "bl_name";
        public static final String COLUMN_NAME_BLUETOOTH_ADDRESS = "bl_address";
        public static final String COLUMN_NAME_LAST_SEEN_TIME = "last_seen_time";
        public static final String COLUMN_NAME_UUID = "uuid";
        public static final String COLUMN_NAME_RSSI = "rssi";
        public static final String COLUMN_NAME_TXPOWER = "tx";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_URL = "url";
        public static final String COLUMN_NAME_DISTANCE = "distance";
        public static final String COLUMN_NAME_MAJOR = "major";
        public static final String COLUMN_NAME_MINOR = "minor";
        public static final String COLUMN_NAME_IS_TRACKED = "is_tracked";

    }

}
