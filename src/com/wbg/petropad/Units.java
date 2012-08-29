package com.wbg.petropad;


public class Units {

    public static final int DISTANCE_MILES = 0;
    public static final int DISTANCE_KILOMETER = 1;

    public static final int VOLUME_GALLON = 2;
    public static final int VOLUME_LITER = 3;

    private static final String DISTANCE_STRING_MILES = "Miles";
    private static final String DISTANCE_STRING_KILOMETERS = "Kilometers";
    private static final String DISTANCE_STRING_MILE = "Mile";
    private static final String DISTANCE_STRING_KILOMETER = "Kilometer";
    private static final String DISTANCE_ABBREV_KILOMETER = "km";
    private static final String DISTANCE_ABBREV_MILE = "m";

    private static final String VOLUME_STRING_GALLONS = "Gallons";
    private static final String VOLUME_STRING_LITERS = "Litres";
    private static final String VOLUME_STRING_GALLON = "Gallon";
    private static final String VOLUME_STRING_LITER = "Litre";
    private static final String VOLUME_ABBREV_GALLON = "g";
    private static final String VOLUME_ABBREV_LITER = "l";

    public static String getDistanceUnitPlural(int unit_id) {
        switch (unit_id) {
            case DISTANCE_MILES:
                return DISTANCE_STRING_MILES;
            case DISTANCE_KILOMETER:
                return DISTANCE_STRING_KILOMETERS;
        }

        return null;
    }

    public static String getDistanceUnitSingular(int unit_id) {
        switch (unit_id) {
            case DISTANCE_MILES:
                return DISTANCE_STRING_MILE;
            case DISTANCE_KILOMETER:
                return DISTANCE_STRING_KILOMETER;
        }

        return null;
    }

    public static String getVolumeUnitPlural(int unit_id) {
        switch (unit_id) {
            case VOLUME_GALLON:
                return VOLUME_STRING_GALLONS;
            case VOLUME_LITER:
                return VOLUME_STRING_LITERS;
        }

        return null;
    }

    public static String getVolumeUnitSingular(int unit_id) {
        switch (unit_id) {
            case VOLUME_GALLON:
                return VOLUME_STRING_GALLON;
            case VOLUME_LITER:
                return VOLUME_STRING_LITER;
        }

        return null;
    }

    public static String getEfficiencyMeasure(int distance_unit_id, int volume_unit_id) {
        StringBuilder sb = new StringBuilder();

        switch (distance_unit_id) {
            case DISTANCE_MILES:
                sb.append(DISTANCE_ABBREV_MILE);
                break;
            case DISTANCE_KILOMETER:
                sb.append(DISTANCE_ABBREV_KILOMETER);
                break;
        }

        sb.append("p");

        switch (volume_unit_id) {
            case VOLUME_GALLON:
                sb.append(VOLUME_ABBREV_GALLON);
                break;
            case VOLUME_LITER:
                sb.append(VOLUME_ABBREV_LITER);
                break;
        }

        return sb.toString();
    }
}
