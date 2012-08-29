package com.wbg.petropad;

public class Entry {

    public static final int FIELD_ENTRY_ID = 0;
    public static final int FIELD_ODOMETER_READING_ID = 1;
    public static final int FIELD_TIMESTAMP = 2;
    public static final int FIELD_ODOMETER_READING = 3;
    public static final int FIELD_FILL_AMOUNT = 4;
    public static final int FIELD_PRICE_PER_UNIT = 5;

    private long _id;
    public final long vehicle_id;
    private long _odometer_reading_id;
    public final long timestamp;
    public final int odometer_reading;
    public final int fill_amount;
    public final int price_per_unit;

    public Entry(long id, long vehicle_id, long odometer_reading_id, int odometer_reading, int fill_amount, int price_per_unit, long timestamp) {
        this._id = id;
        this.vehicle_id = vehicle_id;
        this._odometer_reading_id = odometer_reading_id;
        this.timestamp = timestamp;
        this.odometer_reading = odometer_reading;
        this.fill_amount = fill_amount;
        this.price_per_unit = price_per_unit;
    }

    public Entry(long vehicle_id, int odometer_reading, int fill_amount, int price_per_unit, long timestamp) {
        this._id = -1;
        this.vehicle_id = vehicle_id;
        this.timestamp = timestamp;
        this.odometer_reading = odometer_reading;
        this.fill_amount = fill_amount;
        this.price_per_unit = price_per_unit;
    }

    public void setOdometerReadingId(long id) {
        _odometer_reading_id = id;
    }

    public long getOdometerReadingId() {
        return _odometer_reading_id;
    }

    public void setId(long id) {
        _id = id;
    }

    public long getId() {
        return _id;
    }
}
