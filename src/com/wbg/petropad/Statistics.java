package com.wbg.petropad;

import java.util.Date;

public class Statistics {
    public static final long MILLISECONDS_IN_DAY = 24 * 60 * 60 * 1000;

    public final int efficiency;
    public final int distance_driven;
    public final Date date_from;
    public final Date date_to;
    public final int num_days;
    public final int cost_per_distance_unit;

    public Statistics(int efficiency, int distance_driven, Date date_from, Date date_to, int num_days, int cost_per_distance_unit) {
        this.efficiency = efficiency;
        this.distance_driven = distance_driven;
        this.date_from = date_from;
        this.date_to = date_to;
        this.num_days = num_days;
        this.cost_per_distance_unit = cost_per_distance_unit;
    }

    public static Statistics Efficiency(Vehicle vehicle) {
        if (vehicle.entries == null) {
            vehicle.initializeEntries();
        }
        int num_entries = vehicle.entries.size();

        if (num_entries < 2) {
            return null;
        }

        Entry entryFrom = vehicle.entries.get(num_entries - 2);
        Entry entryTo = vehicle.entries.get(num_entries - 1);
        int num_days = Math.round((entryTo.timestamp - entryFrom.timestamp) / MILLISECONDS_IN_DAY);
        int distance_driven = entryTo.odometer_reading - entryFrom.odometer_reading;
        int distance_driven_nrml = distance_driven * 1000;
        int amount_spent = entryTo.price_per_unit * entryTo.fill_amount;
        int cost_per_distance_unit = Math.round(amount_spent / distance_driven_nrml);
        int efficiency = Math.round(distance_driven_nrml / entryTo.fill_amount);
        return new Statistics(efficiency, distance_driven, new Date(entryFrom.timestamp), new Date(entryTo.timestamp), num_days, cost_per_distance_unit);
    }
}
