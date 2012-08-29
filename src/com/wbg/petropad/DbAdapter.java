package com.wbg.petropad;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbAdapter extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "petropad";
    private static final int DATABASE_VERSION = 2;

    private static final String SQL_CREATE_ENTRY_TABLE =
            "CREATE TABLE entry \n" +
                    "(entry_id INTEGER PRIMARY KEY AUTOINCREMENT, \t \n" +
                    "odometer_reading_id INTEGER NOT NULL, \n" +
                    "distance_unit INTEGER NOT NULL DEFAULT 0, \n" +
                    "fill_amount INTEGER NOT NULL, \n" +
                    "price_per_unit INTEGER NOT NULL, \n" +
                    "volume_unit INTEGER NOT NULL DEFAULT 0, \n" +
                    "currency_type INTEGER NOT NULL DEFAULT 0,\n" +
                    "previous_entry_missed TINYINT NOT NULL DEFAULT 0,\n" +
                    "partial_fillup TINYINT NOT NULL DEFAULT 0,\n" +
                    "note VARCHAR(510) NULL, \n" +
                    "trip_id INTEGER NULL,\n" +
                    "FOREIGN KEY (odometer_reading_id)  REFERENCES vehicle(odometer_reading_id)\n" +
                    "FOREIGN KEY (trip_id)  REFERENCES trip(trip_id));";

    private static final String SQL_CREATE_VEHICLE_TABLE =
            "CREATE TABLE vehicle \n" +
                    "(vehicle_id INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                    "odometer_units INTEGER NOT NULL, \n" +
                    "vehicle_name VARCHAR(255) NOT NULL UNIQUE,\n" +
                    "make VARCHAR(64) NULL,\n" +
                    "model VARCHAR(64) NULL,\n" +
                    "year VARCHAR(4) NULL,\n" +
                    "vin VARCHAR(17) NULL,\n" +
                    "image BLOB NULL);";

    private static final String SQL_CREATE_ODOMETER_READING_TABLE =
            "CREATE TABLE odometer_reading\n" +
                    "(odometer_reading_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "odometer_reading INTEGER NOT NULL,\n" +
                    "vehicle_id INTEGER NOT NULL,\n" +
                    "timestamp INTEGER NOT NULL,\n" +
                    "FOREIGN KEY (vehicle_id)  REFERENCES vehicle(vehicle_id));";

    private static final String SQL_CREATE_TRIP_TABLE =
            "CREATE TABLE trip\n" +
                    "(trip_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "label VARCHAR(255) NOT NULL,\n" +
                    "start_odometer_reading_id INTEGER NOT NULL,\n" +
                    "end_odometer_reading_id INTEGER NULL,\n" +
                    "FOREIGN KEY (start_odometer_reading_id)  REFERENCES vehicle(odometer_reading_id)\n" +
                    "FOREIGN KEY (end_odometer_reading_id)  REFERENCES vehicle(odometer_reading_id));";

    public DbAdapter(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //To change body of implemented methods use File | Settings | File Templates.
        Log.v(Petropad.TAG, "Creating Database...");
        sqLiteDatabase.execSQL(SQL_CREATE_VEHICLE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_ODOMETER_READING_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TRIP_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRY_TABLE);

        Log.v(Petropad.TAG, "Database Created.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}
