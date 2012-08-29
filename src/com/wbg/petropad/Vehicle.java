package com.wbg.petropad;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.wbg.PetropadConstraintException;

import java.util.ArrayList;
import java.util.Date;

public class Vehicle {
    public final int id;
    public final String name;
    public final int odometer_units;

    public ArrayList<Entry> entries;

    public Vehicle(int id, String name, int odometer_units) {
        this.id = id;
        this.name = name;
        this.odometer_units = odometer_units;
        this.entries = null;
    }

    public void initializeEntries() {
        //we only initialize once, or we could force a refresh by
        //setting entries to null and calling initializeEntries
        if (entries != null) {
            return;
        }

        SQLiteDatabase db = Petropad.dbAdapter.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT entry_id, entry.odometer_reading_id, timestamp, odometer_reading, fill_amount, price_per_unit FROM entry, odometer_reading WHERE vehicle_id = ? AND entry.odometer_reading_id = odometer_reading.odometer_reading_id ORDER BY timestamp ASC", new String[]{String.valueOf(id)});
        int num_rows = c.getCount();

        entries = new ArrayList<Entry>(num_rows);

        for (int i = 0; i < num_rows; i++) {
            c.moveToPosition(i);
            entries.add(i, new Entry(c.getLong(Entry.FIELD_ENTRY_ID), c.getLong(Entry.FIELD_ODOMETER_READING_ID), id, c.getInt(Entry.FIELD_ODOMETER_READING), c.getInt(Entry.FIELD_FILL_AMOUNT), c.getInt(Entry.FIELD_PRICE_PER_UNIT), c.getLong(Entry.FIELD_TIMESTAMP)));
        }

        c.close();
        db.close();
    }

    public void replaceEntry(Entry originalEntry, Entry newEntry) throws PetropadConstraintException, PetropadValidationException {
        int curEntryIndex = entries.indexOf(originalEntry);
        if (curEntryIndex == -1) {
            throw new PetropadConstraintException("Entry doesn't exist for specified vehicle.");
        }

        entries.remove(originalEntry);

        try {
            addEntry(newEntry);
            //if we get an exception trying to add it
            //we put the entry back in its original location
            //and rethrow the exception
        } catch (PetropadConstraintException ex) {
            entries.add(curEntryIndex, originalEntry);
            throw ex;
        } catch (PetropadValidationException ex) {
            entries.add(curEntryIndex, originalEntry);
            throw ex;
        }
    }

    //adds an entry to its associated vehicle,
    //and ensures that the order is correct, ordered by timestamp
    public void addEntry(Entry entryToAdd) throws PetropadValidationException, PetropadConstraintException {

        if (entryToAdd.vehicle_id != this.id) {
            throw new PetropadConstraintException("Attempted to add an entry to a vehicle without the same id.");
        }

        int num_entries = entries.size();

        if (num_entries == 0) {
            entries.add(entryToAdd);
            return;
        }

        for (int i = num_entries - 1; i >= 0; i--) {
            Entry e = entries.get(i);
            if (entryToAdd.timestamp > e.timestamp) {

                if (entryToAdd.odometer_reading <= e.odometer_reading) {
                    throw new PetropadValidationException(R.string.exception_odometer_reading_lower_than_previous_entry, new Date(e.timestamp), e.odometer_reading);
                }

                entries.add(i + 1, entryToAdd);
                return;
            }
        }

        //if we get here, it's the earliest timestamp and therefore first entry in the batch
        Entry e = entries.get(0);
        if (entryToAdd.odometer_reading > e.odometer_reading) {
            throw new PetropadValidationException(R.string.exception_odometer_reading_higher_than_subsequent_entry, new Date(e.timestamp), e.odometer_reading);
        }
        entries.add(0, entryToAdd);
    }


    public Entry getEntryById(int entry_id) {
        int num_entries = entries.size();
        for (int i = 0; i < num_entries; i++) {
            if (entries.get(i).getId() == entry_id) {
                return entries.get(i);
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return name;
    }
}
