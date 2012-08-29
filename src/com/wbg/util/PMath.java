package com.wbg.util;

public class PMath
{

    public static final int PRECISION = 3;
    //returned when the string doesn't contain a decimal at the specified precision
    public static final int ERROR_NOT_DECIMAL_STRING = -1;

    //returned when a number format exception is thrown by Integer.parseInt
    public static final int ERROR_NUMBER_FORMAT_EXCEPTION = -2;
    public static final int ERROR_EXCEEDS_MAXIMUM_PRECISION = -5;

    public static String formatToDecimalString(int value, int showPrecision)
    {

        String str = String.valueOf(value);
        int num_digits = str.length();
        if (value < 1000)
        {
            if (showPrecision == 0)
            {
                return "0";
            }
            
            int num_zeros = showPrecision - num_digits;
            char[] zeros = new char[num_zeros];
            for (int i = 0; i < num_zeros; i++)
            {
                zeros[i] = '0';
            }
            return "0." + new String(zeros) + value;
        }
        String wholes = str.substring(0, num_digits - PRECISION);
        if (showPrecision == 0)
        {
            return wholes;
        }
        String decimals = str.substring(num_digits - PRECISION, num_digits - (PRECISION - showPrecision));
        return wholes + "." + decimals;
    }

    //all values are stored as ints, so when working
    //with decimal values we use integers with the
    //last 3 digits being decimal values
    public static int normalizeNumber(CharSequence text)
    {

        String[] value = text.toString().split("\\.");
        if (value.length == 1)
        {
            try
            {
                return Integer.parseInt(value[0]) * 1000;
            }
            catch (NumberFormatException ex)
            {
                return ERROR_NUMBER_FORMAT_EXCEPTION;
            }
        }
        int precision = value[1].length();
        if (precision > 3)
        {
            return ERROR_EXCEEDS_MAXIMUM_PRECISION;
        }

        int wholes;
        int decimals;

        try
        {
            wholes = value[0].trim().equals("") ? 0 : Integer.parseInt(value[0]) * 1000;
            decimals = Integer.parseInt(value[1]);
        }
        catch (NumberFormatException ex)
        {
            return ERROR_NUMBER_FORMAT_EXCEPTION;
        }

        if (precision == 1)
        {
            decimals *= 100;
        }
        else if (precision == 2)
        {
            decimals *= 10;
        }

        return wholes + decimals;
    }
}
