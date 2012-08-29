package com.wbg.petropad;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.util.Locale;

public class PetropadAddVehicle extends Activity {

    private Button _btnAddVehicle;
    private TextView _txtVehicleName;
    private RadioButton _chkMiles;
    private RadioButton _chkKilometers;
    private LinearLayout _layoutAdditionalInfo;
    private ImageButton _btnAddAdditionalInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_vehicle);
        _btnAddAdditionalInfo = (ImageButton)findViewById(R.id.btn_optional_info);
        _layoutAdditionalInfo = (LinearLayout) findViewById(R.id.layout_additional_info);
        _btnAddVehicle = (Button) findViewById(R.id.BtnAddVehicle);
        _txtVehicleName = (TextView) findViewById(R.id.TxtVehicleName);
        _chkMiles = (RadioButton) findViewById(R.id.chkMiles);
        _chkKilometers = (RadioButton) findViewById(R.id.chkKilometers);

        _btnAddVehicle.setOnClickListener(btnAddVehicle_OnClick);
        _btnAddAdditionalInfo.setOnClickListener(btnAddAdditionalInfo_OnClick);

        if (Petropad.locale.equals(Locale.US)) {
            _chkMiles.setChecked(true);
        } else {
            _chkKilometers.setChecked(true);
        }

    }

    private View.OnClickListener btnAddAdditionalInfo_OnClick = new View.OnClickListener() {
        public void onClick(View v) {
            if(_layoutAdditionalInfo.getVisibility() == View.VISIBLE){
                _layoutAdditionalInfo.setVisibility(View.GONE);
            }else if(_layoutAdditionalInfo.getVisibility() == View.GONE){
                _layoutAdditionalInfo.setVisibility(View.VISIBLE);
            }
        }
    };

    private View.OnClickListener btnAddVehicle_OnClick = new View.OnClickListener() {
        public void onClick(View v) {
            String vehicle_name = _txtVehicleName.getText().toString().trim();

            if(vehicle_name.equals("")){
                Toast.makeText(v.getContext(), R.string.toast_invalid_vehicle_name, Toast.LENGTH_SHORT).show();
                return;
            }

            int odometer_units = _chkMiles.isChecked() ? Units.DISTANCE_MILES : Units.DISTANCE_KILOMETER;
            Log.v(Petropad.TAG, String.format("inserting entry: %1$s", vehicle_name));
            SQLiteDatabase db = Petropad.dbAdapter.getWritableDatabase();
            SQLiteStatement statement = db.compileStatement("INSERT INTO vehicle (vehicle_name, odometer_units) values (?, ?)");
            statement.bindString(1, vehicle_name);
            statement.bindLong(2, odometer_units);
            int vehicle_id;

            try {
                vehicle_id = (int) statement.executeInsert();
            }
            catch (SQLiteConstraintException ex) {
                Toast.makeText(v.getContext(), R.string.toast_duplicate_vehicle_name, Toast.LENGTH_SHORT).show();
                return;
            }

            Intent resultIntent = new Intent();
            resultIntent.putExtra("vehicle_id", vehicle_id);
            resultIntent.putExtra("vehicle_name", vehicle_name);
            resultIntent.putExtra("odometer_units", odometer_units);
            setResult(Petropad.RESULT_ADD_VEHICLE, resultIntent);
            finish();
        }
    };
}
