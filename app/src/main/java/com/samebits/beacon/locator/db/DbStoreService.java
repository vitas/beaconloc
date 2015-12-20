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

package com.samebits.beacon.locator.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.samebits.beacon.locator.model.DetectedBeacon;

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
            + " INTEGER"
            + SEPARATOR
            + ScanColumns.COLUMN_NAME_RSSI
            + " INTEGER"
            + SEPARATOR
            + ScanColumns.COLUMN_NAME_MAJOR
            + " TEXT"
            + SEPARATOR
            + ScanColumns.COLUMN_NAME_MINOR
            + " TEXT"
            + SEPARATOR
            + ScanColumns.COLUMN_NAME_TXPOWER
            + " INTEGER"
            + SEPARATOR
            + " PRIMARY KEY ("
            + ScanColumns.COLUMN_NAME_LAST_SEEN_TIME + SEPARATOR
            + ScanColumns.COLUMN_NAME_UUID + "))";

    private static final String SQL_DELETE_DATA = "DROP TABLE IF EXISTS "
            + ScanColumns.TABLE_NAME;

    private static SQLiteDatabase db;

    public DbStoreService(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = this.getWritableDatabase();
    }


    public SQLiteDatabase getDb() {
        return db;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_DATA);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion,
                            int newVersion) {
    }

    @Override
    public boolean saveBeacon(DetectedBeacon beacon) {
        ContentValues values = new ContentValues();

        values.put(ScanColumns.COLUMN_NAME_LAST_SEEN_TIME,  beacon.getTimeLastSeen());
        values.put(ScanColumns.COLUMN_NAME_BLUETOOTH_NAME,  beacon.getBluetoothName());
        values.put(ScanColumns.COLUMN_NAME_BLUETOOTH_ADDRESS,  beacon.getBluetoothAddress());
        values.put(ScanColumns.COLUMN_NAME_RSSI,  beacon.getRssi());
        values.put(ScanColumns.COLUMN_NAME_DISTANCE,  beacon.getDistance());
        values.put(ScanColumns.COLUMN_NAME_TXPOWER,  beacon.getTxPower());
        values.put(ScanColumns.COLUMN_NAME_UUID,  beacon.getUUID());
        values.put(ScanColumns.COLUMN_NAME_MAJOR,  beacon.getMajor());
        values.put(ScanColumns.COLUMN_NAME_MINOR,  beacon.getMinor());

        return (getDb().insert(ScanColumns.TABLE_NAME, null, values) == -1)?false:true;

    }

    //TODO
    @Override
    public DetectedBeacon loadBeacon(String id) {
        return null;
    }

    protected static abstract class ScanColumns implements BaseColumns {
        public static final String TABLE_NAME = "DetectedBeacon";
        public static final String COLUMN_NAME_BLUETOOTH_NAME = "bl_name";
        public static final String COLUMN_NAME_BLUETOOTH_ADDRESS = "bl_address";
        public static final String COLUMN_NAME_LAST_SEEN_TIME = "last_seen_time";
        public static final String COLUMN_NAME_RSSI = "rssi";
        public static final String COLUMN_NAME_DISTANCE = "distance";
        public static final String COLUMN_NAME_MAJOR = "major";
        public static final String COLUMN_NAME_MINOR = "minor";
        public static final String COLUMN_NAME_TXPOWER = "tx";
        public static final String COLUMN_NAME_UUID = "uuid";
    }

}
