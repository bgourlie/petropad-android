package com.wbg.petropad;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.wbg.util.PMath;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

public class Petropad extends Activity {
    public static final String TAG = "Petropad";
    public static ArrayList<Vehicle> vehicles;
    public static int current_vehicle;

    public static DbAdapter dbAdapter;


    public static final int RESULT_NEW_ENTRY = RESULT_FIRST_USER;
    public static final int RESULT_ADD_VEHICLE = RESULT_FIRST_USER + 1;
    public static final int RESULT_VIEW_ENTRIES = RESULT_FIRST_USER + 2;
    public static final int RESULT_EDIT_ENTRY = RESULT_FIRST_USER + 3;

    public static final int MENU_GROUP_DEFAULT = 0;

    public static final int MENU_VIEW_ENTRIES = 0;
    public static final int MENU_CREATE_ENTRY = 1;
    public static final int MENU_MANAGE_VEHICLES = 2;

    public static final Locale locale = Locale.getDefault();
    public static final Currency currency = Currency.getInstance(locale);

    public static int distance_unit_id;
    public static int volume_unit_id;

    private static Spinner lstVehicles;
    private static TextView lblVehicle;
    private static ImageButton btnAddVehicle;
    private static ImageButton btnNewEntry;
    private static TextView lblBtnNewEntry;
    private static TextView lblEfficiency;
    private static TextView lblDistanceDriven;
    private static TextView lblStatsHeader;
    private static TextView lblDaysBetweenFillups;
    private static TextView lblCostPerDistanceUnit;
    private static RelativeLayout layoutStats;

    private static boolean data_possibly_stale;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //i18n init
        if (locale.equals(Locale.US)) {
            distance_unit_id = Units.DISTANCE_MILES;
            volume_unit_id = Units.VOLUME_GALLON;
        } else {
            distance_unit_id = Units.DISTANCE_KILOMETER;
            volume_unit_id = Units.VOLUME_LITER;
        }

        //variable binding
        lstVehicles = (Spinner) findViewById(R.id.list_vehicles);
        lblVehicle = (TextView) findViewById(R.id.label_for_vehicle_list);
        btnAddVehicle = (ImageButton) findViewById(R.id.btn_add_vehicle);
        btnNewEntry = (ImageButton) findViewById(R.id.btn_add_entry);
        lblBtnNewEntry = (TextView) findViewById(R.id.btn_lbl_add_entry);
        lblEfficiency = (TextView) findViewById(R.id.label_efficiency);
        lblDistanceDriven = (TextView) findViewById(R.id.label_distance_driven);
        lblStatsHeader = (TextView) findViewById(R.id.label_statistics_header);
        lblDaysBetweenFillups = (TextView) findViewById(R.id.label_days_between_fillups);
        lblCostPerDistanceUnit = (TextView) findViewById(R.id.label_cost_per_distance_unit);
        layoutStats = (RelativeLayout) findViewById(R.id.statistics_group);

        //event binding
        lstVehicles.setOnItemSelectedListener(this.lstVehicles_OnItemSelected);
        btnAddVehicle.setOnClickListener(btnAddVehicle_OnClick);
        btnNewEntry.setOnClickListener(btnNewEntry_OnClick);

        dbAdapter = new DbAdapter(this);

        //initialize vehicle data
        SQLiteDatabase db = Petropad.dbAdapter.getWritableDatabase();
        Cursor c = db.rawQuery("select vehicle_id, vehicle_name, odometer_units from vehicle", new String[]{});

        int num_vehicles = c.getCount();
        vehicles = new ArrayList<Vehicle>(num_vehicles);
        current_vehicle = 0;

        if (num_vehicles > 0) {
            for (int i = 0; i < num_vehicles; i++) {
                c.moveToPosition(i);
                vehicles.add(i, new Vehicle(c.getInt(0), c.getString(1), c.getInt(2)));
            }

            lblBtnNewEntry.setText(getString(R.string.button_label_add_entry, vehicles.get(current_vehicle).name));
            lblVehicle.setText(R.string.message_viewing_statistics_for);
            ArrayAdapter adapter = new ArrayAdapter<Vehicle>(this, android.R.layout.simple_spinner_item, vehicles);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lstVehicles.setAdapter(adapter);
            data_possibly_stale = true;
        } else {
            lstVehicles.setVisibility(View.GONE);
            lblBtnNewEntry.setVisibility(View.GONE);
            btnNewEntry.setVisibility(View.GONE);
            lblVehicle.setText(R.string.message_add_vehicle);
            data_possibly_stale = false;
        }

        c.close();
        db.close();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (data_possibly_stale) { refreshStats(); }
    }

    @Override
    public void onPause() {
        super.onPause();
        data_possibly_stale = true;
    }

    private void refreshStats() {
        //fixes a bug where if no vehicles exist and
        //the back button is pressed  when adding a vehicle,
        //we get a crash
        if (vehicles.size() == 0) {
            return;
        }

        Statistics stats = Statistics.Efficiency(vehicles.get(current_vehicle));

        if (stats == null) {
            lblStatsHeader.setText(R.string.message_not_enough_data_to_determine_mpg);
            layoutStats.setVisibility(View.GONE);
        } else {
            DateFormat df = DateFormat.getDateInstance();
            lblStatsHeader.setText(getString(R.string.header_statistics, df.format(stats.date_from), df.format(stats.date_to)));
            layoutStats.setVisibility(View.VISIBLE);
            lblEfficiency.setText(getString(R.string.stat_fuel_efficiency, stats.efficiency, Units.getEfficiencyMeasure(distance_unit_id, volume_unit_id)));
            lblDistanceDriven.setText(getString(R.string.stat_distance_driven, stats.distance_driven, Units.getDistanceUnitPlural(distance_unit_id)));
            lblDaysBetweenFillups.setText(getString(R.string.stat_days_between_fillups, stats.num_days));
            lblCostPerDistanceUnit.setText(getString(R.string.stat_cost_per_distance_unit, Units.getDistanceUnitSingular(distance_unit_id), currency.getSymbol(), PMath.formatToDecimalString(stats.cost_per_distance_unit, 3)));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(MENU_GROUP_DEFAULT, MENU_VIEW_ENTRIES, 0, R.string.menu_view_entries);
        menu.add(MENU_GROUP_DEFAULT, MENU_MANAGE_VEHICLES, 0, R.string.menu_view_entries);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_VIEW_ENTRIES:

                if (vehicles.size() == 0) {
                    Toast.makeText(this, R.string.toast_no_vehicles, Toast.LENGTH_SHORT).show();
                    return false;
                }

                Intent viewEntries = new Intent(Petropad.this, PetropadViewEntries.class);
                viewEntries.putExtra("vehicle_index", current_vehicle);
                Petropad.this.startActivityForResult(viewEntries, RESULT_VIEW_ENTRIES);
                return true;
            default:
                Log.e(TAG, String.format("Unknown menu item picked: %1$d", item.getItemId()));
        }

        return false;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_NEW_ENTRY:
                //refreshStats();
                return;
            case RESULT_ADD_VEHICLE:
                Bundle vehicleData = data.getExtras();
                //add vehicle to our local cache and initialize
                Vehicle vehicle = new Vehicle(vehicleData.getInt("vehicle_id"), vehicleData.getString("vehicle_name"), vehicleData.getInt("odometer_units"));
                vehicles.add(vehicle);

                //if UI elements were hidden because there were no vehicles
                //now we make them visible
                if (lstVehicles.getVisibility() == View.GONE) {
                    lstVehicles.setVisibility(View.VISIBLE);
                    lblBtnNewEntry.setVisibility(View.VISIBLE);
                    btnNewEntry.setVisibility(View.VISIBLE);
                    lblVehicle.setText(R.string.message_viewing_statistics_for);
                }

                //refresh the spinner adapter
                ArrayAdapter adapter = new ArrayAdapter<Vehicle>(this, android.R.layout.simple_spinner_item, vehicles);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                lstVehicles.setAdapter(adapter);
                lstVehicles.setSelection(current_vehicle);
        }
    }

    private AdapterView.OnItemSelectedListener lstVehicles_OnItemSelected = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            current_vehicle = position;
            lblBtnNewEntry.setText(getString(R.string.button_label_add_entry, vehicles.get(current_vehicle).name));
            refreshStats();
            Log.v(TAG, String.format("current_vehicle set to %1$d", current_vehicle));
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
            //do nothing?
        }
    };

    private View.OnClickListener btnAddVehicle_OnClick = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intentAddVehicle = new Intent(Petropad.this, PetropadAddVehicle.class);
            Petropad.this.startActivityForResult(intentAddVehicle, RESULT_ADD_VEHICLE);
        }
    };

    private View.OnClickListener btnNewEntry_OnClick = new View.OnClickListener() {
        public void onClick(View v) {
            if (vehicles.size() == 0) {
                Toast.makeText(v.getContext(), R.string.toast_no_vehicles, Toast.LENGTH_SHORT).show();
                return;
            }

            Intent i = new Intent(Petropad.this, PetropadNewEntry.class);
            i.putExtra("mode", PetropadNewEntry.ENTRY_MODE_NEW);
            i.putExtra("vehicle_index", current_vehicle);
            Petropad.this.startActivityForResult(i, RESULT_NEW_ENTRY);
        }
    };
}
