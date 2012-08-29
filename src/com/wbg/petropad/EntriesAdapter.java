package com.wbg.petropad;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.wbg.util.PMath;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EntriesAdapter implements ListAdapter {

    private final ArrayList<Entry> entries;
    private final int num_entries;

    public EntriesAdapter(ArrayList<Entry> entries) {
        this.entries = entries;
        num_entries = entries.size();
    }

    public boolean areAllItemsEnabled() {
        return true;
    }

    public boolean isEnabled(int i) {
        return true;
    }

    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        //not implemented
    }

    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
        //not implemented
    }

    public int getCount() {
        return num_entries;
    }

    public Object getItem(int i) {
        return entries.get(i);
    }

    public long getItemId(int i) {
        return entries.get(i).getId();
    }

    public boolean hasStableIds() {
        return true;
    }

    public View getView(int i, View convertView, ViewGroup parent) {
        Context c = parent.getContext();
        View v = View.inflate(c, R.layout.entry_row, null);
        TextView txtEntryDate = (TextView) v.findViewById(R.id.lblEntryRowEntryDate);
        TextView txtOdometerReading = (TextView) v.findViewById(R.id.lblEntryRowOdometerReading);
        TextView txtFillAmount = (TextView) v.findViewById(R.id.lblEntryRowFillAmount);
        TextView txtPricePerUnit = (TextView) v.findViewById(R.id.lblEntryRowPricePerUnit);
        DateFormat df = DateFormat.getInstance();
        Entry entry = entries.get(i);
        txtEntryDate.setText(c.getString(R.string.list_item_label_timestamp, df.format(new Date(entry.timestamp))));
        txtOdometerReading.setText(c.getString(R.string.list_item_label_odometer_reading, entry.odometer_reading));
        txtFillAmount.setText(c.getString(R.string.list_item_label_fill_amount, PMath.formatToDecimalString(entry.fill_amount, 3), Units.getVolumeUnitPlural(Petropad.volume_unit_id)));
        txtPricePerUnit.setText(c.getString(R.string.list_item_label_price_per_unit, Petropad.currency.getSymbol(), PMath.formatToDecimalString(entry.price_per_unit, Petropad.currency.getDefaultFractionDigits()), Units.getVolumeUnitSingular(Petropad.volume_unit_id)));
        return v;
    }

    public int getItemViewType(int i) {
        return 0;
    }

    public int getViewTypeCount() {

        return 1;
    }

    public boolean isEmpty() {
        return num_entries == 0;
    }
}
